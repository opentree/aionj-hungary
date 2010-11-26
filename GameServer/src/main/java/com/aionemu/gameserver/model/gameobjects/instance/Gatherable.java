/*
 * This file is part of aion-unique <aion-unique.com>.
 *
 *  aion-unique is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  aion-unique is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with aion-unique.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.aionemu.gameserver.model.gameobjects.instance;

import java.util.Iterator;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.controllers.movement.StartMovingListener;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.DescriptionId;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.knownList.StaticObjectKnownList;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.GatherableTemplate;
import com.aionemu.gameserver.model.templates.gather.Material;
import com.aionemu.gameserver.model.templates.spawn.SpawnTemplate;
import com.aionemu.gameserver.network.aion.serverpackets.SM_GATHERABLE_INFO;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.RespawnService;
import com.aionemu.gameserver.skillengine.task.GatheringTask;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.World;

/**
 * @author ATracer
 *
 */
public class Gatherable extends VisibleObject
{
	private int				gatherCount;

	private int				currentGatherer;

	private GatheringTask	task;

	public enum GatherState
	{
		GATHERED, GATHERING, IDLE
	}

	private GatherState	state	= GatherState.IDLE;

	public Gatherable(int objId, SpawnTemplate spawnTemplate)
	{
		super(objId, spawnTemplate);
		objectTemplate = DataManager.GATHERABLE_DATA.getGatherableTemplate(spawnTemplate.getTemplateId());
		this.setKnownlist(new StaticObjectKnownList(this));
	}

	@Override
	public String getName()
	{
		return objectTemplate.getName();
	}

	@Override
	public GatherableTemplate getObjectTemplate()
	{
		return (GatherableTemplate) objectTemplate;
	}

	/**
	 *  Start gathering process
	 *  
	 * @param player
	 */
	public void onStartUse(final Player player)
	{
		//basic actions, need to improve here
		final GatherableTemplate template = this.getObjectTemplate();

		if (!checkPlayerSkill(player, template))
			return;

		List<Material> materials = template.getMaterials().getMaterial();

		int index = 0;
		Material material = materials.get(index); //default is 0
		int count = materials.size();

		if (count < 1)
		{
			//error - theoretically if XML data is correct, this should never happen.
			return;
		}

		else if (count == 1)
		{
			//default is 0
		}

		else
		{
			if (player.getInventory().isFull())
			{
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.EXTRACT_GATHER_INVENTORY_IS_FULL());
				return;
			}

			int gatherRate = 1; // 1x rates (probably make config later, if fixed to non-linear statistic probability)
			float maxrate = 0;
			int rate = 0;
			int i = 0; //index counter

			//sort materials to ascending order
			SortedMap<Integer, Integer> hasMat = new TreeMap<Integer, Integer>();
			for (Material mat : materials)
			{
				maxrate += mat.getRate(); //get maxrate
				hasMat.put(mat.getRate(), i); //sort and save index of materials (key is rate and rate is unique on each gatherId)
				i++;
			}

			Iterator<Integer> it = hasMat.keySet().iterator();
			while (it.hasNext())
			{
				rate = it.next();
				float percent = Rnd.get() * 100f;
				float chance = ((rate / maxrate) * 100f * gatherRate);

				// default index is to 0, look to up little bit on 'material'
				if (percent < chance)
				{
					index = hasMat.get(rate); //return index					
					material = materials.get(index);
					break;
				}
			}
		}

		final Material finalMaterial = material;

		if (state != GatherState.GATHERING)
		{
			state = GatherState.GATHERING;
			currentGatherer = player.getObjectId();
			player.getObserveController().attach(new StartMovingListener()
			{

				@Override
				public void moved()
				{
					finishGathering(player);
				}
			});
			int skillLvlDiff = player.getSkillList().getSkillLevel(template.getHarvestSkill()) - template.getSkillLevel();
			task = new GatheringTask(player, this, finalMaterial, skillLvlDiff);
			task.start();
		}
	}

	/**
	 *  Checks whether player have needed skill for gathering and skill level is sufficient
	 *  
	 * @param player
	 * @param template
	 * @return
	 */
	private boolean checkPlayerSkill(final Player player, final GatherableTemplate template)
	{
		int harvestSkillId = template.getHarvestSkill();

		//check skill is available
		if (!player.getSkillList().isSkillPresent(harvestSkillId))
		{
			//TODO send some message ?
			return false;
		}
		if (player.getSkillList().getSkillLevel(harvestSkillId) < template.getSkillLevel())
		{
			//TODO send some message ?
			return false;
		}
		return true;
	}

	public void completeInteraction()
	{
		state = GatherState.IDLE;
		gatherCount++;
		if (gatherCount == getObjectTemplate().getHarvestCount())
			onDie();
	}

	public void rewardPlayer(Player player)
	{
		if (player != null)
		{
			int skillLvl = getObjectTemplate().getSkillLevel();
			int xpReward = (int) ((0.008 * (skillLvl + 100) * (skillLvl + 100) + 60) * player.getRates().getGatheringXPRate());

			if (player.getSkillList().addSkillXp(player, getObjectTemplate().getHarvestSkill(), xpReward))
			{
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.EXTRACT_GATHER_SUCCESS_GETEXP());
				player.getCommonData().addExp(xpReward);
			}
			else
				PacketSendUtility.sendPacket(
						player,
						SM_SYSTEM_MESSAGE.MSG_DONT_GET_PRODUCTION_EXP(new DescriptionId(DataManager.SKILL_DATA.getSkillTemplate(
								getObjectTemplate().getHarvestSkill()).getNameId())));

		}
	}

	/**
	 *  Called by client when some action is performed or on finish gathering
	 *  Called by move observer on player move
	 *  
	 * @param player
	 */
	public void finishGathering(Player player)
	{
		if (currentGatherer == player.getObjectId())
		{
			if (state == GatherState.GATHERING)
			{
				task.abort();
			}
			currentGatherer = 0;
			state = GatherState.IDLE;
		}
	}

	private void onDie()
	{
		RespawnService.scheduleRespawnTask(this);
		World.getInstance().despawn(this);
	}

	@Override
	public void onRespawn()
	{
		this.gatherCount = 0;
	}

	/* (non-Javadoc)
	 * @see com.aionemu.gameserver.model.gameobjects.VisibleObject#see(com.aionemu.gameserver.model.gameobjects.VisibleObject)
	 */
	@Override
	public void see(VisibleObject object)
	{
		super.see(object);
		if (object instanceof Player)
		{
			PacketSendUtility.sendPacket((Player)object, new SM_GATHERABLE_INFO(this));
		}
	}
	
}
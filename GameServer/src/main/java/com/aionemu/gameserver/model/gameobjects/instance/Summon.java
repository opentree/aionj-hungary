/*
 * This file is part of aion-unique <aion-unique.org>.
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

import com.aionemu.gameserver.controllers.effect.EffectController;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.TaskId;
import com.aionemu.gameserver.model.TribeClass;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.interfaces.ISummoned;
import com.aionemu.gameserver.model.gameobjects.knownList.KnownList;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.stats.StatEnum;
import com.aionemu.gameserver.model.gameobjects.stats.SummonGameStats;
import com.aionemu.gameserver.model.gameobjects.stats.SummonLifeStats;
import com.aionemu.gameserver.model.templates.NpcTemplate;
import com.aionemu.gameserver.model.templates.VisibleObjectTemplate;
import com.aionemu.gameserver.model.templates.spawn.SpawnTemplate;
import com.aionemu.gameserver.model.templates.stats.SummonStatsTemplate;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ATTACK_STATUS.TYPE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SUMMON_OWNER_REMOVE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SUMMON_PANEL_REMOVE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SUMMON_UPDATE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.restrictions.RestrictionsManager;
import com.aionemu.gameserver.services.LifeStatsRestoreService;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.skillengine.model.Skill;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;

/**
 * @author ATracer
 * 
 */
public class Summon extends Creature implements ISummoned
{

	private Player		master;
	private SummonMode	mode;
	private final byte	level;

	public static enum SummonMode
	{
		ATTACK(0), GUARD(1), REST(2), RELEASE(3);

		private int	id;

		private SummonMode(int id)
		{
			this.id = id;
		}

		/**
		 * @return the id
		 */
		public int getId()
		{
			return id;
		}
	}

	/**
	 * 
	 * @param objId
	 * @param controller
	 * @param spawnTemplate
	 * @param objectTemplate
	 * @param statsTemplate 
	 * @param position
	 */
	public Summon(int objId, SpawnTemplate spawnTemplate, VisibleObjectTemplate objectTemplate, SummonStatsTemplate statsTemplate, byte level)
	{
		super(objId, spawnTemplate);
		this.objectTemplate = objectTemplate;
		this.level = level;
		super.setGameStats(new SummonGameStats(this, statsTemplate));
		super.setLifeStats(new SummonLifeStats(this));
		super.setEffectController(new EffectController(this));
		super.setKnownlist(new KnownList(this));
		this.mode = SummonMode.GUARD;
	}

	/**
	 * @return the owner
	 */
	@Override
	public Player getMaster()
	{
		return master;
	}

	@Override
	public String getName()
	{
		return objectTemplate.getName();
	}

	/**
	 * @return the level
	 */
	@Override
	public byte getLevel()
	{
		return level;
	}

	@Override
	public void initializeAi()
	{
		// TODO Auto-generated method stub
	}

	@Override
	public NpcTemplate getObjectTemplate()
	{
		return super.getObjectTemplate();
	}

	public int getNpcId()
	{
		return getObjectTemplate().getTemplateId();
	}

	public int getNameId()
	{
		return getObjectTemplate().getNameId();
	}

	/**
	 * @return the mode
	 */
	public SummonMode getMode()
	{
		return mode;
	}

	/**
	 * @param mode the mode to set
	 */
	public void setMode(SummonMode mode)
	{
		this.mode = mode;
	}

	@Override
	public boolean isEnemyNpc(Npc visibleObject)
	{
		return master.isEnemyNpc(visibleObject);
	}

	@Override
	public boolean isEnemyPlayer(Player visibleObject)
	{
		return master.isEnemyPlayer(visibleObject);
	}

	@Override
	public boolean isEnemySummon(Summon summon)
	{
		return master.isEnemySummon(summon);
	}

	@Override
	public TribeClass getTribe()
	{
		return master.getTribe();
	}

	@Override
	public boolean isAggressiveTo(Creature creature)
	{
		return creature.isAggroFrom(this);
	}

	@Override
	public boolean isAggroFrom(Npc npc)
	{
		if (getMaster() == null)
			return false;

		return getMaster().isAggroFrom(npc);
	}

	/* (non-Javadoc)
	 * @see com.aionemu.gameserver.model.gameobjects.interfaces.ISummoned#setMaster(com.aionemu.gameserver.model.gameobjects.Creature)
	 */
	@Override
	public void setMaster(Creature master)
	{
		this.master = (Player) master;
	}

	private long	lastAttackMilis	= 0;

	@Override
	public void notSee(VisibleObject object, boolean isOutOfRange)
	{
		super.notSee(object, isOutOfRange);
		if (getMaster() == null)
			return;

		if (object.getObjectId() == getMaster().getObjectId())
		{
			release(UnsummonType.DISTANCE);
		}
	}

	/**
	 * Release summon
	 */
	public void release(final UnsummonType unsummonType)
	{

		if (getMode() == SummonMode.RELEASE)
			return;
		setMode(SummonMode.RELEASE);

		final Player master = getMaster();
		final int summonObjId = getObjectId();

		switch (unsummonType)
		{
			case COMMAND:
				PacketSendUtility.sendPacket(master, SM_SYSTEM_MESSAGE.SUMMON_UNSUMMON(getNameId()));
				PacketSendUtility.sendPacket(master, new SM_SUMMON_UPDATE(this));
				break;
			case DISTANCE:
				PacketSendUtility.sendPacket(master, SM_SYSTEM_MESSAGE.SUMMON_UNSUMMON_BY_TOO_DISTANCE());
				PacketSendUtility.sendPacket(master, new SM_SUMMON_UPDATE(this));
				break;
			case LOGOUT:
			case UNSPECIFIED:
				break;
		}

		ThreadPoolManager.getInstance().schedule(new Runnable()
		{

			@Override
			public void run()
			{
				setMaster(null);
				master.setSummon(null);
				delete();

				switch (unsummonType)
				{
					case COMMAND:
					case DISTANCE:
					case UNSPECIFIED:
						PacketSendUtility.sendPacket(master, SM_SYSTEM_MESSAGE.SUMMON_DISMISSED(getNameId()));
						PacketSendUtility.sendPacket(master, new SM_SUMMON_OWNER_REMOVE(summonObjId));

						// TODO temp till found on retail
						PacketSendUtility.sendPacket(master, new SM_SUMMON_PANEL_REMOVE());
						break;
					case LOGOUT:
						break;
				}
			}
		}, 5000);
	}

	/**
	 * Change to rest mode
	 */
	public void restMode()
	{
		setMode(SummonMode.REST);
		Player master = getMaster();
		PacketSendUtility.sendPacket(master, SM_SYSTEM_MESSAGE.SUMMON_RESTMODE(this.getNameId()));
		PacketSendUtility.sendPacket(master, new SM_SUMMON_UPDATE(this));
		checkCurrentHp();
	}

	private void checkCurrentHp()
	{
		if (!getLifeStats().isFullyRestoredHp())
			addNewTask(TaskId.RESTORE, LifeStatsRestoreService.getInstance().scheduleHpRestoreTask(getLifeStats()));
	}

	/**
	 * Change to guard mode
	 */
	public void guardMode()
	{
		setMode(SummonMode.GUARD);
		Player master = getMaster();
		PacketSendUtility.sendPacket(master, SM_SYSTEM_MESSAGE.SUMMON_GUARDMODE(this.getNameId()));
		PacketSendUtility.sendPacket(master, new SM_SUMMON_UPDATE(this));
		checkCurrentHp();
	}

	/**
	 * Change to attackMode
	 */
	public void attackMode()
	{
		setMode(SummonMode.ATTACK);
		Player master = getMaster();
		PacketSendUtility.sendPacket(master, SM_SYSTEM_MESSAGE.SUMMON_ATTACKMODE(this.getNameId()));
		PacketSendUtility.sendPacket(master, new SM_SUMMON_UPDATE(this));
		cancelTask(TaskId.RESTORE);
	}

	@Override
	public void attackTarget(Creature target)
	{
		Player master = getMaster();

		if (!RestrictionsManager.canAttack(master, target))
			return;

		int attackSpeed = getGameStats().getCurrentStat(StatEnum.ATTACK_SPEED);
		long milis = System.currentTimeMillis();
		if (milis - lastAttackMilis < attackSpeed)
		{
			/**
			 * Hack!
			 */
			return;
		}
		lastAttackMilis = milis;

		super.attackTarget(target);
	}

	@Override
	public void onAttack(Creature creature, int skillId, TYPE type, int damage)
	{
		//temp 
		if (getMode() == SummonMode.RELEASE)
			return;

		super.onAttack(creature, skillId, type, damage);
		PacketSendUtility.sendPacket(getMaster(), new SM_SUMMON_UPDATE(this));
	}

	@Override
	public void onDie(Creature lastAttacker)
	{
		super.onDie(lastAttacker);
		release(UnsummonType.UNSPECIFIED);
		PacketSendUtility.broadcastPacket(this, new SM_EMOTION(this, EmotionType.DIE, 0, lastAttacker == null ? 0 : lastAttacker.getObjectId()));
	}

	public void useSkill(int skillId, Creature target)
	{
		Skill skill = SkillEngine.getInstance().getSkill(this, skillId, 1, target);
		if (skill != null)
		{
			skill.useSkill();
		}
	}

	public static enum UnsummonType
	{
		LOGOUT, DISTANCE, COMMAND, UNSPECIFIED
	}
}

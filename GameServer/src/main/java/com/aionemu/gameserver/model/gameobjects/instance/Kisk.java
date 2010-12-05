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

import java.util.ArrayList;
import java.util.List;

import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.TaskId;
import com.aionemu.gameserver.model.alliance.PlayerAllianceMember;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.interfaces.IDialogRequest;
import com.aionemu.gameserver.model.gameobjects.interfaces.ISummoned;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.RequestResponseHandler;
import com.aionemu.gameserver.model.legion.Legion;
import com.aionemu.gameserver.model.templates.spawn.SpawnTemplate;
import com.aionemu.gameserver.model.templates.stats.KiskStatsTemplate;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ATTACK_STATUS.TYPE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_KISK_UPDATE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_QUESTION_WINDOW;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.KiskService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.World;

/**
 * @author Sarynth
 *
 */
public class Kisk extends Npc implements ISummoned, IDialogRequest
{
	private String				ownerName;
	private Legion				ownerLegion;
	private Race				ownerRace;
	private int					ownerObjectId;
	private Player				master;
	private KiskStatsTemplate	kiskStatsTemplate;

	private int					remainingResurrections;
	private final long			kiskSpawnTime;

	private final List<Player>	kiskMembers			= new ArrayList<Player>();
	private int					currentMemberCount	= 0;

	/**
	 * 
	 * @param objId
	 * @param controller
	 * @param spawnTemplate
	 * @param objectTemplate
	 */
	public Kisk(int objId, SpawnTemplate spawnTemplate)
	{
		super(objId, spawnTemplate);

		this.kiskStatsTemplate = this.getObjectTemplate().getKiskStatsTemplate();
		if (this.kiskStatsTemplate == null)
			this.kiskStatsTemplate = new KiskStatsTemplate();

		remainingResurrections = this.kiskStatsTemplate.getMaxResurrects();
		kiskSpawnTime = System.currentTimeMillis() / 1000;
	}

	/**
	 * Required so that the enemy race can attack the Kisk!
	 */
	/*	@Override
		public boolean isAggressiveTo(Creature creature)
		{
			if (creature instanceof Player)
			{
				Player player = (Player) creature;
				if (player.getCommonData().getRace() != this.ownerRace)
					return true;
			}
			return false;
		}

		@Override
		public boolean isEnemyNpc(Npc npc)
		{
			return npc instanceof Monster || npc.isAggressiveTo(this);
		}

		@Override
		public boolean isEnemyPlayer(Player player)
		{
			return player.getCommonData().getRace() != this.ownerRace;
		}
	*/
	/**
	 * 1 ~ race
	 * 2 ~ legion
	 * 3 ~ solo
	 * 4 ~ group
	 * 5 ~ alliance
	 * @return useMask
	 */
	public int getUseMask()
	{
		return this.kiskStatsTemplate.getUseMask();
	}

	public List<Player> getCurrentMemberList()
	{
		return this.kiskMembers;
	}

	/**
	 * @return
	 */
	public int getCurrentMemberCount()
	{
		return this.currentMemberCount;
	}

	/**
	 * @return
	 */
	public int getMaxMembers()
	{
		return this.kiskStatsTemplate.getMaxMembers();
	}

	/**
	 * @return
	 */
	public int getRemainingResurrects()
	{
		return this.remainingResurrections;
	}

	/**
	 * @return
	 */
	public int getMaxRessurects()
	{
		return this.kiskStatsTemplate.getMaxResurrects();
	}

	/**
	 * @return
	 */
	public int getRemainingLifetime()
	{
		long timeElapsed = (System.currentTimeMillis() / 1000) - kiskSpawnTime;
		int timeRemaining = (int) (7200 - timeElapsed); // Fixed 2 hours 2 * 60 * 60
		return (timeRemaining > 0 ? timeRemaining : 0);
	}

	/**
	 * @param player
	 * @return
	 */
	public boolean canBind(Player player)
	{
		String playerName = player.getName();

		if (playerName != this.ownerName)
		{
			// Check if they fit the usemask
			switch (this.getUseMask())
			{
				case 1: // Race
					if (this.ownerRace == player.getCommonData().getRace())
						return false;
					break;

				case 2: // Legion
					if (ownerLegion == null)
						return false;
					if (!ownerLegion.isMember(player.getObjectId()))
						return false;
					break;

				case 3: // Solo
					return false; // Already Checked Name

				case 4: // Group (PlayerGroup or PlayerAllianceGroup)
					boolean isMember = false;
					if (player.isInGroup())
					{
						for (Player member : player.getPlayerGroup().getMembers())
						{
							if (member.getObjectId() == this.ownerObjectId)
							{
								isMember = true;
							}
						}
					}
					else if (player.isInAlliance())
					{
						for (PlayerAllianceMember allianceMember : player.getPlayerAlliance().getMembersForGroup(player.getObjectId()))
						{
							if (allianceMember.getObjectId() == this.ownerObjectId)
							{
								isMember = true;
							}
						}
					}
					if (isMember == false)
						return false;
					break;

				case 5: // Alliance
					if (!player.isInAlliance() || player.getPlayerAlliance().getPlayer(this.ownerObjectId) == null)
						return false;
					break;

				default:
					return false;
			}
		}

		if (this.getCurrentMemberCount() >= getMaxMembers())
			return false;

		return true;
	}

	/**
	 * @param player
	 */
	public void addPlayer(Player player)
	{
		kiskMembers.add(player);
		player.setKisk(this);
		this.currentMemberCount++;
		this.broadcastKiskUpdate();
	}

	/**
	 * @param player
	 */
	public void reAddPlayer(Player player)
	{
		kiskMembers.add(player);
		player.setKisk(this);
		PacketSendUtility.sendPacket(player, new SM_KISK_UPDATE(this));
	}

	/**
	 * @param player
	 */
	public void removePlayer(Player player)
	{
		kiskMembers.remove(player);
		player.setKisk(null);
		this.currentMemberCount--;
		this.broadcastKiskUpdate();
	}

	/**
	 * Sends SM_KISK_UPDATE to each member
	 */
	private void broadcastKiskUpdate()
	{
		// Logic to prevent enemy race from knowing kisk information.
		for (Player member : this.kiskMembers)
		{
			if (!this.getKnownList().knowns(member))
				PacketSendUtility.sendPacket(member, new SM_KISK_UPDATE(this));
		}
		for (VisibleObject obj : this.getKnownList().getKnownObjects().values())
		{
			if (obj instanceof Player)
			{
				Player target = (Player) obj;
				if (target.getCommonData().getRace() == this.ownerRace)
					PacketSendUtility.sendPacket(target, new SM_KISK_UPDATE(this));
			}
		}
	}

	/**
	 * @param message
	 */
	public void broadcastPacket(SM_SYSTEM_MESSAGE message)
	{
		for (Player member : kiskMembers)
		{
			if (member != null)
				PacketSendUtility.sendPacket(member, message);
		}
	}

	/**
	 * @param player
	 */
	public void resurrectionUsed(Player player)
	{
		remainingResurrections--;
		if (remainingResurrections <= 0)
		{
			player.getKisk().onDespawn(true);
		}
		else
		{
			broadcastKiskUpdate();
		}
	}

	/**
	 * @return ownerRace
	 */
	public Race getOwnerRace()
	{
		return this.ownerRace;
	}

	/**
	 * @return ownerName
	 */
	public String getOwnerName()
	{
		return this.ownerName;
	}

	/**
	 * @return ownerObjectId
	 */
	public int getOwnerObjectId()
	{
		return this.ownerObjectId;
	}

	@Override
	public void onAttack(Creature creature, int skillId, TYPE type, int damage)
	{

		if (this.getLifeStats().isFullyRestoredHp())
		{
			List<Player> members = this.getCurrentMemberList();
			for (Player member : members)
			{
				PacketSendUtility.sendPacket(member, SM_SYSTEM_MESSAGE.STR_BINDSTONE_IS_ATTACKED);
			}
		}

		super.onAttack(creature, skillId, type, damage);

	}

	@Override
	public void onDespawn(boolean forced)
	{
		this.broadcastPacket(SM_SYSTEM_MESSAGE.STR_BINDSTONE_IS_REMOVED);
		removeKisk();
	}

	@Override
	public void onDie(Creature lastAttacker)
	{
		PacketSendUtility.broadcastPacket(this, new SM_EMOTION(this, EmotionType.DIE, 0, 0));
		this.broadcastPacket(SM_SYSTEM_MESSAGE.STR_BINDSTONE_IS_DESTROYED);
		removeKisk();
	}

	private void removeKisk()
	{
		KiskService.removeKisk(this);
		final Kisk kisk = this;
		// Schedule World Removal
		addTask(TaskId.DECAY, ThreadPoolManager.getInstance().schedule(new Runnable()
		{
			@Override
			public void run()
			{
				if (kisk != null && kisk.isSpawned())
					World.getInstance().despawn(kisk);
			}
		}, 3 * 1000));
	}

	@Override
	public void onDialogRequest(Player player)
	{

		if (player.getKisk() == this)
		{
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_BINDSTONE_ALREADY_REGISTERED);
			return;
		}

		if (canBind(player))
		{
			RequestResponseHandler responseHandler = new RequestResponseHandler(this)
			{

				@Override
				public void acceptRequest(StaticNpc requester, Player responder)
				{
					Kisk kisk = (Kisk) requester;

					// Check again if it's full (If they waited to press OK)
					if (!kisk.canBind(responder))
					{
						PacketSendUtility.sendPacket(responder, SM_SYSTEM_MESSAGE.STR_CANNOT_REGISTER_BINDSTONE_HAVE_NO_AUTHORITY);
						return;
					}
					KiskService.onBind(kisk, responder);
				}

				@Override
				public void denyRequest(StaticNpc requester, Player responder)
				{
					// Nothing Happens
				}
			};

			boolean requested = player.getResponseRequester().putRequest(SM_QUESTION_WINDOW.STR_BIND_TO_KISK, responseHandler);
			if (requested)
			{
				PacketSendUtility.sendPacket(player, new SM_QUESTION_WINDOW(SM_QUESTION_WINDOW.STR_BIND_TO_KISK, player.getObjectId()));
			}
		}
		else if (getCurrentMemberCount() >= getMaxMembers())
		{
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_CANNOT_REGISTER_BINDSTONE_FULL);
		}
		else
		{
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_CANNOT_REGISTER_BINDSTONE_HAVE_NO_AUTHORITY);
		}
	}

	/* (non-Javadoc)
	 * @see com.aionemu.gameserver.model.gameobjects.interfaces.ISummoned#setMaster(com.aionemu.gameserver.model.gameobjects.Creature)
	 */
	@Override
	public void setMaster(Creature master)
	{
		if (master instanceof Player)
		{
			Player player = (Player) master;
			this.master = player;
			this.ownerName = player.getName();
			this.ownerLegion = player.getLegion();
			this.ownerRace = player.getCommonData().getRace();
			this.ownerObjectId = player.getObjectId();
		}
	}

	/* (non-Javadoc)
	 * @see com.aionemu.gameserver.model.gameobjects.interfaces.ISummoned#getMaster()
	 */
	@Override
	public Creature getMaster()
	{
		return this.master;
	}
}

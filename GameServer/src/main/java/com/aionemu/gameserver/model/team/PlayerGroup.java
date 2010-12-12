/*
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, 
 * MA  02110-1301, USA.
 *
 * http://www.gnu.org/copyleft/gpl.html
 */
package com.aionemu.gameserver.model.team;

import java.util.List;

import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.instance.StaticNpc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.group.GroupEvent;
import com.aionemu.gameserver.model.group.LootGroupRules;
import com.aionemu.gameserver.model.team.interfaces.ITeamProperties;

/**
 * @author lyahim
 *
 */
public class PlayerGroup extends Team<Player> implements ITeamProperties
{
	private final int				RoundRobinNr	= 0;
	private final LootGroupRules	lootGroupRules	= new LootGroupRules();
	private List<Player>			inRangePlayers;

	public PlayerGroup(int teamId, Player leader)
	{
		super(teamId, leader);
		//		leader.setPlayerGroup(this);
		//		PacketSendUtility.sendPacket(leader, new SM_GROUP_INFO(this));
	}

	@Override
	public void addMember(Player member)
	{
		//		super.addMember(member);
		//		member.setPlayerGroup(this);
		//		PacketSendUtility.sendPacket(member, new SM_GROUP_INFO(this));
		//		updateGroupUIToEvent(member, GroupEvent.ENTER);
	}

	/**
	 * This method will return a round robin group member.
	 * 
	 * @param npc
	 *            The killed Npc
	 * @return memberObjId or 0 if the selected player isn't in range.
	 */
	public int getRoundRobinMember(StaticNpc npc)
	{
		return RoundRobinNr;
		//		RoundRobinNr = ++RoundRobinNr % size();
		//		int i = 0;
		//		for (Player player : getMembers())
		//		{
		//			if (i == RoundRobinNr)
		//			{
		//				if (MathUtil.isIn3dRange(player, npc, GroupConfig.GROUP_MAX_DISTANCE))
		//				{ // the player is in range of the killed NPC.
		//					return player.getObjectId();
		//				}
		//				else
		//				{
		//					return 0;
		//				}
		//			}
		//			i++;
		//		}
		//		return 0;
	}

	/**
	 * Removes player from group
	 * 
	 * @param player
	 */
	public void removePlayerFromGroup(Player player)
	{
		//		removeMember(player);
		//		player.setPlayerGroup(null);
		//		updateGroupUIToEvent(player, GroupEvent.LEAVE);
		//
		//		/**
		//		 * Inform all group members player has left the group
		//		 */
		//		PacketSendUtility.broadcastPacket(player, new SM_LEAVE_GROUP_MEMBER(), true, new ObjectFilter<Player>()
		//		{
		//			@Override
		//			public boolean acceptObject(Player object)
		//			{
		//				return object.getPlayerGroup() == null ? true : false;
		//			}
		//		});
	}

	public void updateGroupUIToEvent(Player subjective, GroupEvent groupEvent)
	{
		//		switch (groupEvent)
		//		{
		//			case CHANGELEADER:
		//			{
		//				for (Player member : this.getMembers())
		//				{
		//					PacketSendUtility.sendPacket(member, new SM_GROUP_INFO(this));
		//					if (subjective.equals(member))
		//						PacketSendUtility.sendPacket(member, SM_SYSTEM_MESSAGE.CHANGE_GROUP_LEADER());
		//					PacketSendUtility.sendPacket(member, new SM_GROUP_MEMBER_INFO(this, subjective, groupEvent));
		//				}
		//			}
		//				break;
		//			case LEAVE:
		//			{
		//				boolean changeleader = false;
		//				if (subjective == this.getLeader())// change group leader
		//				{
		//					this.setLeader(this.getMembers().iterator().next());
		//					changeleader = true;
		//				}
		//				for (Player member : this.getMembers())
		//				{
		//					if (changeleader)
		//					{
		//						PacketSendUtility.sendPacket(member, new SM_GROUP_INFO(this));
		//						PacketSendUtility.sendPacket(member, SM_SYSTEM_MESSAGE.CHANGE_GROUP_LEADER());
		//					}
		//					if (!subjective.equals(member))
		//						PacketSendUtility.sendPacket(member, new SM_GROUP_MEMBER_INFO(this, subjective, groupEvent));
		//					if (this.size() > 1)
		//						PacketSendUtility.sendPacket(member, SM_SYSTEM_MESSAGE.MEMBER_LEFT_GROUP(subjective.getName()));
		//				}
		//				eventToSubjective(subjective, GroupEvent.LEAVE);
		//			}
		//				break;
		//			case ENTER:
		//			{
		//				eventToSubjective(subjective, GroupEvent.ENTER);
		//				for (Player member : this.getMembers())
		//				{
		//					if (!subjective.equals(member))
		//						PacketSendUtility.sendPacket(member, new SM_GROUP_MEMBER_INFO(this, subjective, groupEvent));
		//				}
		//			}
		//				break;
		//			default:
		//			{
		//				for (Player member : this.getMembers())
		//				{
		//					if (!subjective.equals(member))
		//						PacketSendUtility.sendPacket(member, new SM_GROUP_MEMBER_INFO(this, subjective, groupEvent));
		//				}
		//			}
		//				break;
		//		}
	}

	//	private void eventToSubjective(Player subjective, GroupEvent groupEvent)
	//	{
	//				for (Player member : getMembers())
	//				{
	//					if (!subjective.equals(member))
	//						PacketSendUtility.sendPacket(subjective, new SM_GROUP_MEMBER_INFO(this, member, groupEvent));
	//				}
	//				if (groupEvent == GroupEvent.LEAVE)
	//					PacketSendUtility.sendPacket(subjective, SM_SYSTEM_MESSAGE.YOU_LEFT_GROUP());
	//	}

	public void setLootGroupRules(LootGroupRules lgr)
	{
		//		this.lootGroupRules = lgr;
		//		for (Player member : getMembers())
		//			PacketSendUtility.sendPacket(member, new SM_GROUP_INFO(this));
	}

	/**
	 * @return the lootGroupRules
	 */
	public LootGroupRules getLootGroupRules()
	{
		return lootGroupRules;
	}

	@Override
	public boolean isFull()
	{
		return members.size() == 6;
	}

	@Override
	public String getName()
	{
		return "Player Group: " + getObjectId();
	}

	@Override
	public void getReward(Npc owner)
	{
		//		// Find Group Members and Determine Highest Level
		//		List<Player> players = new ArrayList<Player>();
		//		int partyLvlSum = 0;
		//		int highestLevel = 0;
		//		for (Player member : getMembers())
		//		{
		//			if (!member.isOnline())
		//				continue;
		//			if (MathUtil.isIn3dRange(member, owner, GroupConfig.GROUP_MAX_DISTANCE))
		//			{
		//				if (member.getLifeStats().isAlreadyDead())
		//					continue;
		//				players.add(member);
		//				partyLvlSum += member.getLevel();
		//				if (member.getLevel() > highestLevel)
		//					highestLevel = member.getLevel();
		//			}
		//		}
		//
		//		// All are dead or not nearby.
		//		if (players.size() == 0)
		//			return;
		//
		//		//AP reward
		//		int apRewardPerMember = 0;
		//		WorldType worldType = owner.getWorldType();
		//
		//		//WorldType worldType = sp.getWorld().getWorldMap(player.getWorldId()).getWorldType();
		//		if (worldType == WorldType.ABYSS)
		//		{
		//			// Split Evenly
		//			apRewardPerMember = Math.round(StatFunctions.calculateGroupAPReward(highestLevel, owner) / players.size());
		//		}
		//
		//		// Exp reward
		//		long expReward = StatFunctions.calculateGroupExperienceReward(highestLevel, owner);
		//
		//		// Party Bonus 2 members 10%, 3 members 20% ... 6 members 50%
		//		int bonus = 1;
		//		if (players.size() == 0)
		//			return;
		//		else if (players.size() > 0)
		//			bonus = 100 + ((players.size() - 1) * 10);
		//
		//		for (Player member : players)
		//		{
		//			// Exp reward
		//			long currentExp = member.getCommonData().getExp();
		//			long reward = (expReward * bonus * member.getLevel()) / (partyLvlSum * 100);
		//			reward *= member.getRates().getGroupXpRate();
		//			// Players 10 levels below highest member get 0 exp.
		//			if (highestLevel - member.getLevel() >= 10)
		//				reward = 0;
		//			member.getCommonData().setExp(currentExp + reward);
		//
		//			PacketSendUtility.sendPacket(member, SM_SYSTEM_MESSAGE.EXP(Long.toString(reward)));
		//
		//			// DP reward
		//			int currentDp = member.getCommonData().getDp();
		//			int dpReward = StatFunctions.calculateGroupDPReward(member, owner);
		//			member.getCommonData().setDp(dpReward + currentDp);
		//
		//			// AP reward
		//			if (apRewardPerMember > 0)
		//				member.getCommonData().addAp(Math.round(apRewardPerMember * member.getRates().getApNpcRate()));
		//
		//			QuestEngine.getInstance().onKill(new QuestEnv(owner, member, 0, 0));
		//		}
		//
		//		// Give Drop
		//		setInRangePlayers(players);
		//		Player leader = getLeader();
		//		DropService.getInstance().registerDrop(owner, leader, highestLevel);
	}

	/**
	 * @param inRangePlayers the inRangePlayers to set
	 */
	public void setInRangePlayers(List<Player> inRangePlayers)
	{
		this.inRangePlayers = inRangePlayers;
	}

	/**
	 * @return the inRangePlayers
	 */
	public List<Player> getInRangePlayers()
	{
		return inRangePlayers;
	}
}

/*
 * This file is part of aion-emu <aion-emu.com>.
 *
 *  aion-emu is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  aion-emu is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with aion-emu.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.aionemu.gameserver.utils;

import java.util.Collection;

import com.aionemu.commons.objects.filter.ObjectFilter;
import com.aionemu.gameserver.model.ChatType;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.legion.Legion;
import com.aionemu.gameserver.model.team.Team;
import com.aionemu.gameserver.model.team.interfaces.ITeamProperties;
import com.aionemu.gameserver.network.aion.AbstractAionServerPacket;
import com.aionemu.gameserver.network.aion.AionChannelHandler;
import com.aionemu.gameserver.network.aion.serverpackets.SM_MESSAGE;

/**
 * This class contains static methods, which are utility methods, all of them are interacting only with objects passed
 * as parameters.<br>
 * These methods could be placed directly into Player class, but we want to keep Player class as a pure data holder.<br>
 * 
 * @author Luno
 * 
 */
public class PacketSendUtility
{
	/**
	 * Sends message to player (used for system messages)
	 * 
	 * @param player
	 * @param msg
	 */
	public static void sendMessage(Player player, String msg)
	{
		sendPacket(player, new SM_MESSAGE(0, null, msg, ChatType.ANNOUNCEMENTS));
	}

	/**
	 * Sends message to player (used for system notices)
	 * 
	 * @param player
	 * @param msg
	 */
	public static void sendSysMessage(Player player, String msg)
	{
		sendPacket(player, new SM_MESSAGE(0, null, msg, ChatType.SYSTEM_NOTICE));
	}

	/**
	 * Send packet to this player.
	 * 
	 * @param player
	 * @param packet
	 */
	public static void sendPacket(Player player, AbstractAionServerPacket<AionChannelHandler> packet)
	{
		if (player.getClientConnection() != null)
			player.getClientConnection().sendPacket(packet);
	}

	/**
	 * Broadcast packet to all visible players.
	 * 
	 * @param player
	 * 
	 * @param packet
	 *            ServerPacket that will be broadcast
	 * @param toSelf
	 *            true if packet should also be sent to this player
	 */
	public static void broadcastPacket(Player player, AbstractAionServerPacket<AionChannelHandler> packet, boolean toSelf)
	{
		if (toSelf)
			sendPacket(player, packet);

		broadcastPacket(player, packet);
	}

	/**
	 * Broadcast packet to all visible players.
	 * 
	 * @param visibleObject
	 * @param packet
	 */
	public static void broadcastPacketAndReceive(VisibleObject visibleObject, AbstractAionServerPacket<AionChannelHandler> packet)
	{
		if (visibleObject instanceof Player)
			sendPacket((Player) visibleObject, packet);

		broadcastPacket(visibleObject, packet);
	}

	/**
	 * Broadcast packet to all Players from knownList of the given visible object.
	 * 
	 * @param visibleObject
	 * @param packet
	 */
	public static void broadcastPacket(VisibleObject visibleObject, AbstractAionServerPacket<AionChannelHandler> packet)
	{
		for (VisibleObject obj : visibleObject.getKnownList().getKnownObjects().values())
		{
			if (obj instanceof Player)
				sendPacket(((Player) obj), packet);
		}
	}

	/**
	 * Broadcasts packet to all visible players matching a filter
	 * 
	 * @param player
	 * 
	 * @param packet
	 *            ServerPacket to be broadcast
	 * @param toSelf
	 *            true if packet should also be sent to this player
	 * @param filter
	 *            filter determining who should be messaged
	 */
	public static void broadcastPacket(Player player, AbstractAionServerPacket<AionChannelHandler> packet, boolean toSelf, ObjectFilter<Player> filter)
	{
		if (toSelf)
		{
			sendPacket(player, packet);
		}

		for (VisibleObject obj : player.getKnownList().getKnownObjects().values())
		{
			if (obj instanceof Player)
			{
				Player target = (Player) obj;
				if (filter.acceptObject(target))
					sendPacket(target, packet);
			}
		}
	}

	/**
	 * Send packet for team members, if team is group send all group members or not only to the sender
	 * if team is alliance, and sender is a player, give all alli member packet but sender no
	 * if team is alliance, and sender is a group, give all other group member a packet.
	 * @param sender
	 * @param team
	 * @param packet
	 * @param tosender
	 */
	@SuppressWarnings(
	{ "unchecked", "rawtypes" })
	public static void broadcastPacketToTeam(ITeamProperties sender, Team team, AbstractAionServerPacket<AionChannelHandler> packet, boolean tosender)
	{
		if (tosender && sender instanceof Player)
			sendPacket((Player) sender, packet);

		for (ITeamProperties memb : (Collection<ITeamProperties>) team.getMembers())
		{
			if (memb instanceof Player)
			{
				Player player = (Player) memb;
				if (player.getObjectId() != sender.getObjectId())
					sendPacket(player, packet);
			}
			else if (memb instanceof Team)
			{
				for (ITeamProperties groupmemb : (Collection<ITeamProperties>) ((Team) memb).getMembers())
				{
					if (memb instanceof Player)
					{
						Player player = (Player) groupmemb;
						if (player.getPlayerGroup().getObjectId() != sender.getObjectId())
							sendPacket(player, packet);
					}
				}
			}
		}
	}

	/**
	 * Broadcasts packet to all legion members of a legion
	 * 
	 * @param legion
	 *            Legion to broadcast packet to
	 * @param packet
	 *            ServerPacket to be broadcast
	 */
	public static void broadcastPacketToLegion(Legion legion, AbstractAionServerPacket<AionChannelHandler> packet)
	{
		for (Player onlineLegionMember : legion.getOnlineLegionMembers())
		{
			sendPacket(onlineLegionMember, packet);
		}
	}

	public static void broadcastPacketToLegion(Legion legion, AbstractAionServerPacket<AionChannelHandler> packet, int playerObjId)
	{
		for (Player onlineLegionMember : legion.getOnlineLegionMembers())
		{
			if (onlineLegionMember.getObjectId() != playerObjId)
				sendPacket(onlineLegionMember, packet);
		}
	}
}

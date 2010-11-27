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
package com.aionemu.gameserver.network.aion.serverpackets;

import org.apache.log4j.Logger;

import com.aionemu.gameserver.model.gameobjects.player.Friend;
import com.aionemu.gameserver.network.aion.AbstractAionServerPacket;
import com.aionemu.gameserver.network.aion.AionChannelHandler;

/**
 * Sent to update a player's status in a friendlist
 * @author Lyahim, Ben
 *
 */
public class SM_FRIEND_UPDATE extends AbstractAionServerPacket<AionChannelHandler>
{
	private int				friendObjId;

	private static Logger	log	= Logger.getLogger(SM_FRIEND_UPDATE.class);

	public SM_FRIEND_UPDATE(int friendObjId)
	{
		this.friendObjId = friendObjId;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void writeImpl(AionChannelHandler cHandler)
	{
		Friend f = cHandler.getActivePlayer().getFriendList().getFriend(friendObjId);
		if (f == null)
			log.debug("Attempted to update friend list status of " + friendObjId + " for " + cHandler.getActivePlayer().getName()
					+ " - object ID not found on friend list");
		else
		{
			writeS(f.getName());
			writeD(f.getLevel());
			writeD(f.getPlayerClass().getClassId());
			writeC(f.isOnline() ? 1 : 0); // Online status - No idea why this and f.getStatus are used
			writeD(f.getMapId());
			writeD(f.getLastOnlineTime()); // Date friend was last online as a Unix timestamp.
			writeS(f.getNote());
			writeC(f.getStatus().getIntValue());
		}
	}
}

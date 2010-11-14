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

import com.aionemu.gameserver.model.gameobjects.player.Friend;
import com.aionemu.gameserver.model.gameobjects.player.FriendList;
import com.aionemu.gameserver.network.aion.AbstractAionServerPacket;
import com.aionemu.gameserver.network.aion.AionChannelHandler;

/**
 * Sends a friend list to the client
 * @author Lyahim, Ben
 *
 */
public class SM_FRIEND_LIST extends AbstractAionServerPacket<AionChannelHandler>
{
	
	@Override
	protected void writeImpl(AionChannelHandler cHandler)
	{
		FriendList list = cHandler.getActivePlayer().getFriendList();

		writeH((0 - list.getSize()));
		writeC( 0); // Unk

        for (Friend friend : list)
        {
            writeS(friend.getName());
            writeD(friend.getLevel());
            writeD(friend.getPlayerClass().getClassId());
            writeC( 1); // Unk
            writeD(friend.getMapId());
            writeD(friend.getLastOnlineTime()); // Date friend was last online as a Unix timestamp.
            writeS(friend.getNote()); // Friend note
            writeC( friend.getStatus().getIntValue());
        }
	}
}

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
package com.aionemu.gameserver.network.chatserver.serverpackets;

import com.aionemu.commons.network.netty.packet.AbstractServerPacket;
import com.aionemu.gameserver.network.chatserver.ChatServerChannelHandler;

/**
 * @author ATracer, Lyahim
 */
public class SM_CS_PLAYER_AUTH extends AbstractServerPacket<ChatServerChannelHandler>
{
	private int		playerId;
	private String	playerLogin;

	public SM_CS_PLAYER_AUTH(int playerId, String playerLogin)
	{
		this.playerId = playerId;
		this.playerLogin = playerLogin;
	}

	@Override
	protected void writeImpl(ChatServerChannelHandler cHandler)
	{
		writeD(playerId);
		writeS(playerLogin);
	}
}

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
package com.aionemu.chatserver.network.gameserver.clientpackets;

import org.apache.log4j.Logger;

import com.aionemu.chatserver.network.gameserver.GameServerChannelHandler;
import com.aionemu.chatserver.service.ChatService;
import com.aionemu.commons.network.netty.packet.AbstractClientPacket;

/**
 * @author ATracer, Lyahim
 */
public class CM_PLAYER_LOGOUT extends AbstractClientPacket<GameServerChannelHandler>
{
	private static final Logger	log	= Logger.getLogger(CM_PLAYER_LOGOUT.class);

	private int					playerId;

	public CM_PLAYER_LOGOUT(int opcode)
	{
		super(opcode);
	}

	@Override
	protected void readImpl()
	{
		playerId = readD();
	}

	@Override
	protected void runImpl()
	{
		ChatService.getInstance().playerLogout(playerId);
		log.info("Player logout " + playerId);
	}
}

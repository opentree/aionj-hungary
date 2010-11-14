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

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import org.apache.log4j.Logger;

import com.aionemu.chatserver.model.ChatClient;
import com.aionemu.chatserver.network.gameserver.GameServerChannelHandler;
import com.aionemu.chatserver.network.gameserver.serverpackets.SM_PLAYER_AUTH_RESPONSE;
import com.aionemu.chatserver.service.ChatService;
import com.aionemu.commons.network.netty.packet.AbstractClientPacket;

/**
 * @author ATracer, Lyahim
 */
public class CM_PLAYER_AUTH extends AbstractClientPacket<GameServerChannelHandler>
{
	private static final Logger	log	= Logger.getLogger(CM_PLAYER_AUTH.class);

	private int					playerId;
	
	private String				playerLogin;

	public CM_PLAYER_AUTH(int opcode)
	{
		super(opcode);
	}

	@Override
	protected void readImpl()
	{
		playerId = readD();
		playerLogin = readS();
	}

	@Override
	protected void runImpl()
	{
		GameServerChannelHandler cch = (GameServerChannelHandler) getChannelHandler();
		ChatClient chatClient = null;
		try
		{
			chatClient = ChatService.getInstance().registerPlayer(playerId, playerLogin);
		}
		catch (NoSuchAlgorithmException e)
		{
			log.error("Error registering player on ChatServer: " + e.getMessage());
		}
		catch (UnsupportedEncodingException e)
		{
			log.error("Error registering player on ChatServer: " + e.getMessage());
		}

		if (chatClient != null)
		{
			cch.sendPacket(new SM_PLAYER_AUTH_RESPONSE(chatClient));
		}
		else
		{
			log.info("Player was not authed " + playerId);
		}
	}
}

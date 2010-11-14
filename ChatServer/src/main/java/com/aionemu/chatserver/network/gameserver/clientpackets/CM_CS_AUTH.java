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

import com.aionemu.chatserver.network.gameserver.GameServerChannelHandler;
import com.aionemu.chatserver.network.gameserver.GsAuthResponse;
import com.aionemu.chatserver.network.gameserver.serverpackets.SM_GS_AUTH_RESPONSE;
import com.aionemu.chatserver.service.GameServerService;
import com.aionemu.commons.network.netty.State;
import com.aionemu.commons.network.netty.packet.AbstractClientPacket;

/**
 * 
 * @author ATracer, Lyahim
 * 
 */
public class CM_CS_AUTH extends AbstractClientPacket<GameServerChannelHandler>
{
	/**
	 * Password for authentication
	 */
	private String				password;

	/**
	 * Id of GameServer
	 */
	private byte				gameServerId;

	/**
	 * Default address for server
	 */
	private byte[]				defaultAddress;

	public CM_CS_AUTH(int opcode)
	{
		super(opcode);
	}

	@Override
	protected void readImpl()
	{
		gameServerId = (byte) readC();

		defaultAddress = readB(readC());
		password = readS();
	}

	@Override
	protected void runImpl()
	{
		GameServerChannelHandler cch = getChannelHandler();
		
		GsAuthResponse resp = GameServerService.getInstance().registerGameServer(cch, gameServerId, defaultAddress, password);

		switch (resp)
		{
			case AUTHED:
				cch.setState(State.AUTHED);
				cch.sendPacket(new SM_GS_AUTH_RESPONSE(resp));
				break;
			case NOT_AUTHED:
				cch.sendPacket(new SM_GS_AUTH_RESPONSE(resp));
				break;
			default:
				cch.close(new SM_GS_AUTH_RESPONSE(resp));
		}
	}
}

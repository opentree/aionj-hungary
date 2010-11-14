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
package com.aionemu.chatserver.network.gameserver.serverpackets;

import com.aionemu.chatserver.configs.Config;
import com.aionemu.chatserver.network.gameserver.GameServerChannelHandler;
import com.aionemu.chatserver.network.gameserver.GsAuthResponse;
import com.aionemu.commons.network.netty.packet.AbstractServerPacket;

/**
 * @author ATracer, Lyahim
 */
public class SM_GS_AUTH_RESPONSE extends AbstractServerPacket<GameServerChannelHandler>
{
	private GsAuthResponse	response;

	public SM_GS_AUTH_RESPONSE(GsAuthResponse resp)
	{
		this.response = resp;
	}

	@Override
	protected void writeImpl(GameServerChannelHandler cHandler)
	{
		writeC(response.getResponseId());
		writeB(cHandler.getByteIP());
		writeH(Config.GAMESERVER_ADDRESS.getPort());
	}

}

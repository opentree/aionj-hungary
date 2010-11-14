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
package com.aionemu.chatserver.network.aion;

import com.aionemu.chatserver.network.aion.clientpackets.CM_CHANNEL_MESSAGE;
import com.aionemu.chatserver.network.aion.clientpackets.CM_CHANNEL_REQUEST;
import com.aionemu.chatserver.network.aion.clientpackets.CM_PLAYER_AUTH;
import com.aionemu.chatserver.network.aion.serverpackets.SM_CHANNEL_MESSAGE;
import com.aionemu.chatserver.network.aion.serverpackets.SM_CHANNEL_RESPONSE;
import com.aionemu.chatserver.network.aion.serverpackets.SM_PLAYER_AUTH_RESPONSE;
import com.aionemu.commons.network.netty.State;
import com.aionemu.commons.network.netty.handler.AbstractPacketHandlerFactory;

/**
 * @author lyahim
 *
 */
public class AionPacketHandlerFactory extends AbstractPacketHandlerFactory<AionChannelHandler>
{	
	/**
	 * Creates new instance of <tt>AionPacketHandlerFactory</tt><br>
	 */
	public AionPacketHandlerFactory()
	{
		super(null, null);
		//Client Packet's
		addPacket(new CM_CHANNEL_MESSAGE(0x18), State.AUTHED);
		addPacket(new CM_CHANNEL_REQUEST(0x10), State.AUTHED);
		addPacket(new CM_PLAYER_AUTH(0x05), State.CONNECTED);
		
		//Server Packet's
		addPacket(SM_CHANNEL_MESSAGE.class, 0x1A);
		addPacket(SM_CHANNEL_RESPONSE.class, 0x11);
		addPacket(SM_PLAYER_AUTH_RESPONSE.class, 0x02);

	}
}

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
package com.aionemu.loginserver.network.aion;

import com.aionemu.commons.network.netty.State;
import com.aionemu.commons.network.netty.handler.AbstractPacketHandlerFactory;
import com.aionemu.loginserver.network.aion.clientpackets.CM_AUTH_GG;
import com.aionemu.loginserver.network.aion.clientpackets.CM_LOGIN;
import com.aionemu.loginserver.network.aion.clientpackets.CM_PLAY;
import com.aionemu.loginserver.network.aion.clientpackets.CM_SERVER_LIST;
import com.aionemu.loginserver.network.aion.clientpackets.CM_UPDATE_SESSION;
import com.aionemu.loginserver.network.aion.serverpackets.SM_AUTH_GG;
import com.aionemu.loginserver.network.aion.serverpackets.SM_INIT;
import com.aionemu.loginserver.network.aion.serverpackets.SM_LOGIN_FAIL;
import com.aionemu.loginserver.network.aion.serverpackets.SM_LOGIN_OK;
import com.aionemu.loginserver.network.aion.serverpackets.SM_PLAY_FAIL;
import com.aionemu.loginserver.network.aion.serverpackets.SM_PLAY_OK;
import com.aionemu.loginserver.network.aion.serverpackets.SM_SERVER_LIST;
import com.aionemu.loginserver.network.aion.serverpackets.SM_UPDATE_SESSION;

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
		// Client Packets
		addPacket(new CM_PLAY(0x02), State.AUTHED);
		addPacket(new CM_SERVER_LIST(0x05), State.AUTHED);
		addPacket(new CM_LOGIN(0x0B), State.AUTHED_GG);
		addPacket(new CM_AUTH_GG(0x07), State.CONNECTED);
		addPacket(new CM_UPDATE_SESSION(0x08), State.CONNECTED);

		// Server Packets
		addPacket(SM_INIT.class, 0x00);
		addPacket(SM_LOGIN_FAIL.class, 0x01);
		addPacket(SM_LOGIN_OK.class, 0x03);
		addPacket(SM_SERVER_LIST.class, 0x04);
		addPacket(SM_PLAY_FAIL.class, 0x06);
		addPacket(SM_PLAY_OK.class, 0x07);
		addPacket(SM_AUTH_GG.class, 0x0b);
		addPacket(SM_UPDATE_SESSION.class, 0x0c);

	}

}

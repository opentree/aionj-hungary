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
package com.aionemu.gameserver.network.loginserver;

import com.aionemu.commons.network.netty.State;
import com.aionemu.commons.network.netty.handler.AbstractPacketHandlerFactory;
import com.aionemu.gameserver.network.loginserver.clientpackets.CM_ACCOUNT_RECONNECT_KEY;
import com.aionemu.gameserver.network.loginserver.clientpackets.CM_ACOUNT_AUTH_RESPONSE;
import com.aionemu.gameserver.network.loginserver.clientpackets.CM_BAN_RESPONSE;
import com.aionemu.gameserver.network.loginserver.clientpackets.CM_GS_AUTH_RESPONSE;
import com.aionemu.gameserver.network.loginserver.clientpackets.CM_LS_CONTROL_RESPONSE;
import com.aionemu.gameserver.network.loginserver.clientpackets.CM_REQUEST_KICK_ACCOUNT;
import com.aionemu.gameserver.network.loginserver.serverpackets.SM_ACCOUNT_AUTH;
import com.aionemu.gameserver.network.loginserver.serverpackets.SM_ACCOUNT_DISCONNECTED;
import com.aionemu.gameserver.network.loginserver.serverpackets.SM_ACCOUNT_LIST;
import com.aionemu.gameserver.network.loginserver.serverpackets.SM_ACCOUNT_RECONNECT_KEY;
import com.aionemu.gameserver.network.loginserver.serverpackets.SM_BAN;
import com.aionemu.gameserver.network.loginserver.serverpackets.SM_GS_AUTH;
import com.aionemu.gameserver.network.loginserver.serverpackets.SM_LS_CONTROL;

/**
 * @author lyahim
 *
 */
public class LoginServerPacketHandlerFactory extends AbstractPacketHandlerFactory<LoginServerChannelHandler>
{
	public LoginServerPacketHandlerFactory()
	{
		super(null, null);
		//Client Packet's
		addPacket(new CM_ACCOUNT_RECONNECT_KEY(0x03), State.AUTHED);
		addPacket(new CM_ACOUNT_AUTH_RESPONSE(0x01), State.AUTHED);
		addPacket(new CM_GS_AUTH_RESPONSE(0x00), State.CONNECTED);
		addPacket(new CM_REQUEST_KICK_ACCOUNT(0x02), State.AUTHED);
		addPacket(new CM_LS_CONTROL_RESPONSE(0x04), State.AUTHED);
		addPacket(new CM_BAN_RESPONSE(0x05), State.AUTHED);
		
		//Server Packet's
		addPacket(SM_GS_AUTH.class, 0x00);
		addPacket(SM_ACCOUNT_AUTH.class, 0x01);
		addPacket(SM_ACCOUNT_RECONNECT_KEY.class, 0x02);
		addPacket(SM_ACCOUNT_DISCONNECTED.class, 0x03);
		addPacket(SM_ACCOUNT_LIST.class, 0x04);
		addPacket(SM_LS_CONTROL.class, 0x05);		
		addPacket(SM_BAN.class, 0x06);
	}
}

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
package com.aionemu.loginserver.network.gameserver;

import com.aionemu.commons.network.netty.handler.AbstractChannelHandler.State;
import com.aionemu.commons.network.netty.handler.AbstractPacketHandlerFactory;
import com.aionemu.loginserver.network.gameserver.clientpackets.CM_ACCOUNT_AUTH;
import com.aionemu.loginserver.network.gameserver.clientpackets.CM_ACCOUNT_DISCONNECTED;
import com.aionemu.loginserver.network.gameserver.clientpackets.CM_ACCOUNT_LIST;
import com.aionemu.loginserver.network.gameserver.clientpackets.CM_ACCOUNT_RECONNECT_KEY;
import com.aionemu.loginserver.network.gameserver.clientpackets.CM_BAN;
import com.aionemu.loginserver.network.gameserver.clientpackets.CM_GS_AUTH;
import com.aionemu.loginserver.network.gameserver.clientpackets.CM_LS_CONTROL;
import com.aionemu.loginserver.network.gameserver.serverpackets.SM_ACCOUNT_AUTH_RESPONSE;
import com.aionemu.loginserver.network.gameserver.serverpackets.SM_ACCOUNT_RECONNECT_KEY;
import com.aionemu.loginserver.network.gameserver.serverpackets.SM_BAN_RESPONSE;
import com.aionemu.loginserver.network.gameserver.serverpackets.SM_GS_AUTH_RESPONSE;
import com.aionemu.loginserver.network.gameserver.serverpackets.SM_LS_CONTROL_RESPONSE;
import com.aionemu.loginserver.network.gameserver.serverpackets.SM_REQUEST_KICK_ACCOUNT;

/**
 * @author lyahim
 *
 */
public class GameServerPacketHandlerFactory extends AbstractPacketHandlerFactory<GameServerChannelHandler>
{
	public GameServerPacketHandlerFactory()
	{
		super(null, null);
		//Client Packets
		addPacket(new CM_GS_AUTH(0x00), State.CONNECTED);
		addPacket(new CM_ACCOUNT_AUTH(0x01), State.AUTHED);
		addPacket(new CM_ACCOUNT_RECONNECT_KEY(0x02), State.AUTHED);
		addPacket(new CM_ACCOUNT_DISCONNECTED(0x03), State.AUTHED);
		addPacket(new CM_ACCOUNT_LIST(0x04), State.AUTHED);
		addPacket(new CM_LS_CONTROL(0x05), State.AUTHED);
		addPacket(new CM_BAN(0x06), State.AUTHED);
		
		//Server Packets
		addPacket(SM_GS_AUTH_RESPONSE.class, 0x00);
		addPacket(SM_ACCOUNT_AUTH_RESPONSE.class, 0x01);
		addPacket(SM_REQUEST_KICK_ACCOUNT.class, 0x02);
		addPacket(SM_ACCOUNT_RECONNECT_KEY.class, 0x03);
		addPacket(SM_LS_CONTROL_RESPONSE.class, 0x04);
		addPacket(SM_BAN_RESPONSE.class, 0x05);
	}
}

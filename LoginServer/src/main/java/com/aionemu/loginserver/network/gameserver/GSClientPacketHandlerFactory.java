/*
 * This file is part of aion-lightning <aion-lightning.com>.
 *
 *  aion-lightning is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  aion-lightning is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with aion-lightning.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.aionemu.loginserver.network.gameserver;

import com.aionemu.commons.netty.State;
import com.aionemu.loginserver.network.gameserver.clientpackets.*;

/**
 * @author Mr. Poke
 *
 */
public class GSClientPacketHandlerFactory
{
	private GsPacketHandler handler;

	public static final GSClientPacketHandlerFactory getInstance()
	{
		return SingletonHolder.instance;
	}
	
	/**
	 * Creates new instance of <tt>AionPacketHandlerFactory</tt><br>
	 */
	private GSClientPacketHandlerFactory()
	{
		handler	= new GsPacketHandler();
		addPacket(new CM_GS_AUTH(0x00), State.CONNECTED);
		addPacket(new CM_ACCOUNT_AUTH(0x01), State.AUTHED);
		addPacket(new CM_ACCOUNT_RECONNECT_KEY(0x02), State.AUTHED);
		addPacket(new CM_ACCOUNT_DISCONNECTED(0x03), State.AUTHED);
		addPacket(new CM_ACCOUNT_LIST(0x04), State.AUTHED);
		addPacket(new CM_LS_CONTROL(0x05), State.AUTHED);
		addPacket(new CM_BAN(0x06), State.AUTHED);
	}

	public GsPacketHandler getPacketHandler()
	{
		return handler;
	}

	private void addPacket(GsClientPacket prototype, State... states)
	{
		handler.addPacketPrototype(prototype, states);
	}

	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder
	{
		protected static final GSClientPacketHandlerFactory instance = new GSClientPacketHandlerFactory();
	}
}

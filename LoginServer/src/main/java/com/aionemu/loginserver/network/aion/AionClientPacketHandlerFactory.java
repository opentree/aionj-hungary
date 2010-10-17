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
package com.aionemu.loginserver.network.aion;

import com.aionemu.commons.netty.State;
import com.aionemu.loginserver.network.aion.clientpackets.*;

/**
 * @author Mr. Poke
 *
 */
public class AionClientPacketHandlerFactory
{

	private AionPacketHandler handler;

	public static final AionClientPacketHandlerFactory getInstance()
	{
		return SingletonHolder.instance;
	}
	
	/**
	 * Creates new instance of <tt>AionPacketHandlerFactory</tt><br>
	 */
	private AionClientPacketHandlerFactory()
	{
		handler	= new AionPacketHandler();
		addPacket(new CM_PLAY(0x02), State.AUTHED);
		addPacket(new CM_SERVER_LIST(0x05), State.AUTHED);
		addPacket(new CM_LOGIN(0x0B), State.AUTHED_GG);
		addPacket(new CM_AUTH_GG(0x07), State.CONNECTED);
		addPacket(new CM_UPDATE_SESSION(0x08), State.CONNECTED);
	}

	public AionPacketHandler getPacketHandler()
	{
		return handler;
	}

	private void addPacket(AionClientPacket prototype, State... states)
	{
		handler.addPacketPrototype(prototype, states);
	}

	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder
	{
		protected static final AionClientPacketHandlerFactory instance = new AionClientPacketHandlerFactory();
	}

}

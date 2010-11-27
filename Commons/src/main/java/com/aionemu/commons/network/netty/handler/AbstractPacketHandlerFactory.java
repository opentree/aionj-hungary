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
package com.aionemu.commons.network.netty.handler;

import org.jboss.netty.buffer.ChannelBuffer;

import com.aionemu.commons.network.netty.State;
import com.aionemu.commons.network.netty.packet.AbstractClientPacket;
import com.aionemu.commons.network.netty.packet.AbstractServerPacket;

/**
 * @author lyahim
 * 
 */
public class AbstractPacketHandlerFactory<T extends AbstractChannelHandler>
{
	private ClientPacketHandler<T>	cHandler;
	private ServerPacketHandler<T>	sHandler;

	protected AbstractPacketHandlerFactory(ServerPacketHandler<T> sph, ClientPacketHandler<T> cph)
	{
		if(sph == null)
			sHandler = new ServerPacketHandler<T>();
		else
			sHandler = sph;

		if(cph == null)
			cHandler = new ClientPacketHandler<T>();
		else
			cHandler = cph;
	}

	public void addPacket(Class<? extends AbstractServerPacket<T>> packetClass, int opcode)
	{
		sHandler.addPacketOpcode(packetClass, opcode);
	}

	public void addPacket(AbstractClientPacket<T> packetPrototype, State... states)
	{
		cHandler.addPacketPrototype(packetPrototype, states);
	}

	public int getServerPacketopCode(Class<? extends AbstractServerPacket<AbstractChannelHandler>> packetClass)
	{
		return sHandler.getOpCode(packetClass);
	}

	@SuppressWarnings("unchecked")
	public AbstractClientPacket<T> handleClientPacket(ChannelBuffer data, AbstractChannelHandler ch)
	{
		return cHandler.handle(data, (T) ch);
	}

}

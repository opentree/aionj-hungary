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

package com.aionemu.commons.netty.handler;

import javolution.util.FastMap;

import org.apache.log4j.Logger;
import org.jboss.netty.buffer.ChannelBuffer;

import com.aionemu.commons.netty.State;
import com.aionemu.commons.netty.packet.BaseClientPacket;

/**
 * @author Mr.Poke
 */
public class PacketHandler <T  extends AbstractChannelHandler>
{

	/**
	 * logger for this class
	 */
	private static final Logger							log					= Logger.getLogger(PacketHandler.class);

	private FastMap<State, FastMap<Integer, BaseClientPacket<T>>>	packetsPrototypes	= new FastMap<State, FastMap<Integer, BaseClientPacket<T>>>();

	/**
	 * Reads one packet from given ByteBuffer
	 * 
	 * @param data
	 * @param client
	 * @return AionClientPacket object from binary data
	 */
	public BaseClientPacket<T> handle(ChannelBuffer data, T client)
	{
		State state = client.getState();
		int id = data.readByte() & 0xff;
		return getPacket(state, id, data, client);
	}

	public void addPacketPrototype(BaseClientPacket<T> packetPrototype, State... states)
	{
		for(State state : states)
		{
			FastMap<Integer, BaseClientPacket<T>> pm = packetsPrototypes.get(state);
			if(pm == null)
			{
				pm = new FastMap<Integer, BaseClientPacket<T>>();
				packetsPrototypes.put(state, pm);
			}
			pm.put(packetPrototype.getOpCode(), packetPrototype);
		}
	}

	@SuppressWarnings("unchecked")
	protected BaseClientPacket<T> getPacket(State state, int id, ChannelBuffer buf, AbstractChannelHandler con)
	{
		BaseClientPacket<T> prototype = null;

		FastMap<Integer, BaseClientPacket<T>> pm = packetsPrototypes.get(state);
		if(pm != null)
		{
			prototype = pm.get(id);
		}

		if(prototype == null)
		{
			unknownPacket(state, id);
			return null;
		}

		BaseClientPacket<T> res =  (BaseClientPacket<T>) prototype.clonePacket();
		res.setBuf(buf);
		res.setConnection((T) con);

		return res;
	}

	/**
	 * Logs unknown packet.
	 * 
	 * @param state
	 * @param id
	 * @param data
	 */
	private void unknownPacket(State state, int id)
	{
		log.debug(String.format("[UNKNOWN PACKET] : 0x%02X, state=%s %n", id, state.toString()));
	}

}

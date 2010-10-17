/**
 * This file is part of aion-emu <aion-emu.com>.
 *
 *  aion-emu is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  aion-emu is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with aion-emu.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.aionemu.loginserver.network.aion;

import javolution.util.FastMap;

import org.apache.log4j.Logger;
import org.jboss.netty.buffer.ChannelBuffer;

import com.aionemu.commons.netty.State;

/**
 * @author -Nemesiss-
 */
public class AionPacketHandler
{

	/**
	 * logger for this class
	 */
	private static final Logger							log					= Logger.getLogger(AionPacketHandler.class);

	private FastMap<State, FastMap<Integer, AionClientPacket>>	packetsPrototypes	= new FastMap<State, FastMap<Integer, AionClientPacket>>();

	/**
	 * Reads one packet from given ByteBuffer
	 * 
	 * @param data
	 * @param client
	 * @return AionClientPacket object from binary data
	 */
	public AionClientPacket handle(ChannelBuffer data, AionConnection client)
	{
		State state = client.getState();
		int id = data.readByte() & 0xff;
		return getPacket(state, id, data, client);
	}

	public void addPacketPrototype(AionClientPacket packetPrototype, State... states)
	{
		for(State state : states)
		{
			FastMap<Integer, AionClientPacket> pm = packetsPrototypes.get(state);
			if(pm == null)
			{
				pm = new FastMap<Integer, AionClientPacket>();
				packetsPrototypes.put(state, pm);
			}
			pm.put(packetPrototype.getOpCode(), packetPrototype);
		}
	}

	private AionClientPacket getPacket(State state, int id, ChannelBuffer buf, AionConnection con)
	{
		AionClientPacket prototype = null;

		FastMap<Integer, AionClientPacket> pm = packetsPrototypes.get(state);
		if(pm != null)
		{
			prototype = pm.get(id);
		}

		if(prototype == null)
		{
			unknownPacket(state, id);
			return null;
		}

		AionClientPacket res = prototype.clonePacket();
		res.setBuf(buf);
		res.setConnection(con);

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

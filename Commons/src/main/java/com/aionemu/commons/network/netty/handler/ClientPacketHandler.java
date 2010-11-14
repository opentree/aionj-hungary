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

import javolution.util.FastMap;

import org.apache.log4j.Logger;
import org.jboss.netty.buffer.ChannelBuffer;

import com.aionemu.commons.network.netty.State;
import com.aionemu.commons.network.netty.packet.AbstractClientPacket;

/**
 * @author lyahim
 *
 */
public class ClientPacketHandler<T extends AbstractChannelHandler>
{
    private static final Logger log = Logger.getLogger(ClientPacketHandler.class);
    
    private FastMap<State, FastMap<Integer, AbstractClientPacket<T>>> packetsPrototypes = new FastMap<State, FastMap<Integer, AbstractClientPacket<T>>>();

    public void addPacketPrototype(AbstractClientPacket<T> packetPrototype, State... states)
    {
		for(State state : states)
		{
			FastMap<Integer, AbstractClientPacket<T>> pm = packetsPrototypes.get(state);
			if(pm == null)
			{
				pm = new FastMap<Integer, AbstractClientPacket<T>>();
				packetsPrototypes.put(state, pm);
			}
			pm.put(packetPrototype.getOpCode(), packetPrototype);
		}
    }

    @SuppressWarnings("unchecked")
	protected AbstractClientPacket<T> getPacket(int id, ChannelBuffer buf, AbstractChannelHandler ch)
    {
        AbstractClientPacket<T> prototype = null;
        
        FastMap<Integer, AbstractClientPacket<T>> pm = packetsPrototypes.get(ch.getState());
        
        if(pm != null)
            prototype = pm.get(Integer.valueOf(id));
        
        if(prototype == null)
        {
            unknownPacket(ch.getState(), id);
            return null;
        } 
        else
        {
			AbstractClientPacket<T> res = (AbstractClientPacket<T>) prototype.clonePacket();
            res.setBuf(buf);
            res.setChannelHandler((T) ch);
            return res;
        }
    }

    public AbstractClientPacket<T> handle(ChannelBuffer data, T ch)
    {
        int id = data.readByte() & 0xff;
        return getPacket(id, data, ch);
    }
    
    protected static void unknownPacket(State state, int id)
    {
		log.warn(String.format("[UNKNOWN PACKET] : received 0x%02X, state=%s %n", id, state.toString()));
    }
}

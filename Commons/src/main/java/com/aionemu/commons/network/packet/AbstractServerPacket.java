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
package com.aionemu.commons.network.packet;

import java.nio.ByteOrder;

import org.jboss.netty.buffer.ChannelBuffers;

import com.aionemu.commons.network.netty.handler.AbstractChannelHandler;

/**
 * @author lyahim
 *
 */
public abstract class AbstractServerPacket<T extends AbstractChannelHandler> extends AbstractPacket
{

    public AbstractServerPacket()
    {
		this.setBuf(ChannelBuffers.buffer(ByteOrder.LITTLE_ENDIAN, 2 * 8192));
    }

    protected final void writeD(int value)
    {
        buf.writeInt(value);
    }

    protected final void writeH(int value)
    {
        buf.writeShort((short)value);
    }

    protected final void writeC(int value)
    {
        buf.writeByte((byte)value);
    }

    protected final void writeDF(double value)
    {
        buf.writeDouble(value);
    }

    protected final void writeF(float value)
    {
        buf.writeFloat(value);
    }

    protected final void writeB(byte data[])
    {
        buf.writeBytes(data);
    }

    protected final void writeS(String text)
    {
        if(text == null)
        {
            buf.writeChar(0);
        } else
        {
            int len = text.length();
            for(int i = 0; i < len; i++)
                buf.writeChar(text.charAt(i));

            buf.writeChar(0);
        }
    }

    protected final void writeQ(long data)
    {
        buf.writeLong(data);
    }

    public void write(T channelhandler)
    {
		writeH((short) 0);
		writeC(getOpCode());
        writeImpl(channelhandler);
    }

    protected abstract void writeImpl(T channelhandler);
}

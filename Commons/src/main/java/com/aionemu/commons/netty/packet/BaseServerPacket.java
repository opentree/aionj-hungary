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
package com.aionemu.commons.netty.packet;

import java.nio.ByteOrder;

import org.jboss.netty.buffer.ChannelBuffers;

import com.aionemu.commons.netty.handler.AbstractChannelHandler;

public abstract class BaseServerPacket extends AbstractPacket
{

	/**
	 * 
	 * @param opCode
	 */
	public BaseServerPacket()
	{
		super(0);
		this.setBuf(ChannelBuffers.buffer(ByteOrder.LITTLE_ENDIAN, 2 * 8192));
	}

	/**
	 * 
	 * @param buf
	 * @param value
	 */
	protected final void writeD(int value)
	{
		buf.writeInt(value);
	}

	/**
	 * 
	 * @param buf
	 * @param value
	 */
	protected final void writeH(int value)
	{
		buf.writeShort((short) value);
	}

	/**
	 * 
	 * @param buf
	 * @param value
	 */
	protected final void writeC(int value)
	{
		buf.writeByte((byte) value);
	}

	/**
	 * Write double to buffer.
	 * 
	 * @param buf
	 * @param value
	 */
	protected final void writeDF(double value)
	{
		buf.writeDouble(value);
	}

	/**
	 * Write float to buffer.
	 * 
	 * @param buf
	 * @param value
	 */
	protected final void writeF(float value)
	{
		buf.writeFloat(value);
	}

	/**
	 * 
	 * @param buf
	 * @param data
	 */
	protected final void writeB(byte[] data)
	{
		buf.writeBytes(data);
	}

	/**
	 * Write String to buffer
	 * 
	 * @param buf
	 * @param text
	 */
	protected final void writeS(String text)
	{
		if (text == null)
		{
			buf.writeChar('\000');
		}
		else
		{
			final int len = text.length();
			for (int i = 0; i < len; i++)
				buf.writeChar(text.charAt(i));
			buf.writeChar('\000');
		}
	}

	/**
	 * 
	 * @param buf
	 * @param data
	 */
	protected final void writeUTF(String text)
	{
		int len = text.length();
		for (int i = 0; i < len; i++)
			buf.writeChar(text.charAt(i));
	}

	/**
	 * 
	 * @param buf
	 * @param data
	 */
	protected final void writeQ(long data)
	{
		buf.writeLong(data);
	}

	/**
	 * 
	 * @param client
	 */
	public void write(AbstractChannelHandler client)
	{
	}

}

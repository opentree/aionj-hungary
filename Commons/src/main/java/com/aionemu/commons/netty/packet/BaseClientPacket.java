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

import javolution.text.TextBuilder;

import org.apache.log4j.Logger;

import com.aionemu.commons.netty.handler.AbstractChannelHandler;

public abstract class BaseClientPacket<T extends AbstractChannelHandler> extends AbstractPacket implements Runnable,
	Cloneable
{
	private static final Logger	log	= Logger.getLogger(BaseClientPacket.class);

	/**
	 * Owner of this packet.
	 */
	private T					client;

	/**
	 * 
	 * @param channelBuffer
	 * @param opCode
	 */
	public BaseClientPacket(int opCode)
	{
		super(opCode);
	}

	public int getRemainingBytes()
	{
		return buf.readableBytes();
	}

	/**
	 * Perform packet read
	 * 
	 * @return boolean
	 */
	public boolean read()
	{
		try
		{
			readImpl();
			if(getRemainingBytes() > 0)
				log.debug("Packet " + this + " not fully readed!");
			return true;
		}
		catch(Exception ex)
		{
			log.error("Reading failed for packet " + this, ex);
			return false;
		}

	}

	/**
	 * Perform packet action
	 */
	public void run()
	{
		try
		{
			runImpl();
		}
		catch(Exception ex)
		{
			log.error("Running failed for packet " + this, ex);
		}
	}

	protected abstract void readImpl();

	protected abstract void runImpl();

	/**
	 * Read int from this packet buffer.
	 * 
	 * @return int
	 */
	protected final int readD()
	{
		try
		{
			return buf.readInt();
		}
		catch(Exception e)
		{
			log.error("Missing D for: " + this);
		}
		return 0;
	}

	/**
	 * Read byte from this packet buffer.
	 * 
	 * @return int
	 */
	protected final int readC()
	{
		try
		{
			return buf.readByte() & 0xFF;
		}
		catch(Exception e)
		{
			log.error("Missing C for: " + this);
		}
		return 0;
	}

	/**
	 * Read short from this packet buffer.
	 * 
	 * @return int
	 */
	protected final int readH()
	{
		try
		{
			return buf.readShort() & 0xFFFF;
		}
		catch(Exception e)
		{
			log.error("Missing H for: " + this);
		}
		return 0;
	}

	/**
	 * Read double from this packet buffer.
	 * 
	 * @return double
	 */
	protected final double readDF()
	{
		try
		{
			return buf.readDouble();
		}
		catch(Exception e)
		{
			log.error("Missing DF for: " + this);
		}
		return 0;
	}

	/**
	 * Read double from this packet buffer.
	 * 
	 * @return double
	 */
	protected final float readF()
	{
		try
		{
			return buf.readFloat();
		}
		catch(Exception e)
		{
			log.error("Missing F for: " + this);
		}
		return 0;
	}

	/**
	 * Read long from this packet buffer.
	 * 
	 * @return long
	 */
	protected final long readQ()
	{
		try
		{
			return buf.readLong();
		}
		catch(Exception e)
		{
			log.error("Missing Q for: " + this);
		}
		return 0;
	}

	/**
	 * Read String from this packet buffer.
	 * 
	 * @return String
	 */
	protected final String readS()
	{
		TextBuilder tb = TextBuilder.newInstance();
		char ch;
		try
		{
			while((ch = buf.readChar()) != 0)
				tb.append(ch);
		}
		catch(Exception e)
		{
			log.error("Missing S for: " + this);
		}

		String toString = tb.toString();

		TextBuilder.recycle(tb);

		return toString;
	}

	/**
	 * Read String from this packet buffer.
	 * 
	 * @return String
	 */
	protected final String readUTF(int lenght)
	{
		TextBuilder tb = TextBuilder.newInstance();
		try
		{
			for(int index = 0; index < lenght; index++)
			{
				tb.append(buf.getChar(index));
			}
		}
		catch(Exception e)
		{
			log.error("Missing S for: " + this);
		}
		return tb.toString();
	}

	/**
	 * Read n bytes from this packet buffer, n = length.
	 * 
	 * @param length
	 * @return byte[]
	 */
	protected final byte[] readB(int length)
	{
		byte[] result = new byte[length];
		try
		{
			buf.readBytes(result);
		}
		catch(Exception e)
		{
			log.error("Missing byte[] for: " + this);
		}
		return result;
	}

	/**
	 * @return Connection that is owner of this packet.
	 */
	public final T getConnection()
	{
		return client;
	}

	/**
	 * Attach client connection to this packet.
	 * 
	 * @param client
	 */
	public void setConnection(T client)
	{
		this.client = client;
	}

	/**
	 * Clones this packet object.
	 * 
	 * @return AionClientPacket
	 */

	public BaseClientPacket<?> clonePacket()
	{
		try
		{
			return (BaseClientPacket<?>) super.clone();
		}
		catch(CloneNotSupportedException e)
		{
			return null;
		}
	}

	/**
	 * Send new AionServerPacket to connection that is owner of this packet. This method is equvalent to:
	 * client.sendPacket(msg);
	 * 
	 * @param msg
	 */
	public void sendPacket(BaseServerPacket msg)
	{
		client.sendPacket(msg);
	}
}

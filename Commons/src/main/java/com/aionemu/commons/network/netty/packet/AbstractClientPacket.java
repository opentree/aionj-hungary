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
package com.aionemu.commons.network.netty.packet;

import org.apache.log4j.Logger;

import com.aionemu.commons.network.netty.handler.AbstractChannelHandler;

/**
 * @author lyahim
 *
 */
public abstract class AbstractClientPacket<T extends AbstractChannelHandler> extends AbstractPacket implements Runnable, Cloneable
{
	private static final Logger log = Logger.getLogger(AbstractClientPacket.class);
	
	protected T channelHandler;

	public AbstractClientPacket(int opCode)
	{
	    super(opCode);
	}
	
	public int getRemainingBytes()
	{
	    return buf.readableBytes();
	}
	
	public boolean read()
	{
	    try
	    {
	        readImpl();
	        if(getRemainingBytes() > 0)
	            log.debug((new StringBuilder()).append("Packet ").append(this).append(" not fully readed!").toString());
	        return true;
	    }
	    catch(Exception ex)
	    {
	        log.error((new StringBuilder()).append("Reading failed for packet ").append(this).toString(), ex);
	    }
	    return false;
	}
	
	public void run()
	{
	    try
	    {
	        runImpl();
	    }
	    catch(Exception ex)
	    {
	        log.error((new StringBuilder()).append("Running failed for packet ").append(this).toString(), ex);
	    }
	}
	
	protected abstract void readImpl();
	
	protected abstract void runImpl();
	
	protected final int readD()
	{
	    try
	    {
	        return buf.readInt();
	    }
	    catch(Exception e)
	    {
	        log.error((new StringBuilder()).append("Missing D for: ").append(this).toString());
	    }
	    return 0;
	}
	
	protected final int readC()
	{
	    try
	    {
	        return buf.readByte() & 0xff;
	    }
	    catch(Exception e)
	    {
	        log.error((new StringBuilder()).append("Missing C for: ").append(this).toString());
	    }
	    return 0;
	}
	
	protected final int readH()
	{
	    try
	    {
	        return buf.readShort() & 0xffff;
	    }
	    catch(Exception e)
	    {
	        log.error((new StringBuilder()).append("Missing H for: ").append(this).toString());
	    }
	    return 0;
	}
	
	protected final double readDF()
	{
	    try
	    {
	        return buf.readDouble();
	    }
	    catch(Exception e)
	    {
	        log.error((new StringBuilder()).append("Missing DF for: ").append(this).toString());
	    }
	    return 0.0D;
	}
	
	protected final float readF()
	{
	    try
	    {
	        return buf.readFloat();
	    }
	    catch(Exception e)
	    {
	        log.error((new StringBuilder()).append("Missing F for: ").append(this).toString());
	    }
	    return 0.0F;
	}
	
	protected final long readQ()
	{
	    try
	    {
	        return buf.readLong();
	    }
	    catch(Exception e)
	    {
	        log.error((new StringBuilder()).append("Missing Q for: ").append(this).toString());
	    }
	    return 0L;
	}
	
	protected final String readS()
	{
	    StringBuffer sb = new StringBuffer();
	    char ch;
	    try
	    {
	        while((ch = buf.readChar()) != 0) 
	            sb.append(ch);
	    }
	    catch(Exception e)
	    {
	        log.error((new StringBuilder()).append("Missing S for: ").append(this).toString());
	    }
	    return sb.toString();
	}
	
	protected final byte[] readB(int length)
	{
	    byte result[] = new byte[length];
	    try
	    {
	        buf.readBytes(result);
	    }
	    catch(Exception e)
	    {
	        log.error((new StringBuilder()).append("Missing byte[] for: ").append(this).toString());
	    }
	    return result;
	}
	
	public T getChannelHandler()
	{
	    return channelHandler;
	}
	
	@SuppressWarnings("unchecked")
	protected void sendPacket(AbstractServerPacket<T> packet)
	{
	    channelHandler.sendPacket((AbstractServerPacket<AbstractChannelHandler>) packet);
	}
	
	@SuppressWarnings("unchecked")
	public AbstractClientPacket<T> clonePacket()
	{
	    try
	    {
	        return (AbstractClientPacket<T>)super.clone();
	    }
	    catch(CloneNotSupportedException e)
	    {
	        return null;
	    }
	}
	
	public void setChannelHandler(T channelHandler)
	{
	    this.channelHandler = channelHandler;
	}

}

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

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;

import org.apache.log4j.Logger;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;

import com.aionemu.commons.network.packet.AbstractClientPacket;
import com.aionemu.commons.network.packet.AbstractServerPacket;

/**
 * @author lyahim
 *
 */
public abstract class AbstractChannelHandler extends SimpleChannelUpstreamHandler
{
    private static final Logger log = Logger.getLogger(AbstractChannelHandler.class);

    protected State state;
    protected InetAddress inetAddress;
    protected Channel channel;
    private AbstractPacketHandlerFactory<? extends AbstractChannelHandler> abstractPacketHandlerFactory;
    
    public AbstractChannelHandler(AbstractPacketHandlerFactory<? extends AbstractChannelHandler> aphf)
    {
    	super();
    	abstractPacketHandlerFactory = aphf;
    }
    
    
    @Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception
	{
		super.channelConnected(ctx, e);
		state = State.CONNECTED;
		channel = ctx.getChannel();
		inetAddress = ((InetSocketAddress) e.getChannel().getRemoteAddress()).getAddress();		
	}
        
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception
    {
        if(e.getCause().getClass().equals(IOException.class)) //server force close
        {
    		e.getChannel().close();       	
        }
        else if(e.getCause().getClass().equals(NullPointerException.class))
        {
        	log.error("NETTY: Exception caught: ", e.getCause());        	
        }
        else
        {
        	log.error("NETTY: Exception caught: ", e.getCause());        	
    		e.getChannel().close();      	
        }
    }

    public void close()
    {
		channel.close();
    }

    public void close(AbstractServerPacket<? extends AbstractChannelHandler> lastpacket)
    {
        sendPacket(lastpacket);
        close();
    }

    public String getIP()
    {
        return inetAddress.getHostAddress();
    }
    
    public byte[] getByteIP()
    {
        return inetAddress.getAddress();
    }
    
    public enum State
    {
    	CONNECTED,
    	AUTHED_GG,
    	AUTHED,
    	ENTERED;
    }

    public State getState()
    {
        return state;
    }

    public void setState(State state)
    {
        this.state = state;
    }

	@SuppressWarnings("unchecked")
	public void sendPacket(AbstractServerPacket<? extends AbstractChannelHandler> abstractserverpacket)
    {
    	AbstractServerPacket<AbstractChannelHandler> spacket = (AbstractServerPacket<AbstractChannelHandler>) abstractserverpacket;
    	
    	spacket.setOpCode(abstractPacketHandlerFactory.getServerPacketopCode((Class<? extends AbstractServerPacket<AbstractChannelHandler>>) spacket.getClass()));
    	spacket.write(this);
		channel.write(spacket.getBuf());
		log.debug("Sent packet: " + spacket.getClass().getSimpleName());
    }
    
	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception
	{
		super.messageReceived(ctx, e);
		/**
		 * Packet is frame decoded and decrypted at this stage Here packet will be read and submitted to execution
		 */
		AbstractClientPacket<? extends AbstractChannelHandler> packet = abstractPacketHandlerFactory.handleClientPacket((ChannelBuffer) e.getMessage(), this);
		if (packet != null && packet.read())
		{
			packet.run();
			log.debug("Received packet: " + packet.getClass().getSimpleName());
		}
	}
}

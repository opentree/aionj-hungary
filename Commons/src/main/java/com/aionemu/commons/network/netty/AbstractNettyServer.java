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
package com.aionemu.commons.network.netty;

import java.net.InetSocketAddress;
import java.nio.ByteOrder;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.buffer.HeapChannelBufferFactory;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

import com.aionemu.commons.utils.ThreadPoolManager;

/**
 * @author lyahim
 *
 */

public abstract class AbstractNettyServer
{
	/**
	 * Base method for initialize Client ChannelFactorys
	 * @return NioServerSocketChannelFactory
	 */
	protected NioClientSocketChannelFactory initClientChannelFactory()
	{
		return new NioClientSocketChannelFactory(ThreadPoolManager.getInstance(),ThreadPoolManager.getInstance(),Runtime.getRuntime().availableProcessors());
	}
	
	/**
	 * Base method for initialize Server ChannelFactorys
	 * @return NioServerSocketChannelFactory
	 */
	protected NioServerSocketChannelFactory initServerChannelFactory()
	{
		return new NioServerSocketChannelFactory(ThreadPoolManager.getInstance(),ThreadPoolManager.getInstance(),Runtime.getRuntime().availableProcessors());
	}
	/**
	 * Base shutdown process for server
	 */
    public void shutDown()
    {
    	ThreadPoolManager.getInstance().shutdown();
    }
    
	protected Channel initServerChannel(ChannelFactory channelFactory, InetSocketAddress address, ChannelPipelineFactory channelPipelineFactory)
	{
		ServerBootstrap bootstrap = new ServerBootstrap(channelFactory);
		bootstrap.setPipelineFactory(channelPipelineFactory);
		bootstrap.setOption("child.bufferFactory", HeapChannelBufferFactory.getInstance(ByteOrder.LITTLE_ENDIAN));
		bootstrap.setOption("child.tcpNoDelay", true);
		bootstrap.setOption("child.keepAlive", true);
		bootstrap.setOption("child.reuseAddress", true);
		bootstrap.setOption("child.connectTimeoutMillis", 100);
		bootstrap.setOption("readWriteFair", true);

		return bootstrap.bind(address);
	}
	
	protected ChannelFuture initClientChannel(ChannelFactory channelFactory, InetSocketAddress address, ChannelPipelineFactory channelPipelineFactory)
	{
		ClientBootstrap bootstrap = new ClientBootstrap(channelFactory);
		bootstrap.setPipelineFactory(channelPipelineFactory);
		bootstrap.setOption("bufferFactory", HeapChannelBufferFactory.getInstance(ByteOrder.LITTLE_ENDIAN));
		bootstrap.setOption("keepAlive", true);

		return bootstrap.connect(address);
	}

    /**
     * Initialize NettyServer
     */
    public abstract void initialize();
    
}

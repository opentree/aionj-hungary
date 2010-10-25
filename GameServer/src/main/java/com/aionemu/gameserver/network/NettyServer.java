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
package com.aionemu.gameserver.network;

import java.net.InetSocketAddress;
import java.nio.ByteOrder;

import org.apache.log4j.Logger;
import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.buffer.HeapChannelBufferFactory;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.group.ChannelGroup;
import org.jboss.netty.channel.group.ChannelGroupFuture;
import org.jboss.netty.channel.group.DefaultChannelGroup;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

import com.aionemu.gameserver.configs.network.NetworkConfig;
import com.aionemu.gameserver.network.aion.AionPacketHandlerFactory;
import com.aionemu.gameserver.network.aion.GameToClientPipelineFactory;
import com.aionemu.gameserver.network.loginserver.GameToLoginPipelineFactory;
import com.aionemu.gameserver.network.loginserver.LoginServer;
import com.aionemu.gameserver.network.loginserver.LsPacketHandlerFactory;
import com.aionemu.gameserver.utils.ThreadPoolManager;

/**
 * @author Mr. Poke
 * 
 */
public class NettyServer
{
	private static final Logger					log				= Logger.getLogger(NettyServer.class);

	private final ChannelGroup					channelGroup	= new DefaultChannelGroup(NettyServer.class.getName());

	private final GameToLoginPipelineFactory	gameToLoginPipeLineFactory;
	private final GameToClientPipelineFactory	gameToClientPipeLineFactory;
	
	private ChannelFactory						gameToLoginChannelFactory;
	private ChannelFactory						gameToClientChannelFactory;

	private ChannelFuture gameToLoginFuture;

	public NettyServer()
	{

		LsPacketHandlerFactory.getInstance();
		AionPacketHandlerFactory.getInstance();

		gameToLoginPipeLineFactory = new GameToLoginPipelineFactory();
		gameToClientPipeLineFactory = new GameToClientPipelineFactory();

		gameToLoginChannelFactory = initClientChannelFactory();
		gameToClientChannelFactory = initServerChannelFactory();

		gameToLoginFuture = initClientChannel(gameToLoginChannelFactory, NetworkConfig.LOGIN_ADDRESS,
				gameToLoginPipeLineFactory);
		gameToLoginFuture.awaitUninterruptibly().getChannel();

		Channel gameToClientChannel = initServerChannel(gameToClientChannelFactory, NetworkConfig.GAME_ADDRESS,
				gameToClientPipeLineFactory);

		channelGroup.add(gameToClientChannel);

		LoginServer.getInstance().setGameToLoginFuture(gameToLoginFuture);

		log.info("Network initalized.");
	}

	public static final NettyServer getInstance()
	{
		return SingletonHolder.instance;
	}

	/**
	 * 
	 * @return NioServerSocketChannelFactory
	 */
	private NioServerSocketChannelFactory initServerChannelFactory()
	{
		return new NioServerSocketChannelFactory(ThreadPoolManager.getInstance(), ThreadPoolManager.getInstance(), 2);
	}

	/**
	 * 
	 * @return NioServerSocketChannelFactory
	 */
	private NioClientSocketChannelFactory initClientChannelFactory()
	{
		return new NioClientSocketChannelFactory(ThreadPoolManager.getInstance(), ThreadPoolManager.getInstance(), 2);
	}
	/**
	 * 
	 * @param channelFactory
	 * @param listenAddress
	 * @param port
	 * @param channelPipelineFactory
	 * @return Channel
	 */
	private Channel initServerChannel(ChannelFactory channelFactory, InetSocketAddress address,
		ChannelPipelineFactory channelPipelineFactory)
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

	/**
	 * 
	 * @param channelFactory
	 * @param listenAddress
	 * @param port
	 * @param channelPipelineFactory
	 * @return Channel
	 */
	private ChannelFuture initClientChannel(ChannelFactory channelFactory, InetSocketAddress address,
		ChannelPipelineFactory channelPipelineFactory)
	{
		ClientBootstrap bootstrap = new ClientBootstrap(channelFactory);
		bootstrap.setPipelineFactory(channelPipelineFactory);
		bootstrap.setOption("bufferFactory", HeapChannelBufferFactory.getInstance(ByteOrder.LITTLE_ENDIAN));
		bootstrap.setOption("keepAlive", true);

		return bootstrap.connect(address);
	}
	/**
	 * Shutdown server
	 */
	public void shutdownAll()
	{
		ChannelGroupFuture future = channelGroup.close();
		future.awaitUninterruptibly();
		gameToLoginChannelFactory.releaseExternalResources();
	}

	public ChannelFuture getGameToLoginChannel()
	{
		return gameToLoginFuture;
	}

	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder
	{
		protected static final NettyServer	instance	= new NettyServer();
	}
}

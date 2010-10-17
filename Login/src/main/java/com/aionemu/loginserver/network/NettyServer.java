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
package com.aionemu.loginserver.network;

import java.net.InetSocketAddress;
import java.nio.ByteOrder;

import org.apache.log4j.Logger;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.buffer.HeapChannelBufferFactory;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.group.ChannelGroup;
import org.jboss.netty.channel.group.ChannelGroupFuture;
import org.jboss.netty.channel.group.DefaultChannelGroup;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

import com.aionemu.loginserver.configs.Config;
import com.aionemu.loginserver.network.aion.AionClientPacketHandlerFactory;
import com.aionemu.loginserver.network.aion.LoginToClientPipeLineFactory;
import com.aionemu.loginserver.network.console.ConsolePipelineFactory;
import com.aionemu.loginserver.network.gameserver.GSClientPacketHandlerFactory;
import com.aionemu.loginserver.network.gameserver.LoginToGamePipelineFactory;
import com.aionemu.loginserver.utils.ThreadPoolManager;

/**
 * @author Mr. Poke
 * 
 */
public class NettyServer
{
	private static final Logger					log				= Logger.getLogger(NettyServer.class);

	private final ChannelGroup					channelGroup	= new DefaultChannelGroup(NettyServer.class.getName());

	private final LoginToClientPipeLineFactory	loginToClientPipeLineFactory;

	private final LoginToGamePipelineFactory	loginToGamePipelineFactory;

	private ConsolePipelineFactory		consolePipelineFactory = null;

	private ChannelFactory						loginToClientChannelFactory;

	private ChannelFactory						loginToGameChannelFactory;

	private ChannelFactory						consoleChannelFactory = null;

	public NettyServer()
	{
		this.loginToClientPipeLineFactory = new LoginToClientPipeLineFactory();
		this.loginToGamePipelineFactory = new LoginToGamePipelineFactory();

		loginToClientChannelFactory = initChannelFactory();
		loginToGameChannelFactory = initChannelFactory();

		Channel loginToClientChannel = initChannel(loginToClientChannelFactory, Config.LOGIN_ADDRESS,
			loginToClientPipeLineFactory);
		Channel loginToGameChannel = initChannel(loginToGameChannelFactory, Config.GAME_ADDRESS,
			loginToGamePipelineFactory);

		channelGroup.add(loginToClientChannel);
		AionClientPacketHandlerFactory.getInstance();
		channelGroup.add(loginToGameChannel);
		GSClientPacketHandlerFactory.getInstance();

		if(Config.CONSOLE_ENABLED)
		{
			this.consolePipelineFactory = new ConsolePipelineFactory();
			consoleChannelFactory = initChannelFactory();
			ServerBootstrap bootstrap = new ServerBootstrap(consoleChannelFactory);
			bootstrap.setPipelineFactory(consolePipelineFactory);
			channelGroup.add(bootstrap.bind(Config.CONSOLE_ADDRESS));
		}

		log.info("Login Server started.");
	}

	public static final NettyServer getInstance()
	{
		return SingletonHolder.instance;
	}

	/**
	 * 
	 * @return NioServerSocketChannelFactory
	 */
	private NioServerSocketChannelFactory initChannelFactory()
	{
		return new NioServerSocketChannelFactory(ThreadPoolManager.getInstance(), ThreadPoolManager.getInstance(), 2);
	}

	/**
	 * 
	 * @param channelFactory
	 * @param listenAddress
	 * @param port
	 * @param channelPipelineFactory
	 * @return Channel
	 */
	private Channel initChannel(ChannelFactory channelFactory, InetSocketAddress address,
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
	 * Shutdown server
	 */
	public void shutdownAll()
	{
		ChannelGroupFuture future = channelGroup.close();
		future.awaitUninterruptibly();
		loginToClientChannelFactory.releaseExternalResources();
		loginToGameChannelFactory.releaseExternalResources();
	}

	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder
	{
		protected static final NettyServer	instance	= new NettyServer();
	}
}

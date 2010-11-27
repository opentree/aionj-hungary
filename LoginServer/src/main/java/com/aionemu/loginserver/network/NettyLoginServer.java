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

import org.apache.log4j.Logger;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.group.ChannelGroup;
import org.jboss.netty.channel.group.ChannelGroupFuture;
import org.jboss.netty.channel.group.DefaultChannelGroup;

import com.aionemu.commons.network.netty.AbstractNettyServer;
import com.aionemu.loginserver.configs.Config;
import com.aionemu.loginserver.network.aion.AionPipeLineFactory;
import com.aionemu.loginserver.network.gameserver.GameServerPipeLineFactory;

/**
 * @author lyahim
 * 
 */
public class NettyLoginServer extends AbstractNettyServer {
	private static final Logger logger = Logger
			.getLogger(NettyLoginServer.class);

	private final ChannelGroup channelGroup = new DefaultChannelGroup(
			NettyLoginServer.class.getName());

	private final AionPipeLineFactory loginToClientPipeLineFactory;
	private final GameServerPipeLineFactory loginToGamePipelineFactory;

	private ChannelFactory loginToClientChannelFactory;
	private ChannelFactory loginToGameChannelFactory;

	public static final NettyLoginServer getInstance() {
		return SingletonHolder.instance;
	}

	public NettyLoginServer() {
		this.loginToClientPipeLineFactory = new AionPipeLineFactory();
		this.loginToGamePipelineFactory = new GameServerPipeLineFactory();
		initialize();
	}

	@Override
	public void initialize() {
		loginToClientChannelFactory = initServerChannelFactory();
		loginToGameChannelFactory = initServerChannelFactory();

		channelGroup.add(initServerChannel(loginToClientChannelFactory,
				Config.CLIENT_ADDRESS, loginToClientPipeLineFactory));
		channelGroup.add(initServerChannel(loginToGameChannelFactory,
				Config.GAMESERVER_ADDRESS, loginToGamePipelineFactory));

		logger.info("Login Server started");
	}

	@Override
	public void shutDown() {
		ChannelGroupFuture future = channelGroup.close();
		future.awaitUninterruptibly();
		loginToClientChannelFactory.releaseExternalResources();
		loginToGameChannelFactory.releaseExternalResources();
		super.shutDown();
	}

	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder {
		protected static final NettyLoginServer instance = new NettyLoginServer();
	}
}

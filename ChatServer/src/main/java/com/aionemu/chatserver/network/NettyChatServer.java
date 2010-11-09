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
package com.aionemu.chatserver.network;

import org.apache.log4j.Logger;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.group.ChannelGroup;
import org.jboss.netty.channel.group.ChannelGroupFuture;
import org.jboss.netty.channel.group.DefaultChannelGroup;

import com.aionemu.chatserver.configs.Config;
import com.aionemu.chatserver.network.aion.AionPipeLineFactory;
import com.aionemu.chatserver.network.gameserver.GameServerPipeLineFactory;
import com.aionemu.commons.network.netty.AbstractNettyServer;

/**
 * @author Lyahim
 */
public class NettyChatServer extends AbstractNettyServer
{
	private static final Logger				logger			= Logger.getLogger(NettyChatServer.class);

	private final ChannelGroup				channelGroup	= new DefaultChannelGroup(NettyChatServer.class.getName());
	private final AionPipeLineFactory		chatToClientPipeLineFactory;
	private final GameServerPipeLineFactory	chatToGamePipelineFactory;
	
	private ChannelFactory					chatToClientChannelFactory;
	private ChannelFactory					chatToGameChannelFactory;

	public static final NettyChatServer getInstance()
	{
		return SingletonHolder.instance;
	}

	private NettyChatServer()
	{
		this.chatToClientPipeLineFactory = new AionPipeLineFactory();
		this.chatToGamePipelineFactory = new GameServerPipeLineFactory();
		initialize();
		logger.info("NettyChatServer started!");
	}
	
	@Override
	public void initialize()
	{
		chatToClientChannelFactory = initServerChannelFactory();
		chatToGameChannelFactory = initServerChannelFactory();

		channelGroup.add(initServerChannel(chatToClientChannelFactory,Config.CLIENT_ADDRESS, chatToClientPipeLineFactory));
		channelGroup.add(initServerChannel(chatToGameChannelFactory, Config.GAMESERVER_ADDRESS, chatToGamePipelineFactory));
		
		logger.info("Chat Server started");	
	}
	
	@Override
	public void shutDown()
	{
		ChannelGroupFuture future = channelGroup.close();
		future.awaitUninterruptibly();
		chatToClientChannelFactory.releaseExternalResources();
		chatToGameChannelFactory.releaseExternalResources();
		super.shutDown();
	}
	
	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder
	{
		protected static final NettyChatServer instance = new NettyChatServer();
	}

}

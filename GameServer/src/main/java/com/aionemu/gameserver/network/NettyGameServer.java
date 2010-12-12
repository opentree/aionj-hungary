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
package com.aionemu.gameserver.network;

import org.apache.log4j.Logger;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.group.ChannelGroup;
import org.jboss.netty.channel.group.ChannelGroupFuture;
import org.jboss.netty.channel.group.DefaultChannelGroup;

import com.aionemu.commons.network.netty.AbstractNettyServer;
import com.aionemu.gameserver.configs.main.GSConfig;
import com.aionemu.gameserver.configs.network.NetworkConfig;
import com.aionemu.gameserver.network.aion.AionPacketHandlerFactory;
import com.aionemu.gameserver.network.aion.AionPipeLineFactory;
import com.aionemu.gameserver.network.chatserver.ChatServerPipeLineFactory;
import com.aionemu.gameserver.network.loginserver.LoginServerPipeLineFactory;
import com.aionemu.gameserver.utils.ThreadPoolManager;

/**
 * @author lyahim
 *
 */
public class NettyGameServer extends AbstractNettyServer
{
	private static final Logger					logger			= Logger.getLogger(NettyGameServer.class);

	private final ChannelGroup					channelGroup	= new DefaultChannelGroup(NettyGameServer.class.getName());

	private final AionPipeLineFactory			gameToClientPipeLineFactory;
	private final ChatServerPipeLineFactory		gameToChatPipelineFactory;
	private final LoginServerPipeLineFactory	gameToLoginPipelineFactory;

	private ChannelFactory						gameToClientChannelFactory;
	private ChannelFactory						gameToChatChannelFactory;
	private ChannelFactory						gameToLoginChannelFactory;

	private AionPacketHandlerFactory			aionPacketHandlerFactory;

	/**
	 * @return Returns the aionPacketHandlerFactory.
	 */
	public AionPacketHandlerFactory getAionPacketHandlerFactory()
	{
		return aionPacketHandlerFactory;
	}

	public static final NettyGameServer getInstance()
	{
		return SingletonHolder.instance;
	}

	public NettyGameServer()
	{
		aionPacketHandlerFactory = new AionPacketHandlerFactory();
		gameToClientPipeLineFactory = new AionPipeLineFactory(aionPacketHandlerFactory);
		gameToChatPipelineFactory = new ChatServerPipeLineFactory();
		gameToLoginPipelineFactory = new LoginServerPipeLineFactory();

		initialize();
	}

	@Override
	public void initialize()
	{
		gameToClientChannelFactory = initServerChannelFactory();
		gameToChatChannelFactory = initClientChannelFactory();
		gameToLoginChannelFactory = initClientChannelFactory();

		channelGroup.add(initServerChannel(gameToClientChannelFactory, NetworkConfig.CLIENT_ADDRESS, gameToClientPipeLineFactory));
		
		connectToLoginServer();

		if (!GSConfig.DISABLE_CHAT_SERVER)
		{
			ThreadPoolManager.getInstance().scheduleAtFixedRate((new Runnable()
			{
				@Override
				public void run()
				{
					logger.info("Connecting to ChatServer: " + NetworkConfig.CHAT_ADDRESS);
					try
					{
						ChannelFuture chat = initClientChannel(gameToChatChannelFactory, NetworkConfig.CHAT_ADDRESS, gameToChatPipelineFactory);
						chat.getChannel().getCloseFuture().awaitUninterruptibly();
					}
					catch (Exception e)
					{
					}
				}
			}), 5000, 10000);
		}

		logger.info("GameServer started");
	}

	@Override
	public void shutDown()
	{
		ChannelGroupFuture future = channelGroup.close();
		future.awaitUninterruptibly();
		gameToClientChannelFactory.releaseExternalResources();
		gameToChatChannelFactory.releaseExternalResources();
		gameToLoginChannelFactory.releaseExternalResources();
		super.shutDown();
	}

	public void connectToLoginServer()
	{
		ThreadPoolManager.getInstance().execute((new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					Thread.sleep(5000);
					logger.info("Connecting to LoginServer: " + NetworkConfig.LOGIN_ADDRESS);
					initClientChannel(gameToLoginChannelFactory, NetworkConfig.LOGIN_ADDRESS, gameToLoginPipelineFactory);
				}
				catch (Exception e)
				{
				}
			}
		}));
	}
	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder
	{
		protected static final NettyGameServer	instance	= new NettyGameServer();
	}

}

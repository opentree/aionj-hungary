package com.aionemu.chatserver.network.gameserver;

import org.apache.log4j.Logger;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;

import com.aionemu.commons.network.netty.handler.AbstractChannelHandler;
import com.aionemu.commons.network.netty.handler.AbstractPacketHandlerFactory;


public class GameServerChannelHandler extends AbstractChannelHandler
{
	private static final Logger	log	= Logger.getLogger(GameServerChannelHandler.class);

	public GameServerChannelHandler(AbstractPacketHandlerFactory<GameServerChannelHandler> acphf)
	{
		super(acphf);
	}
	
	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception
	{
		super.channelConnected(ctx, e);
		log.info("GameServer connected Ip:" + inetAddress.getHostAddress());
	}
	
	@Override
	public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception
	{
		super.channelDisconnected(ctx, e);
		log.warn("Connection with GameServer lost...");
	}
}

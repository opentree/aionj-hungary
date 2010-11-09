package com.aionemu.chatserver.network.aion;

import org.apache.log4j.Logger;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;

import com.aionemu.chatserver.model.ChatClient;
import com.aionemu.commons.network.netty.handler.AbstractChannelHandler;
import com.aionemu.commons.network.netty.handler.AbstractPacketHandlerFactory;

public class AionChannelHandler extends AbstractChannelHandler
{
	private static final Logger			log	= Logger.getLogger(AionChannelHandler.class);

	private ChatClient					chatClient;

	public AionChannelHandler(AbstractPacketHandlerFactory<AionChannelHandler> acphf)
	{
		super(acphf);
	}

	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception
	{
		super.channelConnected(ctx, e);
		log.info("Client connected Ip:" + inetAddress.getHostAddress());
	}
			
	/**
	 * @return the chatClient
	 */
	public ChatClient getChatClient()
	{
		return chatClient;
	}

	/**
	 * @param chatClient
	 *            the chatClient to set
	 */
	public void setChatClient(ChatClient chatClient)
	{
		this.chatClient = chatClient;
	}

}

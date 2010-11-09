package com.aionemu.chatserver.network.gameserver;

import org.apache.log4j.Logger;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.handler.execution.ExecutionHandler;
import org.jboss.netty.handler.logging.LoggingHandler;
import org.jboss.netty.logging.InternalLogLevel;

import com.aionemu.commons.network.netty.AbstractPipeLineFactory;
import com.aionemu.commons.network.netty.coder.PacketFrameDecoder;
import com.aionemu.commons.network.netty.coder.PacketFrameEncoder;

public class GameServerPipeLineFactory extends AbstractPipeLineFactory
{

	@Override
	public ChannelPipeline getPipeline() throws Exception
	{
		ChannelPipeline pipeline = Channels.pipeline();

//		pipeline.addLast("logger", new LoggingHandler(Logger.class, InternalLogLevel.INFO, true));
		pipeline.addLast("logger", new LoggingHandler(Logger.class, InternalLogLevel.DEBUG, true));
		pipeline.addLast("framedecoder", new PacketFrameDecoder());
		pipeline.addLast("frameencoder", new PacketFrameEncoder(2));
//		pipeline.addLast("packetdecoder", new GameServerPacketDecoder());
//		pipeline.addLast("packetencoder", new GameServerPacketEncoder());
		pipeline.addLast("executor", new ExecutionHandler(pipelineExecutor));
		pipeline.addLast("handler", new GameServerChannelHandler(new GameServerPacketHandlerFactory()));

		return pipeline;
	}

}

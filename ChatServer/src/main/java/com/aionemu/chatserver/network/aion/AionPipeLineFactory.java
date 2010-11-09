package com.aionemu.chatserver.network.aion;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.handler.execution.ExecutionHandler;

import com.aionemu.chatserver.network.netty.coder.LoginPacketDecoder;
import com.aionemu.chatserver.network.netty.coder.LoginPacketEncoder;
import com.aionemu.chatserver.network.netty.coder.PacketFrameDecoder;
import com.aionemu.commons.network.netty.AbstractPipeLineFactory;

public class AionPipeLineFactory extends AbstractPipeLineFactory
{

	@Override
	public ChannelPipeline getPipeline() throws Exception
	{
		ChannelPipeline pipeline = Channels.pipeline();

		pipeline.addLast("framedecoder", new PacketFrameDecoder());
		pipeline.addLast("packetdecoder", new LoginPacketDecoder());
		pipeline.addLast("packetencoder", new LoginPacketEncoder());
		pipeline.addLast("executor", new ExecutionHandler(pipelineExecutor));
		pipeline.addLast("handler", new AionChannelHandler(new AionPacketHandlerFactory()));
		return pipeline;
	}
}

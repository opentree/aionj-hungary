package com.aionemu.gameserver.network.aion;

import org.apache.log4j.Logger;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.handler.codec.frame.LengthFieldBasedFrameDecoder;
import org.jboss.netty.handler.execution.ExecutionHandler;
import org.jboss.netty.handler.logging.LoggingHandler;
import org.jboss.netty.logging.InternalLogLevel;

import com.aionemu.commons.netty.coder.LengthFieldBasedFrameEncoder;
import com.aionemu.commons.netty.coder.PacketDecrypter;
import com.aionemu.commons.netty.coder.PacketEncrypter;
import com.aionemu.gameserver.utils.ThreadPoolManager;

/**
 * @author MrPoke
 */
public class GameToClientPipelineFactory implements ChannelPipelineFactory
{
	private static final int	MAX_PACKET_LENGTH		= 8192 * 2;
	private static final int	LENGTH_FIELD_OFFSET		= 0;
	private static final int	LENGTH_FIELD_LENGTH		= 2;
	private static final int	LENGTH_FIELD_ADJUSTMENT	= -2;
	private static final int	INITIAL_BYTES_TO_STRIP	= 2;

	public GameToClientPipelineFactory()
	{
	}

	/**
	 * Decoding process will include the following handlers: - framedecoder -
	 * packetdecoder - handler
	 * 
	 * Encoding process: - packetencoder
	 * 
	 * Please note the sequence of handlers
	 */
	@Override
	public ChannelPipeline getPipeline() throws Exception
	{
		ChannelPipeline pipeline = Channels.pipeline();

		pipeline.addLast("logger", new LoggingHandler(Logger.class, InternalLogLevel.INFO, true));

		pipeline.addLast("framedecoder", new LengthFieldBasedFrameDecoder(MAX_PACKET_LENGTH, LENGTH_FIELD_OFFSET, LENGTH_FIELD_LENGTH, LENGTH_FIELD_ADJUSTMENT,
				INITIAL_BYTES_TO_STRIP));
		pipeline.addLast("frameencoder", new LengthFieldBasedFrameEncoder(2));

		pipeline.addLast("packetencrypter", new PacketEncrypter());
		pipeline.addLast("packetdecrypter", new PacketDecrypter());

		pipeline.addLast("executor", new ExecutionHandler(ThreadPoolManager.getInstance()));
		pipeline.addLast("handler", new AionConnection());

		return pipeline;
	}
}

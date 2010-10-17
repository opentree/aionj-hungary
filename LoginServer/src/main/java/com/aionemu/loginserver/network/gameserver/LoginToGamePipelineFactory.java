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
package com.aionemu.loginserver.network.gameserver;

import org.apache.log4j.Logger;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.handler.codec.frame.LengthFieldBasedFrameDecoder;
import org.jboss.netty.handler.execution.ExecutionHandler;
import org.jboss.netty.handler.logging.LoggingHandler;
import org.jboss.netty.logging.InternalLogLevel;

import com.aionemu.commons.netty.coder.LengthFieldBasedFrameEncoder;
import com.aionemu.loginserver.utils.ThreadPoolManager;

/**
 * @author ATracer
 */
public class LoginToGamePipelineFactory implements ChannelPipelineFactory
{
	private static final int	MAX_PACKET_LENGTH		= 8192 * 2;
	private static final int	LENGTH_FIELD_OFFSET		= 0;
	private static final int	LENGTH_FIELD_LENGTH		= 2;
	private static final int	LENGTH_FIELD_ADJUSTMENT	= -2;
	private static final int	INITIAL_BYTES_TO_STRIP	= 2;

	public LoginToGamePipelineFactory()
	{
	}

	/**
	 * Decoding process will include the following handlers: - framedecoder - packetdecoder - handler
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

		pipeline.addLast("framedecoder", new LengthFieldBasedFrameDecoder(MAX_PACKET_LENGTH, LENGTH_FIELD_OFFSET,
			LENGTH_FIELD_LENGTH, LENGTH_FIELD_ADJUSTMENT, INITIAL_BYTES_TO_STRIP));

		pipeline.addLast("frameencoder", new LengthFieldBasedFrameEncoder(2));

		pipeline.addLast("executor", new ExecutionHandler(ThreadPoolManager.getInstance()));
		pipeline.addLast("handler", new GsConnection());

		return pipeline;
	}
}

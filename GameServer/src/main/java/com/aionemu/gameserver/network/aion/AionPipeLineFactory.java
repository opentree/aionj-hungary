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
package com.aionemu.gameserver.network.aion;

import org.apache.log4j.Logger;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.handler.execution.ExecutionHandler;
import org.jboss.netty.handler.logging.LoggingHandler;
import org.jboss.netty.logging.InternalLogLevel;

import com.aionemu.commons.network.netty.AbstractPipeLineFactory;
import com.aionemu.commons.network.netty.coder.PacketDecoder;
import com.aionemu.commons.network.netty.coder.PacketEncoder;
import com.aionemu.commons.network.netty.coder.PacketFrameDecoder;
import com.aionemu.commons.network.netty.coder.PacketFrameEncoder;

/**
 * @author lyahim
 *
 */
public class AionPipeLineFactory extends AbstractPipeLineFactory
{
	AionPacketHandlerFactory	aionPacketHandlerFactory;

	public AionPipeLineFactory(AionPacketHandlerFactory aionPacketHandlerFactory)
	{
		super();
		this.aionPacketHandlerFactory = aionPacketHandlerFactory;
	}

	@Override
	public ChannelPipeline getPipeline() throws Exception
	{
		ChannelPipeline pipeline = Channels.pipeline();

		//		pipeline.addLast("logger", new LoggingHandler(Logger.class, InternalLogLevel.INFO, true));
		pipeline.addLast("logger", new LoggingHandler(Logger.class, InternalLogLevel.DEBUG, true));
		pipeline.addLast("framedecoder", new PacketFrameDecoder());
		pipeline.addLast("frameencoder", new PacketFrameEncoder(2));
		pipeline.addLast("packetdecoder", new PacketDecoder());
		pipeline.addLast("packetencoder", new PacketEncoder());
		pipeline.addLast("executor", new ExecutionHandler(pipelineExecutor));
		pipeline.addLast("handler", new AionChannelHandler(aionPacketHandlerFactory));

		return pipeline;
	}

}

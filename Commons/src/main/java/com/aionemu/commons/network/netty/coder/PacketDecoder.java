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
package com.aionemu.commons.network.netty.coder;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneDecoder;

import com.aionemu.commons.network.netty.handler.ICryptedChannelHandler;

/**
 * @author lyahim
 *
 */
public class PacketDecoder extends OneToOneDecoder
{
	@Override
    protected Object decode(ChannelHandlerContext ctx, Channel channel, Object msg) throws Exception
    {
		Object object = ctx.getPipeline().get("handler");
		ChannelBuffer buf = (ChannelBuffer) msg;
		if(buf != null && object != null && object instanceof ICryptedChannelHandler)
		{
			ICryptedChannelHandler handler = (ICryptedChannelHandler) object;
			handler.decrypt(buf);
			return buf;
		}
		throw new Exception("Mistake in decode!");
    }
}

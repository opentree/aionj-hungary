/*
 * This file is part of aion-lightning <aion-lightning.com>.
 *
 *  aion-lightning is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  aion-lightning is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with aion-lightning.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.aionemu.commons.netty.coder;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;

/**
 * @author Mr. Poke
 * 
 */
public class LengthFieldBasedFrameEncoder extends OneToOneEncoder
{

	private final int	LENGTH;

	/**
	 * @param lENGTH
	 */
	public LengthFieldBasedFrameEncoder(int length)
	{
		super();
		LENGTH = length;
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.netty.handler.codec.oneone.OneToOneEncoder#encode(org.jboss.netty.channel.ChannelHandlerContext,
	 * org.jboss.netty.channel.Channel, java.lang.Object)
	 */
	@Override
	protected Object encode(ChannelHandlerContext ctx, Channel channel, Object msg) throws Exception
	{
		ChannelBuffer buf = (ChannelBuffer) msg;
		switch (LENGTH)
		{
			case 1:
				buf.setByte(0, buf.readableBytes());
				break;
			case 2:
				buf.setShort(0, buf.readableBytes());
				break;
			case 4:
				buf.setInt(0, buf.readableBytes());
				break;
		}
		return buf;
	}

}

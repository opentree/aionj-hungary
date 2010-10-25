/**
 * This file is part of aion-emu <aion-emu.com>.
 *
 *  aion-emu is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  aion-emu is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with aion-emu.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.aionemu.gameserver.network.loginserver;

import org.apache.log4j.Logger;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;

import com.aionemu.commons.netty.handler.AbstractChannelHandler;
import com.aionemu.gameserver.network.loginserver.serverpackets.SM_GS_AUTH;
import com.aionemu.gameserver.utils.ThreadPoolManager;

/**
 * Object representing connection between LoginServer and GameServer.
 * 
 * @author -Nemesiss-
 * 
 */
public class LoginServerConnection extends AbstractChannelHandler
{

	/**
	 * Logger for this class.
	 */
	private static final Logger				log					= Logger.getLogger(LoginServer.class);

	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception
	{
		super.channelConnected(ctx, e);
		/**
		 * send first packet - authentication.
		 */
		this.sendPacket(new SM_GS_AUTH());
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception
	{
		ChannelBuffer buf = (ChannelBuffer) e.getMessage();
		LsClientPacket clientPacket =  LsPacketHandlerFactory.getInstance().getPacketHandler().handle(buf, this);
		if(clientPacket != null && clientPacket.read())
		{
			ThreadPoolManager.getInstance().execute(clientPacket);
		}
	}

	/**
	 * Invoked when a Channel was disconnected from its remote peer
	 */
	@Override
	public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception
	{
		LoginServer.getInstance().loginServerDown();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception
	{
		log.error("Game to login connection error:", e.getCause());
		e.getChannel().close();
	}

	/**
	 * @return String info about this connection
	 */
	@Override
	public String toString()
	{
		return "LoginServer " + getIP();
	}
}

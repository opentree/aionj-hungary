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
package com.aionemu.gameserver.network.loginserver;

import java.net.ConnectException;

import org.apache.log4j.Logger;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;

import com.aionemu.commons.network.netty.handler.AbstractChannelHandler;
import com.aionemu.commons.network.netty.handler.AbstractPacketHandlerFactory;
import com.aionemu.gameserver.network.loginserver.serverpackets.SM_GS_AUTH;

/**
 * @author lyahim
 *
 */
public class LoginServerChannelHandler extends AbstractChannelHandler
{
	
	private static final Logger	log	= Logger.getLogger(LoginServerChannelHandler.class);

	
	public LoginServerChannelHandler(AbstractPacketHandlerFactory<LoginServerChannelHandler> acphf)
	{
		super(acphf);
	}
	
	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception
	{
		super.channelConnected(ctx, e);
		log.info("LoginServer connected Ip:" + inetAddress.getHostAddress());
		LoginServer.getInstance().setChannelHandler(this);
		sendPacket(new SM_GS_AUTH());
	}
	
	@Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception
    {
		if(e.getCause().getClass().equals(ConnectException.class))
		{
			log.info("Can't connect to LoginServer: " + e.getCause().getMessage());
			e.getChannel().close();
		}
		else
			super.exceptionCaught(ctx, e);
    }
    
	@Override
	public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception
	{
		LoginServer.getInstance().loginServerDown();
		super.channelDisconnected(ctx, e);
	}

}

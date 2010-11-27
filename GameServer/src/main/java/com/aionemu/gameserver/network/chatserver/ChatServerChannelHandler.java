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
package com.aionemu.gameserver.network.chatserver;

import java.net.ConnectException;

import org.apache.log4j.Logger;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;

import com.aionemu.commons.network.netty.handler.AbstractChannelHandler;
import com.aionemu.commons.network.netty.handler.AbstractPacketHandlerFactory;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.chatserver.serverpackets.SM_CS_AUTH;
import com.aionemu.gameserver.network.chatserver.serverpackets.SM_CS_PLAYER_AUTH;
import com.aionemu.gameserver.network.chatserver.serverpackets.SM_CS_PLAYER_LOGOUT;

/**
 * @author lyahim
 *
 */
public class ChatServerChannelHandler extends AbstractChannelHandler
{
	private static final Logger	log	= Logger.getLogger(ChatServerChannelHandler.class);

	public ChatServerChannelHandler(AbstractPacketHandlerFactory<ChatServerChannelHandler> acphf)
	{
		super(acphf);
	}

	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception
	{
		super.channelConnected(ctx, e);
		sendPacket(new SM_CS_AUTH());
		ChatServer.getInstance().setChannelHandler(this);
		log.info("ChatServer connected Ip:" + inetAddress.getHostAddress());
	}

	@Override
	public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception
	{
		super.channelDisconnected(ctx, e);
		log.warn("Connection with ChatServer lost...");
	}

	public void sendPlayerLoginRequst(Player player)
	{
		sendPacket(new SM_CS_PLAYER_AUTH(player.getObjectId(), player.getAcountName()));
	}

	public void sendPlayerLogout(Player player)
	{
		sendPacket(new SM_CS_PLAYER_LOGOUT(player.getObjectId()));
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception
	{
		if (e.getCause().getClass().equals(ConnectException.class))
		{
			log.info("Can't connect to ChatServer: " + e.getCause().getMessage());
			e.getChannel().close();
		}
		else
			super.exceptionCaught(ctx, e);
	}
}

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
package com.aionemu.loginserver.network.gameserver;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.MessageEvent;

import com.aionemu.commons.netty.handler.AbstractChannelHandler;
import com.aionemu.loginserver.GameServerInfo;
import com.aionemu.loginserver.utils.ThreadPoolManager;

/**
 * Object representing connection between LoginServer and GameServer.
 * 
 * @author -Nemesiss-
 */
public class GsConnection extends AbstractChannelHandler
{

	/**
	 * GameServerInfo for this GsConnection.
	 */
	private GameServerInfo	gameServerInfo	= null;

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception
	{
		super.messageReceived(ctx, e);

		GsClientPacket clientPacket =  GSClientPacketHandlerFactory.getInstance().getPacketHandler().handle((ChannelBuffer) e.getMessage(), this);
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
		if(gameServerInfo != null)
		{
			gameServerInfo.setGsConnection(null);
			gameServerInfo.clearAccountsOnGameServer();
			gameServerInfo = null;
		}
	}

	/**
	 * @return GameServerInfo for this GsConnection or null if this GsConnection is not authenticated yet.
	 */
	public GameServerInfo getGameServerInfo()
	{
		return gameServerInfo;
	}

	/**
	 * @param gameServerInfo
	 *            Set GameServerInfo for this GsConnection.
	 */
	public void setGameServerInfo(GameServerInfo gameServerInfo)
	{
		this.gameServerInfo = gameServerInfo;
	}

	/**
	 * @return String info about this connection
	 */
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("GameServer [ID:");
		if(gameServerInfo != null)
		{
			sb.append(gameServerInfo.getId());
		}
		else
		{
			sb.append("null");
		}
		sb.append("] ").append(getIP());
		return sb.toString();
	}
}

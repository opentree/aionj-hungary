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

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.chatserver.serverpackets.SM_CS_PLAYER_AUTH;
import com.aionemu.gameserver.network.chatserver.serverpackets.SM_CS_PLAYER_LOGOUT;

/**
 * @author ATracer
 */
public class ChatServer
{

	private ChatServerConnection		chatServer;
	
	public static final ChatServer getInstance()
	{
		return SingletonHolder.instance;
	}

	private ChatServer()
	{
	}

	/**
	 * @param player
	 * @param token
	 */
	public void sendPlayerLoginRequst(Player player)
	{
		if(chatServer != null)
			chatServer.sendPacket(new SM_CS_PLAYER_AUTH(player.getObjectId(), player.getAcountName()));
	}
	
	/**
	 * 
	 * @param player
	 */
	public void sendPlayerLogout(Player player)
	{
		if(chatServer != null)
			chatServer.sendPacket(new SM_CS_PLAYER_LOGOUT(player.getObjectId()));
	}
	
	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder
	{
		protected static final ChatServer instance = new ChatServer();
	}

	public void chatServerDown() {
		// TODO Auto-generated method stub
		
	}
}

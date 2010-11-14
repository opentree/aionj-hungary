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
package com.aionemu.gameserver.configs.network;

import java.net.InetSocketAddress;

import com.aionemu.commons.configuration.Property;

public class NetworkConfig
{
	/**
	 * Game Server address
	 */
	@Property(key = "network.client.address", defaultValue = "*:7777")
	public static InetSocketAddress	CLIENT_ADDRESS;

	/**
	 * Max allowed online players
	 */
	@Property(key = "network.client.maxplayers", defaultValue = "100")
	public static int				MAX_ONLINE_PLAYERS;

	/**
	 * LoginServer address
	 */
	@Property(key = "network.login.address", defaultValue = "localhost:9014")
	public static InetSocketAddress	LOGIN_ADDRESS;

	/**
	 * ChatServer address
	 */
	@Property(key = "network.chat.address", defaultValue = "localhost:9021")
	public static InetSocketAddress	CHAT_ADDRESS;

	/**
	 * Password for this GameServer ID for authentication at ChatServer.
	 */
	@Property(key = "network.chat.password", defaultValue = "")
	public static String			CHAT_PASSWORD;

	/**
	 * GameServer id that this GameServer will request at LoginServer.
	 */
	@Property(key = "network.login.gsid", defaultValue = "0")
	public static int				GAMESERVER_ID;

	/**
	 * Password for this GameServer ID for authentication at LoginServer.
	 */
	@Property(key = "network.login.password", defaultValue = "")
	public static String			LOGIN_PASSWORD;
}

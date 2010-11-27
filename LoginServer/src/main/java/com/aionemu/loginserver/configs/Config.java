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
package com.aionemu.loginserver.configs;

import java.net.InetSocketAddress;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.aionemu.commons.configuration.ConfigurableProcessor;
import com.aionemu.commons.configuration.Property;
import com.aionemu.commons.database.DatabaseConfig;
import com.aionemu.commons.utils.AEInfos;
import com.aionemu.commons.utils.PropertiesUtils;

/**
 * @author -Nemesiss-
 * @author SoulKeeper
 */
public class Config {
	/**
	 * Logger for this class.
	 */
	protected static final Logger log = Logger.getLogger(Config.class);

	/**
	 * Login Server address to client
	 */
	@Property(key = "network.client.address", defaultValue = "*:2106")
	public static InetSocketAddress CLIENT_ADDRESS;

	/**
	 * Game Server address
	 */
	@Property(key = "network.gameserver.address", defaultValue = "localhost:9014")
	public static InetSocketAddress GAMESERVER_ADDRESS;

	@Property(key = "network.console.enabled", defaultValue = "false")
	public static boolean CONSOLE_ENABLED;

	@Property(key = "network.console.address", defaultValue = "localhost:9999")
	public static InetSocketAddress CONSOLE_ADDRESS;

	@Property(key = "network.fastreconnection.time", defaultValue = "10")
	public static int FAST_RECONNECTION_TIME;

	/**
	 * Number of trys of login before ban
	 */
	@Property(key = "network.client.logintrybeforeban", defaultValue = "5")
	public static int LOGIN_TRY_BEFORE_BAN;

	/**
	 * Ban time in minutes
	 */
	@Property(key = "network.client.bantimeforbruteforcing", defaultValue = "15")
	public static int WRONG_LOGIN_BAN_TIME;

	/**
	 * Should server automaticly create accounts for users or not?
	 */
	@Property(key = "accounts.autocreate", defaultValue = "true")
	public static boolean ACCOUNT_AUTO_CREATION;

	/**
	 * Load configs from files.
	 */
	public static void load() {
		try {
			AEInfos.printSection("Network");
			Properties[] props = PropertiesUtils
					.loadAllFromDirectory("./config/network");

			ConfigurableProcessor.process(Config.class, props);
			ConfigurableProcessor.process(DatabaseConfig.class, props);
		} catch (Exception e) {
			log.fatal("Can't load loginserver configuration", e);
			throw new Error("Can't load loginserver configuration", e);
		}
	}
}

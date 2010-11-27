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
package com.aionemu.loginserver.utils;

import java.sql.Timestamp;

import org.apache.log4j.Logger;

import com.aionemu.loginserver.configs.Config;
import com.aionemu.loginserver.controller.BannedIpController;
import com.aionemu.loginserver.network.aion.clientpackets.CM_LOGIN;

import javolution.util.FastMap;

/**
 * @author Mr. Poke
 * 
 */
public class FloodProtector {
	/**
	 * Logger for this class.
	 */
	private static final Logger log = Logger.getLogger(CM_LOGIN.class);

	private FastMap<String, Long> flood = new FastMap<String, Long>();

	public static final FloodProtector getInstance() {
		return SingletonHolder.instance;
	}

	public boolean addIp(String ip) {
		Long time = flood.get(ip);
		if (time == null
				|| System.currentTimeMillis() - time > Config.FAST_RECONNECTION_TIME) {
			flood.put(ip, System.currentTimeMillis());
			return false;
		}
		Timestamp newTime = new Timestamp(System.currentTimeMillis()
				+ Config.WRONG_LOGIN_BAN_TIME * 60000);
		BannedIpController.banIp(ip, newTime);
		log.info("[AUDIT]FloodProtector:" + ip + " IP banned for "
				+ Config.WRONG_LOGIN_BAN_TIME + " min");
		return true;
	}

	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder {
		protected static final FloodProtector instance = new FloodProtector();
	}
}

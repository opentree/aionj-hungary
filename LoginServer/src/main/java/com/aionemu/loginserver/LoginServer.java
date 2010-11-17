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
package com.aionemu.loginserver;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;

import com.aionemu.commons.database.DatabaseFactory;
import com.aionemu.commons.log4j.exceptions.Log4jInitializationError;
import com.aionemu.commons.scripting.AionScriptEngineManager;
import com.aionemu.commons.services.LoggingService;
import com.aionemu.commons.utils.AEInfos;
import com.aionemu.commons.utils.ExitCode;
import com.aionemu.loginserver.configs.Config;
import com.aionemu.loginserver.controller.AccountController;
import com.aionemu.loginserver.controller.BannedIpController;
import com.aionemu.loginserver.network.NettyLoginServer;
import com.aionemu.loginserver.network.ncrypt.KeyGen;
import com.aionemu.loginserver.utils.BruteForceProtector;
import com.aionemu.loginserver.utils.FloodProtector;

/**
 * @author -Nemesiss-
 */
public class LoginServer
{
	/**
	 * Logger for this class.
	 */
	private static final Logger	log	= Logger.getLogger(LoginServer.class);

	public static void main(String[] args)
	{
		new LoginServer();
	}

	public LoginServer() throws Log4jInitializationError
	{
		long start = System.currentTimeMillis();

		LoggingService.init();
		log.info("Logging Initialized.");

		Config.load();

		AEInfos.printSection("DataBase");
		DatabaseFactory.init();

		try
		{
			File scripts = new File("data/scripts/scripts.cfg");
			AionScriptEngineManager.getInstance().executeScriptList(scripts);
		}
		catch (IOException ioe)
		{
			log.fatal("Failed loading scripts.cfg, no script going to be loaded");
		}

		/**
		 * Initialize Key Generator
		 */
		try
		{
			AEInfos.printSection("KeyGen");
			KeyGen.init();
		}
		catch(Exception e)
		{
			log.fatal("Failed initializing Key Generator. Reason: " + e.getMessage(), e);
			System.exit(ExitCode.CODE_ERROR);
		}

		AEInfos.printSection("GSTable");
		GameServerTable.load();
		AEInfos.printSection("BannedIP");
		BannedIpController.load();

		FloodProtector.getInstance();
		BruteForceProtector.getInstance();
		AccountController.getInstance();

		AEInfos.printSection("Network");
		NettyLoginServer.getInstance();
		
		System.gc();
		
		Runtime.getRuntime().addShutdownHook(Shutdown.getInstance());
		
		AEInfos.printSection("System");
		AEInfos.printAllInfos();

		AEInfos.printSection("LoginServerLog");
		log.info("Total Boot Time: " + (System.currentTimeMillis() - start) / 1000 + " seconds.");
	}
}

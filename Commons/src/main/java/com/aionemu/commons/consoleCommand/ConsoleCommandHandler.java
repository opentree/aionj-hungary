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
package com.aionemu.commons.consoleCommand;

import java.io.File;
import java.util.Map;

import javolution.util.FastMap;

import org.apache.log4j.Logger;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;

import com.aionemu.commons.scripting.scriptmanager.ScriptManager;

/**
 * @author Mr. Poke
 * 
 */
public class ConsoleCommandHandler
{

	private static final Logger					log				= Logger.getLogger(ConsoleCommandHandler.class);

	private Map<String, AbstractConsoleCommand>	commands;

	public static final File					DESCRIPTOR_FILE	= new File("./data/scripts/system/consoleCommand.xml");

	private ScriptManager						sm;

	public static final ConsoleCommandHandler getInstance()
	{
		return SingletonHolder.instance;
	}

	ConsoleCommandHandler()
	{
		commands = new FastMap<String, AbstractConsoleCommand>();
		sm = new ScriptManager();
		sm.setGlobalClassListener(new ConsoleCommandLoader());
	}

	public void load()
	{
		try
		{
			sm.load(DESCRIPTOR_FILE);
		}
		catch(Exception e)
		{
			log.warn("Can't initialize console handlers.", e);
		}
		log.info("Loaded " + commands.size() + " console commands.");
	}

	void registerCommand(AbstractConsoleCommand command)
	{
		if(command == null)
			throw new NullPointerException("Command instance cannot be null");

		String commandName = command.getCommandName();

		AbstractConsoleCommand old = commands.put(commandName, command);
		if(old != null)
		{
			log.warn("Overriding handler for command " + commandName + " from " + old.getClass().getName() + " to "
				+ command.getClass().getName());
		}
	}

	public String handleCommand(String message, SimpleChannelUpstreamHandler con)
	{
		String[] commandAndParams = message.split(" ", 2);

		String command = commandAndParams[0];
		AbstractConsoleCommand c = commands.get(command);

		if(c == null)
			return null;
		String[] params = new String[] {};
		if(commandAndParams.length > 1)
			params = commandAndParams[1].split(" ", c.getSplitSize());

		return c.executeCommand(con, params);
	}

	/**
	 * Clear all registered handlers (before reload).
	 */
	public void clearHandlers()
	{
		this.commands.clear();
	}

	/**
	 * Returns count of available admin command handlers.
	 * 
	 * @return count of available admin command handlers.
	 */
	public int getSize()
	{
		return this.commands.size();
	}

	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder
	{
		protected static final ConsoleCommandHandler	instance	= new ConsoleCommandHandler();
	}
}

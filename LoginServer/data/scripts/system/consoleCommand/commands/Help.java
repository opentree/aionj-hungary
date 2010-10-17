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
package commands;

import org.jboss.netty.channel.SimpleChannelUpstreamHandler;

import com.aionemu.commons.consoleCommand.AbstractConsoleCommand;

/**
 * @author Mr. Poke
 *
 */
public class Help extends AbstractConsoleCommand
{

	/**
	 * @param commandName
	 */
	public Help()
	{
		super("help");
	}

	@Override
	public int getSplitSize()
	{
		return 1;
	}

	/* (non-Javadoc)
	 * @see com.aionemu.loginserver.consolCommand.AbstractConsolCommand#executeCommand(com.aionemu.loginserver.network.console.ConsoleHandler, java.lang.String[])
	 */
	@Override
	public String executeCommand(SimpleChannelUpstreamHandler con, String[] params)
	{
		return "This is help..... :P";
	}

}

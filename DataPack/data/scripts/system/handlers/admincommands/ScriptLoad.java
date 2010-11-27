/*
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, 
 * MA  02110-1301, USA.
 *
 * http://www.gnu.org/copyleft/gpl.html
 */
package system.handlers.admincommands;

import java.io.File;

import javax.script.ScriptException;

import com.aionemu.commons.scripting.AionScriptEngineManager;
import com.aionemu.gameserver.configs.administration.AdminConfig;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.chathandlers.AdminCommand;
import com.aionemu.gameserver.utils.chathandlers.AdminCommandChatHandler;

/**
 * @author Mr. Poke
 * 
 */
public class ScriptLoad extends AdminCommand
{

	public ScriptLoad()
	{
		super("script_load");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.aionemu.gameserver.utils.chathandlers.AdminCommand#executeCommand
	 * (com.aionemu.gameserver.model.gameobjects.player.Player,
	 * java.lang.String[])
	 */
	@Override
	public void executeCommand(Player admin, String[] params)
	{
		if (admin.getAccessLevel() < AdminConfig.COMMAND_SCRIPT_LOAD)
		{
			PacketSendUtility.sendMessage(admin, "You dont have enough rights to execute this command!");
			return;
		}
		if (params.length != 1)
		{
			PacketSendUtility.sendMessage(admin, "Example: //script_load system/handlers/quest/ascension/_1006Ascension.java");
			return;
		}

		File file = new File(AionScriptEngineManager.SCRIPT_FOLDER, params[0]);
		if (file.isFile())
		{
			try
			{
				AionScriptEngineManager.getInstance().executeScript(file);
			}
			catch (ScriptException e)
			{
				PacketSendUtility.sendMessage(admin, "Failed loading: " + params[0]);
				AionScriptEngineManager.getInstance().reportScriptFileError(file, e);
			}
			catch (Exception e)
			{
				PacketSendUtility.sendMessage(admin, "Failed loading: " + params[0]);
			}
		}
		else
		{
			PacketSendUtility.sendMessage(admin, "File Not Found: " + params[0]);
		}

	}

	public static void main(String[] args)
	{
		AdminCommandChatHandler.getInstance().registerAdminCommand(new ScriptLoad());
	}
}

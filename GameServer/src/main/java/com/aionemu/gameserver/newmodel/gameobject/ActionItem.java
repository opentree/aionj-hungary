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
package com.aionemu.gameserver.newmodel.gameobject;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.newmodel.gameobject.interfaces.IDialogRequest;
import com.aionemu.gameserver.newmodel.gameobject.interfaces.IDialogSelect;

/**
 * @author lyahim
 *
 */
public class ActionItem extends SpawnedObject implements IDialogSelect, IDialogRequest
{

	/**
	 * @param objId
	 */
	public ActionItem(Integer objId)
	{
		super(objId);
	}

	/* (non-Javadoc)
	 * @see com.aionemu.gameserver.model.newgameobject.interfaces.IDialogRequest#onDialogRequest(com.aionemu.gameserver.model.gameobjects.player.Player)
	 */
	@Override
	public void onDialogRequest(Player player)
	{
	}

	/* (non-Javadoc)
	 * @see com.aionemu.gameserver.model.newgameobject.interfaces.IDialogSelect#onDialogSelect(int, com.aionemu.gameserver.model.gameobjects.player.Player, int)
	 */
	@Override
	public void onDialogSelect(int dialogId, Player player, int questId)
	{
	}

}

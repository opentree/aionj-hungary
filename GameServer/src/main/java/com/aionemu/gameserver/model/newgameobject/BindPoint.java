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
package com.aionemu.gameserver.model.newgameobject;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.newgameobject.interfaces.IDialogRequest;

/**
 * @author lyahim
 *
 */
public class BindPoint extends SpawnedObject implements IDialogRequest
{

	/**
	 * @param objId
	 */
	public BindPoint(Integer objId)
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

}

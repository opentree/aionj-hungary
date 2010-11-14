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

import com.aionemu.gameserver.newmodel.gameobject.Player.Player;
import com.aionemu.gameserver.newmodel.gameobject.interfaces.IReward;

/**
 * @author lyahim
 *
 */
public final class Gatherable extends SpawnedObject<Gatherable> implements IReward
{

	public Gatherable(Integer objId)
	{
		super(objId);
	}

	/* (non-Javadoc)
	 * @see com.aionemu.gameserver.model.newgameobject.interfaces.IReward#doReward(com.aionemu.gameserver.model.newgameobject.Player.Player)
	 */
	@Override
	public void doReward(Player player)
	{
	}

	/* (non-Javadoc)
	 * @see com.aionemu.gameserver.newmodel.gameobject.SpawnedObject#onSpawn()
	 */
	@Override
	protected void onSpawn()
	{
	}

	/* (non-Javadoc)
	 * @see com.aionemu.gameserver.newmodel.gameobject.SpawnedObject#onRespawn()
	 */
	@Override
	protected void onRespawn()
	{
	}

}

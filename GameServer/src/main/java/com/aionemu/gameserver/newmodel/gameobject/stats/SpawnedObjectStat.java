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
package com.aionemu.gameserver.newmodel.gameobject.stats;

import com.aionemu.gameserver.newmodel.gameobject.SpawnedObject;

/**
 * @author Mr. Poke
 *
 */
public class SpawnedObjectStat
{
	private SpawnedObject owner;

	/**
	 * @param owner
	 */
	public SpawnedObjectStat(SpawnedObject owner)
	{
		super();
		this.owner = owner;
	}
	
	/**
	 * @return Returns the owner.
	 */
	public SpawnedObject getOwner()
	{
		return owner;
	}

	/**
	 * @return the currentHp
	 */
	public int getCurrentHp()
	{
		return 100;
	}

	/**
	 * @return the currentMp
	 */
	public int getCurrentMp()
	{
		return 100;
	}
	
	/**
	 * @return maxHp
	 */
	public int getMaxHp()
	{
		return 100;
	}
	
	/**
	 * @return maxMp
	 */
	public int getMaxMp()
	{
		return 100;
	}
	
	/**
	 * @return movementSpeed
	 */
	public int getMovementSpeed()
	{
		return 0;
	}
}

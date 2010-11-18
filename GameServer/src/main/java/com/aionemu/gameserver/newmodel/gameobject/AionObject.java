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
package com.aionemu.gameserver.newmodel.gameobject;

import com.aionemu.gameserver.utils.idfactory.IDFactory;

/**
 * This is the base class for all "in-game" objects, that player can interact with, such as: npcs, monsters, players,
 * items.<br>
 * <br>
 * Each AionObject is uniquely identified by objectId.
 * 
 * @author -Nemesiss-
 * @author SoulKeeper
 * 
 */
public abstract class AionObject
{
	/**
	 * Unique id, for all game objects such as: items, players, monsters.
	 */
	protected int	objectId;

	public AionObject()
	{
		 this.objectId = IDFactory.getInstance().nextId();
	}

	/**
	 * Returns unique ObjectId of AionObject
	 * 
	 * @return Int ObjectId
	 */
	public int getObjectId()
	{
		return objectId;
	}

	/**
	 * Returns name of the object.<br>
	 * Unique for players, common for NPCs, items, etc
	 * 
	 * @return name of the object
	 */
	public abstract String getName();

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj)
	{
		if(this == obj)
			return true;
		if(obj == null)
			return false;
		if(getClass() != obj.getClass())
			return false;
		AionObject other = (AionObject) obj;
		if(objectId != other.objectId)
			return false;
		return true;
	}
}

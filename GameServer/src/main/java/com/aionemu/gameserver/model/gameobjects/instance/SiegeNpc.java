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
package com.aionemu.gameserver.model.gameobjects.instance;

import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.siege.SiegeRace;
import com.aionemu.gameserver.model.templates.spawn.SpawnTemplate;

/**
 * @author Mr. Poke
 *
 */
public class SiegeNpc extends Npc
{
	private int siegeId;
	SiegeRace siegeRace;
	/**
	 * @param objId
	 * @param spawnTemplate
	 */
	public SiegeNpc(int objId, SpawnTemplate spawnTemplate)
	{
		super(objId, spawnTemplate);
	}

	public SiegeRace getSiegeRace() 
	{
		return siegeRace;
	}

	public int getSiegeId() 
	{
		return siegeId;
	}
}

/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.aionemu.gameserver.model;

import com.aionemu.gameserver.model.templates.spawn.SpawnTemplate;

/**
 * This class permit to pass (x, y, z, heading) position data to method.<BR>
 * <BR>
 */
public final class Position
{

	public final float	x;
	public final float	y;
	public final float	z;
	public final byte	heading;

	public Position(SpawnTemplate spawn)
	{

		x = spawn.getX();
		y = spawn.getY();
		z = spawn.getZ();
		heading = spawn.getHeading();
	}

	public Position(float x, float y, float z, byte heading)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		this.heading = heading;
	}
}

/*
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
package com.aionemu.gameserver.newmodel.templates.spawn;

/**
 * @author Luno
 * 
 * modified by ATracer
 */

public class SpawnTemplate
{
	private byte heading;
	private float z;
	private float y;
	private float x;
	private int mapId;
	private int staticid;
	
	/**
	 * Constructor used by unmarshaller
	 */
	public SpawnTemplate()
	{
		
	}

	/**
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @param heading
	 */
	public SpawnTemplate(float x, float y, float z, byte heading)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		this.heading = heading;
	}
	
	public int getMapId()
	{
		return mapId;
	}

	public float getX()
	{
		return x;
	}

	public float getY()
	{
		return y;
	}

	public float getZ()
	{
		return z;
	}

	public byte getHeading()
	{
		return heading;
	}

	/**
	 * @return the staticid
	 */
	public int getStaticid()
	{
		return staticid;
	}

	/**
	 * @return
	 */
	public SpawnTime getSpawnTime()
	{
		return null;
	}
}

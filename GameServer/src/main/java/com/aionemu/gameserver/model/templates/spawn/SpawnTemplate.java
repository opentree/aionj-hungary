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
package com.aionemu.gameserver.model.templates.spawn;

/**
 * @author Luno
 * 
 * modified by ATracer
 */

public class SpawnTemplate
{
	private int id;
	private int templateId;
	private byte heading;
	private float z;
	private float y;
	private float x;
	private int interval;
	private int mapId;
	private int staticid;
	private SpawnTime spawnTime;

	/**
	 * @param id
	 * @param templateId
	 * @param heading
	 * @param x
	 * @param y
	 * @param z
	 * @param interval
	 * @param mapId
	 * @param staticid
	 */
	public SpawnTemplate(int id, int templateId, int mapId, float x, float y, float z, byte heading, int interval, int staticid, SpawnTime spawnTime)
	{
		super();
		this.id = id;
		this.templateId = templateId;
		this.heading = heading;
		this.z = z;
		this.y = y;
		this.x = x;
		this.interval = interval;
		this.mapId = mapId;
		this.staticid = staticid;
		this.spawnTime = spawnTime;
	}

	/**
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @param heading
	 */
	public SpawnTemplate(int templateId, int mapId, float x, float y, float z, byte heading)
	{
		this.templateId = templateId;
		this.x = x;
		this.y = y;
		this.z = z;
		this.heading = heading;
		this.interval = 0;
		this.staticid = 0;
		this.spawnTime = SpawnTime.ALL;
	}
	
	/**
	 * @return Returns the id.
	 */
	public int getId()
	{
		return id;
	}

	/**
	 * @param id The id to set.
	 */
	public void setId(int id)
	{
		this.id = id;
	}

	public int getMapId()
	{
		return mapId;
	}

	/**
	 * @return Returns the templateId.
	 */
	public int getTemplateId()
	{
		return templateId;
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
	 * @return Returns the interval.
	 */
	public int getInterval()
	{
		return interval;
	}

	/**
	 * @return the staticid
	 */
	public int getStaticid()
	{
		return staticid;
	}

	/**
	 * @return Returns the spawnTime.
	 */
	public SpawnTime getSpawnTime()
	{
		return spawnTime;
	}
}

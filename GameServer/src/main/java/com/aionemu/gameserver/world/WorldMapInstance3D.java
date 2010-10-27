/*
 * This file is part of aion-unique <aion-unique.org>.
 *
 *  aion-unique is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  aion-unique is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with aion-unique.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.aionemu.gameserver.world;

import org.apache.log4j.Logger;

/**
 * @author Mr. Poke
 * 
 */
public class WorldMapInstance3D extends WorldMapInstance
{

	/**
	 * Logger for this class.
	 */
	private static final Logger				log			= Logger.getLogger(WorldMapInstance3D.class);

	private MapRegion[][][]		regions;
	/**
	 * @param parent
	 * @param instanceId
	 */
	public WorldMapInstance3D(WorldMap parent, int instanceId)
	{
		super(parent, instanceId);
	}

	protected void initMapRegions()
	{
		regions = new MapRegion[getParent().getWorldSize()/regionSize+1][getParent().getWorldSize()/regionSize+1][5000/regionSize+1];
		int size = this.getParent().getWorldSize()/regionSize;
		int zSize = 5000/regionSize; 
		
		for (int x=0; x < size ; x++)
		{
			for (int y=0; y < size ; y++)
			{
				for (int z=0; z < zSize ; z++)
				{
					createMapRegion(x,y,z);
				}
			}
		}
	}

	/**
	 * Create new MapRegion and add link to neighbours.
	 * 
	 * @param regionId
	 * @return newly created map region
	 */
	@Override
	protected MapRegion createMapRegion(int x, int y, int z)
	{
		MapRegion r = new MapRegion(this, true);
		regions[x][y][z] = r;

		for(int x2 = x - 1; x2 <= x + 1; x2++)
		{
			for(int y2 = y - 1; y2 <= y + 1; y2++)
			{
				for(int z2 = z - 1; z2 <= z + 1; z2++)
				{
					if((x2 == x && y2 == y && z2 == z) || x2 == -1 || y2 == -1 || z2 == -1)
						continue;

					MapRegion neighbour = regions[x2][y2][z2];
					if(neighbour != null)
					{
						r.addNeighbourRegion(neighbour);
						neighbour.addNeighbourRegion(r);
					}
				}
			}
		}
		return r;
	}

	/**
	 * Returns MapRegion that contains given x,y coordinates. If the region doesn't exist, it's created.
	 *
	 * @param x
	 * @param y
	 * @return a MapRegion
	 */
	MapRegion getRegion(float x, float y, float z)
	{
		MapRegion region = null;
		try
		{
			region = regions[(int)x/regionSize][(int)y/regionSize][((int)z+offset)/regionSize];
		}
		catch (ArrayIndexOutOfBoundsException e)
		{
			log.warn("MAP REGION: Cord out of world!!! x: "+x+" y: "+y+" z: "+z);
			return null;
		}
		if(region == null)
		{
			log.warn("MAP REGION: Not found!!! x: "+x+" y: "+y+" z: "+z);
		}
		return region;
	}

}

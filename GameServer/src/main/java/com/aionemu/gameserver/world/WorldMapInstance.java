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
package com.aionemu.gameserver.world;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;

import javolution.util.FastMap;

import com.aionemu.gameserver.configs.main.OptionsConfig;
import com.aionemu.gameserver.model.gameobjects.AionObject;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.group.PlayerGroup;
import com.aionemu.gameserver.world.exceptions.DuplicateAionObjectException;

/**
 * World map instance object.
 * 
 * @author -Nemesiss-
 * 
 */
public class WorldMapInstance
{
	/**
	 * Logger for this class.
	 */
	private static final Logger					log					= Logger.getLogger(WorldMapInstance.class);

	/**
	 * Size of region
	 */
	public static final int						regionSize			= OptionsConfig.REGION_SIZE;

	protected static final int					offset				= 1000;
	/**
	 * WorldMap witch is parent of this instance.
	 */
	private final WorldMap						parent;
	/**
	 * Map of active regions.
	 */
	protected MapRegion[][]						regions;

	/**
	 * All objects spawned in this world map instance
	 */
	private final Map<Integer, VisibleObject>	worldMapObjects		= new FastMap<Integer, VisibleObject>().shared();

	/**
	 * All players spawned in this world map instance
	 */
	private final Map<Integer, Player>			worldMapPlayers		= new FastMap<Integer, Player>().shared();

	private final Set<Integer>					registeredObjects	= Collections.newSetFromMap(new FastMap<Integer, Boolean>().shared());

	private PlayerGroup							registeredGroup		= null;

	private Future<?>							emptyInstanceTask	= null;

	/**
	 * Id of this instance (channel)
	 */
	private int									instanceId;

	/**
	 * Constructor.
	 * 
	 * @param parent
	 */
	public WorldMapInstance(WorldMap parent, int instanceId)
	{
		this.parent = parent;
		this.instanceId = instanceId;
		initMapRegions();
	}

	/**
	 * Return World map id.
	 * 
	 * @return world map id
	 */
	public Integer getMapId()
	{
		return getParent().getMapId();
	}

	protected void initMapRegions()
	{
		this.regions = new MapRegion[parent.getWorldSize() / regionSize + 1][parent.getWorldSize() / regionSize + 1];
		int size = parent.getWorldSize() / regionSize;
		//Create all mapRegion
		for (int x = 0; x <= size; x++)
		{
			for (int y = 0; y <= size; y++)
			{
				regions[x][y] = new MapRegion(this, false);
			}
		}

		// Add Neighbour
		for (int x = 0; x <= size; x++)
		{
			for (int y = 0; y <= size; y++)
			{
				MapRegion mapRegion = regions[x][y];
				for (int x2 = x - 1; x2 <= x + 1; x2++)
				{
					for (int y2 = y - 1; y2 <= y + 1; y2++)
					{
						if ((x2 == x && y2 == y) || x2 == -1 || y2 == -1)
							continue;
						try
						{
							MapRegion neighbour = regions[x2][y2];
							mapRegion.addNeighbourRegion(neighbour);
						}
						catch (ArrayIndexOutOfBoundsException e)
						{
							continue;
						}
						catch (NullPointerException e)
						{
							continue;
						}
					}
				}
			}
		}

		log.debug(this.getMapId() + " Created map regions: " + regions.length);
	}

	/**
	 * Returns WorldMap witch is parent of this instance
	 * 
	 * @return parent
	 */
	public WorldMap getParent()
	{
		return parent;
	}

	/**
	 * Returns MapRegion that contains coordinates of VisibleObject. If the region doesn't exist, it's created.
	 *
	 * @param object
	 *
	 * @return a MapRegion
	 */
	MapRegion getRegion(VisibleObject object)
	{
		return getRegion(object.getX(), object.getY(), object.getZ());
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
			region = regions[(int) x / regionSize][(int) y / regionSize];
		}
		catch (ArrayIndexOutOfBoundsException e)
		{
			log.warn("MAP REGION: Cord out of world!!! x: " + x + " y: " + y + " z: " + z);
		}
		if (region == null)
		{
			log.warn("MAP REGION: Not found!!! x: " + x + " y: " + y + " z: " + z);
		}
		return region;
	}

	/**
	 * Returs {@link World} instance to which belongs this WorldMapInstance
	 * 
	 * @return World
	 */
	public World getWorld()
	{
		return getParent().getWorld();
	}

	/**
	 * 
	 * @param object
	 */
	public void addObject(VisibleObject object)
	{
		if (worldMapObjects.put(object.getObjectId(), object) != null)
			throw new DuplicateAionObjectException();

		if (object instanceof Player)
			worldMapPlayers.put(object.getObjectId(), (Player) object);
	}

	/**
	 * 
	 * @param object
	 */
	public void removeObject(AionObject object)
	{
		worldMapObjects.remove(object.getObjectId());
		if (object instanceof Player)
			worldMapPlayers.remove(object.getObjectId());
	}

	/**
	 * @return the instanceIndex
	 */
	public int getInstanceId()
	{
		return instanceId;
	}

	/**
	 * Check player is in instance
	 * 
	 * @param objId
	 * @return
	 */
	public boolean isInInstance(int objId)
	{
		return worldMapPlayers.containsKey(objId);
	}

	public Collection<VisibleObject> getAllWorldMapObjects()
	{
		return worldMapObjects.values();
	}

	public Collection<Player> getAllWorldMapPlayers()
	{
		return worldMapPlayers.values();
	}

	public void registerGroup(PlayerGroup group)
	{
		registeredGroup = group;
		register(group.getGroupId());
	}

	/**
	 * @param objectId
	 */
	public void register(int objectId)
	{
		registeredObjects.add(objectId);
	}

	/**
	 * @return the registeredObjects
	 */
	public int getRegisteredObjectsSize()
	{
		return registeredObjects.size();
	}

	/**
	 * @param objectId
	 * @return
	 */
	public boolean isRegistered(int objectId)
	{
		return registeredObjects.contains(objectId);
	}

	/**
	 * @return the emptyInstanceTask
	 */
	public Future<?> getEmptyInstanceTask()
	{
		return emptyInstanceTask;
	}

	/**
	 * @param emptyInstanceTask
	 *            the emptyInstanceTask to set
	 */
	public void setEmptyInstanceTask(Future<?> emptyInstanceTask)
	{
		this.emptyInstanceTask = emptyInstanceTask;
	}

	/**
	 * @return the registeredGroup
	 */
	public PlayerGroup getRegisteredGroup()
	{
		return registeredGroup;
	}

	/**
	 * 
	 * @return
	 */
	public int playersCount()
	{
		return worldMapPlayers.size();
	}
}

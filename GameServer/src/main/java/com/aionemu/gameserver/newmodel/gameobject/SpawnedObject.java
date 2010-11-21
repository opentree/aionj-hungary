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

import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.newmodel.gameobject.interfaces.IReward;
import com.aionemu.gameserver.newmodel.gameobject.knowlist.KnownList;
import com.aionemu.gameserver.newmodel.templates.IObjectTemplate;
import com.aionemu.gameserver.newmodel.templates.spawn.SpawnTemplate;
import com.aionemu.gameserver.newmodel.world.MapRegion;
import com.aionemu.gameserver.newmodel.world.World;
import com.aionemu.gameserver.newmodel.world.WorldPosition;


/**
 * @author lyahim
 *
 */

public abstract class SpawnedObject extends AionObject
{
	/**
	 * @param objectId
	 * @param templateId
	 */
	public SpawnedObject(int objectId, int templateId)
	{
		super(objectId, templateId);
	}

	protected IObjectTemplate 	objectTemplate;
	protected WorldPosition		position;
	protected KnownList			knownlist;
	protected SpawnTemplate		spawnTemplate;
	protected int				templateId;
	
	public void spawn(SpawnTemplate spawnTemplate, int instanceId)
	{
		this.objectTemplate = DataManager.NPC_DATA.getNpcTemplate(templateId);
		this.spawnTemplate = spawnTemplate;
		this.position = new WorldPosition();
		this.knownlist = new KnownList(this);
		onSpawn(instanceId);
	}
	
	public void respawn()
	{	
		onDespawn();
		onRespawn();
	}
	
	public void despawn()
	{	
		onDespawn();
	}

	@Override
	public String getName()
	{
		if(objectTemplate != null)
			return objectTemplate.getName();
		return "SpawnedObject: " + this.getClass().getSimpleName();
	}
	
	/**
	 * @return Returns the objectTemplate.
	 */
	public IObjectTemplate getObjectTemplate()
	{
		return objectTemplate;
	}

	public void delete()
	{
/*		if(position.isSpawned())
			World.getInstance().despawn(getOwner());
		World.getInstance().removeObject(getOwner());*/
	}
	public void onDespawn(){}
	
	private void onSpawn(int instanceId)
	{
		World world = World.getInstance();
		world.storeObject(this);
		world.setPosition(this, spawnTemplate.getMapId(), instanceId, spawnTemplate.getX(), spawnTemplate.getY(), spawnTemplate.getZ(),
				spawnTemplate.getHeading());
		world.spawn(this);
	}
	
	public void onRespawn()
	{
		if(this instanceof IReward)
		{
//			DropService.getInstance().unregisterDrop(this);	
		}
	}

	/**
	 * @param object
	 */
	public void see(SpawnedObject object)
	{
	}

	/**
	 * @param object
	 * @param isOutOfRange
	 */
	public void notSee(SpawnedObject object, boolean isOutOfRange)
	{
	}

	/**
	 * @return
	 */
	public KnownList getKnownList()
	{
		return knownlist;
	}

	/**
	 * @return
	 */
	public MapRegion getActiveRegion()
	{
		return position.getMapRegion();
	}

	/**
	 * @return
	 */
	public WorldPosition getPosition()
	{
		return position;
	}

	/**
	 * @return
	 */
	public SpawnTemplate getSpawn()
	{
		return spawnTemplate;
	}
	
}

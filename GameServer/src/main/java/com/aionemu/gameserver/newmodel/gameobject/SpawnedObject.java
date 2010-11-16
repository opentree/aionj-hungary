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

import com.aionemu.gameserver.newmodel.gameobject.interfaces.IReward;
import com.aionemu.gameserver.newmodel.templates.IObjectTemplate;
import com.aionemu.gameserver.newmodel.templates.spawn.SpawnTemplate;
import com.aionemu.gameserver.world.KnownList;
import com.aionemu.gameserver.world.WorldPosition;


/**
 * @author lyahim
 *
 */

public abstract class SpawnedObject<T> extends AionObject
{
	protected IObjectTemplate 	objectTemplate;
	protected WorldPosition		position;
	protected KnownList			knownlist;
	protected SpawnTemplate		spawnTemplate;
	
	@SuppressWarnings("unchecked")
	public T spawn(SpawnTemplate spawnTemplate, KnownList knownList)
	{
		this.spawnTemplate = spawnTemplate;
		this.position = new WorldPosition();
		this.knownlist = knownList;
		onSpawn();
		return (T) this;
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
		
	protected SpawnedObject(Integer objId, IObjectTemplate objectTemplate)
	{
		super(objId);
		this.objectTemplate = objectTemplate;
	}

	@Override
	public String getName()
	{
		if(objectTemplate != null)
			return objectTemplate.getName();
		return "SpawnedObject: " + this.getClass().getSimpleName();
	}
	
	public void delete()
	{
/*		if(position.isSpawned())
			World.getInstance().despawn(getOwner());
		World.getInstance().removeObject(getOwner());*/
	}
	protected void onDespawn(){}
	
	protected void onSpawn(){}
	
	protected void onRespawn()
	{
		if(this instanceof IReward)
		{
//			DropService.getInstance().unregisterDrop(this);	
		}
	}
	
}

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

import com.aionemu.gameserver.newmodel.templates.IObjectTemplate;
import com.aionemu.gameserver.newmodel.templates.spawn.SpawnTemplate;
import com.aionemu.gameserver.world.KnownList;
import com.aionemu.gameserver.world.World;
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
	public T spawn(IObjectTemplate objectTemplate, SpawnTemplate spawnTemplate, KnownList knownList)
	{
		this.objectTemplate = objectTemplate;
		this.spawnTemplate = spawnTemplate;
		this.position = new WorldPosition();
		this.position.setIsSpawned(true);
		this.knownlist = knownList;
		onSpawn();
		return (T) this;
	}
	
	public void respawn(T spawned)
	{	
		delete();
		onRespawn();
	}
		
	protected SpawnedObject(Integer objId)
	{
		super(objId);
	}

	@Override
	public String getName()
	{
		return objectTemplate.getName();
	}
	
	public void delete()
	{
/*		if(position.isSpawned())
			World.getInstance().despawn(getOwner());
		World.getInstance().removeObject(getOwner());*/
	}
	
	protected abstract void onSpawn();
	
	protected abstract void onRespawn();
	
}

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
package com.aionemu.gameserver.taskmanager.tasks;

import java.util.Map;

import javolution.text.TextBuilder;
import javolution.util.FastMap;

import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.templates.spawn.SpawnTemplate;
import com.aionemu.gameserver.taskmanager.AbstractPeriodicTaskManager;

/**
 * @author Mr. Poke
 *
 */
public final class RespawnTaskManager extends AbstractPeriodicTaskManager
{
	
	public static RespawnTaskManager getInstance()
	{
		return SingletonHolder._instance;
	}
	
	private final FastMap<VisibleObject, Long> _respawnTasks = new FastMap<VisibleObject, Long>();
	
	private RespawnTaskManager()
	{
		super(1000);
	}
	
	public boolean hasRespawnTask(VisibleObject actor)
	{
		readLock();
		try
		{
			return _respawnTasks.containsKey(actor);
		}
		finally
		{
			readUnlock();
		}
	}
	
	public double getRemainingRespawnTime(VisibleObject actor)
	{
		readLock();
		try
		{
			double remaining = _respawnTasks.get(actor) - System.currentTimeMillis();
			
			return remaining / getRespawnTime0(actor);
		}
		finally
		{
			readUnlock();
		}
	}
	
	public void addRespawnTask(VisibleObject actor)
	{
		writeLock();
		try
		{
			_respawnTasks.put(actor, System.currentTimeMillis() + getRespawnTime0(actor));
		}
		finally
		{
			writeUnlock();
		}
	}
	
	public void addRespawnTask(VisibleObject actor, long respawnTime)
	{
		writeLock();
		try
		{
			_respawnTasks.put(actor, respawnTime);
		}
		finally
		{
			writeUnlock();
		}
	}

	private int getRespawnTime0(VisibleObject actor)
	{
		SpawnTemplate spawnTemplate = actor.getSpawn();
		if (spawnTemplate == null)
			return 0;
		return spawnTemplate.getInterval()*1000;
	}
	
	public void cancelRespawnTask(VisibleObject actor)
	{
		writeLock();
		try
		{
			_respawnTasks.remove(actor);
		}
		finally
		{
			writeUnlock();
		}
	}
	
	@Override
	public void run()
	{
		writeLock();
		try
		{
			for (Map.Entry<VisibleObject, Long> entry : _respawnTasks.entrySet())
			{
				if (System.currentTimeMillis() > entry.getValue())
				{
					final VisibleObject actor = entry.getKey();
					
					actor.onRespawn();
					
					_respawnTasks.remove(actor);
				}
			}
		}
		finally
		{
			writeUnlock();
		}
	}
	
	public String getStats()
	{
		readLock();
		final TextBuilder sb = TextBuilder.newInstance();
		try
		{
			sb.append("============= RespawnTask Manager Report ============").append("\r\n");
			sb.append("Tasks count: ").append(_respawnTasks.size()).append("\r\n");
			sb.append("Tasks dump:").append("\r\n");
			
			for (VisibleObject actor : _respawnTasks.keySet())
			{
				sb.append("(").append(_respawnTasks.get(actor) - System.currentTimeMillis()).append(") - ");
				sb.append(actor.getClass().getSimpleName()).append("/").append(actor.getName()).append("\r\n");
			}
			
			return sb.toString();
		}
		finally
		{
			TextBuilder.recycle(sb);
			readUnlock();
		}
	}
	
	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder
	{
		protected static final RespawnTaskManager _instance = new RespawnTaskManager();
	}
}

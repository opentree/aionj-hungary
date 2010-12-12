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
package com.aionemu.gameserver.taskmanager.tasks;

import java.util.Map;

import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.instance.Kisk;
import com.aionemu.gameserver.taskmanager.AbstractPeriodicTaskManager;

import javolution.text.TextBuilder;
import javolution.util.FastMap;

public final class DecayTaskManager extends AbstractPeriodicTaskManager
{
	public static final int DECAY_TIME = 300000; //5 min
	
	public static DecayTaskManager getInstance()
	{
		return SingletonHolder._instance;
	}
	
	private final FastMap<VisibleObject, Long> _decayTasks = new FastMap<VisibleObject, Long>();
	
	private DecayTaskManager()
	{
		super(1000);
	}
	
	public boolean hasDecayTask(VisibleObject actor)
	{
		readLock();
		try
		{
			return _decayTasks.containsKey(actor);
		}
		finally
		{
			readUnlock();
		}
	}
	
	public double getRemainingDecayTime(VisibleObject actor)
	{
		readLock();
		try
		{
			double remaining = _decayTasks.get(actor) - System.currentTimeMillis();
			
			return remaining / getDecayTime0(actor);
		}
		finally
		{
			readUnlock();
		}
	}
	
	public void addDecayTask(VisibleObject actor)
	{
		writeLock();
		try
		{
			_decayTasks.put(actor, System.currentTimeMillis() + getDecayTime0(actor));
		}
		finally
		{
			writeUnlock();
		}
	}
	
	private int getDecayTime0(VisibleObject actor)
	{
		if (actor instanceof Kisk)
			return 3000;
		return DECAY_TIME;
	}
	
	public void cancelDecayTask(VisibleObject actor)
	{
		writeLock();
		try
		{
			_decayTasks.remove(actor);
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
			for (Map.Entry<VisibleObject, Long> entry : _decayTasks.entrySet())
			{
				if (System.currentTimeMillis() > entry.getValue())
				{
					final VisibleObject actor = entry.getKey();
					
					actor.onDespawn();
					
					_decayTasks.remove(actor);
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
			sb.append("============= DecayTask Manager Report ============").append("\r\n");
			sb.append("Tasks count: ").append(_decayTasks.size()).append("\r\n");
			sb.append("Tasks dump:").append("\r\n");
			
			for (VisibleObject actor : _decayTasks.keySet())
			{
				sb.append("(").append(_decayTasks.get(actor) - System.currentTimeMillis()).append(") - ");
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
		protected static final DecayTaskManager _instance = new DecayTaskManager();
	}
}
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
import com.aionemu.gameserver.newmodel.gameobject.enums.eGatherState;
import com.aionemu.gameserver.newmodel.templates.IObjectTemplate;
import com.aionemu.gameserver.newmodel.templates.spawn.SpawnTemplate;
import com.aionemu.gameserver.skillengine.task.GatheringTask;

/**
 * @author lyahim
 *
 */
public final class Gatherable extends SpawnedObject
{
	/**
	 * @param objectId
	 * @param templateId
	 */
	public Gatherable(int objectId, int templateId)
	{
		super(objectId, templateId);
	}

	private int gatherCount;
	
	private int currentGatherer;
	//TODO move
	private GatheringTask task;
	
	private eGatherState state = eGatherState.IDLE;

	/* (non-Javadoc)
	 * @see com.aionemu.gameserver.newmodel.gameobject.SpawnedObject#spawn(com.aionemu.gameserver.newmodel.templates.spawn.SpawnTemplate, int)
	 */
	@Override
	public void spawn(SpawnTemplate spawnTemplate, int instanceId)
	{
		super.spawn(spawnTemplate, instanceId);
		objectTemplate=DataManager.GATHERABLE_DATA.getGatherableTemplate(templateId);
	}

	@Override
	public void onRespawn()
	{
		this.gatherCount = 0;
	}

}

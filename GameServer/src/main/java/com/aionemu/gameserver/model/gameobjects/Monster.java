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
package com.aionemu.gameserver.model.gameobjects;

import com.aionemu.gameserver.controllers.MonsterController;
import com.aionemu.gameserver.model.templates.ObjectTemplate;
import com.aionemu.gameserver.model.templates.spawn.SpawnTemplate;

public class Monster extends Npc
{	
	/**
	 * 
	 * @param objId
	 * @param controller
	 * @param spawn
	 * @param objectTemplate
	 */
	public Monster(int objId, MonsterController controller, SpawnTemplate spawn, ObjectTemplate objectTemplate)
	{
		super(objId, controller, spawn, objectTemplate);
	}

	@Override
	public MonsterController getController()
	{
		return (MonsterController) super.getController();
	}

	@Override
	public void initializeAi()
	{
		
	}
}

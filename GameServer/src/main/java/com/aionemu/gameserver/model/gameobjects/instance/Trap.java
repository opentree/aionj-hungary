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
package com.aionemu.gameserver.model.gameobjects.instance;

import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.interfaces.ISummoned;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.spawn.SpawnTemplate;

/**
 * @author ATracer
 *
 */
public class Trap extends Npc implements ISummoned
{

	/**
	 * Skill that will be used upon execution
	 */
	private int			skillId;
	/**
	 * Creator of this trap.
	 */
	private Creature	master;

	/**
	 * 
	 * @param objId
	 * @param controller
	 * @param spawnTemplate
	 * @param objectTemplate
	 */
	public Trap(int objId, SpawnTemplate spawnTemplate)
	{
		super(objId, spawnTemplate);
	}

	/**
	 * @return the skillId
	 */
	public int getSkillId()
	{
		return skillId;
	}

	/**
	 * @param skillId the skillId to set
	 */
	public void setSkillId(int skillId)
	{
		this.skillId = skillId;
	}

	@Override
	public byte getLevel()
	{
		return (this.master == null ? 1 : this.master.getLevel());
	}

	@Override
	public void initializeAi()
	{
	}

	@Override
	public boolean isEnemyNpc(Npc visibleObject)
	{
		return this.master.isEnemyNpc(visibleObject);
	}

	@Override
	public boolean isEnemyPlayer(Player visibleObject)
	{
		return this.master.isEnemyPlayer(visibleObject);
	}

	@Override
	public Creature getActingCreature()
	{
		return this.master;
	}

	@Override
	public Creature getMaster()
	{
		return this.master;
	}

	/* (non-Javadoc)
	 * @see com.aionemu.gameserver.model.gameobjects.interfaces.ISummoned#setMaster(com.aionemu.gameserver.model.gameobjects.Creature)
	 */
	@Override
	public void setMaster(Creature creature)
	{
	}
}

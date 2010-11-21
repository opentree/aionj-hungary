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
package com.aionemu.gameserver.model.gameobjects.stats;

import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.state.CreatureState;

/**
 * @author Mr. Poke
 *
 */
public class CreatureStats extends StaticNpcStats
{
	private Creature owner;

	/**
	 * @param owner
	 */
	public CreatureStats(Creature owner)
	{
		this.owner = owner;
	}
	
	/* (non-Javadoc)
	 * @see com.aionemu.gameserver.model.gameobjects.stats.StaticNpcStats#getAttackSpeed()
	 */
	@Override
	public int getAttackSpeed()
	{
		return owner.getGameStats().getCurrentStat(StatEnum.ATTACK_SPEED);
	}
	
	/* (non-Javadoc)
	 * @see com.aionemu.gameserver.model.gameobjects.stats.StaticNpcStats#getMovementSpeed()
	 */
	@Override
	public int getMovementSpeed()
	{
		//TODO Walking Speed
		if (owner.isInState(CreatureState.FLYING))
			return owner.getGameStats().getCurrentStat(StatEnum.FLY_SPEED);
		else
			return owner.getGameStats().getCurrentStat(StatEnum.SPEED);
	}
	
	/* (non-Javadoc)
	 * @see com.aionemu.gameserver.model.gameobjects.stats.StaticNpcStats#getCurrentHp()
	 */
	@Override
	public int getCurrentHp()
	{
		return owner.getLifeStats().getCurrentHp();
	}
	
	/* (non-Javadoc)
	 * @see com.aionemu.gameserver.model.gameobjects.stats.StaticNpcStats#getMaxHp()
	 */
	@Override
	public int getMaxHp()
	{
		return owner.getLifeStats().getMaxHp();
	}
}

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
package com.aionemu.gameserver.model.gameobjects.interfaces;

import com.aionemu.gameserver.model.gameobjects.Creature;

/**
 * @author Mr. Poke
 *
 */
public interface ISummoned
{
	/**
	 * For summons and different kind of servants<br>
	 *  it will return currently acting player.<br>
	 *  
	 *  This method is used for duel and enemy relations,<br>
	 *  rewards<br>
	 *  
	 * @return Master of this creature or self
	 */
	public Creature getMaster();
	
	public void setMaster(Creature creature);
}

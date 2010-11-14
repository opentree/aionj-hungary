/*
 * This file is part of aion-unique <aionu-unique.org>.
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
package com.aionemu.gameserver.network.aion.serverpackets;

import java.util.Set;

import com.aionemu.gameserver.network.aion.AbstractAionServerPacket;
import com.aionemu.gameserver.network.aion.AionChannelHandler;

/**
 * @author Lyahim, lord_rex
 *
 */
public class SM_RECIPE_LIST extends AbstractAionServerPacket<AionChannelHandler>
{
	private Integer[] recipeIds;
	private int count;
	
	public SM_RECIPE_LIST(Set<Integer> recipeIds)
	{
		this.recipeIds = recipeIds.toArray(new Integer[recipeIds.size()]);
		this.count = recipeIds.size();
	}
	
	@Override
	protected void writeImpl(AionChannelHandler cHandler)
	{
		writeH(count);
		for(int id : recipeIds)
		{
			writeD(id);
			writeC( 0);
		}
	}
}

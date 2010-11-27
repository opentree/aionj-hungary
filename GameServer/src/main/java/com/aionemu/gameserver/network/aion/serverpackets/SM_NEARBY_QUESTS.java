/**
 * This file is part of aion-unique <aion-unique.com>.
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

import java.util.List;

import com.aionemu.gameserver.network.aion.AbstractAionServerPacket;
import com.aionemu.gameserver.network.aion.AionChannelHandler;
import com.aionemu.gameserver.services.QuestService;

/**
 * @author Lyahim, MrPoke
 */

public class SM_NEARBY_QUESTS extends AbstractAionServerPacket<AionChannelHandler>
{
	private Integer[]	questIds;
	private int			size;

	public SM_NEARBY_QUESTS(List<Integer> questIds)
	{
		this.questIds = questIds.toArray(new Integer[questIds.size()]);
		this.size = questIds.size();
	}

	@Override
	protected void writeImpl(AionChannelHandler cHandler)
	{
		int playerLevel = cHandler.getActivePlayer().getLevel();
		writeD(size);
		for (int id : questIds)
		{
			writeH(id);
			if (QuestService.checkLevelRequirement(id, playerLevel))
				writeH(0);
			else
				writeH(2);
		}
	}
}

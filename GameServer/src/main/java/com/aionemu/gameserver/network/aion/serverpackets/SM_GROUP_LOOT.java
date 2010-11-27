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

import com.aionemu.gameserver.network.aion.AbstractAionServerPacket;
import com.aionemu.gameserver.network.aion.AionChannelHandler;

/**
 * @author Lyahim, Rhys2002
 */
public class SM_GROUP_LOOT extends AbstractAionServerPacket<AionChannelHandler>
{
	private int	groupId;
	private int	unk1;
	private int	unk2;
	private int	itemId;
	private int	unk3;
	private int	lootCorpseId;
	private int	distributionId;
	private int	playerId;
	private int	luck;

	/**
	 * @param Player Id must be 0 to start the Roll Options
	 */
	public SM_GROUP_LOOT(int groupId, int itemId, int lootCorpseId, int distributionId)
	{
		this.groupId = groupId;
		this.unk1 = 1;
		this.unk2 = 1;
		this.itemId = itemId;
		this.unk3 = 0;
		this.lootCorpseId = lootCorpseId;
		this.distributionId = distributionId;
		this.playerId = 0;
		this.luck = 1;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void writeImpl(AionChannelHandler cHandler)
	{
		writeD(groupId);
		writeD(unk1);
		writeD(unk2);
		writeD(itemId);
		writeC(unk3);
		writeD(lootCorpseId);
		writeC(distributionId);
		writeD(playerId);
		writeD(luck);
	}
}

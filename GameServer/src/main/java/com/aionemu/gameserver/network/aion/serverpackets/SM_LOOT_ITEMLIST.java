/*
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

import java.util.HashSet;
import java.util.Set;

import com.aionemu.gameserver.model.drop.DropItem;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.AbstractAionServerPacket;
import com.aionemu.gameserver.network.aion.AionChannelHandler;

/**
 * 
 * @author Lyahim, alexa026, Avol, Corrected by Metos
 * 
 * modified by ATracer
 * 
 */
public class SM_LOOT_ITEMLIST extends AbstractAionServerPacket<AionChannelHandler>
{
	private int	targetObjectId;
	private DropItem[] dropItems;
	private int size;

	public SM_LOOT_ITEMLIST(int targetObjectId, Set<DropItem> dropItems, Player player)
	{
		this.targetObjectId = targetObjectId;
		Set<DropItem> tmp = new HashSet<DropItem>();
		for (DropItem item : dropItems)
		{
			if (item.getPlayerObjId() == 0 || player.getObjectId() == item.getPlayerObjId())
				tmp.add(item);
		}
		this.dropItems = tmp.toArray(new DropItem[tmp.size()]);
		size = this.dropItems.length;
	}

	/**
	 * {@inheritDoc} dc
	 */
	@Override
	protected void writeImpl(AionChannelHandler cHandler) 
	{
		writeD(targetObjectId);
		writeC( size);

		for(DropItem dropItem : dropItems)
		{
			writeC( dropItem.getIndex()); // index in droplist
			writeD(dropItem.getDropTemplate().getItemId());
			writeH((int) dropItem.getCount());
			writeD(0);
		}
	}
}

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

import java.util.List;

import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.network.aion.AbstractAionServerPacket;
import com.aionemu.gameserver.network.aion.AionChannelHandler;

/**
 * @author Lyahim, Avol
 */

public class SM_VIEW_PLAYER_DETAILS extends AbstractAionServerPacket<AionChannelHandler>
{
	private List<Item>	items;
	private int			size;
	private int			targetObjId;

	public SM_VIEW_PLAYER_DETAILS(int targetObjId, List<Item> items)
	{
		this.items = items;
		this.size = items.size();
	}

	@Override
	protected void writeImpl(AionChannelHandler cHandler)
	{

		writeD(targetObjId); // unk
		writeC(11); //unk
		writeC(size); // itemCount
		writeC(0);
		writeD(0);
		for (Item item : items)
		{
			//////general info/////////////
			writeD(item.getItemTemplate().getTemplateId());//itemId
			writeH(36); // 
			writeD(item.getItemTemplate().getNameId());// itemNameId
			writeH(0);
			/////who knows/////////////
			writeH(36);
			writeC(4);
			writeC(1);
			writeH(0);
			writeH(0);
			writeC(0);
			////////////////////////
			writeH(0);
			writeC(6);
			writeH(item.getEquipmentSlot()); // slot
			writeH(0);
			writeC(0);
			writeH(62);
			writeH((int) item.getItemCount()); // count
			////////////////////////
			//Here comes the lol part.
			////////////////////////
			writeD(0);
			writeD(0);
			writeD(0);
			writeD(0);
			writeD(0);
			writeC(0);
		}

	}
}
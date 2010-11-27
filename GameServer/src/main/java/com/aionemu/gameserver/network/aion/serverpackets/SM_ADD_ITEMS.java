/*
 * This file is part of aion-unique <aionunique.com>.
 *
 * aion-unique is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * aion-unique is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with aion-unique.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.aionemu.gameserver.network.aion.serverpackets;

import java.util.List;

import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.items.ItemId;
import com.aionemu.gameserver.model.templates.item.ItemTemplate;
import com.aionemu.gameserver.network.aion.AionChannelHandler;

/**
 * 
 * @author Lyahim, ATracer
 * 
 */
public class SM_ADD_ITEMS extends _InventoryPacket
{
	private List<Item>	items;
	private int			size;

	public SM_ADD_ITEMS(List<Item> items)
	{
		this.items = items;
		this.size = items.size();
	}

	/**
	 * {@inheritDoc} dc
	 */

	@Override
	protected void writeImpl(AionChannelHandler cHandler)
	{
		writeH(25); // padding?
		writeH(size); // number of entries
		for (Item item : items)
		{
			writeGeneralInfo(item);

			ItemTemplate itemTemplate = item.getItemTemplate();

			if (itemTemplate.getTemplateId() == ItemId.KINAH.value())
			{
				writeKinah(item, true);
			}
			else if (itemTemplate.isWeapon())
			{
				writeWeaponInfo(item, true);
			}
			else if (itemTemplate.isArmor())
			{
				writeArmorInfo(item, true, false, false);
			}
			else if (itemTemplate.isStigma())
			{
				writeStigmaInfo(item);
			}
			else
			{
				writeGeneralItemInfo(item, false, false);
				writeC(0);
			}
		}
	}
}

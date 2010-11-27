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

import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.items.ItemId;
import com.aionemu.gameserver.model.templates.item.ItemTemplate;
import com.aionemu.gameserver.network.aion.AionChannelHandler;

/**
 * 
 * @author Lyahim, ATracer
 *
 */
public class SM_UPDATE_ITEM extends _InventoryPacket
{
	private Item	item;
	private boolean	isWeaponSwitch	= false;

	public SM_UPDATE_ITEM(Item item)
	{
		this.item = item;
	}

	public SM_UPDATE_ITEM(Item item, boolean isWeaponSwitch)
	{
		this.item = item;
		this.isWeaponSwitch = isWeaponSwitch;
	}

	@Override
	protected void writeGeneralInfo(Item item)
	{
		writeD(item.getObjectId());
		ItemTemplate itemTemplate = item.getItemTemplate();
		writeH(0x24);
		writeD(itemTemplate.getNameId());
		writeH(0);
	}

	@Override
	protected void writeImpl(AionChannelHandler cHandler)
	{

		writeGeneralInfo(item);

		ItemTemplate itemTemplate = item.getItemTemplate();

		if (itemTemplate.getTemplateId() == ItemId.KINAH.value())
		{
			writeKinah(item, true);
		}
		else if (itemTemplate.isWeapon())
		{
			writeWeaponInfo(item, true, isWeaponSwitch, false, false);
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
			writeGeneralItemInfo(item);
		}
	}

	protected void writeGeneralItemInfo(Item item)
	{
		writeH(0x16); //length of details
		writeC(0);
		writeH(item.getItemMask());
		writeD((int) item.getItemCount());
		writeD(0);
		writeD(0);
		writeD(0);
		writeH(0);
		writeC(0);
		writeH(0);
		writeH(item.getEquipmentSlot()); // not equipable items		
	}

	@Override
	protected void writeStigmaInfo(Item item)
	{
		int itemSlotId = item.getEquipmentSlot();
		writeH(0x05); //length of details
		writeC(0x06); //unk
		writeD(item.isEquipped() ? itemSlotId : 0);
	}

	@Override
	protected void writeKinah(Item item, boolean isInventory)
	{
		writeH(0x16); //length of details
		writeC(0);
		writeH(item.getItemMask());
		writeQ(item.getItemCount());
		writeD(0);
		writeD(0);
		writeH(0);
		writeC(0);
		writeC(0x1A); // FF FF equipment
		writeC(0);
	}

}
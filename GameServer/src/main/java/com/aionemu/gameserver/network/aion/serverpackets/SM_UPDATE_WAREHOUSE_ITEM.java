/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.aionemu.gameserver.network.aion.serverpackets;

import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.items.ItemId;
import com.aionemu.gameserver.model.templates.item.ItemTemplate;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.InventoryPacket;
/**
 *
 * @author kosyachok
 */
public class SM_UPDATE_WAREHOUSE_ITEM extends InventoryPacket
{
	Item item;
	int warehouseType;

	public SM_UPDATE_WAREHOUSE_ITEM(Item item, int warehouseType)
	{
		this.item = item;
		this.warehouseType = warehouseType;
	}

	@Override
	protected void writeImpl(AionConnection con)
	{
		writeGeneralInfo(item);

		ItemTemplate itemTemplate = item.getItemTemplate();

		if(itemTemplate.getTemplateId() == ItemId.KINAH.value())
		{
			writeKinah(item, false);
		}
		else if (itemTemplate.isWeapon())
		{
			writeWeaponInfo(item, false);
		}
		else if (itemTemplate.isArmor())
		{
			writeArmorInfo(item, false, false, false);
		}
		else
		{
			writeGeneralItemInfo(item, false, false);
		}
	}

	@Override
	protected void writeGeneralInfo(Item item)
	{
		writeD(item.getObjectId());
		writeC( warehouseType);
		ItemTemplate itemTemplate = item.getItemTemplate();
		writeH(0x24);
		writeD(itemTemplate.getNameId());
		writeH(0);
	}

	@Override
	protected void writeKinah(Item item, boolean isInventory)
	{
		writeH(0x16); //length of details
		writeC( 0);
		writeH(item.getItemMask());
		writeQ(item.getItemCount());
		writeD(0);
		writeD(0);
		writeH(0);
		writeC( 0);
		writeC( 0xFF); // FF FF equipment
		writeC( 0xFF);
	}
}

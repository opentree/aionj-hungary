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
public class SM_WAREHOUSE_UPDATE extends InventoryPacket
{
	private int warehouseType;
	private Item item;


	public SM_WAREHOUSE_UPDATE(Item item, int warehouseType)
	{
		this.warehouseType = warehouseType;
		this.item = item;
	}

	@Override
	protected void writeImpl(AionConnection con)
	{
		writeC( warehouseType);
		writeH(13);
		writeH(1);

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
		ItemTemplate itemTemplate = item.getItemTemplate();
		writeD(itemTemplate.getTemplateId());
		writeC( 0); //some item info (4 - weapon, 7 - armor, 8 - rings, 17 - bottles)
		writeH(0x24);
		writeD(itemTemplate.getNameId());
		writeH(0);
	}
}

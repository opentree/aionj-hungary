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

import java.util.Set;

import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.stats.modifiers.SimpleModifier;
import com.aionemu.gameserver.model.gameobjects.stats.modifiers.StatModifier;
import com.aionemu.gameserver.model.items.ItemSlot;
import com.aionemu.gameserver.model.items.ItemStone;
import com.aionemu.gameserver.model.items.ManaStone;
import com.aionemu.gameserver.model.templates.item.ItemTemplate;
import com.aionemu.gameserver.network.aion.AbstractAionServerPacket;
import com.aionemu.gameserver.network.aion.AionChannelHandler;

/**
 * @author ATracer
 *
 */
public abstract class _InventoryPacket extends AbstractAionServerPacket<AionChannelHandler>
{	
	/**
	 *  The header of every item block
	 * @param buf
	 * @param item
	 */
	protected void writeGeneralInfo(Item item)
	{
		writeD(item.getObjectId());
		ItemTemplate itemTemplate = item.getItemTemplate();
		writeD(itemTemplate.getTemplateId());
		writeH(0x24);
		writeD(itemTemplate.getNameId());
		writeH(0);
	}
	
	protected void writeMailGeneralInfo(Item item)
	{
		writeD(item.getObjectId());
		ItemTemplate itemTemplate = item.getItemTemplate();
		writeD(itemTemplate.getTemplateId());
		writeD(1);
		writeD(0);
		writeH(0x24);
		writeD(itemTemplate.getNameId());
		writeH(0);
	}

	/**
	 *  All misc items
	 * @param buf
	 * @param item
	 */
	protected void writeGeneralItemInfo(Item item, boolean privateStore, boolean mail)
	{
		writeH(0x16); //length of details
		writeC( 0);
		writeH(item.getItemMask());
		writeQ(item.getItemCount());
		writeD(0);
		writeD(0);
		if(!privateStore)
			writeH(0);
		writeC( 0);
		if(!mail)
			writeH(item.getEquipmentSlot()); // not equipable items		
	}
	
	protected void writeStigmaInfo(Item item)
	{
		writeH(325); //length of details 45 01
		writeC( 0x6);
		if(item.isEquipped())
			writeD(item.getEquipmentSlot());
		else
			writeD(0);
		writeC( 0x7);
		writeH(702); //skill id
		writeD(0);
		writeH(0);
		writeD(0x3c);  //0x3c
		
		writeD(0);
		writeD(0);
		
		writeD(0);
		writeD(0);
		writeD(0);
		writeD(0);
		writeD(0);
		writeD(0);
		writeD(0);
		writeD(0);
		writeD(0);
		writeD(0);
		writeD(0);
		writeD(0);
		writeD(0);
		writeD(0);
		writeD(0);
		writeD(0);
		writeD(0);
		writeD(0);
		writeD(0);
		writeD(0);
		writeD(0);
		writeD(0);
		writeD(0);
		writeD(0);
		writeD(0);
		writeD(0);
		writeD(0);
		writeD(0);
		writeD(0);
		writeD(0);
		writeD(0);
		writeD(0);
		writeD(0);
		writeD(0);
		writeD(0);
		writeD(0);
		
		writeD(0);
		writeD(0);	
		writeD(1);//1
		writeD(0);
		
		writeD(0);
		writeD(0);
		writeD(0);
		writeD(0);	
		writeD(0);
		writeD(0);
		writeD(0);
		writeD(0);	
		writeD(0);
		writeD(0);
		writeD(0);
		writeD(0);
		writeD(0);
		writeD(0);
		writeD(0);
		writeD(0);
		
		writeD(0);
		writeD(0);
		writeD(0);
		writeH(0);
		writeH(0x0b); //0b
		
		
		writeC( 0);
		writeD(item.getItemTemplate().getTemplateId());		
		
		writeD(0);
		writeD(0);
		writeD(0);
		writeD(0);
		writeD(0);
		writeD(0);
		writeD(0);
		writeD(0);
		writeC( 0);
		
		writeD(82750); //3E 43 01 00
		
		writeD(0);
		writeD(0);
		writeD(0);
		writeD(0);
		writeC( 0);
		
		writeC( 0x22); // 22
		writeH(0);
	}

	/**
	 * 
	 * @param buf
	 * @param item
	 */
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
		writeH(255); // FF FF equipment
		if(isInventory)
			writeC( 0);
	}

	/**
	 * Write weapon info for non weapon switch items
	 * 
	 * @param buf
	 * @param item
	 * @param isInventory
	 */
	protected void writeWeaponInfo(Item item, boolean isInventory)
	{
		this.writeWeaponInfo(item, isInventory, false, false, false);
	}
	
	/**
	 *  For all weapon. Weapon is identified by weapon_type in xml
	 *  
	 * @param buf
	 * @param item
	 */
	protected void writeWeaponInfo(Item item, boolean isInventory, boolean isWeaponSwitch, boolean privateStore, boolean mail)
	{
		int itemSlotId = item.getEquipmentSlot();
		
		if(isWeaponSwitch)
			writeH(0x05); // next bytes count ??
		else
			writeH(0x4B); // next bytes count ??
		
		writeC( 0x06);	
		writeD(item.isEquipped() ? itemSlotId : 0);
		
		if(!isWeaponSwitch)
		{
			writeC( 0x01);
			writeD(ItemSlot.getSlotsFor(item.getItemTemplate().getItemSlot()).get(0).getSlotIdMask());
			writeD(0x02);
			writeC( 0x0B); //? some details separator
			writeC( item.isSoulBound() ? 1 : 0);
			writeC( item.getEnchantLevel()); //enchant (1-15)
			writeD(item.getItemSkinTemplate().getTemplateId());
			writeC( 0);

			writeItemStones(item);
			
			ItemStone god = item.getGodStone();
			writeD(god == null ? 0 : god.getItemId());
			writeC( 0);
			
			writeD(0);

			writeD(0);//unk 1.5.1.9
			writeC( 0);//unk 1.5.1.9

			writeH(item.getItemMask());
			writeQ(item.getItemCount());
			writeD(0);
			writeD(0);
			if(!privateStore)
				writeH(0);
			writeC( 0);
			if(!mail)
				writeH(item.isEquipped() ? 255 : item.getEquipmentSlot()); // FF FF equipment
			if(isInventory)
				writeC(  0);//item.isEquipped() ? 1 : 0
		}
	}

	/**
	 *  Writes manastones : 6C - statenum mask, 6H - value
	 * @param buf
	 * @param item
	 */
	private void writeItemStones(Item item)
	{
		int count = 0;
		
		if(item.hasManaStones())
		{
			Set<ManaStone> itemStones = item.getItemStones();
			
			for(ManaStone itemStone : itemStones)
			{
				if(count == 6)
					break;

				StatModifier modifier = itemStone.getFirstModifier();
				if(modifier != null)
				{
					count++;
					writeC( modifier.getStat().getItemStoneMask());
				}
			}
			writeB(new byte[(6-count)]);
			count = 0;
			for(ManaStone itemStone : itemStones)
			{
				if(count == 6)
					break;

				StatModifier modifier = itemStone.getFirstModifier();
				if(modifier != null)
				{
					count++;
					writeH(((SimpleModifier)modifier).getValue());
				}
			}
			writeB(new byte[(6-count)*2]);
		}
		else
		{
			writeB(new byte[18]);
		}

		//for now max 6 stones - write some junk
	}

	/**
	 *  For all armor. Armor is identified by armor_type in xml
	 * @param buf
	 * @param item
	 */
	protected void writeArmorInfo(Item item, boolean isInventory, boolean privateStore, boolean mail)
	{
		int itemSlotId = item.getEquipmentSlot();
		writeH(0x4F);
		writeC( 0x06);
		writeD(item.isEquipped() ? itemSlotId : 0);
		writeC( 0x02);
		writeD(ItemSlot.getSlotsFor(item.getItemTemplate().getItemSlot()).get(0).getSlotIdMask());
		writeD(0);
		writeD(0);
		writeC( 0x0B); //? some details separator
		writeC( item.isSoulBound() ? 1 : 0);
		writeC( item.getEnchantLevel()); //enchant (1-15)
		writeD(item.getItemSkinTemplate().getTemplateId());

		writeC( 0);

		writeItemStones(item);

		writeC( 0);
		writeD(item.getItemColor());
		writeD(0);

		writeD(0);//unk 1.5.1.9
		writeC( 0);//unk 1.5.1.9

		writeH(item.getItemMask());
		writeQ(item.getItemCount());
		writeD(0);
		writeD(0);
		if(!privateStore)
			writeH(0);
		writeC( 0);
		if(!mail)
			writeH(item.isEquipped() ? 255 : item.getEquipmentSlot()); // FF FF equipment
		if(isInventory)
			writeC(  1);//item.isEquipped() ? 1 : 0
	}
}

/*
 * This file is part of aion-lightning <aion-lightning.com>.
 *
 *  aion-lightning is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  aion-lightning is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with aion-lightning.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.aionemu.gameserver.network.aion.serverpackets;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Set;

import com.aionemu.gameserver.model.gameobjects.BrokerItem;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.stats.modifiers.SimpleModifier;
import com.aionemu.gameserver.model.gameobjects.stats.modifiers.StatModifier;
import com.aionemu.gameserver.model.items.ItemStone;
import com.aionemu.gameserver.model.items.ManaStone;
import com.aionemu.gameserver.network.aion.AbstractAionServerPacket;
import com.aionemu.gameserver.network.aion.AionChannelHandler;

/**
 * @author Lyahim, IlBuono, kosyachok
 *
 */
public class SM_BROKER_SERVICE extends AbstractAionServerPacket<AionChannelHandler>
{
	private enum BrokerPacketType
	{
		SEARCHED_ITEMS(0),
		REGISTERED_ITEMS(1),
		REGISTER_ITEM(3),
		SHOW_SETTLED_ICON(5),
		SETTLED_ITEMS(5),
		REMOVE_SETTLED_ICON(6);
		
		private int id;
		
		private BrokerPacketType(int id)
		{
			this.id = id;
		}
		
		private int getId()
		{
			return id;
		}
	}
	
	private BrokerPacketType type;
	private BrokerItem[] brokerItems;
	private int itemsCount;
	private int startPage;
	private int message;
	private long settled_kinah;
	
	public SM_BROKER_SERVICE(BrokerItem brokerItem, int message)
	{
		this.type = BrokerPacketType.REGISTER_ITEM;
		this.brokerItems = new BrokerItem[] {brokerItem};
		this.message = message;
	}
	
	public SM_BROKER_SERVICE(int message)
	{
		this.type = BrokerPacketType.REGISTER_ITEM;
		this.message = message;
	}
	
	public SM_BROKER_SERVICE(BrokerItem[] brokerItems)
	{
		this.type = BrokerPacketType.REGISTERED_ITEMS;
		this.brokerItems = brokerItems;
	}
	
	public SM_BROKER_SERVICE(BrokerItem[] brokerItems, long settled_kinah)
	{
		this.type = BrokerPacketType.SETTLED_ITEMS;
		this.brokerItems = brokerItems;
		this.settled_kinah = settled_kinah;
	}
	
	public SM_BROKER_SERVICE(BrokerItem[] brokerItems, int itemsCount, int startPage)
	{
		this.type = BrokerPacketType.SEARCHED_ITEMS;
		this.brokerItems = brokerItems;
		this.itemsCount = itemsCount;
		this.startPage = startPage;
	}
	
	public SM_BROKER_SERVICE(boolean showSettledIcon, long settled_kinah)
	{
		this.type = showSettledIcon ? BrokerPacketType.SHOW_SETTLED_ICON : BrokerPacketType.REMOVE_SETTLED_ICON;
		this.settled_kinah = settled_kinah;
	}
	
	@Override
	protected void writeImpl(AionChannelHandler cHandler)
	{	
		switch (type)
		{
			case SEARCHED_ITEMS:
				writeSearchedItems();
				break;
			case REGISTERED_ITEMS:
				writeRegisteredItems();
				break;
			case REGISTER_ITEM:
				writeRegisterItem();
				break;
			case SHOW_SETTLED_ICON:
				writeShowSettledIcon();
				break;
			case REMOVE_SETTLED_ICON:
				writeRemoveSettledIcon();
				break;
			case SETTLED_ITEMS:
				writeShowSettledItems();
				break;
		}
			
	}
	
	private void writeSearchedItems()
	{
		writeC( type.getId());
		writeD(itemsCount);
		writeC( 0);
		writeH(startPage);
		writeH(brokerItems.length);
		for(BrokerItem item : brokerItems)
		{
			if(item.getItem().getItemTemplate().isArmor() || item.getItem().getItemTemplate().isWeapon())
				writeArmorWeaponInfo(item);
			else
				writeCommonInfo(item);
		}
	}
	
	private void writeRegisteredItems()
	{
		writeC( type.getId());
		writeD(0x00);
		writeH(brokerItems.length); //you can register a max of 15 items, so 0x0F
		for(BrokerItem item : brokerItems)
		{
			writeD(item.getItemUniqueId());
			writeD(item.getItemId());
			writeQ(item.getPrice());
			writeQ(item.getItem().getItemCount());
			writeQ(item.getItem().getItemCount());
			Timestamp currentTime = new Timestamp(Calendar.getInstance().getTimeInMillis());
			int daysLeft = Math.round((item.getExpireTime().getTime() - currentTime.getTime()) / 86400000);
			writeH(daysLeft);
			writeC( 0);
			writeD(item.getItemId());
			writeD(0);
			writeD(0);
			writeD(0);
			writeD(0);
			writeD(0);
			writeD(0);
			writeD(0);
			writeD(0);
			writeH(0);
		}
	}
	
	private void writeRegisterItem()
	{
		writeC( type.getId());
		writeH(message);
		if(message == 0)
		{
			BrokerItem itemForRegistration = brokerItems[0];
			writeD(itemForRegistration.getItemUniqueId());
			writeD(itemForRegistration.getItemId());
			writeQ(itemForRegistration.getPrice());
			writeQ(itemForRegistration.getItem().getItemCount());
			writeQ(itemForRegistration.getItem().getItemCount());
			writeH(8); //days left
			writeC( 0);
			writeD(itemForRegistration.getItemId());
			writeD(0);
			writeD(0);
			writeD(0);
			writeD(0);
			writeD(0);
			writeD(0);
			writeD(0);
			writeD(0);
			writeH(0);
		}
	}
	
	private void writeShowSettledIcon()
	{
		writeC( type.getId());
		writeQ(settled_kinah);
		writeD(0x00);
		writeH(0x00);
		writeH(0x01);
		writeC( 0x00);
	}
	
	private void writeRemoveSettledIcon()
	{
		writeH(type.getId());
	}
	
	private void writeShowSettledItems()
	{
		writeC( type.getId());
		writeQ(settled_kinah);
        
		writeH(brokerItems.length);
        writeD(0x00); 
        writeC(0x00);
        
		writeH(brokerItems.length);
		for(BrokerItem settledItem : brokerItems)
		{
			writeD(settledItem.getItemId());
			if(settledItem.isSold())
				writeQ(settledItem.getPrice());
			else
				writeQ(0);
			writeQ(settledItem.getItemCount());
			writeQ(settledItem.getItemCount());
			writeD((int)settledItem.getSettleTime().getTime() / 60000);
			writeH(0);
			writeD(settledItem.getItemId());
			writeD(0);
			writeD(0);
			writeD(0);
			writeD(0);
			writeD(0);
			writeD(0);
			writeD(0);
			writeD(0);
			writeH(0);
		}
	}
	
	private void writeArmorWeaponInfo(BrokerItem item)
	{
		writeD(item.getItem().getObjectId());
		writeD(item.getItem().getItemTemplate().getTemplateId());
		writeQ(item.getPrice());
		writeQ(item.getItem().getItemCount());
		writeC( 0);
		writeC( item.getItem().getEnchantLevel());
		writeD(item.getItem().getItemSkinTemplate().getTemplateId());
		writeC( 0);
		
		writeItemStones(item.getItem());
		
		ItemStone god = item.getItem().getGodStone();
		writeD(god == null ? 0 : god.getItemId());
		
		writeC( 0);
		writeD(0);
		writeD(0);
		writeS(item.getSeller());
		writeS(""); //creator
		
	}
	
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
	
	private void writeCommonInfo(BrokerItem item)
	{
		writeD(item.getItem().getObjectId());
		writeD(item.getItem().getItemTemplate().getTemplateId());
		writeQ(item.getPrice());
		writeQ(item.getItem().getItemCount());
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
		writeS(item.getSeller());
		writeS(""); //creator
	}
}

/*
 * This file is part of aion-unique <aion-unique.org>.
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

import java.util.Collection;

import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.Letter;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.item.ItemTemplate;

/**
 * @author kosyachok
 * 
 */
public abstract class _MailServicePacket extends _InventoryPacket
{
	protected void writeLettersList(Collection<Letter> letters, Player player)
	{
		writeC( 2); // 2 - Mailbox letters update
		writeD(player.getObjectId());
		writeC( 0);
		writeH(player.getMailbox().getFreeSlots()); // mailbox free slots
		
		for(Letter letter : letters)
		{
			writeD(letter.getObjectId());
			writeS(letter.getSenderName());
			writeS(letter.getTitle());
			if(letter.isUnread())
				writeC( 0);
			else
				writeC( 1);
			if(letter.getAttachedItem() != null)
			{
				writeD(letter.getAttachedItem().getObjectId());
				writeD(letter.getAttachedItem().getItemTemplate().getTemplateId());
			}
			else
			{
				writeD(0);
				writeD(0);
			}
			writeD((int) letter.getAttachedKinah());
			writeD(0);
			writeC( 0);
		}
	}

	protected void writeEmptyLettersList(Player player)
	{
		writeC( 2);
		writeD(player.getObjectId());
		writeH(0);
		writeC( 0);
	}

	protected void writeMailMessage(int messageId)
	{
		writeC( 1);
		writeC( messageId);
	}

	protected void writeMailboxState(int haveNewMail, int haveUnread)
	{
		writeC( 0);
		writeC( haveNewMail);
		writeC( 0);
		writeC( haveUnread);
		writeD(0);
		writeC( 0);
	}

	protected void writeLetterRead(Letter letter, long time)
	{
		writeC( 3);
		writeD(letter.getRecipientId());
		writeD(1);
		writeD(0);
		writeD(letter.getObjectId());
		writeD(letter.getRecipientId());
		writeS(letter.getSenderName());
		writeS(letter.getTitle());
		writeS(letter.getMessage());

		Item item = letter.getAttachedItem();
		if(item != null)
		{
			ItemTemplate itemTemplate = item.getItemTemplate();

			writeMailGeneralInfo(item);

			if(itemTemplate.isArmor())
				writeArmorInfo(item, false, false, true);
			else if(itemTemplate.isWeapon())
				writeWeaponInfo(item, false, false, false, true);
			else
				writeGeneralItemInfo(item, false, true);
		}
		else
		{
			writeD(0);
			writeD(0);
			writeD(0);
			writeD(0);
			writeD(0);
		}

		writeD((int) letter.getAttachedKinah());
		writeD(0); // AP reward for castle assault/defense (in future)
		writeC( 0);
		writeQ(time / 1000);
		writeC( 0);
	}

	protected void writeLetterState(int letterId, int attachmentType)
	{
		writeC( 5);
		writeD(letterId);
		writeC( attachmentType);
		writeC( 1);
	}

	protected void writeLetterDelete(int letterId)
	{
		writeC( 6);
		writeD(0);
		writeD(0);
		writeD(letterId);
	}
}

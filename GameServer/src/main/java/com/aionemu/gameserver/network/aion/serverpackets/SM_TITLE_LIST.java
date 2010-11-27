/*
 * This file is part of aion-unique <aion-unique.com>.
 *
 *     Aion-unique is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Aion-unique is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with aion-unique.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.aionemu.gameserver.network.aion.serverpackets;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.Title;
import com.aionemu.gameserver.model.gameobjects.player.TitleList;
import com.aionemu.gameserver.network.aion.AbstractAionServerPacket;
import com.aionemu.gameserver.network.aion.AionChannelHandler;

/**
 * 
 * @author Lyahim, Nemiroff
 * @author Lyahim, M@xx Date: 01.12.2009
 * 
 */
public class SM_TITLE_LIST extends AbstractAionServerPacket<AionChannelHandler>
{
	private TitleList	titleList;

	// TODO Make List from DataBase
	public SM_TITLE_LIST(Player player)
	{
		this.titleList = player.getTitleList();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void writeImpl(AionChannelHandler cHandler)
	{
		writeC(0x00); // new 2.0 Packet
		writeC(0); // unk
		writeH(titleList.size());
		for (Title title : titleList.getTitles())
		{
			writeD(title.getTemplate().getTitleId());
			writeD(0);
		}
	}
}

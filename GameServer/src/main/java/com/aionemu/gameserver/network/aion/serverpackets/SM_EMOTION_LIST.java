/**
 * This file is part of aion-emu <aion-emu.com>.
 *
 *  aion-emu is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  aion-emu is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with aion-emu.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.aionemu.gameserver.network.aion.serverpackets;

import com.aionemu.gameserver.network.aion.AbstractAionServerPacket;
import com.aionemu.gameserver.network.aion.AionChannelHandler;

public class SM_EMOTION_LIST extends AbstractAionServerPacket<AionChannelHandler>
{
	@Override
	protected void writeImpl(AionChannelHandler cHandler)
	{
		writeC(0x00);
		writeH(57);
		for (int i = 0; i < 57; i++) // 120 max, for the last emote
		{
			writeD(64 + i); // 120 = /NobodyDance
			writeH(0x00);
		}
	}
}
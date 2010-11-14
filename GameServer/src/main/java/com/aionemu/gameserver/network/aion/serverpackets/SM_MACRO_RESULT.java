/*
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

/**
 * @author Lyahim, xavier
 *
 */
public class SM_MACRO_RESULT extends AbstractAionServerPacket<AionChannelHandler>
{
	public static SM_MACRO_RESULT SM_MACRO_CREATED = new SM_MACRO_RESULT (0x00);
	public static SM_MACRO_RESULT SM_MACRO_DELETED = new SM_MACRO_RESULT (0x01);
	
	private int code;
	
	private SM_MACRO_RESULT (int code) {
		this.code = code;
	}
	
	@Override
	public void writeImpl (AionChannelHandler con) 
	{
		writeC(code);
	}
}

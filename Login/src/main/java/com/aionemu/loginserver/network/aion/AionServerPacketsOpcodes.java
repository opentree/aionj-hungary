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
package com.aionemu.loginserver.network.aion;

import com.aionemu.loginserver.network.aion.serverpackets.*;

import javolution.util.FastList;
import javolution.util.FastMap;

/**
 * @author Mr. Poke
 *
 */
public class AionServerPacketsOpcodes
{

	private static FastMap<Class<? extends AionServerPacket>, Integer>	opcodes	= new FastMap<Class<? extends AionServerPacket>, Integer>();

	static
	{
		FastList<Integer> idSet = new FastList<Integer>();

		addPacketOpcode(SM_INIT.class, 0x00, idSet);
		addPacketOpcode(SM_LOGIN_FAIL.class, 0x01, idSet);
		addPacketOpcode(SM_LOGIN_OK.class, 0x03, idSet);
		addPacketOpcode(SM_SERVER_LIST.class, 0x04, idSet);
		addPacketOpcode(SM_PLAY_FAIL.class, 0x06, idSet);
		addPacketOpcode(SM_PLAY_OK.class, 0x07, idSet);
		addPacketOpcode(SM_AUTH_GG.class, 0x0b, idSet);
		addPacketOpcode(SM_UPDATE_SESSION.class, 0x0c, idSet);
	}

	static int getOpcode(Class<? extends AionServerPacket> packetClass)
	{
		Integer opcode = opcodes.get(packetClass);
		if(opcode == null)
			throw new IllegalArgumentException("There is no opcode for " + packetClass + " defined.");

		return opcode;
	}

	private static void addPacketOpcode(Class<? extends AionServerPacket> packetClass, int opcode, FastList<Integer> idSet)
	{
		if(opcode < 0)
			return;

		if(idSet.contains(opcode))
			throw new IllegalArgumentException(String.format("There already exists another packet with id 0x%02X",
				opcode));

		idSet.add(opcode);
		opcodes.put(packetClass, opcode);
	}
}

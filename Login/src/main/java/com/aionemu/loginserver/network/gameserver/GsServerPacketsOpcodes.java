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
package com.aionemu.loginserver.network.gameserver;

import javolution.util.FastList;
import javolution.util.FastMap;

import com.aionemu.loginserver.network.gameserver.serverpackets.*;

/**
 * @author Mr. Poke
 *
 */
public class GsServerPacketsOpcodes
{
	private static FastMap<Class<? extends GsServerPacket>, Integer>	opcodes	= new FastMap<Class<? extends GsServerPacket>, Integer>();

	static
	{
		FastList<Integer> idSet = new FastList<Integer>();

		addPacketOpcode(SM_GS_AUTH_RESPONSE.class, 0x00, idSet);
		addPacketOpcode(SM_ACCOUNT_AUTH_RESPONSE.class, 0x01, idSet);
		addPacketOpcode(SM_REQUEST_KICK_ACCOUNT.class, 0x02, idSet);
		addPacketOpcode(SM_ACCOUNT_RECONNECT_KEY.class, 0x03, idSet);
		addPacketOpcode(SM_LS_CONTROL_RESPONSE.class, 0x04, idSet);
		addPacketOpcode(SM_BAN_RESPONSE.class, 0x05, idSet);
	}

	static int getOpcode(Class<? extends GsServerPacket> packetClass)
	{
		Integer opcode = opcodes.get(packetClass);
		if(opcode == null)
			throw new IllegalArgumentException("There is no opcode for " + packetClass + " defined.");

		return opcode;
	}

	private static void addPacketOpcode(Class<? extends GsServerPacket> packetClass, int opcode, FastList<Integer> idSet)
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

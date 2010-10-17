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
package com.aionemu.loginserver.network.gameserver;

import com.aionemu.commons.netty.handler.AbstractChannelHandler;
import com.aionemu.commons.netty.packet.BaseServerPacket;

/**
 * Base class for every LS -> GameServer Server Packet.
 * 
 * @author -Nemesiss-
 */
public abstract class GsServerPacket extends BaseServerPacket
{

	/**
	 * @param opcode
	 */
	public GsServerPacket()
	{
		super();
		this.opCode = GsServerPacketsOpcodes.getOpcode(getClass());
	}

	/**
	 * Write this packet data for given connection, to given buffer.
	 * 
	 * @param con
	 * @param buf
	 */
	public final void write(AbstractChannelHandler con)
	{
		writeH((short) 0);
		writeC(getOpCode());
		writeImpl((GsConnection)con);
	}

	/**
	 * Write data that this packet represents to given byte buffer.
	 * 
	 * @param con
	 * @param buf
	 */
	protected abstract void writeImpl(GsConnection con);
}

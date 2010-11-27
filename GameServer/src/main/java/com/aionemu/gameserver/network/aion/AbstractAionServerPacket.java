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
package com.aionemu.gameserver.network.aion;

import com.aionemu.commons.network.netty.packet.AbstractServerPacket;
import com.aionemu.gameserver.network.Crypt;

/**
 * @author lyahim
 *
 */
public abstract class AbstractAionServerPacket<T extends AionChannelHandler> extends AbstractServerPacket<T>
{
	/**
	 * Write packet opcodec and two additional bytes
	 * 
	 * @param buf
	 * @param value
	 */
	private final void writeOP()
	{
		/** obfuscate packet id */
		byte op = Crypt.encodeOpcodec(this.opCode);
		writeC(op);

		/** put static server packet code */
		writeC(Crypt.staticServerPacketCode);

		/** for checksum? */
		writeC((byte) ~op);
	}

	@Override
	public void write(T channelhandler)
	{
		writeH((short) 0);
		writeOP();
		writeImpl(channelhandler);
	}
}

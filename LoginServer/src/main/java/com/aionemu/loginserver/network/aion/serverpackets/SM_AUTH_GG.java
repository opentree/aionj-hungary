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
package com.aionemu.loginserver.network.aion.serverpackets;

import com.aionemu.commons.network.netty.packet.AbstractServerPacket;
import com.aionemu.loginserver.network.aion.AionChannelHandler;

/**
 * @author -Nemesiss-, Lyahim
 */
public class SM_AUTH_GG extends AbstractServerPacket<AionChannelHandler>
{
	/**
	 * Session Id of this connection
	 */
	private final int	sessionId;

	/**
	 * Constructs new instance of <tt>SM_AUTH_GG</tt> packet
	 * 
	 * @param sessionId
	 */
	public SM_AUTH_GG(int sessionId)
	{
		this.sessionId = sessionId;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void writeImpl(AionChannelHandler cHandler)
	{
		writeD(sessionId);
		writeD(0x00);
		writeD(0x00);
		writeD(0x00);
		writeD(0x00);
	}
}

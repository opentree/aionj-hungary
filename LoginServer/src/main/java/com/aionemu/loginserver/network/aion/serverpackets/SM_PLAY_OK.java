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
import com.aionemu.loginserver.network.aion.SessionKey;

/**
 * @author -Nemesiss-
 */
public class SM_PLAY_OK extends AbstractServerPacket<AionChannelHandler>
{
	/**
	 * playOk1 is part of session key - its used for security purposes [checked at game server side]
	 */
	private final int	playOk1;
	/**
	 * playOk2 is part of session key - its used for security purposes [checked at game server side]
	 */
	private final int	playOk2;

	/**
	 * Constructs new instance of <tt>SM_PLAY_OK </tt> packet.
	 * 
	 * @param key
	 *            session key
	 */
	public SM_PLAY_OK(SessionKey key)
	{
		this.playOk1 = key.playOk1;
		this.playOk2 = key.playOk2;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void writeImpl(AionChannelHandler cHandler)
	{
		writeD(playOk1);
		writeD(playOk2);
	}
}

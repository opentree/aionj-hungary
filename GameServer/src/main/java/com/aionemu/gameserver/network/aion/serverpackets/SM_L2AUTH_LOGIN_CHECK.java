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

import com.aionemu.gameserver.model.account.Account;
import com.aionemu.gameserver.network.aion.AbstractAionServerPacket;
import com.aionemu.gameserver.network.aion.AionChannelHandler;

/**
 * @author Lyahim, -Nemesiss-
 * 
 */
public class SM_L2AUTH_LOGIN_CHECK extends AbstractAionServerPacket<AionChannelHandler>
{
	/**
	 * True if client is authed.
	 */
	private final boolean	ok;

	/**
	 * Constructs new <tt>SM_L2AUTH_LOGIN_CHECK </tt> packet
	 * 
	 * @param ok
	 */
	public SM_L2AUTH_LOGIN_CHECK(boolean ok)
	{
		this.ok = ok;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void writeImpl(AionChannelHandler cHandler)
	{
		Account account = cHandler.getAccount();
		if (account != null)
		{
			writeD(ok ? 0x00 : 0x01);
			writeS(account.getName());
		}
		else
		{
			writeD(ok ? 0x00 : 0x01);
			writeS("null");
		}
	}
}

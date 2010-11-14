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
package com.aionemu.gameserver.network.loginserver.serverpackets;

import com.aionemu.gameserver.network.loginserver.LoginServerChannelHandler;
import com.aionemu.commons.network.netty.packet.AbstractServerPacket;

/**
 * This packet is sended by GameServer when player is requesting fast reconnect to login server. LoginServer in response
 * will send reconectKey.
 * 
 * @author Lyahim, -Nemesiss-
 * 
 */
public class SM_ACCOUNT_RECONNECT_KEY extends AbstractServerPacket<LoginServerChannelHandler>
{
	/**
	 * AccountId of client that is requested reconnection to LoginServer.
	 */
	private final int	accountId;

	/**
	 * Constructs new instance of <tt>SM_ACCOUNT_RECONNECT_KEY </tt> packet.
	 * 
	 * @param accountId
	 *            account identifier.
	 */
	public SM_ACCOUNT_RECONNECT_KEY(int accountId)
	{
		this.accountId = accountId;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void writeImpl(LoginServerChannelHandler cHandler)
	{
		writeD(accountId);
	}
}

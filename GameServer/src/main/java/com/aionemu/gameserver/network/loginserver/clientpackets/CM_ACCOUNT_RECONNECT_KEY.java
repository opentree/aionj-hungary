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
package com.aionemu.gameserver.network.loginserver.clientpackets;

import com.aionemu.gameserver.network.loginserver.LoginServer;
import com.aionemu.commons.network.netty.packet.AbstractClientPacket;
import com.aionemu.gameserver.network.loginserver.LoginServerChannelHandler;

/**
 * In this packet LoginServer is sending response for SM_ACCOUNT_RECONNECT_KEY with account name and reconnectionKey.
 * 
 * @author Lyahim, -Nemesiss-
 * 
 */
public class CM_ACCOUNT_RECONNECT_KEY extends AbstractClientPacket<LoginServerChannelHandler>
{
	/**
	 * accountId of account that will be reconnecting.
	 */
	private int			accountId;
	/**
	 * ReconnectKey that will be used for authentication.
	 */
	private int			reconnectKey;

	/**
	 * Constructs new instance of <tt>CM_ACCOUNT_RECONNECT_KEY </tt> packet
	 * @param opcode
	 */
	public CM_ACCOUNT_RECONNECT_KEY(int opcode)
	{
		super(opcode);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void readImpl()
	{
		accountId = readD();
		reconnectKey = readD();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void runImpl()
	{
		LoginServer.getInstance().authReconnectionResponse(accountId, reconnectKey);
	}
}

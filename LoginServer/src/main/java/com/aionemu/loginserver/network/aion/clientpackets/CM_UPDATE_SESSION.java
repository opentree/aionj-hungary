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
package com.aionemu.loginserver.network.aion.clientpackets;

import com.aionemu.commons.network.packet.AbstractClientPacket;
import com.aionemu.loginserver.controller.AccountController;
import com.aionemu.loginserver.network.aion.AionChannelHandler;

/**
 * This packet is send when client was connected to game server and now is reconnection to login server.
 * 
 * @author -Nemesiss-, Lyahim
 */
public class CM_UPDATE_SESSION extends AbstractClientPacket<AionChannelHandler>
{
	/**
	 * accountId is part of session key - its used for security purposes
	 */
	private int	accountId;
	/**
	 * loginOk is part of session key - its used for security purposes
	 */
	private int	loginOk;
	/**
	 * reconectKey is key that server sends to client for fast reconnection to login server - we will check if this key
	 * is valid.
	 */
	private int	reconnectKey;

	/**
	 * Constructs new instance of <tt>CM_UPDATE_SESSION </tt> packet.
	 * 
	 * @param buf
	 *            packet data
	 * @param client
	 *            client
	 */
	public CM_UPDATE_SESSION(int opcode)
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
		loginOk = readD();
		reconnectKey = readD();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void runImpl()
	{
		AccountController.getInstance().authReconnectingAccount(accountId, loginOk, reconnectKey, getChannelHandler());
	}
}

/*
 * This file is part of aion-emu <aion-emu.com>.
 *
 * aion-emu is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * aion-emu is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with aion-emu.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.aionemu.gameserver.network.loginserver.serverpackets;

import java.util.Map;

import com.aionemu.gameserver.network.aion.AionChannelHandler;
import com.aionemu.gameserver.network.loginserver.LoginServerChannelHandler;
import com.aionemu.commons.network.netty.packet.AbstractServerPacket;

/**
 * GameServer packet that sends list of logged in accounts
 * 
 * @author Lyahim, SoulKeeper
 */
public class SM_ACCOUNT_LIST extends AbstractServerPacket<LoginServerChannelHandler>
{

	/**
	 * Map with loaded accounts
	 */
	private final Map<Integer, AionChannelHandler>	accounts;

	/**
	 * constructs new server packet with specified opcode.
	 */
	public SM_ACCOUNT_LIST(Map<Integer, AionChannelHandler> accounts)
	{
		this.accounts = accounts;
	}

	@Override
	protected void writeImpl(LoginServerChannelHandler cHandler)
	{
		writeD(accounts.size());
		for (AionChannelHandler ac : accounts.values())
		{
			writeS(ac.getAccount().getName());
		}
	}
}

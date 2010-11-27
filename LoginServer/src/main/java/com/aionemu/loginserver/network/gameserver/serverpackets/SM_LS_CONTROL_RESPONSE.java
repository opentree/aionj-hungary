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

package com.aionemu.loginserver.network.gameserver.serverpackets;

import com.aionemu.commons.network.netty.packet.AbstractServerPacket;
import com.aionemu.loginserver.network.gameserver.GameServerChannelHandler;

/**
 * 
 * @author Aionchs-Wylovech, Lyahim
 * 
 */
public class SM_LS_CONTROL_RESPONSE extends
		AbstractServerPacket<GameServerChannelHandler> {

	private int type;

	private boolean result;

	private String playerName;

	private int param;

	private String adminName;

	private int accountId;

	public SM_LS_CONTROL_RESPONSE(int type, boolean result, String playerName,
			int accountId, int param, String adminName) {
		this.type = type;
		this.result = result;
		this.playerName = playerName;
		this.param = param;
		this.adminName = adminName;
		this.accountId = accountId;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void writeImpl(GameServerChannelHandler cHandler) {
		writeC(type);
		writeC(result ? 1 : 0);
		writeS(adminName);
		writeS(playerName);
		writeC(param);
		writeD(accountId);
	}
}

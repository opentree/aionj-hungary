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

import java.util.Collection;

import org.apache.log4j.Logger;

import com.aionemu.loginserver.GameServerInfo;
import com.aionemu.loginserver.GameServerTable;
import com.aionemu.loginserver.network.aion.AionConnection;
import com.aionemu.loginserver.network.aion.AionServerPacket;

/**
 * @author -Nemesiss-
 */
public class SM_SERVER_LIST extends AionServerPacket
{
	/**
	 * Logger for this class.
	 */
	protected static Logger	log	= Logger.getLogger(SM_SERVER_LIST.class);

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void writeImpl(AionConnection con)
	{
		Collection<GameServerInfo> servers = GameServerTable.getGameServers();

		writeC(servers.size());// servers
		writeC(con.getAccount().getLastServer());// last server
		for(GameServerInfo gsi : servers)
		{
			writeC(gsi.getId());// server id
			writeB(gsi.getIPAddressForPlayer(con.getIP())); // server IP
			writeD(gsi.getPort());// port
			writeC(0x00); // age limit
			writeC(0x01);// pvp=1
			writeH(gsi.getCurrentPlayers());// currentPlayers
			writeH(gsi.getMaxPlayers());// maxPlayers
			writeC(gsi.isOnline() ? 1 : 0);// ServerStatus, up=1
			writeD(1);// bits);
			writeC(0);// server.brackets ? 0x01 : 0x00);
		}
	}
}

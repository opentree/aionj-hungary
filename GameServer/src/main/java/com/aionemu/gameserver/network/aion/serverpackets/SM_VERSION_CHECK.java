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

import com.aionemu.gameserver.configs.main.GSConfig;
import com.aionemu.gameserver.configs.network.NetworkConfig;
import com.aionemu.gameserver.network.aion.AbstractAionServerPacket;
import com.aionemu.gameserver.network.aion.AionChannelHandler;
import com.aionemu.gameserver.services.ChatService;

/**
 * @author Lyahim, -Nemesiss- CC fix modified by Novo
 */

public class SM_VERSION_CHECK extends AbstractAionServerPacket<AionChannelHandler>
{

	/**
	 * @param chatService
	 */
	public SM_VERSION_CHECK()
	{
	}

	/**
	 * {@inheritDoc}
	 */

	@Override
	protected void writeImpl(AionChannelHandler cHandler)
	{
		writeC(0x00);
		writeC(NetworkConfig.GAMESERVER_ID);
		writeD(0x000188AD);// unk
		writeD(0x000188A6);// unk
		writeD(0x00000000);// unk
		writeD(0x00018898);// unk
		writeD(0x4C346D9D);// unk
		writeC(0x00);// unk
		writeC(GSConfig.SERVER_COUNTRY_CODE);// country code;
		writeC(0x00);// unk
		writeC(GSConfig.SERVER_MODE);//  Server mode : 0x80 = one race / 0x01 = free race / 0x22 = Character
		writeD((int) (System.currentTimeMillis() / 1000));
		writeH(0x015E);
		writeH(0x0A01);
		writeH(0x0A01);
		writeH(0x020A);
		writeC(0x00);
		writeC(0x01);
		writeC(0x00);
		writeC(0x00);
		writeB(ChatService.getIp());
		writeH(ChatService.getPort());
	}
}

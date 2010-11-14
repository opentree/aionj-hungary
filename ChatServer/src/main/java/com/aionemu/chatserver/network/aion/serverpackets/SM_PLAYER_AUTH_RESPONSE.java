/*
 * This file is part of aion-unique <aion-unique.org>.
 *
 *  aion-unique is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  aion-unique is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with aion-unique.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.aionemu.chatserver.network.aion.serverpackets;

import com.aionemu.chatserver.network.aion.AionChannelHandler;
import com.aionemu.commons.network.netty.packet.AbstractServerPacket;

/**
 * @author ATracer, Lyahim
 */
public class SM_PLAYER_AUTH_RESPONSE extends AbstractServerPacket<AionChannelHandler>
{
	@Override
	protected void writeImpl(AionChannelHandler cHandler)
	{
		writeC(0x40); // ?
		writeH(0x01); // ?
		writeD(0x0BDD0000); // TODO this is actually dynamic
	}

}

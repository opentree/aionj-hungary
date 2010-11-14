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
package com.aionemu.gameserver.network.aion.serverpackets;

import com.aionemu.gameserver.network.aion.AbstractAionServerPacket;
import com.aionemu.gameserver.network.aion.AionChannelHandler;

/**
 * 
 * @author Lyahim, Simple
 * 
 */
public class SM_LEGION_UPDATE_SELF_INTRO extends AbstractAionServerPacket<AionChannelHandler>
{
	private String	selfintro;
	private int		playerObjId;

	public SM_LEGION_UPDATE_SELF_INTRO(int playerObjId, String selfintro)
	{
		this.selfintro = selfintro;
		this.playerObjId = playerObjId;
	}

	@Override
	public void writeImpl(AionChannelHandler cHandler)
	{
		writeD(playerObjId);
		writeS(selfintro);
	}
}

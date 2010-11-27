/*
 * This file is part of aion-unique <aion-unique.smfnew.com>.
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
 * @author Lyahim, orz
 * @author Lyahim, Sarynth thx Rhys2002 for packets. :)
 * 
 */
public class SM_DIE extends AbstractAionServerPacket<AionChannelHandler>
{
	private boolean	hasRebirth;
	private boolean	hasItem;
	private int		remainingKiskTime;

	public SM_DIE(boolean hasRebirth, boolean hasItem, int remainingKiskTime)
	{
		this.hasRebirth = hasRebirth;
		this.hasItem = hasItem;
		this.remainingKiskTime = remainingKiskTime;
	}

	@Override
	protected void writeImpl(AionChannelHandler cHandler)
	{
		writeC((hasRebirth ? 1 : 0)); // skillRevive
		writeC((hasItem ? 1 : 0)); // itemRevive
		writeD(remainingKiskTime);
	}
}

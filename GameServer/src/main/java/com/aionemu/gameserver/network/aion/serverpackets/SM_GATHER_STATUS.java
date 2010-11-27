/*
 * This file is part of aion-unique <aion-unique.com>.
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
 *
 */
public class SM_GATHER_STATUS extends AbstractAionServerPacket<AionChannelHandler>
{
	private int	status;
	private int	playerobjid;
	private int	gatherableobjid;

	public SM_GATHER_STATUS(int playerobjid, int gatherableobjid, int status)
	{
		this.playerobjid = playerobjid;
		this.gatherableobjid = gatherableobjid;
		this.status = status;
	}

	/**
	 * {@inheritDoc}
	 */

	@Override
	protected void writeImpl(AionChannelHandler cHandler)
	{

		writeD(playerobjid);
		writeD(gatherableobjid);
		writeH(0); //unk
		writeC(status);

	}
}

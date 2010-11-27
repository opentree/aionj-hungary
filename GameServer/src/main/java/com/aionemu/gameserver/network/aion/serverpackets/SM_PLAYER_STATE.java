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

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.AbstractAionServerPacket;
import com.aionemu.gameserver.network.aion.AionChannelHandler;

/**
 * 
 * So far I've found only one usage for this packet - to stop character blinking ( just after login into game, player's
 * character is blinking )
 * 
 * @author Lyahim, Luno, Sweetkr
 * 
 * states:
 * 0 - normal char
 * 1- crounched invisible char
 * 64 - standing blinking char
 * 128- char is invisible
 * 
 */
public class SM_PLAYER_STATE extends AbstractAionServerPacket<AionChannelHandler>
{
	private int	playerObjId;
	private int	visualState;
	private int	seeState;

	public SM_PLAYER_STATE(Player player)
	{
		this.playerObjId = player.getObjectId();
		this.visualState = player.getVisualState();
		this.seeState = player.getSeeState();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void writeImpl(AionChannelHandler cHandler)
	{
		writeD(playerObjId);
		writeC(visualState);
		writeC(seeState);
		if (visualState == 64)
			writeC(0x01);
		else
			writeC(0x00);
	}
}

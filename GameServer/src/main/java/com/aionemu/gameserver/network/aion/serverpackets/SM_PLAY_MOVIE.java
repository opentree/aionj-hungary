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
 * @author Lyahim, -orz-, MrPoke
 * 
 */
public class SM_PLAY_MOVIE extends AbstractAionServerPacket<AionChannelHandler>
{
	private int	type	= 1;	// if 1: CutSceneMovies else CutScenes
	private int	movieId	= 0;

	public SM_PLAY_MOVIE(int type, int movieId)
	{
		this.type = type;
		this.movieId = movieId;
	}

	@Override
	protected void writeImpl(AionChannelHandler cHandler)
	{
		writeC( type);
		writeD(0x00);
		writeD(0x00);
		writeH(movieId);
		writeD(0x00);
	}
}

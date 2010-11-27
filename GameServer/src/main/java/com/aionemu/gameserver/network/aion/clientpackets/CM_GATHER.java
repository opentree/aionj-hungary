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
package com.aionemu.gameserver.network.aion.clientpackets;

import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.instance.Gatherable;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.commons.network.netty.packet.AbstractClientPacket;
import com.aionemu.gameserver.network.aion.AionChannelHandler;

/**
 * @author Lyahim, ATracer
 *
 */
public class CM_GATHER extends AbstractClientPacket<AionChannelHandler>
{
	boolean	isStartGather	= false;

	public CM_GATHER(int opcode)
	{
		super(opcode);
	}

	@Override
	protected void readImpl()
	{
		int action = readD();
		if (action == 0)
			isStartGather = true;

	}

	@Override
	protected void runImpl()
	{

		Player player = getChannelHandler().getActivePlayer();
		if (player != null)
		{
			VisibleObject target = player.getTarget();
			if (target != null && target instanceof Gatherable)
			{
				if (isStartGather)
				{
					((Gatherable) target).onStartUse(player);
				}
				else
				{
					((Gatherable) target).finishGathering(player);
				}
			}
		}
	}

}

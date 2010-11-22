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
package com.aionemu.gameserver.network.aion.clientpackets;

import com.aionemu.gameserver.model.gameobjects.AionObject;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.instance.Summon;
import com.aionemu.gameserver.model.gameobjects.instance.Summon.UnsummonType;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.commons.network.netty.packet.AbstractClientPacket;
import com.aionemu.gameserver.network.aion.AionChannelHandler;
import com.aionemu.gameserver.world.World;

/**
 * @author Lyahim, ATracer
 *
 */
public class CM_SUMMON_COMMAND extends AbstractClientPacket<AionChannelHandler>
{

	private int mode;
	private int targetObjId;
	
	public CM_SUMMON_COMMAND(int opcode)
	{
		super(opcode);
	}

	@Override
	protected void readImpl()
	{
		mode = readC();
		readD();
		readD();
		targetObjId = readD();
	}

	@Override
	protected void runImpl()
	{
		Player activePlayer = getChannelHandler().getActivePlayer();
		Summon summon = activePlayer.getSummon();
		if(summon != null)
		{
			switch(mode)
			{
				case 0:
					AionObject target = World.getInstance().findAionObject(targetObjId);
					if(target != null && target instanceof Creature)
					{
						summon.attackMode();
					}
					break;
				case 1:
					summon.guardMode();
					break;
				case 2:
					summon.restMode();
					break;
				case 3:
					summon.release(UnsummonType.COMMAND);
					break;
					
			}
		}
	}

}

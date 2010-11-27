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

import com.aionemu.gameserver.controllers.movement.MovementType;
import com.aionemu.gameserver.network.aion.AbstractAionServerPacket;
import com.aionemu.gameserver.network.aion.AionChannelHandler;

/**
 * This packet is displaying movement of players etc.
 * 
 * @author Lyahim, -Nemesiss-
 * 
 */
public class SM_MOVE extends AbstractAionServerPacket<AionChannelHandler>
{
	/**
	 * Object that is moving.
	 */
	private final int			movingCreatureId;
	private final float			x, y, z, x2, y2, z2;
	private final byte			heading;
	private final MovementType	moveType;
	private final byte			glideFlag;

	private boolean				hasDirection;
	private boolean				hasGlideFlag;

	/**
	 * Constructs new <tt>SM_MOVE</tt> packet
	 * 
	 * @param movingCreature
	 * @param x
	 * @param y
	 * @param z
	 * @param x2
	 * @param y2
	 * @param z2
	 * @param heading
	 * @param glideFlag
	 * @param moveType
	 */
	public SM_MOVE(int movingCreatureId, float x, float y, float z, float x2, float y2, float z2, byte heading, byte glideFlag, MovementType moveType)
	{
		this.movingCreatureId = movingCreatureId;
		this.x = x;
		this.y = y;
		this.z = z;
		this.x2 = x2;
		this.y2 = y2;
		this.z2 = z2;
		this.heading = heading;
		this.glideFlag = glideFlag;
		this.moveType = moveType;

		this.hasDirection = true;
		this.hasGlideFlag = true;
	}

	public SM_MOVE(int movingCreatureId, float x, float y, float z, float x2, float y2, float z2, byte heading, MovementType moveType)
	{
		this(movingCreatureId, x, y, z, x2, y2, z2, heading, (byte) 0, moveType);
		this.hasDirection = true;
		this.hasGlideFlag = false;
	}

	public SM_MOVE(int movingCreatureId, float x, float y, float z, byte heading, MovementType moveType)
	{
		this(movingCreatureId, x, y, z, 0, 0, 0, heading, (byte) 0, moveType);
		this.hasDirection = false;
		this.hasGlideFlag = false;
	}

	public SM_MOVE(int movingCreatureId, float x, float y, float z, byte heading, byte glideFlag, MovementType moveType)
	{
		this(movingCreatureId, x, y, z, 0, 0, 0, heading, glideFlag, moveType);
		this.hasDirection = false;
		this.hasGlideFlag = true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void writeImpl(AionChannelHandler cHandler)
	{
		writeD(movingCreatureId);
		writeF(x);
		writeF(y);
		writeF(z);
		writeC(heading);
		writeC(moveType.getMovementTypeId());

		if (this.hasDirection)
		{
			writeF(x2);
			writeF(y2);
			writeF(z2);
		}

		if (this.hasGlideFlag)
		{
			writeC(glideFlag);
		}
	}
}

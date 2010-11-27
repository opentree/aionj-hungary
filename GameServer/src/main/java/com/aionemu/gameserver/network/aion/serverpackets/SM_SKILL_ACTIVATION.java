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
 * @author Lyahim, Sweetkr
 */
public class SM_SKILL_ACTIVATION extends AbstractAionServerPacket<AionChannelHandler>
{

	private boolean	isActive;
	private int		unk;
	private int		skillId;

	/**
	 *  For toggle skills
	 *  
	 * @param skillId
	 * @param isActive
	 */
	public SM_SKILL_ACTIVATION(int skillId, boolean isActive)
	{
		this.skillId = skillId;
		this.isActive = isActive;
		this.unk = 0;
	}

	/**
	 * For stigma remove should work in 1.5.1.15
	 * @param skillId
	 */
	public SM_SKILL_ACTIVATION(int skillId)
	{
		this.skillId = skillId;
		this.isActive = true;
		this.unk = 1;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void writeImpl(AionChannelHandler cHandler)
	{
		writeH(skillId);
		writeD(unk);
		writeC(isActive ? 1 : 0);
	}
}

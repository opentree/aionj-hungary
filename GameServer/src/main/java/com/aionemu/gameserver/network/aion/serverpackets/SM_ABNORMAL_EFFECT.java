/*
 * This file is part of aion-unique <www.aion-unique.com>.
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

import java.util.Collection;

import com.aionemu.gameserver.network.aion.AbstractAionServerPacket;
import com.aionemu.gameserver.network.aion.AionChannelHandler;
import com.aionemu.gameserver.skillengine.model.Effect;

/**
 * @author Lyahim, ATracer
 */
public class SM_ABNORMAL_EFFECT extends AbstractAionServerPacket<AionChannelHandler>
{
	private int					effectedId;
	private int					abnormals;
	private Collection<Effect>	effects;

	public SM_ABNORMAL_EFFECT(int effectedId, int abnormals, Collection<Effect> effects)
	{
		this.effects = effects;
		this.abnormals = abnormals;
		this.effectedId = effectedId;
	}

	@Override
	protected void writeImpl(AionChannelHandler cHandler)
	{
		writeD(effectedId);
		writeC(1); //unk isdebuff
		writeD(0); //unk
		writeD(abnormals); //unk

		writeH(effects.size()); //effects size

		for (Effect effect : effects)
		{
			writeH(effect.getSkillId());
			writeC(effect.getSkillLevel());
			writeC(effect.getTargetSlot());
			writeD(effect.getElapsedTime());
		}
	}
}
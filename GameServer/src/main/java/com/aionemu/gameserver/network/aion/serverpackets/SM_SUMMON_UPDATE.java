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

import com.aionemu.gameserver.model.gameobjects.instance.Summon;
import com.aionemu.gameserver.model.gameobjects.stats.StatEnum;
import com.aionemu.gameserver.network.aion.AbstractAionServerPacket;
import com.aionemu.gameserver.network.aion.AionChannelHandler;

/**
 * @author Lyahim, ATracer
 * 
 */
public class SM_SUMMON_UPDATE extends AbstractAionServerPacket<AionChannelHandler>
{
	private Summon	summon;

	public SM_SUMMON_UPDATE(Summon summon)
	{
		this.summon = summon;
	}

	@Override
	protected void writeImpl(AionChannelHandler cHandler)
	{
		writeC( summon.getLevel());
		writeH(summon.getMode().getId());
		writeD(0);// unk
		writeD(0);// unk
		writeD(summon.getLifeStats().getCurrentHp());
		writeD(summon.getGameStats().getCurrentStat(StatEnum.MAXHP));
		writeD(summon.getGameStats().getCurrentStat(StatEnum.MAIN_HAND_POWER));
		writeH(summon.getGameStats().getCurrentStat(StatEnum.PHYSICAL_DEFENSE));
		writeH(summon.getGameStats().getCurrentStat(StatEnum.MAGICAL_RESIST));
		writeH(summon.getGameStats().getCurrentStat(StatEnum.ACCURACY));
		writeH(summon.getGameStats().getCurrentStat(StatEnum.CRITICAL_RESIST));
		writeH(summon.getGameStats().getCurrentStat(StatEnum.BOOST_MAGICAL_SKILL));
		writeH(summon.getGameStats().getCurrentStat(StatEnum.MAGICAL_ACCURACY));		
		writeH(summon.getGameStats().getCurrentStat(StatEnum.MAGICAL_CRITICAL));
		writeH(summon.getGameStats().getCurrentStat(StatEnum.PARRY));
		writeH(summon.getGameStats().getCurrentStat(StatEnum.EVASION));
		writeD(summon.getGameStats().getBaseStat(StatEnum.MAXHP));
		writeD(summon.getGameStats().getBaseStat(StatEnum.MAIN_HAND_POWER));
		writeH(summon.getGameStats().getBaseStat(StatEnum.PHYSICAL_DEFENSE));		
		writeH(summon.getGameStats().getBaseStat(StatEnum.MAGICAL_RESIST));		
		writeH(summon.getGameStats().getBaseStat(StatEnum.ACCURACY));
		writeH(summon.getGameStats().getBaseStat(StatEnum.CRITICAL_RESIST));
		writeH(summon.getGameStats().getBaseStat(StatEnum.BOOST_MAGICAL_SKILL));
		writeH(summon.getGameStats().getBaseStat(StatEnum.MAGICAL_ACCURACY));		
		writeH(summon.getGameStats().getBaseStat(StatEnum.MAGICAL_CRITICAL));
		writeH(summon.getGameStats().getBaseStat(StatEnum.PARRY));
		writeH(summon.getGameStats().getBaseStat(StatEnum.EVASION));		
	}

}

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

import java.util.List;

import com.aionemu.gameserver.model.alliance.PlayerAlliance;
import com.aionemu.gameserver.network.aion.AbstractAionServerPacket;
import com.aionemu.gameserver.network.aion.AionChannelHandler;

/**
 * @author Lyahim, Sarynth (Thx Rhys2002 for Packets)
 *
 */
public class SM_ALLIANCE_INFO extends AbstractAionServerPacket<AionChannelHandler>
{
	private PlayerAlliance	alliance;

	public SM_ALLIANCE_INFO(PlayerAlliance alliance)
	{
		this.alliance = alliance;
	}

	@Override
	protected void writeImpl(AionChannelHandler cHandler)
	{
		writeH(4);
		writeD(alliance.getObjectId());
		writeD(alliance.getCaptainObjectId());

		List<Integer> ids = alliance.getViceCaptainObjectIds();
		writeD(ids.size() > 0 ? ids.get(0) : 0); // viceLeader1
		writeD(ids.size() > 1 ? ids.get(1) : 0); // viceLeader2
		writeD(ids.size() > 2 ? ids.get(2) : 0); // viceLeader3
		writeD(ids.size() > 3 ? ids.get(3) : 0); // viceLeader4

		writeD(0); //loot rule type - 0 freeforall, 1 roundrobin, 2 leader
		writeD(0); //autoDistribution - 0 or 1
		writeD(0); //this.common_item_above); - 0 normal 2 roll 3 bid
		writeD(0); //this.superior_item_above); - 0 normal 2 roll 3 bid
		writeD(0); //this.heroic_item_above); - 0 normal 2 roll 3 bid
		writeD(0); //this.fabled_item_above); - 0 normal 2 roll 3 bid
		writeD(0); //this.ethernal_item_above); - 0 normal 2 roll 3 bid
		writeD(0); //this.over_ethernal); - 0 normal 2 roll 3 bid
		writeD(0); //this.over_over_ethernal); - 0 normal 2 roll 3 bid
		writeC(0); //unk

		writeD(0); // allianceGroupNumber 1
		writeD(1000); // allianceId 1
		writeD(1); // allianceGroupNumber 2
		writeD(1001); // allianceId 1
		writeD(2); // allianceGroupNumber 3
		writeD(1002); // allianceId 1
		writeD(3); // allianceGroupNumber 4
		writeD(1003); // allianceId 1

		writeD(0); //unk
		writeH(0); //unk
	}
}

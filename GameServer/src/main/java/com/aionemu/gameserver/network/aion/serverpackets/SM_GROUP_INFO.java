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

import com.aionemu.gameserver.model.group.LootDistribution;
import com.aionemu.gameserver.model.group.LootGroupRules;
import com.aionemu.gameserver.model.group.LootRuleType;
import com.aionemu.gameserver.model.group.PlayerGroup;
import com.aionemu.gameserver.network.aion.AbstractAionServerPacket;
import com.aionemu.gameserver.network.aion.AionChannelHandler;

/**
 * @author Lyahim, Lyahim, ATracer
 *
 */
public class SM_GROUP_INFO extends AbstractAionServerPacket<AionChannelHandler>
{
	private int groupid;
	private int leaderid;
	private LootRuleType lootruletype; //0-free-for-all, 1-round-robin 2-leader
	private LootDistribution autodistribution;
	//rare item distribution
	//0-normal, 2-Roll-dice,3-bid
	private int common_item_above;
	private int superior_item_above;
	private int heroic_item_above;
	private int fabled_item_above;
	private int ethernal_item_above;
	private int over_ethernal;
	private int over_over_ethernal;

	public SM_GROUP_INFO(PlayerGroup group) //need a group class whit this parameters
	{
		this.groupid = group.getGroupId();
		this.leaderid = group.getGroupLeader().getObjectId();
		
		LootGroupRules lootRules = group.getLootGroupRules();
		this.lootruletype = lootRules.getLootRule();
		this.autodistribution = lootRules.getAutodistribution();
		this.common_item_above = lootRules.getCommonItemAbove();
		this.superior_item_above = lootRules.getSuperiorItemAbove();
		this.heroic_item_above = lootRules.getHeroicItemAbove();
		this.fabled_item_above = lootRules.getFabledItemAbove();
		this.ethernal_item_above = lootRules.getEthernalItemAbove();
		this.over_ethernal = lootRules.getOverEthernal();
		this.over_over_ethernal = lootRules.getOverOverEthernal();
	}

	@Override
	protected void writeImpl(AionChannelHandler cHandler)
	{
		writeD(this.groupid);
		writeD(this.leaderid);
		writeD(this.lootruletype.getId());
		writeD(this.autodistribution.getId());
		writeD(this.common_item_above);
		writeD(this.superior_item_above);
		writeD(this.heroic_item_above);
		writeD(this.fabled_item_above);
		writeD(this.ethernal_item_above);
		writeD(this.over_ethernal);
		writeD(this.over_over_ethernal);
		writeD(0x00);
		writeH(0x00);
		writeC( 0x00);
	}
}

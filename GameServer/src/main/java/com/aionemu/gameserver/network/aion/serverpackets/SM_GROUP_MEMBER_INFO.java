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

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.PlayerCommonData;
import com.aionemu.gameserver.model.gameobjects.stats.PlayerLifeStats;
import com.aionemu.gameserver.model.group.GroupEvent;
import com.aionemu.gameserver.model.group.PlayerGroup;
import com.aionemu.gameserver.network.aion.AbstractAionServerPacket;
import com.aionemu.gameserver.network.aion.AionChannelHandler;
import com.aionemu.gameserver.skillengine.model.Effect;
import com.aionemu.gameserver.world.WorldPosition;

/**
 * @author Lyahim, Lyahim, ATracer
 *
 */
public class SM_GROUP_MEMBER_INFO extends AbstractAionServerPacket<AionChannelHandler>
{
	private PlayerGroup group;
	private Player player;
	private GroupEvent event;
	
	public SM_GROUP_MEMBER_INFO(PlayerGroup group, Player player, GroupEvent event)
	{
		this.group = group;
		this.player = player;
		this.event = event;
	}
	
	@Override
	protected void writeImpl(AionChannelHandler cHandler)
	{		
		PlayerLifeStats pls = player.getLifeStats();
		PlayerCommonData pcd = player.getCommonData();
		WorldPosition wp = pcd.getPosition();
		
		writeD(group.getGroupId());
		writeD(player.getObjectId());
		writeD(pls.getMaxHp());
		writeD(pls.getCurrentHp());
		writeD(pls.getMaxMp());
		writeD(pls.getCurrentMp());
		writeD(pls.getMaxFp()); //maxflighttime
		writeD(pls.getCurrentFp()); //currentflighttime
		writeD(wp.getMapId());
		writeD(wp.getMapId());
		writeF(wp.getX());
		writeF(wp.getY());
		writeF(wp.getZ());
		writeC( pcd.getPlayerClass().getClassId()); //class id
		writeC( pcd.getGender().getGenderId()); //gender id
		writeC( pcd.getLevel()); //level
		writeC( this.event.getId()); //something events
		writeH(0x01); //channel
		if (this.event == GroupEvent.MOVEMENT)
		{
			return;
		}
		writeS(pcd.getName()); //name
		writeH(0x00); //unk
		writeH(0x00); //unk
		
		List<Effect> abnormalEffects = player.getEffectController().getAbnormalEffects();
		writeH(abnormalEffects.size()); //Abnormal effects
		for(Effect effect : abnormalEffects)
		{
			writeD(effect.getEffectorId()); //casterid
			writeH(effect.getSkillId()); //spellid
			writeC( effect.getSkillLevel()); //spell level
			writeC( effect.getTargetSlot()); //unk ?
			writeD(effect.getElapsedTime()); //estimatedtime
		}
		writeD(0x25F7); //unk 9719
	}
}

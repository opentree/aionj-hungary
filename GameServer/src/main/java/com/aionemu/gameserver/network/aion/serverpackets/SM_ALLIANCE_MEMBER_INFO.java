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

import com.aionemu.gameserver.model.alliance.PlayerAllianceEvent;
import com.aionemu.gameserver.model.alliance.PlayerAllianceMember;
import com.aionemu.gameserver.model.gameobjects.player.PlayerCommonData;
import com.aionemu.gameserver.model.gameobjects.stats.PlayerLifeStats;
import com.aionemu.gameserver.network.aion.AbstractAionServerPacket;
import com.aionemu.gameserver.network.aion.AionChannelHandler;
import com.aionemu.gameserver.skillengine.model.Effect;
import com.aionemu.gameserver.world.WorldPosition;

/**
 * @author Lyahim, Sarynth (Thx Rhys2002 for Packets)
 *
 */
public class SM_ALLIANCE_MEMBER_INFO extends AbstractAionServerPacket<AionChannelHandler>
{
	private PlayerAllianceMember member;
	private PlayerAllianceEvent event;
	
	public SM_ALLIANCE_MEMBER_INFO(PlayerAllianceMember member, PlayerAllianceEvent event)
	{
		this.member = member;
		this.event = event;
	}
	
	@Override
	protected void writeImpl(AionChannelHandler cHandler)
	{
		PlayerCommonData pcd = member.getCommonData();
		WorldPosition wp = pcd.getPosition();
		
		/**
		 * Required so that when member is disconnected, and his
		 * playerAllianceGroup slot is changed, he will continue
		 * to appear as disconnected to the alliance.
		 */
		if (!member.isOnline())
			event = PlayerAllianceEvent.DISCONNECTED;
		
		writeD(member.getAllianceId());
		writeD(member.getObjectId());
		if (member.isOnline())
		{
			PlayerLifeStats pls = member.getPlayer().getLifeStats();
			writeD(pls.getMaxHp());
			writeD(pls.getCurrentHp());
			writeD(pls.getMaxMp());
			writeD(pls.getCurrentMp());
			writeD(pls.getMaxFp());
			writeD(pls.getCurrentFp());
		}
		else
		{
			writeD(0);
			writeD(0);
			writeD(0);
			writeD(0);
			writeD(0);
			writeD(0);
		}
		writeD(wp.getMapId());
		writeD(wp.getMapId());
		writeF(wp.getX());
		writeF(wp.getY());
		writeF(wp.getZ());
		writeC( pcd.getPlayerClass().getClassId());
		writeC( pcd.getGender().getGenderId());
		writeC( pcd.getLevel());
		writeC( this.event.getId());
		writeH(0x00); //channel 0x01?
		switch(this.event)
		{
			case LEAVE:
			case LEAVE_TIMEOUT:
			case BANNED:
			case MOVEMENT:
			case DISCONNECTED:
				break;
				
			case ENTER:
			case UPDATE:
			case RECONNECT:
			case MEMBER_GROUP_CHANGE:
				
			case APPOINT_VICE_CAPTAIN: // Unused maybe...
			case DEMOTE_VICE_CAPTAIN:
			case APPOINT_CAPTAIN:
				writeS(pcd.getName());
				writeD(0x00); //unk
				
				if (member.isOnline())
				{
					List<Effect> abnormalEffects = member.getPlayer().getEffectController().getAbnormalEffects();
					writeH(abnormalEffects.size());
					for(Effect effect : abnormalEffects)
					{
						writeD(effect.getEffectorId());
						writeH(effect.getSkillId());
						writeC( effect.getSkillLevel());
						writeC( effect.getTargetSlot());
						writeD(effect.getElapsedTime());
					}
				}
				else
				{
					writeH(0);
				}
				break;
			default:
				break;
		}
	}
	
}

/*
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, 
 * MA  02110-1301, USA.
 *
 * http://www.gnu.org/copyleft/gpl.html
 */
package com.aionemu.gameserver.model.team.alliance;

import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.team.Team;
import com.aionemu.gameserver.model.team.group.PlayerGroup;
import com.aionemu.gameserver.model.team.interfaces.ITeamProperties;

/**
 * @author lyahim
 *
 */
public class PlayerAlliance extends Team<PlayerGroup> implements ITeamProperties
{
	public PlayerAlliance(int teamId, PlayerGroup leader)
	{
		super(teamId, leader);
	}

	@Override
	public void addMember(PlayerGroup member)
	{
		super.addMember(member);
	}

	@Override
	public boolean isFull()
	{
		return members.size() == 4;
	}

	@Override
	public String getName()
	{
		return "Player Alliance" + getObjectId();
	}

	@Override
	public void getReward(Npc owner)
	{
	}
}

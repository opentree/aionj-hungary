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
package com.aionemu.gameserver.model.team;

import java.util.Collection;

import javolution.util.FastList;

import com.aionemu.gameserver.model.gameobjects.AionObject;
import com.aionemu.gameserver.model.team.interfaces.ITeamProperties;

/**
 * @author lyahim
 *
 */
public abstract class Team<T extends ITeamProperties> extends AionObject
{
	protected T						leader;
	protected final Collection<T>	members	= new FastList<T>().shared();

	public Team(int teamId, T leader)
	{
		super(teamId);
		this.leader = leader;
		members.add(leader);
	}

	public ITeamProperties getLeader()
	{
		return leader;
	}

	public void setLeader(T newleader)
	{
		this.leader = newleader;
	}

	public Collection<T> getMembers()
	{
		return members;
	}

	public void addMember(T member)
	{
		members.add(member);
	}

	public boolean isMember(T member)
	{
		return members.contains(member) ? true : false;
	}

	public void removeMember(T member)
	{
		members.remove(member.getObjectId());
	}

	public int size()
	{
		return members.size();
	}

	public void disband()
	{
		members.clear();
	}

	public void onMemberLogIn(T member)
	{
		removeMember(member);
		addMember(member);
	}

	public abstract boolean isFull();
}

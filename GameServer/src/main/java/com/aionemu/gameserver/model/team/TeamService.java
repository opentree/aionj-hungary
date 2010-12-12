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

import javolution.util.FastMap;

import com.aionemu.gameserver.model.team.interfaces.ITeamProperties;

/**
 * @author lyahim
 *
 */
public abstract class TeamService<T extends ITeamProperties>
{
	private final FastMap<Integer, Team<T>>	teams	= new FastMap<Integer, Team<T>>().shared();

	public Team<T> getTeamById(int id)
	{
		return teams.get(id);
	}

	public void addTeam(Team<T> newTeam)
	{
		teams.put(newTeam.getObjectId(), newTeam);
	}

	public boolean alreadyInTeam(T member)
	{
		for (Team<T> team : teams.values())
			if (team.isMember(member))
				return true;
		return false;
	}
	/*
		public static final TeamService<?> getInstance()
		{
			return SingletonHolder.instance;
		}

		private static class SingletonHolder
		{
			protected static final TeamService	instance	= new TeamService();
		}
	*/
}

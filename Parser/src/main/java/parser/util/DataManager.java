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
package parser.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import parser.clientData.clientNpc.ClientNpcLoader;
import parser.clientData.clientNpc.NpcClient;
import parser.clientData.clientStrings.ClientString;
import parser.clientData.clientStrings.ClientStringLoader;
import parser.clientData.tribeRelation.Tribe;
import parser.clientData.tribeRelation.TribeRelationLoader;

/**
 * @author Mr. Poke
 *
 */
public class DataManager
{

	private Map<String, ClientString>	stringNameMap;
	private List<Tribe> tribeRelations;
	private Map<Integer, NpcClient>	idNpcMap;

	public static final DataManager getInstance()
	{
		return SingletonHolder.instance;
	}

	private DataManager()
	{
		stringNameMap = ClientStringLoader.load();
		tribeRelations = TribeRelationLoader.load();
		idNpcMap = ClientNpcLoader.load();
	}

	/**
	 * @return Returns the stringNameMap.
	 */
	public Map<String, ClientString> getStringNameMap()
	{
		return stringNameMap;
	}

	/**
	 * @return Returns the tribeRelations.
	 */
	public List<Tribe> getTribeRelations()
	{
		return tribeRelations;
	}

	public Map<Integer, NpcClient> getIdNpcMap()
	{
		return idNpcMap;
	}

	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder
	{
		protected static final DataManager	instance	= new DataManager();
	}
}

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

import java.util.List;
import java.util.Map;

import parser.clientData.clientItems.ClientItem;
import parser.clientData.clientNpc.NpcClient;
import parser.clientData.clientStrings.ClientString;
import parser.clientData.clientStrings.ClientStringLoader;
import parser.clientData.tribeRelation.Tribe;

/**
 * @author Mr. Poke
 *
 */
public class DataManager
{

	private Map<String, ClientString>	stringNameMap;
	private List<Tribe> tribeRelations;
	private Map<Integer, NpcClient>	idNpcMap;
	private Map<Integer, ClientItem>	idItemMap;

	private Map<String, Integer>	itemNameIdMap;
	private Map<String, Integer>	npcNameIdMap;
	
	public static final DataManager getInstance()
	{
		return SingletonHolder.instance;
	}

	private DataManager()
	{
		stringNameMap = ClientStringLoader.load();
//		tribeRelations = TribeRelationLoader.load();
//		idNpcMap = ClientNpcLoader.load();
//		idItemMap =ClientItemsLoader.load();
				
//		createNpcNameIdMap();
//		createItemNameIdMap();
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

	/**
	 * @return Returns the idItemMap.
	 */
	public Map<Integer, ClientItem> getIdItemMap()
	{
		return idItemMap;
	}

	/**
	 * @return Returns the itemNameIdMap.
	 */
	public Map<String, Integer> getItemNameIdMap()
	{
		return itemNameIdMap;
	}

	/**
	 * @return Returns the npcNameIdMap.
	 */
	public Map<String, Integer> getNpcNameIdMap()
	{
		return npcNameIdMap;
	}

	private void createNpcNameIdMap()
	{
		for (NpcClient npc : idNpcMap.values())
		{
			npcNameIdMap.put(npc.getName(), npc.getId());
		}
	}
	
	private void createItemNameIdMap()
	{
		for (ClientItem item: idItemMap.values())
		{
			itemNameIdMap.put(item.getName(), item.getId());
		}
			
	}
	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder
	{
		protected static final DataManager	instance = new DataManager();
	}
}

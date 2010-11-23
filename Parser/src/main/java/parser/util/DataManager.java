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

import parser.clientData.clientGather.GatherSrc;
import parser.clientData.clientGather.GatherSrcLoader;
import parser.clientData.clientItems.ClientItem;
import parser.clientData.clientItems.ClientItemsLoader;
import parser.clientData.clientNpc.ClientNpcLoader;
import parser.clientData.clientNpc.NpcClient;
import parser.clientData.clientStrings.ClientString;
import parser.clientData.clientStrings.ClientStringLoader;
import parser.clientData.clientWorldId.Data;
import parser.clientData.clientWorldId.WorldIdLoader;
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
	private Map<Integer, ClientItem>	idItemMap;
	private List<Data> worldIds;
	private Map<String, Integer>	itemNameIdMap;
	private Map<String, Integer>	npcNameIdMap;
	private Map<Integer, GatherSrc> idGatherebleMap;
	private Map<String, Integer> nameGatherebleIdMap;

	public static final DataManager getInstance()
	{
		return SingletonHolder.instance;
	}

	private DataManager()
	{
	}

	/**
	 * @return Returns the stringNameMap.
	 */
	public Map<String, ClientString> getStringNameMap()
	{
		if (stringNameMap == null)
			stringNameMap = ClientStringLoader.load();
		return stringNameMap;
	}

	/**
	 * @return Returns the tribeRelations.
	 */
	public List<Tribe> getTribeRelations()
	{
		if (tribeRelations == null)
			tribeRelations = TribeRelationLoader.load();
		return tribeRelations;
	}

	public Map<Integer, NpcClient> getIdNpcMap()
	{
		if (idNpcMap == null)
			idNpcMap = ClientNpcLoader.load();
		return idNpcMap;
	}

	/**
	 * @return Returns the idItemMap.
	 */
	public Map<Integer, ClientItem> getIdItemMap()
	{
		if (idItemMap == null)
			idItemMap =ClientItemsLoader.load();
		return idItemMap;
	}

	/**
	 * @return Returns the itemNameIdMap.
	 */
	public Map<String, Integer> getItemNameIdMap()
	{
		if (itemNameIdMap == null)
			createItemNameIdMap();
		return itemNameIdMap;
	}

	/**
	 * @return Returns the npcNameIdMap.
	 */
	public Map<String, Integer> getNpcNameIdMap()
	{
		if (npcNameIdMap == null)
			createNpcNameIdMap();
		return npcNameIdMap;
	}

	/**
	 * @return Returns the worldIds.
	 */
	public List<Data> getWorldIds()
	{
		if (worldIds == null)
			worldIds = WorldIdLoader.load();
		return worldIds;
	}

	private void createNpcNameIdMap()
	{
		npcNameIdMap = new HashMap<String, Integer>();
		for (NpcClient npc : idNpcMap.values())
		{
			npcNameIdMap.put(npc.getName().toLowerCase(), npc.getId());
		}
	}
	
	private void createItemNameIdMap()
	{
		itemNameIdMap = new HashMap<String, Integer>();
		for (ClientItem item: getIdItemMap().values())
		{
			itemNameIdMap.put(item.getName().toLowerCase(), item.getId());
		}
			
	}
	/**
	 * @return Returns the idGatherebleMap.
	 */
	public Map<Integer, GatherSrc> getIdGatherebleMap()
	{
		if (idGatherebleMap == null)
			idGatherebleMap = GatherSrcLoader.load();
		return idGatherebleMap;
	}

	/**
	 * @return Returns the nameGatherebleIdMap.
	 */
	public Map<String, Integer> getNameGatherebleIdMap()
	{
		nameGatherebleIdMap = new HashMap<String, Integer>();
		for (GatherSrc item: getIdGatherebleMap().values())
		{
			nameGatherebleIdMap.put(item.getName().toLowerCase(), item.getId());
		}
		return nameGatherebleIdMap;
	}

	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder
	{
		protected static final DataManager	instance	= new DataManager();
	}
}

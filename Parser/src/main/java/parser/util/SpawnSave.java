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

import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

import parser.serverData.spawns.SpawnGroup;

/**
 * @author Mr. Poke
 *
 */
public class SpawnSave
{
	private static int						id					= 1000;

	public static void save()
	{
		FileWriter fw;
		try
		{
			fw = new FileWriter("sql/spawn.sql", false);
			Collection<List<SpawnGroup>> sG = DataManager.getInstance().getSpawnGroup().values();
			fw.write("INSERT INTO `spawn`(`id`, `world`,`templateId`,`x`,`y`,`z`,`heading`,`staticId`, `respawnTime`, `spawnTime`)VALUES\n");
			for (List<SpawnGroup> spawnGroups: sG)
			for (SpawnGroup spawnGroup : spawnGroups)
			{
				if (spawnGroup.getObject().size() != spawnGroup.getPool())
					continue; //TODO Special spawn handler.
				int worldId = spawnGroup.getMap();
				int templateId = spawnGroup.getNpcid();
				int interval = 0;
				if (spawnGroup.getInterval() != null)
					interval = spawnGroup.getInterval();
				
				if (spawnGroup.getSiegeid() != 0 || spawnGroup.getHandler() != null)
					continue;
				for (SpawnGroup.Object spawnPoint : spawnGroup.getObject())
				{
					fw.write("(" + (id++) + ", " + worldId + ", " + templateId + ", " + spawnPoint.getX() + ", " + spawnPoint.getY() + ", " + spawnPoint.getZ()
							+ "," + spawnPoint.getH() + "," + spawnPoint.getStaticid() + "," + interval+",'" + spawnGroup.getTime()+"')");
					if (id % 1000 == 0)
					{
						fw.write(";\nINSERT INTO `spawn`(`id`, `world`,`templateId`,`x`,`y`,`z`,`heading`,`staticId`, `respawnTime`, `spawnTime`)VALUES\n");
					}
					else
						fw.write(",\n");
					System.out.println(id);
				}
			}
			fw.write(";");
			fw.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	/*
	public static void save()
	{
		createDirMap();
		FileWriter fw;
		try
		{
			fw = new FileWriter("sql/spawn.sql", false);
			fw.write("INSERT INTO `spawn`(`id`, `world`,`templateId`,`x`,`y`,`z`,`heading`,`staticId`)VALUES\n");
			for (Data world : DataManager.getInstance().getWorldIds())
			{
				System.out.println("Mission file: " + world.getId() + ".xml");
				for (ObjectsClass object : loadMissionFile(world.getId()).getObjects())
					parseSpawns(object, world.getId(), fw);
			}
			fw.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		System.out.println(missingnameCount);
	}

	private static void parseSpawns(ObjectsClass objects, Integer worldId, FileWriter fw) throws IOException
	{
		for (ObjectClass object : objects.getObject())
		{
			if (object.getPos() == null)
				continue;
			Integer npcId = 0;
			float x, y, z = 0;
			StringTokenizer st = new StringTokenizer(object.getPos().trim().toLowerCase(), ",");
			if (st.countTokens() < 3)
			{
				continue;
			}
			else
			{
				x = Float.parseFloat(st.nextToken());
				y = Float.parseFloat(st.nextToken());
				z = Float.parseFloat(st.nextToken());
			}

			if (object.getNpc() == null || object.getNpc().equals(""))
			{

				List<SpawnGroup> spawnGroups = DataManager.getInstance().getSpawnGroup().get(worldId);
				if (spawnGroups != null)
				{
					end :for (SpawnGroup spawngroup : spawnGroups)
					{
						for (SpawnGroup.Object groupObject : spawngroup.getObject())
						{
							double dist = MathUtil.getDistance(groupObject.getX(), groupObject.getY(), 0, x, y, 0);

							if (dist < 4)
							{
								npcId = spawngroup.getNpcid();
								break end;
							}
						}
					}
				}
				if (npcId == 0)
					missingnameCount++;
				continue;
			}
			else
			{
				npcId = DataManager.getInstance().getNpcNameIdMap().get(object.getNpc().toLowerCase());
				if (npcId == null)
				{
					npcId = DataManager.getInstance().getNameGatherebleIdMap().get(object.getNpc().toLowerCase());
					if (npcId == null)
					{
						continue;
					}
				}
			}

			fw.write("('" + (id++) + "', '" + worldId + "', '" + npcId + "', '" + x + "', '" + y + "', '" + z + "', 0,0)");
			if (id % 1000 == 0)
			{
				fw.write(";\nINSERT INTO `spawn`(`id`, `world`,`templateId`,`x`,`y`,`z`,`heading`,`staticId`)VALUES\n");
			}
			else
				fw.write(",\n");
		}
		for (EntityClass entity : objects.getEntity())
		{
			if (entity.getProperties().size() == 0)
				continue;
			String dir = entity.getProperties().get(0).getFileLadderCGF();
			if (dir == null)
				continue;
			// System.out.println(dir);
			NpcClient npc = npcDir.get(dir.toLowerCase().trim());
			if (npc == null)
				continue;
			Integer npcId = DataManager.getInstance().getNpcNameIdMap().get(entity.getName());
			if (npcId == null)
				continue;

			if (entity.getPos() == null)
			{
				continue;
			}

			StringTokenizer st = new StringTokenizer(entity.getPos().trim().toLowerCase(), ",");
			if (st != null)
			{
				fw.write("('" + (id++) + "', '" + worldId + "', '" + npcId + "', '" + st.nextToken() + "', '" + st.nextToken() + "', '" + st.nextToken()
						+ "', 0," + entity.getEntityId() + "),\n");
			}
		}
	}

	private static void createDirMap()
	{
		for (NpcClient npc : DataManager.getInstance().getIdNpcMap().values())
		{
			if (npc.getDir() == null || npc.getMesh() == null)
				continue;
			String dir = "objects\\" + npc.getDir().replace("/", "\\") + "\\" + npc.getMesh() + ".cgf";
			// System.out.println(dir.toLowerCase().trim());
			npcDir.put(dir.toLowerCase().trim(), npc);
		}
	}

	private static Mission loadMissionFile(Integer worldId)
	{
		try
		{
			JAXBContext jc = JAXBContext.newInstance("parser.clientData.mission");
			Unmarshaller unmarshaller = jc.createUnmarshaller();

			Mission collection;
			collection = (Mission) unmarshaller.unmarshal(new File("xml/world/" + worldId + ".xml"));
			return collection;
		}
		catch (JAXBException e)
		{
			e.printStackTrace();
			return null;
		}
	}*/

}

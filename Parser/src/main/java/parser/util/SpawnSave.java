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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import parser.clientData.clientNpc.NpcClient;
import parser.clientData.clientWorldId.Data;
import parser.clientData.mission.EntityClass;
import parser.clientData.mission.Mission;
import parser.clientData.mission.ObjectClass;
import parser.clientData.mission.ObjectsClass;

/**
 * @author Mr. Poke
 *
 */
public class SpawnSave
{
	private static Map<String, NpcClient> npcDir = new HashMap<String, NpcClient>();
	private static int id = 1000;
	public static void save()
	{
		createDirMap();
		String out = "";
		for (Data world : DataManager.getInstance().getWorldIds())
		{
			System.out.println("Mission file: "+world.getId()+".xml");
			for (ObjectsClass object : loadMissionFile(world.getId()).getObjects())
			out+=parseSpawns(object, world.getId());
		}
		FileWriter fw;
		try
		{
			fw = new FileWriter("sql/spawn.sql", false);
			fw.write(out);
			fw.flush();
			fw.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	private static String parseSpawns(ObjectsClass objects, BigInteger bigInteger) {
		String sql = "INSERT INTO `spawn`(`id`, `world`,`templateId`,`x`,`y`,`z`,`heading`,`staticId`)VALUES\n";
			for (ObjectClass object : objects.getObject()) {
				if (object.getNpc() == null)
					continue;
				Integer npcId = DataManager.getInstance().getNpcNameIdMap().get(object.getNpc().toLowerCase());
				if (npcId == null)
				{
					npcId = DataManager.getInstance().getNameGatherebleIdMap().get(object.getNpc().toLowerCase());
					if (npcId == null)
					{
						continue;
					}
				}
				StringTokenizer st = new StringTokenizer(object.getPos().trim()
						.toLowerCase(), ",");
				if (st != null) {
					sql += "('"+(id++)+"', '"+bigInteger+"', '"+npcId+"', '"+st.nextToken()+"', '"+st.nextToken()+"', '"+st.nextToken()+"', 0,0),\n";
				}
			}
			for (EntityClass entity : objects.getEntity()) {
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

				if (entity.getPos() == null) {
					continue;
				}
				
				StringTokenizer st = new StringTokenizer(entity.getPos().trim()
						.toLowerCase(), ",");
				if (st != null)
				{
					sql += "('"+(id++)+"', '"+bigInteger+"', '"+npcId+"', '"+st.nextToken()+"', '"+st.nextToken()+"', '"+st.nextToken()+"', 0,"+entity.getEntityId()+"),\n";
				}
			}
			return sql;
	}

	private static void createDirMap() {
		for (NpcClient npc : DataManager.getInstance().getIdNpcMap().values())
		{
			if (npc.getDir() == null || npc.getMesh() == null)
				continue;
			String dir = "objects\\" + npc.getDir().replace("/", "\\") + "\\"
					+ npc.getMesh() + ".cgf";
			// System.out.println(dir.toLowerCase().trim());
			npcDir.put(dir.toLowerCase().trim(), npc);
		}
	}

	private static Mission loadMissionFile(BigInteger bigInteger)
	{
		try
		{
			JAXBContext jc = JAXBContext.newInstance("parser.clientData.mission");
			Unmarshaller unmarshaller = jc.createUnmarshaller();

			Mission collection;
			collection = (Mission) unmarshaller.unmarshal(new File("xml/world/"+bigInteger+".xml"));
			return collection;
		}
		catch (JAXBException e)
		{
			e.printStackTrace();
			return null;
		}
	}
}

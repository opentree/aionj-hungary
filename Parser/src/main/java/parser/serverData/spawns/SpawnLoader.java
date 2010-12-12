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
package parser.serverData.spawns;

import static org.apache.commons.io.filefilter.FileFilterUtils.andFileFilter;
import static org.apache.commons.io.filefilter.FileFilterUtils.makeSVNAware;
import static org.apache.commons.io.filefilter.FileFilterUtils.notFileFilter;
import static org.apache.commons.io.filefilter.FileFilterUtils.prefixFileFilter;
import static org.apache.commons.io.filefilter.FileFilterUtils.suffixFileFilter;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.HiddenFileFilter;
import org.apache.commons.io.filefilter.IOFileFilter;

/**
 * @author Mr. Poke
 *
 */
public class SpawnLoader
{
	public static Map<Integer, List<SpawnGroup>> load()
	{
		int spawnCount = 0;
		File dir = new File("xml/spawns");
		Collection<File> files = listFiles(dir, true);
		Map<Integer, List<SpawnGroup>> spawnGroups = new HashMap<Integer, List<SpawnGroup>>();
		try
		{
			for (File file : files)
			{
				System.out.println("SpawnFile: " + file.getPath());
				JAXBContext jc = JAXBContext.newInstance("parser.serverData.spawns");
				Unmarshaller unmarshaller = jc.createUnmarshaller();

				Spawns collection = (Spawns) unmarshaller.unmarshal(file);
				for (SpawnGroup spawnGroup: collection.getSpawn())
				{
					spawnCount+=spawnGroup.getObject().size();
					if (!spawnGroups.containsKey(spawnGroup.getMap()))
						spawnGroups.put(spawnGroup.getMap(), new ArrayList<SpawnGroup>());
					spawnGroups.get(spawnGroup.getMap()).add(spawnGroup);
					
				}
			}
				System.out.println("Size of spawns relations: " + spawnCount);
			return spawnGroups;
		}
		catch (JAXBException e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	private static Collection<File> listFiles(File root, boolean recursive)
	{
		IOFileFilter dirFilter = recursive ? makeSVNAware(HiddenFileFilter.VISIBLE) : null;

		return FileUtils.listFiles(root,
				andFileFilter(andFileFilter(notFileFilter(prefixFileFilter("new")), suffixFileFilter(".xml")), HiddenFileFilter.VISIBLE), dirFilter);
	}
}

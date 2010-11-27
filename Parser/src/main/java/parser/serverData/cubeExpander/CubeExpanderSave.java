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
package parser.serverData.cubeExpander;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Collection;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;

import parser.clientData.clientNpc.NpcClient;
import parser.util.DataManager;

/**
 * @author Mr. Poke
 *
 */
public class CubeExpanderSave
{
	public static void save()
	{
		ObjectFactory objFactory = new ObjectFactory();
		CubeExpander collection = (CubeExpander) objFactory.createCubeExpander();
		List<CubeNpcTemplate> templateList = collection.getCubeNpc();

		int[] price =
		{ 1000, 12000, 80000, 180000, 360000 };

		Collection<NpcClient> npcs = DataManager.getInstance().getIdNpcMap().values();
		for (NpcClient npc : npcs)
		{
			if (npc.getExtendinventoryStart() != 0)
			{
				CubeNpcTemplate template = new CubeNpcTemplate();
				template.setId(npc.getId());
				for (int i = npc.getExtendinventoryStart(); i <= npc.getExtendinventoryEnd(); i++)
				{
					CubeExpand expand = new CubeExpand();
					expand.setLevel(i);
					expand.setPrice(price[i - 1]);
					template.getExpand().add(expand);
				}
				templateList.add(template);
			}

		}

		try
		{
			JAXBContext jaxbContext = JAXBContext.newInstance("parser.serverData.cubeExpander");
			Marshaller marshaller = jaxbContext.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, new Boolean(true));
			marshaller.marshal(collection, new FileOutputStream("static_data/cube_expander/cube_expander.xml"));
		}
		catch (PropertyException e)
		{
			e.printStackTrace();
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (JAXBException e)
		{
			e.printStackTrace();
		}
	}
}

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
package parser.serverData.objectInfos;

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
public class ObjectInfosSave
{
	public static void save()
	{
		ObjectFactory objFactory = new ObjectFactory();
		ObjectInfos collection = (ObjectInfos) objFactory.createObjectInfos();
		List<NpcInfo> templateList = collection.getNpcInfo();

		Collection<NpcClient> npcs = DataManager.getInstance().getIdNpcMap().values();

		for (NpcClient npc : npcs)
		{
			NpcInfo template = new NpcInfo();
			if (npc.getUiType() != null && npc.getUiType().equalsIgnoreCase("Binding_Stone"))
			{
				template.setClassName("BindPoint");
				template.setKnowlistName("StaticObjectKnowlist");
			}
			else if (npc.getUiType() != null && npc.getUiType().equalsIgnoreCase("Trap"))
			{
				template.setClassName("Trap");
			}
			else if (npc.getUiType() != null && npc.getUiType().equalsIgnoreCase("summoned"))
			{
				template.setClassName("Summon");
			}
			else if (npc.getBindstoneType() != null)
			{
				template.setClassName("Kisk");
			}
			else if (npc.getCursorType() != null && npc.getCursorType().equalsIgnoreCase("trade"))
			{
				template.setClassName("DialogNpc");
			}
			template.setId(npc.getId());
			templateList.add(template);
		}

		try
		{
			JAXBContext jaxbContext = JAXBContext.newInstance("parser.serverData.objectInfos");
			Marshaller marshaller = jaxbContext.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, new Boolean(true));
			marshaller.marshal(collection, new FileOutputStream("static_data/object_infos/object_infos.xml"));
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

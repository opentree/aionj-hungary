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
package parser.clientData.clientGather;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

/**
 * @author Mr. Poke
 *
 */
public class GatherSrcLoader
{

	public static Map<Integer, GatherSrc> load()
	{
		try
		{
			JAXBContext jc = JAXBContext.newInstance("parser.clientData.clientGather");
			Unmarshaller unmarshaller = jc.createUnmarshaller();

			GatherSrcs collection;
			collection = (GatherSrcs) unmarshaller.unmarshal(new File("xml/gather_src.xml"));
			System.out.println("Size of gather: " + collection.getGatherSrc().size());
			Map<Integer, GatherSrc> map = new HashMap<Integer, GatherSrc>();
			for (GatherSrc item : collection.getGatherSrc())
			{
				map.put(item.getId(), item);
			}
			return map;
		}
		catch (JAXBException e)
		{
			e.printStackTrace();
		}
		return null;
	}
}

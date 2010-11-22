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
package parser.clientData.clientWorldId;

import java.io.File;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

/**
 * @author Mr. Poke
 *
 */
public class WorldIdLoader
{
	public static List<Data> load()
	{
		try
		{
			JAXBContext jc = JAXBContext.newInstance("parser.clientData.clientWorldId");
			Unmarshaller unmarshaller = jc.createUnmarshaller();

			WorldId collection;
			collection = (WorldId) unmarshaller.unmarshal(new File("xml/WorldId.xml"));
			System.out.println("Size of WorldId relations: " + collection.getData().size());
			return collection.getData();
		}
		catch (JAXBException e)
		{
			e.printStackTrace();
		}
		return null;
	}
}

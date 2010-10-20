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
package parser.serverData.tribeRelation;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;
import java.util.StringTokenizer;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;

import parser.util.DataManager;

/**
 * @author Mr. Poke
 *
 */
public class TribeRelationSave
{
	public static void save ()
	{
		ObjectFactory objFactory = new ObjectFactory();

		TribeRelations collection = (TribeRelations) objFactory.createTribeRelations();
		List<Tribe> templateList = collection.getTribe();

		for (parser.clientData.tribeRelation.Tribe clientTribe : DataManager.getInstance().getTribeRelations())
		{
			Tribe tribe = new Tribe();
			
			tribe.setName(clientTribe.getTribe().toUpperCase());
			
			if (clientTribe.getBaseTribe() != null)
				tribe.setBase(clientTribe.getBaseTribe().toUpperCase());
			
			if (clientTribe.getAggressive() != null)
			{
				Aggro aggro = new Aggro();

				StringTokenizer st = new StringTokenizer(clientTribe.getAggressive(), ",");
				if (st != null)
				{
					while (st.hasMoreTokens())
						aggro.getTo().add(st.nextToken().trim().toUpperCase());
				}
				tribe.setAggro(aggro);
			}
			
			if (clientTribe.getFriendly() != null)
			{
				Friend friend = new Friend();

				StringTokenizer st = new StringTokenizer(clientTribe.getFriendly(), ",");
				if (st != null)
				{
					while (st.hasMoreTokens())
						friend.getTo().add(st.nextToken().trim().toUpperCase());
				}
				tribe.setFriend(friend);
			}

			if (clientTribe.getHostile() != null)
			{
				Hostile hostile = new Hostile();

				StringTokenizer st = new StringTokenizer(clientTribe.getHostile(), ",");
				if (st != null)
				{
					while (st.hasMoreTokens())
						hostile.getTo().add(st.nextToken().trim().toUpperCase());
				}
				tribe.setHostile(hostile);
			}
			if (clientTribe.getSupport() != null)
			{
				Support support = new Support();

				StringTokenizer st = new StringTokenizer(clientTribe.getSupport(), ",");
				if (st != null)
				{
					while (st.hasMoreTokens())
						support.getTo().add(st.nextToken().trim().toUpperCase());
				}
				tribe.setSupport(support);
			}
			if (clientTribe.getNeutral() != null)
			{
				Neutral neutral = new Neutral();

				StringTokenizer st = new StringTokenizer(clientTribe.getNeutral(), ",");
				if (st != null)
				{
					while (st.hasMoreTokens())
						neutral.getTo().add(st.nextToken().trim().toUpperCase());
				}
				tribe.setNeutral(neutral);
			}
			templateList.add(tribe);
		}
		
		try
		{
			JAXBContext jaxbContext = JAXBContext.newInstance("parser.serverData.tribeRelation");
			Marshaller marshaller = jaxbContext.createMarshaller();
				marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,
						new Boolean(true));
			marshaller.marshal(collection, new FileOutputStream("static_data/tribe/tribe_relations.xml"));
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

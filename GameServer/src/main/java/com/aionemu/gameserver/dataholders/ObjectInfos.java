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

package com.aionemu.gameserver.dataholders;

import gnu.trove.TIntObjectHashMap;

import java.util.List;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.aionemu.gameserver.model.templates.NpcInfo;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "object_infos")
public class ObjectInfos
{
	@XmlElement(name = "npc_info")
	protected List<NpcInfo>				npcInfo;

	/** A map containing all npc templates */
	private TIntObjectHashMap<NpcInfo>	npcData	= new TIntObjectHashMap<NpcInfo>();

	void afterUnmarshal(Unmarshaller u, Object parent)
	{
		for (NpcInfo npc : npcInfo)
		{
			npcData.put(npc.getId(), npc);
		}
		npcInfo.clear();
		npcInfo = null;
	}

	public NpcInfo getNpcInfoByTemplateId(int templateId)
	{
		return npcData.get(templateId);
	}

	public int size()
	{
		return npcData.size();
	}
}

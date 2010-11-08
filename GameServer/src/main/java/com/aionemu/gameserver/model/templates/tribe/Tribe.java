/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.aionemu.gameserver.model.templates.tribe;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlList;
import javax.xml.bind.annotation.XmlType;

import com.aionemu.gameserver.model.TribeClass;

/**
 * @author Mr. Poke
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Tribe", propOrder ={ 
		"aggro",
		"hostile",
		"friend",
		"neutral",
		"support" })
public class Tribe
{

	private static List<TribeClass> EMPTY_TRIBE_LIST = new ArrayList<TribeClass>();

	@XmlList
	protected List<TribeClass>	aggro;
	@XmlList
	protected List<TribeClass>	hostile;
	@XmlList
	protected List<TribeClass>	friend;
	@XmlList
	protected List<TribeClass>	neutral;
	@XmlList
	protected List<TribeClass>	support;
	@XmlAttribute
	protected TribeClass		base;
	@XmlAttribute(required = true)
	protected TribeClass		name;

	public List<TribeClass> getAggro()
	{
		if (aggro == null)
		{
			aggro = EMPTY_TRIBE_LIST;
		}
		return this.aggro;
	}

	public List<TribeClass> getHostile()
	{
		if (hostile == null)
		{
			hostile = EMPTY_TRIBE_LIST;
		}
		return this.hostile;
	}

	public List<TribeClass> getFriend()
	{
		if (friend == null)
		{
			friend = EMPTY_TRIBE_LIST;
		}
		return this.friend;
	}

	public List<TribeClass> getNeutral()
	{
		if (neutral == null)
		{
			neutral = EMPTY_TRIBE_LIST;
		}
		return this.neutral;
	}

	public List<TribeClass> getSupport()
	{
		if (support == null)
		{
			support = EMPTY_TRIBE_LIST;
		}
		return this.support;
	}

	public TribeClass getBase()
	{
		return base;
	}

	public TribeClass getName()
	{
		return name;
	}
}

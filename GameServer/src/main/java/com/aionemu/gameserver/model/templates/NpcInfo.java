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

package com.aionemu.gameserver.model.templates;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * @author Mr. Poke
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "NpcInfo")
public class NpcInfo
{

	@XmlAttribute
	protected Integer	id;
	@XmlAttribute(name = "class_name")
	protected String	className;
	@XmlAttribute(name = "knowlist_name")
	protected String	knowlistName;
	@XmlAttribute(name = "ai_name")
	protected String	aiName;
	@XmlAttribute(name = "random_walking")
	protected String	randomWalking;

	/**
	 * Gets the value of the id property.
	 * 
	 * @return
	 *     possible object is
	 *     {@link Integer }
	 *     
	 */
	public Integer getId()
	{
		return id;
	}

	/**
	 * Gets the value of the className property.
	 * 
	 * @return
	 *     possible object is
	 *     {@link String }
	 *     
	 */
	public String getClassName()
	{
		if (className == null)
		{
			return "StaticNpc";
		}
		else
		{
			return className;
		}
	}

	/**
	 * Gets the value of the knowlistName property.
	 * 
	 * @return
	 *     possible object is
	 *     {@link String }
	 *     
	 */
	public String getKnowlistName()
	{
		if (knowlistName == null)
		{
			return "KnownList";
		}
		else
		{
			return knowlistName;
		}
	}

	/**
	 * Gets the value of the aiName property.
	 * 
	 * @return
	 *     possible object is
	 *     {@link String }
	 *     
	 */
	public String getAiName()
	{
		if (aiName == null)
		{
			return "NoActionAI";
		}
		else
		{
			return aiName;
		}
	}

	/**
	 * Gets the value of the randomWalking property.
	 * 
	 * @return
	 *     possible object is
	 *     {@link String }
	 *     
	 */
	public String getRandomWalking()
	{
		if (randomWalking == null)
		{
			return "false";
		}
		else
		{
			return randomWalking;
		}
	}

}

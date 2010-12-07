/*
 * This file is part of aion-unique <aion-unique.org>.
 *
 *  aion-unique is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  aion-unique is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with aion-unique.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.aionemu.gameserver.model.gameobjects.instance;

import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.knownList.StaticObjectKnownList;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.spawn.SpawnTemplate;
import com.aionemu.gameserver.network.aion.serverpackets.SM_GATHERABLE_INFO;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * @author ATracer
 *
 */
public class SpawnedItem extends VisibleObject
{
	public SpawnedItem(int objectId, SpawnTemplate spawnTemplate)
	{
		super(objectId, spawnTemplate);
		objectTemplate = DataManager.ITEM_DATA.getItemTemplate(spawnTemplate.getTemplateId());
		this.setKnownlist(new StaticObjectKnownList(this));
	}

	@Override
	public String getName()
	{
		return objectTemplate.getName();
	}

	@Override
	public void see(VisibleObject object)
	{
		if (object instanceof Player)
			PacketSendUtility.sendPacket((Player) object, new SM_GATHERABLE_INFO(this));
	}
}

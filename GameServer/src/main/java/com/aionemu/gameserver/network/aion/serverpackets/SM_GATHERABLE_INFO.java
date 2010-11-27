/*
 * This file is part of aion-emu <aion-unique.com>.
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
package com.aionemu.gameserver.network.aion.serverpackets;

import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.network.aion.AbstractAionServerPacket;
import com.aionemu.gameserver.network.aion.AionChannelHandler;

/**
 * @author Lyahim, ATracer
 *
 */
public class SM_GATHERABLE_INFO extends AbstractAionServerPacket<AionChannelHandler>
{
	private VisibleObject	visibleObject;

	public SM_GATHERABLE_INFO(VisibleObject visibleObject)
	{
		super();
		this.visibleObject = visibleObject;
	}

	@Override
	public void writeImpl(AionChannelHandler cHandler)
	{
		writeF(visibleObject.getX());
		writeF(visibleObject.getY());
		writeF(visibleObject.getZ());
		writeD(visibleObject.getObjectId());
		writeD(visibleObject.getSpawn().getStaticid()); //unk
		writeD(visibleObject.getObjectTemplate().getTemplateId());
		writeH(1); //unk
		writeC(0);
		writeD(visibleObject.getObjectTemplate().getNameId());
		writeH(0);
		writeH(0);
		writeH(0);
		writeC(100); //unk
	}
}

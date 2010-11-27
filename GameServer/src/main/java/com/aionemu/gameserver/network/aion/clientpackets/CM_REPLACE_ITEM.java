/*
 * This file is part of aion-unique <aion-unique.com>.
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

package com.aionemu.gameserver.network.aion.clientpackets;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.commons.network.netty.packet.AbstractClientPacket;
import com.aionemu.gameserver.network.aion.AionChannelHandler;
import com.aionemu.gameserver.services.ItemService;

/**
 *
 * @author Lyahim, kosyachok
 */
public class CM_REPLACE_ITEM extends AbstractClientPacket<AionChannelHandler>
{

	private int	sourceStorageType;
	private int	sourceItemObjId;
	private int	replaceStorageType;
	private int	replaceItemObjId;

	public CM_REPLACE_ITEM(int opcode)
	{
		super(opcode);
	}

	@Override
	protected void readImpl()
	{
		sourceStorageType = readC();
		sourceItemObjId = readD();
		replaceStorageType = readC();
		replaceItemObjId = readD();
	}

	@Override
	protected void runImpl()
	{
		Player player = getChannelHandler().getActivePlayer();
		ItemService.switchStoragesItems(player, sourceStorageType, sourceItemObjId, replaceStorageType, replaceItemObjId);
	}

}

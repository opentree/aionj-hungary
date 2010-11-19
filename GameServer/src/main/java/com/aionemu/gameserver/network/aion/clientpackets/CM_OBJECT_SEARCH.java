/*
 * This file is part of aion-emu <aion-emu.com>.
 *
 * aion-emu is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * aion-emu is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with aion-emu.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.aionemu.gameserver.network.aion.clientpackets;


import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.commons.network.netty.packet.AbstractClientPacket;
import com.aionemu.gameserver.network.aion.AionChannelHandler;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SHOW_NPC_ON_MAP;
import com.aionemu.gameserver.newmodel.templates.spawn.SpawnTemplate;

/**
 * @author Lyahim, Lyahim
 */
public class CM_OBJECT_SEARCH extends AbstractClientPacket<AionChannelHandler>
{
	private int npcId;
	/**
	 * Constructs new client packet instance.
	 * @param opcode
	 */
	public CM_OBJECT_SEARCH(int opcode)
	{
		super(opcode);

	}

	/**
	 * Nothing to do
	 */
	@Override
	protected void readImpl()
	{
		this.npcId = readD();
	}

	/**
	 * Logging
	 */
	@Override
	protected void runImpl()
	{	
		SpawnTemplate spawnTemplate = DataManager.SPAWNS_DATA.getFirstSpawnByNpcId(npcId);
		if(spawnTemplate != null)
		{
			sendPacket(new SM_SHOW_NPC_ON_MAP(npcId, spawnTemplate.getWorldId(), spawnTemplate.getX(), 
				spawnTemplate.getY(), spawnTemplate.getZ()));
		}
	}
}

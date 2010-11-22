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
package com.aionemu.gameserver.model.gameobjects.instance;

import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.spawn.SpawnTemplate;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;

/**
 * @author Mr. Poke
 *
 */
public class Postman extends StaticNpc
{

	private int RecipientId;	//ID of the express mail recipient, to prevent messenger using by non-recipient players
	/**
	 * @param objId
	 * @param spawnTemplate
	 * @param position
	 */
	public Postman(int objId, SpawnTemplate spawnTemplate, int PlayerId)
	{
		super(objId, spawnTemplate);
		this.objectTemplate = DataManager.NPC_DATA.getNpcTemplate(798044);
		this.RecipientId = PlayerId;
		ThreadPoolManager.getInstance().schedule(new Runnable(){

			@Override
			public void run()
			{
					onDelete();
			}
		},5*60*1000); //Despawn postman after 5 minutes, need to place retail value.
	}

	@Override
	public void onDialogRequest(Player player)
	{
		if ( player.getObjectId() == RecipientId)
		{
			PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 18));
		}
		else
			return;
	}
}

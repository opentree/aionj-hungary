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

import org.apache.log4j.Logger;

import com.aionemu.gameserver.configs.main.CustomConfig;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.ChatType;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.interfaces.IDialogRequest;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.RequestResponseHandler;
import com.aionemu.gameserver.model.templates.BindPointTemplate;
import com.aionemu.gameserver.model.templates.spawn.SpawnTemplate;
import com.aionemu.gameserver.network.aion.serverpackets.SM_LEVEL_UPDATE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_MESSAGE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_QUESTION_WINDOW;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.ItemService;
import com.aionemu.gameserver.services.TeleportService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.WorldType;

/**
 * @author Mr. Poke, Lyahim
 *
 */
public class BindPoint extends StaticNpc implements IDialogRequest
{

	private static Logger			log	= Logger.getLogger(BindPoint.class);

	private final BindPointTemplate	bindPointTemplate;

	/**
	 * @param objId
	 * @param spawnTemplate
	 * @param position
	 */
	public BindPoint(int objId, SpawnTemplate spawnTemplate)
	{
		super(objId, spawnTemplate);
		bindPointTemplate = DataManager.BIND_POINT_DATA.getBindPointTemplate(spawnTemplate.getTemplateId());
	}

	/* (non-Javadoc)
	 * @see com.aionemu.gameserver.model.gameobjects.interfaces.IDialog#onDialogRequest(com.aionemu.gameserver.model.gameobjects.player.Player)
	 */
	@Override
	public void onDialogRequest(Player player)
	{

		if (bindPointTemplate == null)
		{
			log.info("There is no bind point template for npc: " + this.getObjectTemplate().getTemplateId());
			return;
		}

		if (player.getCommonData().getBindPoint() == bindPointTemplate.getBindId())
		{
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_ALREADY_REGISTER_THIS_RESURRECT_POINT());
			return;
		}

		WorldType worldType = World.getInstance().getWorldMap(player.getWorldId()).getWorldType();
		if (!CustomConfig.ENABLE_CROSS_FACTION_BINDING)
		{
			if (worldType == WorldType.ASMODAE && player.getCommonData().getRace() == Race.ELYOS)
			{
				PacketSendUtility.sendPacket(player, new SM_MESSAGE(0, null, "Elyos cannot bind in Asmodian territory.", ChatType.ANNOUNCEMENTS));
				return;

			}
			if (worldType == WorldType.ELYSEA && player.getCommonData().getRace() == Race.ASMODIANS)
			{
				PacketSendUtility.sendPacket(player, new SM_MESSAGE(0, null, "Asmodians cannot bind in Elyos territory.", ChatType.ANNOUNCEMENTS));
				return;
			}
			if (worldType == WorldType.ABYSS)
			{
				if (player.getCommonData().getRace() == Race.ELYOS && player.getTarget().getObjectTemplate().getTemplateId() == 700401)
				{
					PacketSendUtility.sendPacket(player, new SM_MESSAGE(0, null, "Elyos cannot bind in Asmodian territory.", ChatType.ANNOUNCEMENTS));
					return;
				}
				if (player.getCommonData().getRace() == Race.ASMODIANS && player.getTarget().getObjectTemplate().getTemplateId() == 730071)
				{
					PacketSendUtility.sendPacket(player, new SM_MESSAGE(0, null, "Asmodians cannot bind in Elyos territory.", ChatType.ANNOUNCEMENTS));
					return;
				}
			}
		}
		if (worldType == WorldType.PRISON)
		{
			PacketSendUtility.sendPacket(player, new SM_MESSAGE(0, null, "You cannot bind here.", ChatType.ANNOUNCEMENTS));
			return;
		}
		else
		{
			bindHere(player);
		}
	}

	private void bindHere(Player player)
	{
		RequestResponseHandler responseHandler = new RequestResponseHandler(this)
		{
			@Override
			public void acceptRequest(StaticNpc requester, Player responder)
			{
				if (responder.getCommonData().getBindPoint() != bindPointTemplate.getBindId())
				{
					if (ItemService.decreaseKinah(responder, bindPointTemplate.getPrice()))
					{
						responder.getCommonData().setBindPoint(bindPointTemplate.getBindId());
						TeleportService.sendSetBindPoint(responder);
						PacketSendUtility.broadcastPacket(responder, new SM_LEVEL_UPDATE(responder.getObjectId(), 2, responder.getCommonData().getLevel()),
								true);
						PacketSendUtility.sendPacket(responder, SM_SYSTEM_MESSAGE.STR_DEATH_REGISTER_RESURRECT_POINT());
					}
					else
					{
						PacketSendUtility.sendPacket(responder, SM_SYSTEM_MESSAGE.STR_CANNOT_REGISTER_RESURRECT_POINT_NOT_ENOUGH_FEE());
						return;
					}
				}
			}

			@Override
			public void denyRequest(StaticNpc requester, Player responder)
			{
				//do nothing
			}
		};

		boolean requested = player.getResponseRequester().putRequest(SM_QUESTION_WINDOW.STR_BIND_TO_LOCATION, responseHandler);
		if (requested)
		{
			String price = Integer.toString(bindPointTemplate.getPrice());
			PacketSendUtility.sendPacket(player, new SM_QUESTION_WINDOW(SM_QUESTION_WINDOW.STR_BIND_TO_LOCATION, 0, price));
		}
	}
}

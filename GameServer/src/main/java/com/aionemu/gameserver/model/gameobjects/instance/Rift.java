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

import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.interfaces.IDialogRequest;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.RequestResponseHandler;
import com.aionemu.gameserver.model.templates.spawn.SpawnTemplate;
import com.aionemu.gameserver.network.aion.serverpackets.SM_QUESTION_WINDOW;
import com.aionemu.gameserver.network.aion.serverpackets.SM_RIFT_ANNOUNCE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_RIFT_STATUS;
import com.aionemu.gameserver.services.RespawnService;
import com.aionemu.gameserver.services.TeleportService;
import com.aionemu.gameserver.spawnengine.RiftSpawnManager.RiftEnum;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.WorldMapInstance;

/**
 * @author Mr. Poke
 *
 */
public class Rift extends StaticNpc implements IDialogRequest
{

	private boolean			isMaster	= false;
	private SpawnTemplate	slaveSpawnTemplate;
	private Rift			slave;

	private Integer			maxEntries;
	private Integer			maxLevel;

	private int				usedEntries;
	private boolean			isAccepting;

	private final RiftEnum	riftTemplate;

	/**
	 * @param objId
	 * @param spawnTemplate
	 * @param position
	 */
	public Rift(int objId, SpawnTemplate spawnTemplate, Rift slave, RiftEnum riftTemplate)
	{
		super(objId, spawnTemplate);
		this.riftTemplate = riftTemplate;
		if (slave != null)//master rift should be created
		{
			this.slave = slave;
			this.slaveSpawnTemplate = slave.getSpawn();
			this.maxEntries = riftTemplate.getEntries();
			this.maxLevel = riftTemplate.getMaxLevel();

			isMaster = true;
			isAccepting = true;
		}
	}

	@Override
	public void onDialogRequest(Player player)
	{
		if (!isMaster && !isAccepting)
			return;

		final Rift rift = this;
		RequestResponseHandler responseHandler = new RequestResponseHandler(this)
		{
			@Override
			public void acceptRequest(StaticNpc requester, Player responder)
			{
				if (!isAccepting)
					return;

				int worldId = slaveSpawnTemplate.getMapId();
				float x = slaveSpawnTemplate.getX();
				float y = slaveSpawnTemplate.getY();
				float z = slaveSpawnTemplate.getZ();

				TeleportService.teleportTo(responder, worldId, x, y, z, 0);
				usedEntries++;

				if (usedEntries >= maxEntries)
				{
					isAccepting = false;

					RespawnService.scheduleDecayTask(rift);
					RespawnService.scheduleDecayTask(slave);
				}
				PacketSendUtility.broadcastPacket(rift, new SM_RIFT_STATUS(getObjectId(), usedEntries, maxEntries, maxLevel));

			}

			@Override
			public void denyRequest(StaticNpc requester, Player responder)
			{
				//do nothing
			}
		};

		boolean requested = player.getResponseRequester().putRequest(SM_QUESTION_WINDOW.STR_USE_RIFT, responseHandler);
		if (requested)
		{
			PacketSendUtility.sendPacket(player, new SM_QUESTION_WINDOW(SM_QUESTION_WINDOW.STR_USE_RIFT, 0));
		}
	}

	@Override
	public void see(VisibleObject object)
	{
		super.see(object);
		if (!isMaster)
			return;

		if (object instanceof Player)
		{
			PacketSendUtility.sendPacket((Player) object, new SM_RIFT_STATUS(getObjectId(), usedEntries, maxEntries, maxLevel));
		}
	}

	/**
	 * @param activePlayer
	 */
	public void sendMessage(Player activePlayer)
	{
		if (isMaster && isSpawned())
			PacketSendUtility.sendPacket(activePlayer, new SM_RIFT_ANNOUNCE(riftTemplate.getDestination()));
	}

	/**
	 * 
	 */
	public void sendAnnounce()
	{
		if (isMaster && isSpawned())
		{
			WorldMapInstance worldInstance = getPosition().getMapRegion().getParent();
			for (Player player : worldInstance.getAllWorldMapPlayers())
			{
				if (player.isSpawned())
					sendMessage(player);
			}
		}
	}
}

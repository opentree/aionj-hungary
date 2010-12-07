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

import com.aionemu.gameserver.model.gameobjects.interfaces.IDialogRequest;
import com.aionemu.gameserver.model.gameobjects.interfaces.IReward;
import com.aionemu.gameserver.model.gameobjects.player.Player;

/**
 * @author Mr. Poke, Lyahim
 *
 */
public class ActionItem /*extends StaticNpc*/implements IReward, IDialogRequest
{

	/* (non-Javadoc)
	 * @see com.aionemu.gameserver.model.gameobjects.interfaces.IDialogRequest#onDialogRequest(com.aionemu.gameserver.model.gameobjects.player.Player)
	 */
	@Override
	public void onDialogRequest(Player player)
	{
	}

	/* (non-Javadoc)
	 * @see com.aionemu.gameserver.model.gameobjects.interfaces.IReward#doReward()
	 */
	@Override
	public void doReward()
	{
	}
	//
	//	/**
	//	 * @param objId
	//	 * @param spawnTemplate
	//	 */
	//	public ActionItem(int objId, SpawnTemplate spawnTemplate)
	//	{
	//		super(objId, spawnTemplate);
	//	}
	//
	//	private Player	lastActor	= null;
	//
	//	/**
	//	 * 0 - clear object
	//	 * 1 - use object
	//	 * 3 - convert object
	//	 */
	//	@Override
	//	public void onDialogRequest(final Player player)
	//	{
	//		if (!QuestEngine.getInstance().onDialog(new QuestEnv(this, player, 0, -1)))
	//			return;
	//		final int defaultUseTime = 3000;
	//		PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), getObjectId(), defaultUseTime, 1));
	//		PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.START_QUESTLOOT, 0, getObjectId()), true);
	//		ThreadPoolManager.getInstance().schedule(new Runnable()
	//		{
	//			@Override
	//			public void run()
	//			{
	//				PacketSendUtility.sendPacket(player, new SM_USE_OBJECT(player.getObjectId(), getObjectId(), defaultUseTime, 0));
	//				setTarget(player);
	//				lastActor = player;
	//				onDie(player);
	//			}
	//		}, defaultUseTime);
	//	}
	//
	//	@Override
	//	public void doReward()
	//	{
	//		if (lastActor == null)
	//			return;
	//
	//		DropService.getInstance().registerDrop(this, lastActor, lastActor.getLevel());
	//		DropService.getInstance().requestDropList(lastActor, this.getObjectId());
	//
	//		lastActor = null;
	//	}
	//
	//	@Override
	//	public void onRespawn()
	//	{
	//		super.onRespawn();
	//		DropService.getInstance().unregisterDrop(this);
	//	}
	//
	//	@Override
	//	public void onDie(Creature lastAttacker)
	//	{
	//		super.onDie(lastAttacker);
	//
	//		addTask(TaskId.DECAY, RespawnService.scheduleDecayTask(this));
	//		scheduleRespawn();
	//
	//		// deselect target at the end
	//		setTarget(null);
	//		PacketSendUtility.broadcastPacket(this, new SM_LOOKATOBJECT(this));
	//	}
}

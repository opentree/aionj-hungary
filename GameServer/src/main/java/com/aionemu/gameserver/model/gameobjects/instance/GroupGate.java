/* 
 * This file is part of the requirements for the Illusion Gate Skill. 
 * Code References from ATracer's Trap.java of Aion-Unique 
 */
package com.aionemu.gameserver.model.gameobjects.instance;

import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.interfaces.IDialogRequest;
import com.aionemu.gameserver.model.gameobjects.interfaces.ISummoned;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.RequestResponseHandler;
import com.aionemu.gameserver.model.templates.spawn.SpawnTemplate;
import com.aionemu.gameserver.network.aion.serverpackets.SM_QUESTION_WINDOW;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.TeleportService;
import com.aionemu.gameserver.utils.PacketSendUtility;

/** 
 * @author LokiReborn 
 * 
 */
public class GroupGate extends Npc implements ISummoned, IDialogRequest
{

	/** 
	 * Creator of this GroupGate. 
	 */
	private Creature	creator;

	/** 
	 *  
	 * @param objId 
	 * @param controller 
	 * @param spawnTemplate 
	 * @param objectTemplate 
	 */
	public GroupGate(int objId, SpawnTemplate spawnTemplate)
	{
		super(objId, spawnTemplate);
	}

	/*	@Override
		public boolean isEnemyNpc(Npc visibleObject)
		{
			return this.creator.isEnemyNpc(visibleObject);
		}

		@Override
		public boolean isEnemyPlayer(Player visibleObject)
		{
			return this.creator.isEnemyPlayer(visibleObject);
		}*/

	@Override
	public Creature getMaster()
	{
		return this.creator;
	}

	/* (non-Javadoc)
	 * @see com.aionemu.gameserver.model.gameobjects.interfaces.ISummoned#setMaster(com.aionemu.gameserver.model.gameobjects.Creature)
	 */
	@Override
	public void setMaster(Creature master)
	{
		this.creator = master;
	}

	@Override
	public void onDialogRequest(Player player)
	{
		final GroupGate groupgate = this;
		boolean isMember = false;

		if (player.getObjectId() == ((Player) groupgate.getMaster()).getObjectId())
			isMember = true;

		if (player.isInGroup())
		{
			for (Player member : player.getPlayerGroup().getMembers())
			{
				if (member.getObjectId() == ((Player) groupgate.getMaster()).getObjectId())
				{
					isMember = true;
					break;
				}
			}
		}

		if (isMember)
		{
			RequestResponseHandler responseHandler = new RequestResponseHandler(groupgate)
			{

				@Override
				public void acceptRequest(StaticNpc requester, Player responder)
				{
					switch (groupgate.getNpcId())
					{
						case 749017:
							TeleportService.teleportTo(responder, 110010000, 1, 1444.9f, 1577.2f, 572.9f, 0);
							break;
						case 749083:
							TeleportService.teleportTo(responder, 120010000, 1, 1657.5f, 1398.7f, 194.7f, 0);
							break;
					}
				}

				@Override
				public void denyRequest(StaticNpc requester, Player responder)
				{
					// Nothing Happens 
				}
			};

			boolean requested = player.getResponseRequester().putRequest(SM_QUESTION_WINDOW.STR_ASK_GROUP_GATE_DO_YOU_ACCEPT_MOVE, responseHandler);
			if (requested)
			{
				PacketSendUtility.sendPacket(player, new SM_QUESTION_WINDOW(SM_QUESTION_WINDOW.STR_ASK_GROUP_GATE_DO_YOU_ACCEPT_MOVE, player.getObjectId()));
			}
		}
		else
		{
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_CANNOT_USE_MAGIC_PASSAGE);
		}
	}
}

/*
 * This file is part of aion-lightning <aion-lightning.com>.
 *
 *  aion-lightning is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  aion-lightning is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with aion-lightning.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.aionemu.gameserver.model.gameobjects.instance;

import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.TaskId;
import com.aionemu.gameserver.model.TribeClass;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.state.CreatureState;
import com.aionemu.gameserver.model.siege.SiegeRace;
import com.aionemu.gameserver.model.templates.NpcTemplate;
import com.aionemu.gameserver.model.templates.spawn.SpawnTemplate;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_LOOKATOBJECT;
import com.aionemu.gameserver.services.RespawnService;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * @author ViAl
 * @Modified antness
 *
 */
public class SiegeMine extends SiegeNpc
{

	/**
	 * @param objId
	 * @param controller
	 * @param spawnTemplate
	 */
	public SiegeMine(int objId, SpawnTemplate spawnTemplate)
	{
		super(objId, spawnTemplate);
		NpcTemplate npcTemplate = (NpcTemplate) objectTemplate;
		if (getSiegeRace() == SiegeRace.ELYOS)
		{
			npcTemplate.setRace(Race.ELYOS);
			npcTemplate.setTribe(TribeClass.GUARD);
		}
		else if (getSiegeRace() == SiegeRace.ASMODIANS)
		{
			npcTemplate.setRace(Race.ASMODIANS);
			npcTemplate.setTribe(TribeClass.GUARD_DARK);
		}
		else
		// BALAUR
		{
			npcTemplate.setRace(Race.DRAKAN);
			npcTemplate.setTribe(TribeClass.AGGRESSIVESINGLEMONSTER);
		}
	}

	public void see(VisibleObject object)
	{
		if (object instanceof Player)
		{
			if (((Player) object).getCommonData().getRace().getRaceId() != getObjectTemplate().getRace().getRaceId())
			{
				//super.useSkill(owner.getNpcSkillList().getNpcSkills().get(0).getSkillid()); //all mines have only one skill
				useSkill(18406);
				setCasting(null);
				getEffectController().removeAllEffects();
				setState(CreatureState.DEAD);
				addTask(TaskId.DECAY, RespawnService.scheduleDecayTask(this));
				scheduleRespawn();
				PacketSendUtility.broadcastPacket(this, new SM_EMOTION(this, EmotionType.DIE, 0, object == null ? 0 : object.getObjectId()));
				setTarget(null);
				PacketSendUtility.broadcastPacket(this, new SM_LOOKATOBJECT(this));
			}
		}
	}
}

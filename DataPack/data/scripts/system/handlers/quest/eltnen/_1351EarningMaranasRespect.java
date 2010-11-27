/*
 * This file is part of aion-unique <aion-unique.org>.
 *
 * aion-unique is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * aion-unique is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with aion-unique.  If not, see <http://www.gnu.org/licenses/>.
 */
package system.handlers.quest.eltnen;

import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.ItemService;

/**
 * @author Atomics
 */
public class _1351EarningMaranasRespect extends QuestHandler {
	private final static int questId = 1351;

	public _1351EarningMaranasRespect() {
		super(questId);
	}

	@Override
	public void register() {
		qe.setNpcQuestData(203965).addOnQuestStart(questId); // Castor
		qe.setNpcQuestData(203965).addOnTalkEvent(questId); // Castor
		qe.setNpcQuestData(203983).addOnTalkEvent(questId); // Marana
	}

	@Override
	public boolean onDialogEvent(QuestEnv env) {
		final Player player = env.getPlayer();
		int targetId = 0;
		if (env.getVisibleObject() instanceof Npc)
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		long itemCount;
		if (targetId == 203965) {
			if (qs == null || qs.getStatus() == QuestStatus.NONE) {
				if (env.getDialogId() == 25)
					return sendQuestDialog(player, env.getVisibleObject()
							.getObjectId(), 1011);
				else
					return defaultQuestStartDialog(env);
			}
		} else if (targetId == 203983) {
			if (qs != null && qs.getStatus() == QuestStatus.START) {
				if (env.getDialogId() == 25)
					return sendQuestDialog(player, env.getVisibleObject()
							.getObjectId(), 2375);
				else if (env.getDialogId() == 33) {
					itemCount = player.getInventory().getItemCountByItemId(
							182201321);
					if (itemCount > 9) {
						ItemService.removeItemFromInventoryByItemId(player,
								182201321);
						qs.setStatus(QuestStatus.REWARD);
						updateQuestStatus(player, qs);
						return sendQuestDialog(player, env.getVisibleObject()
								.getObjectId(), 5);
					} else
						return sendQuestDialog(player, env.getVisibleObject()
								.getObjectId(), 2716);
				} else
					return defaultQuestStartDialog(env);
			} else if (qs != null && qs.getStatus() == QuestStatus.REWARD) {
				return defaultQuestEndDialog(env);
			}
		}
		return false;
	}
}

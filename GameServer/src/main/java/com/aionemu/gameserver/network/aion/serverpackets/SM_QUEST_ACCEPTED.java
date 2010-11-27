/*
 * This file is part of aion-unique <aion-unique.smfnew.com>.
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

import com.aionemu.gameserver.network.aion.AbstractAionServerPacket;
import com.aionemu.gameserver.network.aion.AionChannelHandler;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/**
 * @author Lyahim, MrPoke
 * 
 */
public class SM_QUEST_ACCEPTED extends AbstractAionServerPacket<AionChannelHandler>
{
	private int	questId;
	private int	status;
	private int	step;
	private int	action;
	private int	timer;

	// accept = 1 - get quest 2 - quest steps/hand in 3 - fail/delete 4 - display client timer	

	/**
	 *  Accept Quest(1)
	 */
	public SM_QUEST_ACCEPTED(int questId, int status, int step)
	{
		this.action = 1;
		this.questId = questId;
		this.status = status;
		this.step = step;
	}

	/**
	 *  Quest Steps/Finish (2)
	 */
	public SM_QUEST_ACCEPTED(int questId, QuestStatus status, int step)
	{
		this.action = 2;
		this.questId = questId;
		this.status = status.value();
		this.step = step;
	}

	/**
	 *  Delete Quest(3)
	 */
	public SM_QUEST_ACCEPTED(int questId)
	{
		this.action = 3;
		this.questId = questId;
		this.status = 0;
		this.step = 0;
	}

	/**
	 *  Display Timer(4)
	 */
	public SM_QUEST_ACCEPTED(int questId, int timer)
	{
		this.action = 4;
		this.questId = questId;
		this.timer = timer;
		this.step = 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void writeImpl(AionChannelHandler cHandler)
	{
		switch (action)
		{
			case 1:
			case 2:
			case 3:
				writeC(action);
				writeD(questId);
				writeC(status);// quest status goes by ENUM value
				writeC(0x0);
				writeD(step);// current quest step
				writeH(0);
				break;
			case 4:
				writeC(action);
				writeD(questId);
				writeD(timer);// sets client timer ie 84030000 is 900 seconds/15 mins
				writeC(0x01);
				writeH(0x0);
				writeC(0x01);
		}
	}
}

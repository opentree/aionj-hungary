/*
 * This file is part of aion-unique <aionunique.smfnew.com>.
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

/**
 * 
 * @author Lyahim, alexa026
 * 
 */
public class SM_DIALOG_WINDOW extends AbstractAionServerPacket<AionChannelHandler>
{
	private int	targetObjectId;
	private int dialogID;
	private int	questId = 0;
	
	public SM_DIALOG_WINDOW(int targetObjectId, int dlgID)
	{
		this.targetObjectId = targetObjectId;
		this.dialogID = dlgID;
	}

	public SM_DIALOG_WINDOW(int targetObjectId , int dlgID , int questId)
	{
		this.targetObjectId = targetObjectId;
		this.dialogID = dlgID;
		this.questId = questId;
	}
	/**
	* {@inheritDoc}
	*/
	
	@Override
	protected void writeImpl(AionChannelHandler cHandler)
	{		
		writeD(targetObjectId);
		writeH(dialogID);
		writeD(questId);
		writeH(0);
	}
}

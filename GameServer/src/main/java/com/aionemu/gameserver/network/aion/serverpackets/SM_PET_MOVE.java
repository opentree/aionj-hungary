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

import com.aionemu.gameserver.model.gameobjects.player.ToyPet;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

/**
 * @author M@xx 
 */
public class SM_PET_MOVE extends AionServerPacket
{
	private int actionId;
	private ToyPet pet;
	
	public SM_PET_MOVE(int actionId, ToyPet pet)
	{
		this.actionId = actionId;
		this.pet = pet;
	}
	

	@Override
	protected void writeImpl(AionConnection con)
	{
		writeD(pet.getDatabaseIndex());
		writeC( actionId);
		switch(actionId)
		{
			case 12:
				// move
				writeF(pet.getX1());
				writeF(pet.getY1());
				writeF(pet.getZ1());
				writeC( pet.getH());
				writeF(pet.getX2());
				writeF(pet.getY2());
				writeF(pet.getZ2());
				break;
			default:
				break;					
		}
	}
}

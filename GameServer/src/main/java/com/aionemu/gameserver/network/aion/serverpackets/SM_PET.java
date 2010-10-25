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

import java.util.List;

import com.aionemu.gameserver.model.gameobjects.player.ToyPet;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

/**
 * @author M@xx
 */
public class SM_PET extends AionServerPacket
{
	private int actionId;
	private ToyPet pet;
	private List<ToyPet> pets;
	private int petUniqueId;
	
	public SM_PET(int actionId)
	{
		this.actionId = actionId;
	}
	
	public SM_PET(int actionId, int petUniqueId)
	{
		this.actionId = actionId;
		this.petUniqueId = petUniqueId;
	}
	
	public SM_PET(int actionId, ToyPet pet)
	{
		this.actionId = actionId;
		this.pet = pet;
	}
	
	public SM_PET(int actionId, List<ToyPet> pets)
	{
		this.actionId = actionId;
		this.pets = pets;
	}

	@Override
	protected void writeImpl(AionConnection con)
	{
		writeH(actionId);
		switch(actionId)
		{
			case 0:
				// load list on login
				int counter = 0;
				writeC( 0); // unk
				writeH(pets.size());
				for(ToyPet p : pets)
				{
					counter++;
					writeS(p.getName());
					writeD(p.getPetId());
					writeD(p.getDatabaseIndex()); //unk
					writeD(0); //unk
					writeD(0); //unk
					writeD(0); //unk
					writeD(1284402195); //creation timestamp - birthday
					writeC( 2); //unk +
					writeD(0); //unk
					writeD(0); //unk
					writeC( 2); //unk +
					writeD(0); //unk
					writeD(0); //unk
					writeC( 1); //unk +
					writeD(0); //unk
					writeD(0); //function id(s) ?
					writeD(0); //unk
					writeD(0); //unk
				}
				break;
			case 1:
				// adopt
				writeS(pet.getName());
				writeD(pet.getPetId());
				writeD(pet.getDatabaseIndex()); //unk
				writeD(0); //unk
				writeD(0); //unk
				writeD(0); //unk
				writeD(0); //unk
				writeC( 0); //unk +
				writeD(0); //unk
				writeD(0); //unk
				writeC( 0); //unk +
				writeD(0); //unk
				writeD(0); //unk
				writeC( 0); //unk +
				writeD(0); //unk
				writeD(0); //unk
				writeD(0); //unk
				writeD(0); //unk
				break;
			case 2:
				// surrender
				writeD(pet.getPetId());
				writeD(pet.getDatabaseIndex()); //unk
				writeD(0); //unk
				writeD(0); //unk
				break;
			case 3:
				// spawn
				writeS(pet.getName());
				writeD(pet.getPetId());
				writeD(pet.getDatabaseIndex());
				
				if(pet.getX1() == 0 && pet.getY1() == 0 && pet.getZ1() == 0)
				{
					writeF(pet.getMaster().getX());
					writeF(pet.getMaster().getY());
					writeF(pet.getMaster().getZ());
					
					writeF(pet.getMaster().getX());
					writeF(pet.getMaster().getY());
					writeF(pet.getMaster().getZ());
					
					writeC( pet.getMaster().getHeading());
				}
				else
				{
					writeF(pet.getX1());
					writeF(pet.getY1());
					writeF(pet.getZ1());
					
					writeF(pet.getX2());
					writeF(pet.getY2());
					writeF(pet.getZ2());
					
					writeC( pet.getH());
				}
				
				writeD(pet.getMaster().getObjectId()); //unk
				
				writeC( 1); //unk
				
				writeD(0); //unk
				
				writeD(0); //unk
				writeD(0); //unk
				writeD(0); //unk
				writeD(0); //unk
				break;
			case 4:
				// dismiss
				writeD(petUniqueId);
				writeC( 0x01);
				break;
			case 10:
				// rename
				writeD(0); //unk
				writeS(pet.getName());
				break;
			default:
				break;					
		}
	}
}

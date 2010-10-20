/*
 * This file is part of aion-unique <aion-unique.org>.
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
package com.aionemu.gameserver.controllers;

import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Servant;
import com.aionemu.gameserver.model.gameobjects.player.Player;

/**
 * @author ATracer
 *
 */
public class ServantController extends NpcController
{

	@Override
	public void onDie(Creature lastAttacker)
	{
		super.onDelete();
		getOwner().getAi().stop();
	}

	@Override
	public void onDialogRequest(Player player)
	{
		return;
	}

	@Override
	public Servant getOwner()
	{
		return  (Servant) super.getOwner();
	}
}
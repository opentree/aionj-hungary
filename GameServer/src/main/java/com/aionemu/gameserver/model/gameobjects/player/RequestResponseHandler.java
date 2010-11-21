/*
 * This file is part of aion-emu <aion-emu.com>.
 *
 *  aion-emu is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  aion-emu is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with aion-emu.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.aionemu.gameserver.model.gameobjects.player;

import com.aionemu.gameserver.model.gameobjects.instance.StaticNpc;

/**
 * Implemented by handlers of <tt>CM_QUESTION_RESPONSE</tt> responses
 * 
 * @author Ben
 * @modified Lyahim
 *
 */
public abstract class RequestResponseHandler
{
	private StaticNpc requester;

	public RequestResponseHandler(StaticNpc requester)
	{
		this.requester = requester;
	}

	/**
	 * Called when a response is received
	 * @param requested Player whom requested this response
	 * @param responder Player whom responded to this request
	 * @param responseCode The response the player gave, usually 0 = no 1 = yes
	 */
	public void handle(Player responder, int response)
	{
		if (response == 0)
			denyRequest(requester, responder);
		else
			acceptRequest(requester, responder);
	}

	/**
	 * Called when the player accepts a request
	 * @param requester Creature whom requested this response
	 * @param responder Player whom responded to this request
	 */
	public abstract void acceptRequest(StaticNpc requester, Player responder);

	/**
	 * Called when the player denies a request
	 * @param requester Creature whom requested this response
	 * @param responder Player whom responded to this request
	 */
	public abstract void denyRequest(StaticNpc requester, Player responder);

}

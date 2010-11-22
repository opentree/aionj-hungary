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

import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.RequestResponseHandler;
import com.aionemu.gameserver.model.templates.spawn.SpawnTemplate;
import com.aionemu.gameserver.network.aion.serverpackets.SM_QUESTION_WINDOW;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * @author Mr. Poke
 *
 */
public class Artifact extends Npc
{

	/**
	 * @param objId
	 * @param spawnTemplate
	 */
	public Artifact(int objId, SpawnTemplate spawnTemplate)
	{
		super(objId, spawnTemplate);
	}

	private int artifactId = 0;
	private Player activePlayer;

	@Override
	public void onDialogRequest(Player player)
	{
		if (artifactId == 0 || activePlayer != null)
			return;
		
		RequestResponseHandler responseHandler = new RequestResponseHandler(this)
		{
			@Override
			public void acceptRequest(StaticNpc requester, Player responder)
			{
			}
			
			@Override
			public void denyRequest(StaticNpc requester, Player responder)
			{
				activePlayer = null;
			}
		};

		boolean requested = player.getResponseRequester().putRequest(160028, responseHandler);
		if (requested)
		{
			activePlayer = player;
			PacketSendUtility.sendPacket(player, new SM_QUESTION_WINDOW(160028, 0, 0));
		}
	}

	/**
	 * @return the artifactId
	 */
	public int getArtifactId()
	{
		return artifactId;
	}

	/**
	 * @param artifactId the artifactId to set
	 */
	public void setArtifactId(int artifactId)
	{
		this.artifactId = artifactId;
	}
}

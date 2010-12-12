/**
 * This file is part of aion-emu <aion-unique.org>.
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
package com.aionemu.gameserver.network.aion.clientpackets;

import com.aionemu.commons.network.netty.packet.AbstractClientPacket;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.AionChannelHandler;
import com.aionemu.gameserver.services.AllianceService;
import com.aionemu.gameserver.services.GroupService;
import com.aionemu.gameserver.world.World;

/**
 * Called when entering the world and during group management
 * 
 * @author Lyahim
 * @author ATracer
 * @author Simple
 */

public class CM_PLAYER_STATUS_INFO extends AbstractClientPacket<AionChannelHandler>
{

	public enum ePlayerStatus
	{
		UNK(-1), GROUP_2(2), GROUP_3(3), GROUP_6(6), LFG(9), ALLIANCE_12(12), ALLIANCE_14(14), ALLIANCE_15(15), ALLIANCE_19(19), ALLIANCE_23(23), ALLIANCE_24(
				24);

		private int	status;

		private ePlayerStatus(int status)
		{
			this.status = status;
		}

		public int getStatus()
		{
			return this.status;
		}

		public static ePlayerStatus getMemberByStatus(int status)
		{
			for (ePlayerStatus memb : ePlayerStatus.values())
			{
				if (memb.getStatus() == status)
					return memb;
			}
			return UNK;
		}
	}

	/**
	 * Definitions
	 */
	private ePlayerStatus	status;
	private int				playerObjId;

	public CM_PLAYER_STATUS_INFO(int opcode)
	{
		super(opcode);
	}

	@Override
	protected void readImpl()
	{
		status = ePlayerStatus.getMemberByStatus(readC());
		playerObjId = readD();
	}

	@Override
	protected void runImpl()
	{
		Player myActivePlayer = getChannelHandler().getActivePlayer();

		switch (status)
		{
			// Note: This is currently used for PlayerGroup...
			// but it also is sent when leaving the alliance.
			case LFG:
				getChannelHandler().getActivePlayer().setLookingForGroup(playerObjId == 2);
				break;

			//Alliance Statuses
			case ALLIANCE_12:
			case ALLIANCE_14:
			case ALLIANCE_15:
			case ALLIANCE_19:
			case ALLIANCE_23:
			case ALLIANCE_24:
				AllianceService.getInstance().playerStatusInfo(myActivePlayer, status, playerObjId);
				break;

			// PlayerGroup Statuses
			case GROUP_2:
			case GROUP_3:
			case GROUP_6:
				Player player = null;

				if (playerObjId == 0)
					player = getChannelHandler().getActivePlayer();
				else
					player = World.getInstance().findPlayer(playerObjId);

				if (player == null || player.getPlayerGroup() == null)
					return;

				GroupService.getInstance().playerStatusInfo(status, player);
				break;

		}
	}
}
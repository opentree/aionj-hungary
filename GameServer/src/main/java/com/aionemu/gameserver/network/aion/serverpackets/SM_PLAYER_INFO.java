/**
 * This file is part of aion-unique <aion-unique.com>.
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
package com.aionemu.gameserver.network.aion.serverpackets;

import java.util.List;

import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.PlayerAppearance;
import com.aionemu.gameserver.model.gameobjects.player.PlayerCommonData;
import com.aionemu.gameserver.model.gameobjects.stats.StatEnum;
import com.aionemu.gameserver.model.items.GodStone;
import com.aionemu.gameserver.network.aion.AbstractAionServerPacket;
import com.aionemu.gameserver.network.aion.AionChannelHandler;

/**
 * This packet is displaying visible players.
 * 
 * @author Lyahim, -Nemesiss-, Avol, srx47  ,M@xx
 */
public class SM_PLAYER_INFO extends AbstractAionServerPacket<AionChannelHandler>
{

	/**
	 * Visible player
	 */
	private final Player	player;
	private boolean			enemy;

	/**
	 * Constructs new <tt>SM_PLAYER_INFO </tt> packet
	 * 
	 * @param player
	 *            actual player.
	 * @param enemy
	 */
	public SM_PLAYER_INFO(Player player, boolean enemy)
	{
		this.player = player;
		this.enemy = enemy;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void writeImpl(AionChannelHandler cHandler)
	{
		PlayerCommonData pcd = player.getCommonData();

		final int raceId = pcd.getRace().getRaceId();
		final int genderId = pcd.getGender().getGenderId();
		final PlayerAppearance playerAppearance = player.getPlayerAppearance();

		writeF(player.getX());// x
		writeF(player.getY());// y
		writeF(player.getZ());// z
		writeD(player.getObjectId());
		/**
		 * A3 female asmodian A2 male asmodian A1 female elyos A0 male elyos
		 */
		writeD(pcd.getTemplateId());
		/**
		 * Transformed state - send transformed model id Regular state - send player model id (from common data)
		 */
		writeD(player.getTransformedModelId() == 0 ? pcd.getTemplateId() : player.getTransformedModelId());
		writeC(0x00); //2.0 new
		writeC(enemy ? 0x00 : 0x26);

		writeC(raceId); // race
		writeC(pcd.getPlayerClass().getClassId());
		writeC(genderId); // sex
		writeH(player.getState());

		byte[] unk = new byte[]
		{ (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00 };
		writeB(unk);

		writeC(player.getHeading());

		writeS(player.getName());

		writeD(pcd.getTitleId());
		writeC(0x0);// if set 0x1 can't jump and fly..
		writeH(player.getCastingSkillId());
		writeH(player.isLegionMember() ? player.getLegion().getLegionId() : 0);
		writeH(0); // User Emblem related?
		writeH(player.isLegionMember() ? player.getLegion().getLegionEmblem().getEmblemId() : 0);
		writeC(0xFF);
		writeC(player.isLegionMember() ? player.getLegion().getLegionEmblem().getColor_r() : 0);
		writeC(player.isLegionMember() ? player.getLegion().getLegionEmblem().getColor_g() : 0);
		writeC(player.isLegionMember() ? player.getLegion().getLegionEmblem().getColor_b() : 0);
		writeS(player.isLegionMember() ? player.getLegion().getLegionName() : "");

		int maxHp = player.getLifeStats().getMaxHp();
		int currHp = player.getLifeStats().getCurrentHp();
		writeC(100 * currHp / maxHp);// %hp
		writeH(pcd.getDp());// current dp
		writeC(0x00);// unk (0x00)

		List<Item> items = player.getEquipment().getEquippedItemsWithoutStigma();
		short mask = 0;
		for (Item item : items)
		{
			mask |= item.getEquipmentSlot();
		}

		writeH(mask);

		for (Item item : items)
		{
			if (item.getEquipmentSlot() < Short.MAX_VALUE * 2)
			{
				writeD(item.getItemSkinTemplate().getTemplateId());
				GodStone godStone = item.getGodStone();
				writeD(godStone != null ? godStone.getItemId() : 0);
				writeD(item.getItemColor());
				writeH(0x00);// unk (0x00)
			}
		}

		writeD(playerAppearance.getSkinRGB());
		writeD(playerAppearance.getHairRGB());
		writeD(playerAppearance.getEyeRGB());
		writeD(playerAppearance.getLipRGB());
		writeC(playerAppearance.getFace());
		writeC(playerAppearance.getHair());
		writeC(playerAppearance.getDeco());
		writeC(playerAppearance.getTattoo());

		writeC(5);// always 5 o0

		writeC(playerAppearance.getFaceShape());
		writeC(playerAppearance.getForehead());

		writeC(playerAppearance.getEyeHeight());
		writeC(playerAppearance.getEyeSpace());
		writeC(playerAppearance.getEyeWidth());
		writeC(playerAppearance.getEyeSize());
		writeC(playerAppearance.getEyeShape());
		writeC(playerAppearance.getEyeAngle());

		writeC(playerAppearance.getBrowHeight());
		writeC(playerAppearance.getBrowAngle());
		writeC(playerAppearance.getBrowShape());

		writeC(playerAppearance.getNose());
		writeC(playerAppearance.getNoseBridge());
		writeC(playerAppearance.getNoseWidth());
		writeC(playerAppearance.getNoseTip());

		writeC(playerAppearance.getCheek());
		writeC(playerAppearance.getLipHeight());
		writeC(playerAppearance.getMouthSize());
		writeC(playerAppearance.getLipSize());
		writeC(playerAppearance.getSmile());
		writeC(playerAppearance.getLipShape());
		writeC(playerAppearance.getJawHeigh());
		writeC(playerAppearance.getChinJut());
		writeC(playerAppearance.getEarShape());
		writeC(playerAppearance.getHeadSize());
		// 1.5.x 0x00, shoulderSize, armLength, legLength (BYTE) after HeadSize

		writeC(playerAppearance.getNeck());
		writeC(playerAppearance.getNeckLength());
		writeC(playerAppearance.getShoulderSize());

		writeC(playerAppearance.getTorso());
		writeC(playerAppearance.getChest()); // only woman
		writeC(playerAppearance.getWaist());

		writeC(playerAppearance.getHips());
		writeC(playerAppearance.getArmThickness());
		writeC(playerAppearance.getHandSize());
		writeC(playerAppearance.getLegThicnkess());

		writeC(playerAppearance.getFootSize());
		writeC(playerAppearance.getFacialRate());

		writeC(0x00); // always 0
		writeC(playerAppearance.getArmLength());
		writeC(playerAppearance.getLegLength());
		writeC(playerAppearance.getShoulders());
		writeC(0x00); // always 0
		writeC(0x00); // 0x00

		writeC(playerAppearance.getVoice());

		writeF(playerAppearance.getHeight());
		writeF(0.25f); // scale
		writeF(2.0f); // gravity or slide surface o_O
		writeF(player.getGameStats().getCurrentStat(StatEnum.SPEED) / 1000f); // move speed

		writeH(player.getGameStats().getBaseStat(StatEnum.ATTACK_SPEED));
		writeH(player.getGameStats().getCurrentStat(StatEnum.ATTACK_SPEED));
		writeC(2);

		writeS(player.hasStore() ? player.getStore().getStoreMessage() : "");// private store message

		/**
		 * Movement
		 */
		writeF(0);
		writeF(0);
		writeF(0);

		writeF(player.getX());// x
		writeF(player.getY());// y
		writeF(player.getZ());// z
		writeC(0x00); // move type

		if (player.isUsingFlyTeleport())
		{
			writeD(player.getFlightTeleportId());
			writeD(player.getFlightDistance());
		}

		writeC(player.getVisualState()); // visualState
		writeS(player.getCommonData().getNote()); // note show in right down windows if your target on player

		writeH(player.getLevel()); // [level]
		writeH(player.getPlayerSettings().getDisplay()); // unk - 0x04
		writeH(player.getPlayerSettings().getDeny()); // unk - 0x00
		writeH(player.getAbyssRank().getRank().getId()); // abyss rank
		writeH(0x00); // unk - 0x01
		writeD(player.getTarget() == null ? 0 : player.getTarget().getObjectId());
		writeC(0); // suspect id
	}
}

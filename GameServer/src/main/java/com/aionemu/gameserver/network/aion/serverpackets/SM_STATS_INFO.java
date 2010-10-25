/**
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
package com.aionemu.gameserver.network.aion.serverpackets;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.PlayerCommonData;
import com.aionemu.gameserver.model.gameobjects.stats.PlayerGameStats;
import com.aionemu.gameserver.model.gameobjects.stats.PlayerLifeStats;
import com.aionemu.gameserver.model.gameobjects.stats.StatEnum;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;
import com.aionemu.gameserver.utils.gametime.GameTimeManager;

/**
 * In this packet Server is sending User Info?
 * 
 * @author -Nemesiss-
 * @author Luno
 */
public class SM_STATS_INFO extends AionServerPacket
{
	/**
	 * Player that stats info will be send
	 */
	private Player	player;
	private PlayerGameStats pgs;
	private PlayerLifeStats pls;
	private PlayerCommonData pcd;
	
	/**
	 * Constructs new <tt>SM_UI</tt> packet
	 * 
	 * @param player
	 */
	public SM_STATS_INFO(Player player)
	{
		this.player = player;
		this.pcd = player.getCommonData();
		this.pgs = player.getGameStats();
		this.pls = player.getLifeStats();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void writeImpl(AionConnection con)
	{
		writeD(player.getObjectId());
		writeD(GameTimeManager.getGameTime().getTime()); // Minutes since 1/1/00 00:00:00

		writeH(pgs.getCurrentStat(StatEnum.POWER));// [current power]
		writeH(pgs.getCurrentStat(StatEnum.HEALTH));// [current health]
		writeH(pgs.getCurrentStat(StatEnum.ACCURACY));// [current accuracy]
		writeH(pgs.getCurrentStat(StatEnum.AGILITY));// [current agility]
		writeH(pgs.getCurrentStat(StatEnum.KNOWLEDGE));// [current knowledge]
		writeH(pgs.getCurrentStat(StatEnum.WILL));// [current will]

		writeH(pgs.getCurrentStat(StatEnum.WATER_RESISTANCE));// [current water]
		writeH(pgs.getCurrentStat(StatEnum.WIND_RESISTANCE));// [current wind]
		writeH(pgs.getCurrentStat(StatEnum.EARTH_RESISTANCE));// [current earth]
		writeH(pgs.getCurrentStat(StatEnum.FIRE_RESISTANCE));// [current fire]
		writeH(0);// [current unknown resistance]
		writeH(0);// [current unknown resistance]

		writeH(player.getLevel());// [level]

		// something like very dynamic
		writeH(0); // [unk]
		writeH(0);// [unk]
		writeH(0);// [unk]

		writeQ(pcd.getExpNeed());// [xp till next lv]
		writeQ(pcd.getExpRecoverable()); // [recoverable exp]
		writeQ(pcd.getExpShown()); // [current xp]

		writeD(0); // [unk]
		writeD(pgs.getCurrentStat(StatEnum.MAXHP)); // [max hp]
		writeD(pls.getCurrentHp());// [unk]

		writeD(pgs.getCurrentStat(StatEnum.MAXMP));// [max mana]
		writeD(pls.getCurrentMp());// [current mana]

		writeH(pgs.getCurrentStat(StatEnum.MAXDP));// [max dp]
		writeH(pcd.getDp());// [current dp]

		writeD(pgs.getCurrentStat(StatEnum.FLY_TIME));// [max fly time]

		writeD(pls.getCurrentFp());// [current fly time]

		writeC( player.getFlyState());// [fly state]
		writeC( 0);// [unk]
		
		writeH(pgs.getCurrentStat(StatEnum.MAIN_HAND_POWER)); // [current main hand attack]

		writeH(pgs.getCurrentStat(StatEnum.OFF_HAND_POWER)); // [off hand attack]

		writeH(pgs.getCurrentStat(StatEnum.PHYSICAL_DEFENSE));// [current pdef]

		writeH(pgs.getCurrentStat(StatEnum.MAIN_HAND_POWER));// [current magic attack ?]

		writeH(pgs.getCurrentStat(StatEnum.MAGICAL_RESIST)); // [current mres]

		writeF(pgs.getCurrentStat(StatEnum.ATTACK_RANGE) / 1000f);// attack range
		writeH(pgs.getCurrentStat(StatEnum.ATTACK_SPEED));// attack speed 
		writeH(pgs.getCurrentStat(StatEnum.EVASION));// [current evasion]
		writeH(pgs.getCurrentStat(StatEnum.PARRY));// [current parry]
		writeH(pgs.getCurrentStat(StatEnum.BLOCK));// [current block]

		writeH(pgs.getCurrentStat(StatEnum.MAIN_HAND_CRITICAL));// [current main hand crit rate]
		writeH(pgs.getCurrentStat(StatEnum.OFF_HAND_CRITICAL));// [current off hand crit rate]

		writeH(pgs.getCurrentStat(StatEnum.MAIN_HAND_ACCURACY));// [current main_hand_accuracy]
		writeH(pgs.getCurrentStat(StatEnum.OFF_HAND_ACCURACY));// [current off_hand_accuracy]

		writeH(0);// [unk]
		writeH(pgs.getCurrentStat(StatEnum.MAGICAL_ACCURACY));// [current magic accuracy]
		writeH(0); // [current concentration]
		writeH(0); // [old current magic boost location]

		writeH(0);// [unk] 1.9 version
		writeH(16256);// [unk] 1.9 version
		writeH(40);// [unk] 1.9 version
		writeH(pgs.getCurrentStat(StatEnum.MAGICAL_ATTACK)+pgs.getCurrentStat(StatEnum.BOOST_MAGICAL_SKILL)); // [current magic boost] 1.9 version
		writeH(pgs.getCurrentStat(StatEnum.BOOST_HEAL)-100); // [current boost_heal]
		writeH(pgs.getCurrentStat(StatEnum.CRITICAL_RESIST)); // [current strike resist]
		writeH(0);// [unk] 1.9 version
		writeH(0);// [unk] 1.9 version
		writeH(0);// [unk] 1.9 version
		writeH(20511 );// [unk] 1.9 version
		
		writeD((27 + (player.getCubeSize() * 9)));// [unk]

		writeD(player.getInventory().size());// [unk]
		writeD(0);// [unk]
		writeD(0);// [unk]
		writeD(pcd.getPlayerClass().getClassId());// [Player Class id]

		writeQ(0);// [unk] 1.9 version
		writeQ(0);// Current energy of repose 1.9
		writeQ(251141);// Max energy of repose 1.9
		writeQ(0);// [unk] 1.9 version

		//writeQ(buf, 4020244);// [current energy of repose]
		//writeQ(buf, 4720968);// [max energy of repose]

		writeH(pgs.getBaseStat(StatEnum.POWER));// [base power]
		writeH(pgs.getBaseStat(StatEnum.HEALTH));// [base health]

		writeH(pgs.getBaseStat(StatEnum.ACCURACY));// [base accuracy]
		writeH(pgs.getBaseStat(StatEnum.AGILITY));// [base agility]

		writeH(pgs.getBaseStat(StatEnum.KNOWLEDGE));// [base knowledge]
		writeH(pgs.getBaseStat(StatEnum.WILL));// [base will]

		writeH(pgs.getBaseStat(StatEnum.WATER_RESISTANCE));// [base water res]
		writeH(pgs.getBaseStat(StatEnum.WIND_RESISTANCE));// [base water res]
		
		writeH(pgs.getBaseStat(StatEnum.EARTH_RESISTANCE));// [base earth resist]
		writeH(pgs.getBaseStat(StatEnum.FIRE_RESISTANCE));// [base water res]

		writeD(0);// [unk]

		writeD(pgs.getBaseStat(StatEnum.MAXHP));// [base hp]

		writeD(pgs.getBaseStat(StatEnum.MAXMP));// [base mana]

		writeD(pgs.getBaseStat(StatEnum.MAXDP));// [base dp]
		writeD(pgs.getBaseStat(StatEnum.FLY_TIME));// [fly time]

		writeH(pgs.getBaseStat(StatEnum.MAIN_HAND_POWER));// [base main hand attack]
		writeH(pgs.getBaseStat(StatEnum.OFF_HAND_POWER));// [base off hand attack]

		writeH(pgs.getBaseStat(StatEnum.MAIN_HAND_POWER)); // [base magic attack ?] 
		writeH(pgs.getBaseStat(StatEnum.PHYSICAL_DEFENSE)); // [base pdef]

		writeH(pgs.getBaseStat(StatEnum.MAGICAL_RESIST)); // [base magic res]

		writeH(0); // [unk]

		writeF(pgs.getCurrentStat(StatEnum.ATTACK_RANGE) / 1000f);// [current attack range]

		writeH(pgs.getBaseStat(StatEnum.EVASION)); // [base evasion]

		writeH(pgs.getBaseStat(StatEnum.PARRY)); // [base parry]
 
		writeH(pgs.getBaseStat(StatEnum.BLOCK)); // [base block]

		writeH(pgs.getBaseStat(StatEnum.MAIN_HAND_CRITICAL)); // [base main hand crit rate]
		writeH(pgs.getBaseStat(StatEnum.OFF_HAND_CRITICAL)); // [base off hand crit rate]

		writeH(pgs.getCurrentStat(StatEnum.MAGICAL_CRITICAL)); // [base or current MAGICAL crit rate] VERSION 1.9 
		writeH(0); // [unk] VERSION 1.9 
		
		writeH(pgs.getBaseStat(StatEnum.MAIN_HAND_ACCURACY)); // [base main hand accuracy]
		writeH(pgs.getBaseStat(StatEnum.OFF_HAND_ACCURACY)); // [base off hand accuracy]

		writeH(0); // [base Casting speed] VERSION 1.9 

		writeH(pgs.getBaseStat(StatEnum.MAGICAL_ACCURACY));// [base magic accuracy]

		writeH(0); // [base concentration]
		writeH(pgs.getBaseStat(StatEnum.MAGICAL_ATTACK)+pgs.getBaseStat(StatEnum.BOOST_MAGICAL_SKILL));// [base magic boost]

		writeH(pgs.getBaseStat(StatEnum.BOOST_HEAL)-100); // [base boostheal]
		writeH(pgs.getBaseStat(StatEnum.CRITICAL_RESIST)); // [base strike resist]
		writeH(0); // [unk] VERSION 1.9 
		writeH(0); // [unk] VERSION 1.9 
		writeH(0); // [unk] VERSION 1.9 

	}
}

/*
 * This file is part of aion-unique <aionunique.smfnew.com>.
 *
 * aion-unique is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * aion-unique is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with aion-unique.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.aionemu.gameserver.network.aion.serverpackets;

import java.util.List;

import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.network.aion.AbstractAionServerPacket;
import com.aionemu.gameserver.network.aion.AionChannelHandler;
import com.aionemu.gameserver.skillengine.model.Effect;

/**
 * 
 * @author Lyahim, alexa026, Sweetkr
 * 
 */
public class SM_CASTSPELL_END extends AbstractAionServerPacket<AionChannelHandler>
{
	private Creature		attacker;
	private Creature		target;
	private int				spellid;
	private int				level;
	private int				cooldown;
	private List<Effect>	effects;
	private int 			spellStatus;
	private float			x;
	private float			y;
	private float			z;
	private int				targetType;
	private boolean			chainSuccess;

	public SM_CASTSPELL_END(Creature attacker, Creature target, List<Effect> effects,
		int spellid, int level, int cooldown, boolean chainSuccess, int spellStatus)
	{
		// TODO: Pass Skill type instead?
		this.attacker = attacker;
		this.target = target;
		this.spellid = spellid;// empty
		this.level = level;
		this.effects = effects;
		this.cooldown = cooldown;
		this.spellStatus = spellStatus;
		this.chainSuccess = chainSuccess;
		this.targetType = 0;
	}
	
	public SM_CASTSPELL_END(Creature attacker, Creature target, List<Effect> effects,
		int spellid, int level, int cooldown, boolean chainSuccess, int spellStatus, float x, float y, float z)
	{
		this(attacker, target, effects, spellid, level, cooldown, chainSuccess, spellStatus);
		this.x = x;
		this.y = y;
		this.z = z;
		this.targetType = 1;
	}

	/**
	 * {@inheritDoc}
	 */

	@Override
	protected void writeImpl(AionChannelHandler cHandler)
	{
		writeD(attacker.getObjectId());
		writeC( targetType);
		switch(targetType)
		{
			case 0:
				writeD(target.getObjectId());
				break;
			case 1:
				writeF(x);
				writeF(y);
				writeF(z + 0.4f);
				break;
		}
		writeH(spellid);
		writeC( level);
		writeD(cooldown);
		writeH(560); // time?
		writeC( 0); // unk

		/**
		 * 0 : chain skill (counter too)
		 * 16 : no damage to all target like dodge, resist or effect size is 0
		 * 32 : regular
		 */
		if (chainSuccess)
			writeH(32);
		else
			writeH(0);
			
		/**
		 * Dash Type
		 * 
		 * 1 : teleport to back (1463)
		 * 2 : dash (816)
		 * 4 : assault (803)
		 */
		writeC( 0);

	// TODO refactor skill engine
	/*	switch(attacker.getDashType().getId())
		{
			case 1:
			case 2:
			case 4:
				writeC( heading);
				writeF(x);
				writeF(y);
				writeF(z);
				break;
			default:
				break;
		}*/

		writeH(effects.size());
		for(Effect effect : effects)
		{
			writeD(effect.getEffected().getObjectId());
			writeC( 0); // unk

			int attackerMaxHp = attacker.getLifeStats().getMaxHp();
			int attackerCurrHp = attacker.getLifeStats().getCurrentHp();
			int targetMaxHp = target.getLifeStats().getMaxHp();
			int targetCurrHp = target.getLifeStats().getCurrentHp();

			writeC( 100 * targetCurrHp / targetMaxHp); // target %hp
			writeC( 100 * attackerCurrHp / attackerMaxHp); // attacker %hp
			
			
			/**
			 * Spell Status
			 * 
			 * 1 : stumble
			 * 2 : knockback
			 * 4 : open aerial
			 * 8 : close aerial
			 * 16 : spin
			 * 32 : block
			 * 64 : parry
			 * 128 : dodge
			 * 256 : resist
			 */
			writeC( this.spellStatus);

			switch(this.spellStatus)
			{
				case 1:
				case 2:
				case 4:
				case 8:
					writeF(target.getX());
					writeF(target.getY());
					writeF(target.getZ() + 0.4f);
					break;
				case 16:
					writeC( target.getHeading());
					break;
				default:
					break;
			}

			writeC( 16); // unk
			writeC( 0); // current carve signet count

			writeC( 1); // unk always 1
			writeC( 0); // be 1 - when use Mana Treatment
			writeD(effect.getReserved1()); // damage
			writeC( effect.getAttackStatus().getId());
			writeC( effect.getShieldDefense());

			switch(effect.getShieldDefense())
			{
				case 1: // reflect shield
					writeD(0x00);
					writeD(0x00);
					writeD(0x00);
					writeD(0x00); // reflect damage
					writeD(0x00); // skill id
					break;
				case 2: // normal shield
				default:
					break;
			}
		}
	}
}

/*
 * This file is part of aion-emu <aion-emu.com>.
 *
 * aion-emu is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * aion-emu is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with aion-emu.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.aionemu.gameserver.network.aion.serverpackets;

import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.state.CreatureState;
import com.aionemu.gameserver.model.gameobjects.stats.StatEnum;
import com.aionemu.gameserver.network.aion.AbstractAionServerPacket;
import com.aionemu.gameserver.network.aion.AionChannelHandler;

/**
 * Emotion packet
 * 
 * @author Lyahim, SoulKeeper
 */
public class SM_EMOTION extends AbstractAionServerPacket<AionChannelHandler>
{
	/**
	 * Object id of emotion sender
	 */
	private final int			senderObjectId;

	/**
	 * Some unknown variable
	 */
	private final EmotionType	emotionType;

	/**
	 * ID of emotion
	 */
	private int					emotion;

	/**
	 * Object id of emotion target
	 */
	private int					targetObjectId;

	/**
	 * Temporary Speed..
	 */
	private float				speed	= 6.0f;

	private int					state;

	private int					baseAttackSpeed;
	private int					currentAttackSpeed;

	/**
	 * Coordinates of player
	 */
	private float				x;
	private float				y;
	private float				z;
	private byte				heading;

	/**
	 * This constructor should be used when emotion and targetid is 0
	 * 
	 * @param creature
	 * @param emotionType
	 */
	public SM_EMOTION(Creature creature, EmotionType emotionType)
	{
		this(creature, emotionType, 0, 0);
	}

	/**
	 * Constructs new server packet with specified opcode
	 * 
	 * @param senderObjectId
	 *            who sended emotion
	 * @param unknown
	 *            Dunno what it is, can be 0x10 or 0x11
	 * @param emotionId
	 *            emotion to play
	 * @param emotionId
	 *            who target emotion
	 */
	public SM_EMOTION(Creature creature, EmotionType emotionType, int emotion, int targetObjectId)
	{
		this.senderObjectId = creature.getObjectId();
		this.emotionType = emotionType;
		this.emotion = emotion;
		this.targetObjectId = targetObjectId;
		this.state = creature.getState();
		this.baseAttackSpeed = creature.getStats().getBaseAttackSpeed();
		this.currentAttackSpeed = creature.getStats().getCurrentAttackSpeed();
		this.speed = creature.getStats().getMovementSpeed() / 1000f;
	}

	/**
	 * Used to open a door.
	 * 
	 * @param doorId
	 */
	public SM_EMOTION(int doorId)
	{
		this.senderObjectId = doorId;
		this.emotionType = EmotionType.SWITCH_DOOR;
	}

	/**
	 * New
	 *
	 */
	public SM_EMOTION(Player player, EmotionType emotionType, int emotion, float x, float y, float z, byte heading, int targetObjectId)
	{
		this.senderObjectId = player.getObjectId();
		this.emotionType = emotionType;
		this.emotion = emotion;
		this.x = x;
		this.y = y;
		this.z = z;
		this.heading = heading;
		this.targetObjectId = targetObjectId;

		if (player.isInState(CreatureState.FLYING))
			this.speed = player.getGameStats().getCurrentStat(StatEnum.FLY_SPEED) / 1000f;
		else
			this.speed = player.getGameStats().getCurrentStat(StatEnum.SPEED) / 1000f;

		this.state = player.getState();
		this.baseAttackSpeed = player.getGameStats().getBaseStat(StatEnum.ATTACK_SPEED);
		this.currentAttackSpeed = player.getGameStats().getCurrentStat(StatEnum.ATTACK_SPEED);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void writeImpl(AionChannelHandler cHandler)
	{
		writeD(senderObjectId);
		writeC(emotionType.getTypeId());
		switch (emotionType)
		{
			case SELECT_TARGET:
				// select target
				writeH(state);
				writeF(speed);
				break;
			case JUMP:
				// jump
				writeH(state);
				writeF(speed);
				break;
			case SIT:
				// sit
				writeH(state);
				writeF(speed);
				break;
			case STAND:
				// stand
				writeH(state);
				writeF(speed);
				break;
			case CHAIR_SIT:
				// sit (chair)
				writeH(state);
				writeF(speed);
				writeF(x);
				writeF(y);
				writeF(z);
				writeC(heading);
				break;
			case CHAIR_UP:
				// stand (chair)
				writeH(state);
				writeF(speed);
				writeF(x);
				writeF(y);
				writeF(z);
				writeC(heading);
				break;
			case START_FLYTELEPORT:
				// fly teleport (start)
				writeH(state);
				writeF(speed);
				writeD(emotion); // teleport Id
				break;
			case LAND_FLYTELEPORT:
				// fly teleport (land)
				writeH(state);
				writeF(speed);
				break;
			case FLY:
				// toggle flight mode
				writeH(state);
				writeF(speed);
				break;
			case LAND:
				// toggle land mode
				writeH(state);
				writeF(speed);
				break;
			case DIE:
				// die
				writeH(state);
				writeF(speed);
				writeD(targetObjectId);
				break;
			case RESURRECT:
				// resurrect
				writeH(state);
				writeF(speed);
				break;
			case EMOTE:
				// emote
				writeH(state);
				writeF(speed);
				writeD(targetObjectId);
				writeH(emotion);
				writeC(1);
				break;
			case ATTACKMODE:
				// toggle attack mode
				writeH(state);
				writeF(speed);
				break;
			case NEUTRALMODE:
				// toggle normal mode
				writeH(state);
				writeF(speed);
				break;
			case WALK:
				// toggle walk
				writeH(state);
				writeF((speed - (speed * 75f) / 100f));
				break;
			case RUN:
				// toggle run
				writeH(state);
				writeF(speed);
				break;
			case SWITCH_DOOR:
				// toggle doors
				writeH(9);
				writeD(0);
				break;
			case START_EMOTE:
				// emote startloop
				writeH(state);
				writeF(speed);
				writeH(baseAttackSpeed);
				writeH(currentAttackSpeed);
				break;
			case OPEN_PRIVATESHOP:
				// private shop open
				writeH(state);
				writeF(speed);
				break;
			case CLOSE_PRIVATESHOP:
				// private shop close
				writeH(state);
				writeF(speed);
				break;
			case START_EMOTE2:
				// emote startloop
				writeH(state);
				writeF(speed);
				writeH(baseAttackSpeed);
				writeH(currentAttackSpeed);
				break;
			case POWERSHARD_ON:
				// powershard on
				writeH(state);
				writeF(speed);
				break;
			case POWERSHARD_OFF:
				// powershard off
				writeH(state);
				writeF(speed);
				break;
			case ATTACKMODE2:
				// toggle attack mode
				writeH(state);
				writeF(speed);
				break;
			case NEUTRALMODE2:
				// toggle normal mode
				writeH(state);
				writeF(speed);
				break;
			case START_LOOT:
				// looting start
				writeH(state);
				writeF(speed);
				writeD(targetObjectId);
				break;
			case END_LOOT:
				// looting end
				writeH(state);
				writeF(speed);
				writeD(targetObjectId);
				break;
			case START_QUESTLOOT:
				// looting start (quest)
				writeH(state);
				writeF(speed);
				writeD(targetObjectId);
				break;
			case END_QUESTLOOT:
				// looting end (quest)
				writeH(state);
				writeF(speed);
				writeD(targetObjectId);
				break;
			default:
				writeH(state);
				writeF(speed);
				if (targetObjectId != 0)
				{
					writeD(targetObjectId);
				}
		}
	}
}

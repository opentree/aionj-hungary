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
package com.aionemu.gameserver.network.aion.serverpackets;

import java.util.Map.Entry;

import com.aionemu.gameserver.model.NpcType;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.instance.StaticNpc;
import com.aionemu.gameserver.model.gameobjects.interfaces.ISummoned;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.items.ItemSlot;
import com.aionemu.gameserver.model.items.NpcEquippedGear;
import com.aionemu.gameserver.model.templates.item.ItemTemplate;
import com.aionemu.gameserver.model.templates.spawn.SpawnTemplate;
import com.aionemu.gameserver.network.aion.AbstractAionServerPacket;
import com.aionemu.gameserver.network.aion.AionChannelHandler;

/**
 * This packet is displaying visible npc/monsters.
 * 
 * @author Lyahim, -Nemesiss-
 * 
 */
public class SM_NPC_INFO extends AbstractAionServerPacket<AionChannelHandler>
{
	private final StaticNpc	npc;
	private final int		npcTypeId;

	/**
	 * Constructs new <tt>SM_NPC_INFO </tt> packet
	 * 
	 * @param npc
	 *            visible npc.
	 * @param player 
	 */
	public SM_NPC_INFO(StaticNpc npc, Player player)
	{
		this.npc = npc;
		if (npc instanceof Creature)
			npcTypeId = (player.isAggroFrom((Creature) npc) ? NpcType.AGGRESSIVE.getId() : npc.getObjectTemplate().getNpcType().getId());
		else
			npcTypeId = npc.getObjectTemplate().getNpcType().getId();

	}

	/**
	 * Constructs new <tt>SM_NPC_INFO </tt> packet
	 * 
	 * @param npc
	 *            visible npc.
	 * @param player 
	 */
	/*	public SM_NPC_INFO(StaticNpc npc)
		{
			this.npc = npc;
			npc.getObjectTemplate().getNpcType();
			npcTemplate = npc.getObjectTemplate();
			npcTypeId = npcTemplate.getNpcType().getId();
			npcId = npc.getObjectTemplate().getTemplateId();

		}*/

	/**
	 * Constructs new <tt>SM_NPC_INFO </tt> packet
	 * 
	 * @param player 
	 * @param kisk - the visible npc.
	 */
	/*	public SM_NPC_INFO(Player player, Kisk kisk)
		{
			this.npc = kisk;
			npcTypeId = (kisk.isAggroFrom(player) ? NpcType.ATTACKABLE.getId() : NpcType.NON_ATTACKABLE.getId());
			npcTemplate = kisk.getObjectTemplate();
			npcId = kisk.getNpcId();

			masterObjId = kisk.getOwnerObjectId();
			masterName = kisk.getOwnerName();
		}*/

	/**
	 * 
	 * @param player
	 * @param groupgate - the visible npc.
	 */
	/*	public SM_NPC_INFO(Player player, GroupGate groupgate)
		{
			this.npc = groupgate;
			npcTypeId = (groupgate.isAggroFrom(player) ? NpcType.ATTACKABLE.getId() : NpcType.NON_ATTACKABLE.getId());
			npcTemplate = groupgate.getObjectTemplate();
			npcId = groupgate.getNpcId();

			Player owner = (Player) groupgate.getMaster();
			if (owner != null)
			{
				masterObjId = owner.getObjectId();
				masterName = owner.getName();
			}
		}*/

	/**
	 * 
	 * @param summon
	 */
	/*	public SM_NPC_INFO(Summon summon)
		{
			this.npc = summon;
			npcTemplate = summon.getObjectTemplate();
			npcTypeId = npcTemplate.getNpcType().getId();
			npcId = summon.getNpcId();
			Player owner = summon.getMaster();
			if (owner != null)
			{
				masterObjId = owner.getObjectId();
				masterName = owner.getName();
				speed = owner.getGameStats().getCurrentStat(StatEnum.SPEED) / 1000f;
			}
			else
			{
				masterName = "LOST";
			}
		}*/

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void writeImpl(AionChannelHandler cHandler)
	{
		writeF(npc.getX());// x
		writeF(npc.getY());// y
		writeF(npc.getZ());// z
		writeD(npc.getObjectId());
		//		writeD(npcId);
		//		writeD(npcId);
		writeD(npc.getObjectTemplate().getTemplateId());
		writeD(npc.getObjectTemplate().getTemplateId());

		writeC(npcTypeId);

		writeH(npc.getState());// unk 65=normal,0x47 (71)= [dead npc ?]no drop,0x21(33)=fight state,0x07=[dead monster?]
								// no drop
								// 3,19 - wings spread (NPCs)
								// 5,6,11,21 - sitting (NPC)
								// 7,23 - dead (no drop)
								// 8,24 - [dead][NPC only] - looks like some orb of light (no normal mesh)
								// 32,33 - fight mode

		writeC(npc.getHeading());
		writeD(npc.getObjectTemplate().getNameId());
		writeD(npc.getObjectTemplate().getTitleId());// titleID
		//		writeD(npcTemplate.getNameId());
		//		writeD(npcTemplate.getTitleId());// titleID

		writeH(0x00);// unk
		writeC(0x00);// unk
		writeD(0x00);// unk

		/*
		 * Master Info (Summon, Kisk, Etc)
		 */
		if (npc instanceof ISummoned && ((ISummoned) npc).getMaster() != null)
		{
			writeD(((ISummoned) npc).getMaster().getObjectId());// masterObjectId
			writeS(((ISummoned) npc).getMaster().getName());// masterName
			//		writeD(masterObjId);// masterObjectId
			//		writeS(masterName);// masterName
		}
		else
		//TODO need?
		{
			writeD(0);
			writeS("");
		}

		int maxHp = npc.getStats().getMaxHp();
		int currHp = npc.getStats().getCurrentHp();
		writeC(100 * currHp / maxHp);// %hp
		writeD(npc.getStats().getCurrentHp());
		writeC(npc.getLevel());// lvl

		//		NpcEquippedGear gear = npcTemplate.getEquipment();
		NpcEquippedGear gear = npc.getObjectTemplate().getEquipment();
		if (gear == null)
			writeH(0x00);
		else
		{
			writeH(gear.getItemsMask());
			for (Entry<ItemSlot, ItemTemplate> item : gear.getItems()) // getting it from template ( later if we make sure that npcs actually use items, we'll make Item from it )
			{
				writeD(item.getValue().getTemplateId());
				writeD(0x00);
				writeD(0x00);
				writeH(0x00);
			}
		}

		writeF(1.5f);// unk
		//		writeF(npcTemplate.getHeight());
		writeF(npc.getObjectTemplate().getHeight());
		//TODO: Walking, Runing, Flying speed....
		writeF(npc.getStats().getMovementSpeed());// speed

		writeH(npc.getStats().getBaseAttackSpeed());
		writeH(npc.getStats().getCurrentAttackSpeed());

		writeC(0x00);// unk

		/**
		 * Movement
		 */
		writeF(npc.getX());// x
		writeF(npc.getY());// y
		writeF(npc.getZ());// z
		writeC(0x00); // move type
		SpawnTemplate spawn = npc.getSpawn();
		if (spawn == null)
			writeH(0);
		else
			writeH(spawn.getStaticid());
		writeC(0);
		writeC(0); // all unknown
		writeC(0);
		writeC(0);
		writeC(0);
		writeC(0);
		writeC(0);
		writeC(0);
		writeC(npc.getVisualState()); // visualState

		/**
		 * 1 : normal (kisk too)
		 * 2 : summon
		 * 32 : trap
		 * 1024 : holy servant, noble energy
		 */
		//TODO ??????????
		writeH(1);
		writeC(0x00);// unk
		writeD(npc.getTarget() == null ? 0 : npc.getTarget().getObjectId());
	}
}

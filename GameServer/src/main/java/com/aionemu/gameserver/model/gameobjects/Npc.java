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
package com.aionemu.gameserver.model.gameobjects;

import java.util.List;

import com.aionemu.gameserver.controllers.attack.AttackResult;
import com.aionemu.gameserver.controllers.attack.AttackUtil;
import com.aionemu.gameserver.controllers.effect.EffectController;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.ChatType;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.TaskId;
import com.aionemu.gameserver.model.TribeClass;
import com.aionemu.gameserver.model.alliance.PlayerAlliance;
import com.aionemu.gameserver.model.gameobjects.instance.StaticNpc;
import com.aionemu.gameserver.model.gameobjects.instance.Summon;
import com.aionemu.gameserver.model.gameobjects.interfaces.IDialog;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.RequestResponseHandler;
import com.aionemu.gameserver.model.gameobjects.state.CreatureState;
import com.aionemu.gameserver.model.gameobjects.stats.NpcGameStats;
import com.aionemu.gameserver.model.gameobjects.stats.NpcLifeStats;
import com.aionemu.gameserver.model.group.PlayerGroup;
import com.aionemu.gameserver.model.templates.NpcTemplate;
import com.aionemu.gameserver.model.templates.TradeListTemplate;
import com.aionemu.gameserver.model.templates.npcskill.NpcSkillList;
import com.aionemu.gameserver.model.templates.spawn.SpawnTemplate;
import com.aionemu.gameserver.model.templates.stats.NpcRank;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ATTACK;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ATTACK_STATUS;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_LOOKATOBJECT;
import com.aionemu.gameserver.network.aion.serverpackets.SM_MESSAGE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_PET;
import com.aionemu.gameserver.network.aion.serverpackets.SM_PLASTIC_SURGERY;
import com.aionemu.gameserver.network.aion.serverpackets.SM_QUESTION_WINDOW;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SELL_ITEM;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_TRADELIST;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ATTACK_STATUS.TYPE;
import com.aionemu.gameserver.questEngine.QuestEngine;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.restrictions.RestrictionsManager;
import com.aionemu.gameserver.services.AllianceService;
import com.aionemu.gameserver.services.CraftSkillUpdateService;
import com.aionemu.gameserver.services.CubeExpandService;
import com.aionemu.gameserver.services.DropService;
import com.aionemu.gameserver.services.GroupService;
import com.aionemu.gameserver.services.ItemService;
import com.aionemu.gameserver.services.LegionService;
import com.aionemu.gameserver.services.RespawnService;
import com.aionemu.gameserver.services.TeleportService;
import com.aionemu.gameserver.services.TradeService;
import com.aionemu.gameserver.services.WarehouseService;
import com.aionemu.gameserver.utils.MathUtil;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.stats.StatFunctions;
import com.aionemu.gameserver.world.KnownList;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.WorldType;

/**
 * This class is a base class for all in-game NPCs, what includes: monsters and npcs that player can talk to (aka
 * Citizens)
 * 
 * @author Luno
 * 
 */
public class Npc extends Creature implements IDialog
{
	
	private NpcSkillList npcSkillList;
	
	/**
	 * Constructor creating instance of Npc.
	 * 
	 * @param spawn
	 *            SpawnTemplate which is used to spawn this npc
	 * @param objId
	 *            unique objId
	 */
	public Npc(int objId, SpawnTemplate spawnTemplate)
	{
		super(objId, spawnTemplate);
		this.objectTemplate = DataManager.NPC_DATA.getNpcTemplate(spawnTemplate.getTemplateId());
		super.setEffectController(new EffectController(this));
		super.setKnownlist(new KnownList(this));
		super.setGameStats(new NpcGameStats(this));
		super.setLifeStats(new NpcLifeStats(this));
	}

	@Override
	public NpcTemplate getObjectTemplate()
	{
		return (NpcTemplate) objectTemplate;
	}
	@Override
	public String getName()
	{
		return getObjectTemplate().getName();
	}

	public int getNpcId()
	{
		return getObjectTemplate().getTemplateId();
	}

	@Override
	public byte getLevel()
	{
		return getObjectTemplate().getLevel();
	}

	/**
	 * @return the lifeStats
	 */
	@Override
	public NpcLifeStats getLifeStats()
	{
		return (NpcLifeStats) super.getLifeStats();
	}

	/**
	 * @return the gameStats
	 */
	@Override
	public NpcGameStats getGameStats()
	{
		return (NpcGameStats) super.getGameStats();
	}

	public boolean hasWalkRoutes()
	{
		return false;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isAggressive()
	{
		TribeClass currentTribe = getObjectTemplate().getTribe();
		return DataManager.TRIBE_RELATIONS_DATA.hasAggressiveRelations(currentTribe) || isGuard() || isHostile();
	}
	
	public boolean isHostile()
	{
		TribeClass currentTribe = getObjectTemplate().getTribe();
		return DataManager.TRIBE_RELATIONS_DATA.hasHostileRelations(currentTribe);
	}
	
	@Override
	public boolean isAggressiveTo(Creature creature)
	{
		return creature.isAggroFrom(this) || creature.isHostileFrom(this);
	}
	
	@Override
	public boolean isAggroFrom(Npc npc)
	{
		return DataManager.TRIBE_RELATIONS_DATA.isAggressiveRelation(npc.getTribe(), getTribe());
	}
	
	@Override
	public boolean isHostileFrom(Npc npc)
	{
		return DataManager.TRIBE_RELATIONS_DATA.isHostileRelation(npc.getTribe(), getTribe());
	}

	@Override
	public boolean isSupportFrom(Npc npc)
	{
		return DataManager.TRIBE_RELATIONS_DATA.isSupportRelation(npc.getTribe(), getTribe());
	}

	/**
	 * 
	 * @return
	 */
	public boolean isGuard()
	{
		return getObjectTemplate().getTribe().isGuard();
	}
	
	@Override
	public TribeClass getTribe()
	{
		return this.getObjectTemplate().getTribe();
	}
	
	public int getAggroRange()
	{
		return getObjectTemplate().getAggroRange();
	}
	
	@Override
	public void initializeAi()
	{
	}

	/**
	 *  Check whether npc located at initial spawn location
	 *  
	 * @return true or false
	 */
	public boolean isAtSpawnLocation()
	{
		return MathUtil.getDistance(getSpawn().getX(), getSpawn().getY(), getSpawn().getZ(),
			getX(), getY(), getZ()) < 3 ;
	}

	/**
	 * @return the npcSkillList
	 */
	public NpcSkillList getNpcSkillList()
	{
		return npcSkillList;
	}

	/**
	 * @param npcSkillList the npcSkillList to set
	 */
	public void setNpcSkillList(NpcSkillList npcSkillList)
	{
		this.npcSkillList = npcSkillList;
	}
	
	@Override
	public boolean isEnemyNpc(Npc visibleObject)
	{
		return ((DataManager.TRIBE_RELATIONS_DATA.isAggressiveRelation(getTribe(),visibleObject.getTribe())) || (DataManager.TRIBE_RELATIONS_DATA.isHostileRelation(getTribe(),visibleObject.getTribe())));
	}

	@Override
	public boolean isEnemyPlayer(Player visibleObject)
	{
		return (!((DataManager.TRIBE_RELATIONS_DATA.isSupportRelation(getTribe(),visibleObject.getTribe())) || (DataManager.TRIBE_RELATIONS_DATA.isFriendlyRelation(getTribe(),visibleObject.getTribe()))) );
	}
	
	@Override
	public boolean isEnemySummon(Summon visibleObject)
	{
		Player player = visibleObject.getMaster();
		if (player != null)
			return (!((DataManager.TRIBE_RELATIONS_DATA.isSupportRelation(getTribe(),player.getTribe())) || (DataManager.TRIBE_RELATIONS_DATA.isFriendlyRelation(getTribe(),player.getTribe()))) );
		return true;
	}
	
	@Override
	protected boolean canSeeNpc(Npc npc)
	{
		return true; //TODO
	}

	@Override
	protected boolean canSeePlayer(Player player)
	{
		if(!player.isInState(CreatureState.ACTIVE))
			return false;
		
		if (player.getVisualState() == 1 && getObjectTemplate().getRank() == NpcRank.NORMAL)
		   return false;
		
		if (player.getVisualState() == 2 && (getObjectTemplate().getRank() == NpcRank.ELITE || getObjectTemplate().getRank() == NpcRank.NORMAL))
		   return false;
		
		if (player.getVisualState() >= 3)
		   return false;
		
		return true;
	}

	/* (non-Javadoc)
	 * @see com.aionemu.gameserver.model.gameobjects.IDialog#onDialogRequest(com.aionemu.gameserver.model.gameobjects.player.Player)
	 */
	@Override
	public void onDialogRequest(Player player)
	{
	}
	
	public void onDespawn(boolean forced)
	{
		if(forced)
			cancelTask(TaskId.DECAY);

		if(this == null || !isSpawned())
			return;

		World.getInstance().despawn(this);
	}
	
	@Override
	public void attackTarget(Creature target)
	{
		
		/**
		 * Check all prerequisites
		 */
		if(getLifeStats().isAlreadyDead() || !isSpawned())
			return;

		if(!canAttack())
			return;

		NpcGameStats gameStats = getGameStats();
		
		/**
		 * notify attack observers
		 */
		super.attackTarget(target);
		
		/**
		 * Calculate and apply damage
		 */
		List<AttackResult> attackList = AttackUtil.calculateAttackResult(this, target);

		int damage = 0;
		for(AttackResult result : attackList)
		{
			damage += result.getDamage();
		}

		int attackType = 0; // TODO investigate attack types (0 or 1)
		PacketSendUtility.broadcastPacket(this, new SM_ATTACK(this, target, gameStats
			.getAttackCounter(), 274, attackType, attackList));
		
		target.onAttack(this, damage);
		gameStats.increaseAttackCounter();
	}
	

	
	@Override
	public void onRespawn()
	{
		super.onRespawn();
		
		cancelTask(TaskId.DECAY);
		
		//set state from npc templates
		if(getObjectTemplate().getState() != 0)
			setState(getObjectTemplate().getState());
		else
			setState(CreatureState.NPC_IDLE);
		
		getLifeStats().setCurrentHpPercent(100);
	}

	@Override
	public void onDie(Creature lastAttacker)
	{
		super.onDie(lastAttacker);

		addTask(TaskId.DECAY, RespawnService.scheduleDecayTask(this));
		scheduleRespawn();

		PacketSendUtility.broadcastPacket(this,
			new SM_EMOTION(this, EmotionType.DIE, 0, lastAttacker == null ? 0 : lastAttacker.getObjectId()));
		
		
		this.doReward();

		// deselect target at the end
		setTarget(null);
		PacketSendUtility.broadcastPacket(this, new SM_LOOKATOBJECT(this));
	}

	@Override
		public void doReward()
	{
		AionObject winner = getAggroList().getMostDamage(); 
		
		if(winner == null)
			return;
		
		if (winner instanceof PlayerAlliance)
			AllianceService.getInstance().doReward((PlayerAlliance)winner, this);
		else if (winner instanceof PlayerGroup)
			GroupService.getInstance().doReward((PlayerGroup)winner, this);
		else if (((Player)winner).isInGroup())
			GroupService.getInstance().doReward(((Player)winner).getPlayerGroup(), this);
		else
		{
			super.doReward();
			
			Player player = (Player)winner;
			
			long expReward = StatFunctions.calculateSoloExperienceReward(player, this);
			player.getCommonData().addExp(expReward);

			int currentDp = player.getCommonData().getDp();
			int dpReward = StatFunctions.calculateSoloDPReward(player, this);
			player.getCommonData().setDp(dpReward + currentDp);
			
			WorldType worldType = World.getInstance().getWorldMap(player.getWorldId()).getWorldType();
			if(worldType == WorldType.ABYSS)
			{
				int apReward = StatFunctions.calculateSoloAPReward(player, this);
				player.getCommonData().addAp(apReward);
			}
			
			QuestEngine.getInstance().onKill(new QuestEnv(this, player, 0 , 0));
			
			DropService.getInstance().registerDrop(this , player, player.getLevel());			
		}
	}

	/**
	 * This method should be called to make forced despawn of NPC and delete it from the world
	 */
	public void onDelete()
	{
		if(isInWorld())
		{
			this.onDespawn(true);
			this.delete();
		}
	}

	/**
	 * Handle dialog
	 */
	@Override
	public void onDialogSelect(int dialogId, final Player player, int questId)
	{

		if (!MathUtil.isInRange(this, player, 10))
			return;
		int targetObjectId = getObjectId();

		if(QuestEngine.getInstance().onDialog(new QuestEnv(this, player, questId, dialogId)))
			return;
		switch(dialogId)
		{
			case 2:
				TradeListTemplate tradeListTemplate = DataManager.TRADE_LIST_DATA.getTradeListTemplate(getNpcId());
				if (tradeListTemplate == null)
				{
					PacketSendUtility.sendMessage(player, "Buy list is missing!!");
					break;
				}
				int tradeModifier = tradeListTemplate.getSellPriceRate();
				PacketSendUtility.sendPacket(player, new SM_TRADELIST(this,
					TradeService.getTradeListData().getTradeListTemplate(getNpcId()),
					player.getPrices().getVendorBuyModifier()*tradeModifier/100));
				break;
			case 3:
				PacketSendUtility.sendPacket(player, new SM_SELL_ITEM(targetObjectId, player.getPrices().getVendorSellModifier()));
				break;
			case 4:
				// stigma
				PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(targetObjectId, 1));		
				break;
			case 5:
				// create legion
				if(MathUtil.isInRange(this, player, 10)) // avoiding exploit with sending fake dialog_select packet
				{
					PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(targetObjectId, 2));
				}
				else
				{
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.LEGION_CREATE_TOO_FAR_FROM_NPC());
				}
				break;
			case 6:
				// disband legion
				if(MathUtil.isInRange(this, player, 10)) // avoiding exploit with sending fake dialog_select packet
				{
					LegionService.getInstance().requestDisbandLegion(this, player);
				}
				else
				{
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.LEGION_DISPERSE_TOO_FAR_FROM_NPC());
				}
				break;
			case 7:
				// recreate legion
				if(MathUtil.isInRange(this, player, 10)) // voiding exploit with sending fake client dialog_select
				// packet
				{
					LegionService.getInstance().recreateLegion(this, player);
				}
				else
				{
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.LEGION_DISPERSE_TOO_FAR_FROM_NPC());
				}
				break;
			case 20:
				// warehouse
				if(MathUtil.isInRange(this, player, 10)) // voiding exploit with sending fake client dialog_select
				// packet
				{
					if(!RestrictionsManager.canUseWarehouse(player))
						return;

					PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(targetObjectId, 26));
					WarehouseService.sendWarehouseInfo(player, true);
				}
				break;
			case 27:
				// Consign trade?? npc karinerk, koorunerk
				PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(targetObjectId, 13));
				break;
			case 29:
				// soul healing
				final long expLost = player.getCommonData().getExpRecoverable();
				final double factor = (expLost < 1000000 ?
					0.25 - (0.00000015 * expLost) 
					: 0.1);
				final int price = (int) (expLost * factor);
				
				RequestResponseHandler responseHandler = new RequestResponseHandler(this){
					@Override
					public void acceptRequest(StaticNpc requester, Player responder)
					{
						if(ItemService.decreaseKinah(player, price))
						{
							PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.EXP(String.valueOf(expLost)));
							PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.SOUL_HEALED());
							player.getCommonData().resetRecoverableExp();
						}
						else
						{
							PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.NOT_ENOUGH_KINAH(price));
						}
					}

					@Override
					public void denyRequest(StaticNpc requester, Player responder)
					{
						// no message
					}
				};
				if(player.getCommonData().getExpRecoverable() > 0)
				{
					boolean result = player.getResponseRequester().putRequest(SM_QUESTION_WINDOW.STR_SOUL_HEALING,
						responseHandler);
					if(result)
					{
						PacketSendUtility.sendPacket(player, new SM_QUESTION_WINDOW(
							SM_QUESTION_WINDOW.STR_SOUL_HEALING, 0, String.valueOf(price)
						));
					}
				}
				else
				{
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.DONT_HAVE_RECOVERED_EXP());
				}
				break;
			case 30:
				switch(getNpcId())
				{
					case 204089:
						TeleportService.teleportTo(player, 120010000, 1, 984f, 1543f, 222.1f, 0);
						break;
					case 203764:
						TeleportService.teleportTo(player, 110010000, 1, 1462.5f, 1326.1f, 564.1f, 0);
						break;
					case 203981:
						TeleportService.teleportTo(player, 210020000, 1, 439.3f, 422.2f, 274.3f, 0);
						break;
				}
				break;
			case 31:
				switch(getNpcId())
				{
					case 204087:
						TeleportService.teleportTo(player, 120010000, 1, 1005.1f, 1528.9f, 222.1f, 0);
						break;
					case 203875:
						TeleportService.teleportTo(player, 110010000, 1, 1470.3f, 1343.5f, 563.7f, 21);
						break;
					case 203982:
						TeleportService.teleportTo(player, 210020000, 1, 446.2f, 431.1f, 274.5f, 0);
						break;
				}			
				break;				
			case 35:
				// Godstone socketing
				PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(targetObjectId, 21));
				break;
			case 36:
				// remove mana stone
				PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(targetObjectId, 20));
				break;
			case 37:
				// modify appearance
				PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(targetObjectId, 19));
				break;
			case 38:
				// flight and teleport
				TeleportService.showMap(player, targetObjectId, getNpcId());
				break;
			case 39:
				// improve extraction
			case 40:
				// learn tailoring armor smithing etc...
				CraftSkillUpdateService.getInstance().learnSkill(player, this);
				break;
			case 41:
				// expand cube
				CubeExpandService.expandCube(player, this);
				break;
			case 42:
				WarehouseService.expandWarehouse(player, this);
				break;
			case 47:
				// legion warehouse
				if(MathUtil.isInRange(this, player, 10))
					LegionService.getInstance().openLegionWarehouse(player);
				break;
			case 50:
				// WTF??? Quest dialog packet
				break;
			case 52:
				if(MathUtil.isInRange(this, player, 10)) // avoiding exploit with sending fake dialog_select packet
				{
					PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(targetObjectId, 28));
				}
				break;
			case 53:
				// coin reward
				PacketSendUtility.sendPacket(player, new SM_MESSAGE(0, null, "This feature is not available yet",
					ChatType.ANNOUNCEMENTS));
				break;
			case 55:
				/** @author IlBuono */
				if (player.getInventory().getItemCountByItemId(169650000) > 0 || player.getInventory().getItemCountByItemId(169650001) > 0)
					PacketSendUtility.sendPacket(player, new SM_PLASTIC_SURGERY(player, true, false));  
				else
					PacketSendUtility.sendPacket(player, new SM_PLASTIC_SURGERY(player, false, false));  
				player.setEditMode(true);
				break;
			case 56:
				/** @author IlBuono */
				if (player.getInventory().getItemCountByItemId(169660000) > 0 || player.getInventory().getItemCountByItemId(169660001) > 0)
					PacketSendUtility.sendPacket(player, new SM_PLASTIC_SURGERY(player, true, true));  
				else
					PacketSendUtility.sendPacket(player, new SM_PLASTIC_SURGERY(player, false, true));  
	            player.setEditMode(true);
	            break;
			case 60:
				// armsfusion
				PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(targetObjectId, 29));
				break;
			case 61:
				// armsbreaking
				PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(targetObjectId, 30));
				break;
				case 65:
				// adopt pet
				PacketSendUtility.sendPacket(player, new SM_PET(6));
				break;
			case 66:
				// surrender pet
				PacketSendUtility.sendPacket(player, new SM_PET(7));
				break;
			default:
				if(questId > 0)
					PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(targetObjectId, dialogId, questId));
				else
					PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(targetObjectId, dialogId));
				break;
		}
	}

	@Override
	public void onAttack(Creature creature, int skillId, TYPE type, int damage)
	{
		if(getLifeStats().isAlreadyDead())
			return;

		super.onAttack(creature, skillId, type, damage);
		
		Creature actingCreature = creature.getActingCreature();
		if(actingCreature instanceof Player)
			if(QuestEngine.getInstance().onAttack(new QuestEnv(this, (Player) actingCreature, 0, 0)))
				return;

		for (VisibleObject obj : getKnownList().getKnownObjects().values())
		{
			if (obj instanceof Npc)
			{
				Npc tmp = (Npc)obj;
				if (isSupportFrom(tmp) && MathUtil.isInRange(this, obj, 10))
				{
					tmp.getAggroList().addHate(creature, 10);
				}
					
			}
		}
		getLifeStats().reduceHp(damage, actingCreature);

		PacketSendUtility.broadcastPacket(this, new SM_ATTACK_STATUS(this, type, skillId, damage));
	}
}

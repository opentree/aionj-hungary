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
package com.aionemu.gameserver.model.gameobjects;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import javolution.util.FastMap;

import org.apache.log4j.Logger;

import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.controllers.ObserveController;
import com.aionemu.gameserver.controllers.attack.AggroList;
import com.aionemu.gameserver.controllers.attack.AttackResult;
import com.aionemu.gameserver.controllers.attack.AttackUtil;
import com.aionemu.gameserver.controllers.effect.EffectController;
import com.aionemu.gameserver.controllers.movement.MovementType;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.TaskId;
import com.aionemu.gameserver.model.TribeClass;
import com.aionemu.gameserver.model.gameobjects.instance.SiegeNpc;
import com.aionemu.gameserver.model.gameobjects.instance.StaticNpc;
import com.aionemu.gameserver.model.gameobjects.interfaces.IReward;
import com.aionemu.gameserver.model.gameobjects.interfaces.ISummoned;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.state.CreatureSeeState;
import com.aionemu.gameserver.model.gameobjects.state.CreatureState;
import com.aionemu.gameserver.model.gameobjects.stats.CreatureGameStats;
import com.aionemu.gameserver.model.gameobjects.stats.CreatureLifeStats;
import com.aionemu.gameserver.model.gameobjects.stats.StatEnum;
import com.aionemu.gameserver.model.templates.spawn.SpawnTemplate;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ATTACK;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ATTACK_STATUS;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ATTACK_STATUS.TYPE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_LOOKATOBJECT;
import com.aionemu.gameserver.network.aion.serverpackets.SM_MOVE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SKILL_CANCEL;
import com.aionemu.gameserver.questEngine.QuestEngine;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.services.QuestService;
import com.aionemu.gameserver.services.RespawnService;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.skillengine.effect.EffectId;
import com.aionemu.gameserver.skillengine.model.HealType;
import com.aionemu.gameserver.skillengine.model.Skill;
import com.aionemu.gameserver.taskmanager.tasks.PacketBroadcaster;
import com.aionemu.gameserver.taskmanager.tasks.PacketBroadcaster.BroadcastMode;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.World;

/**
 * This class is representing movable objects, its base class for all in game objects that may move
 * 
 * @author -Nemesiss-, Lyahim
 * 
 */
public abstract class Creature extends StaticNpc
{
	private static final Logger						log			= Logger.getLogger(Creature.class);

	private CreatureLifeStats<? extends Creature>	lifeStats;
	private CreatureGameStats<? extends Creature>	gameStats;
	private EffectController						effectController;
	private int										seeState	= CreatureSeeState.NORMAL.getId();
	private Skill									castingSkill;
	private Map<Integer, Long>						skillCoolDowns;
	private int										transformedModelId;
	private ObserveController						observeController;
	private final AggroList							aggroList;

	/**
	 * 
	 * @param objId
	 * @param controller
	 * @param spawnTemplate
	 * @param objectTemplate
	 * @param position
	 */
	public Creature(int objId, SpawnTemplate spawnTemplate)
	{
		super(objId, spawnTemplate);
		initializeAi();

		this.aggroList = new AggroList(this);
	}

	/**
	 * @return the lifeStats
	 */
	public CreatureLifeStats<? extends Creature> getLifeStats()
	{
		return lifeStats;
	}

	/**
	 * @param lifeStats the lifeStats to set
	 */
	public void setLifeStats(CreatureLifeStats<? extends Creature> lifeStats)
	{
		this.lifeStats = lifeStats;
	}

	/**
	 * @return the gameStats
	 */
	public CreatureGameStats<? extends Creature> getGameStats()
	{
		return gameStats;
	}

	/**
	 * @param gameStats the gameStats to set
	 */
	public void setGameStats(CreatureGameStats<? extends Creature> gameStats)
	{
		this.gameStats = gameStats;
	}

	public abstract void initializeAi();

	/**
	 * @return the effectController
	 */
	public EffectController getEffectController()
	{
		return effectController;
	}

	/**
	 * @param effectController the effectController to set
	 */
	public void setEffectController(EffectController effectController)
	{
		this.effectController = effectController;
	}

	/**
	 *  Is creature casting some skill
	 *  
	 * @return
	 */
	public boolean isCasting()
	{
		return castingSkill != null;
	}

	/**
	 *  Set current casting skill or null when skill ends
	 *  
	 * @param castingSkill
	 */
	public void setCasting(Skill castingSkill)
	{
		this.castingSkill = castingSkill;
	}

	/**
	 *  Current casting skill id
	 *  
	 * @return
	 */
	public int getCastingSkillId()
	{
		return castingSkill != null ? castingSkill.getSkillTemplate().getSkillId() : 0;
	}

	/**
	 *  Current casting skill
	 *  
	 * @return
	 */
	public Skill getCastingSkill()
	{
		return castingSkill;
	}

	/**
	 * All abnormal effects are checked that disable movements
	 * 
	 * @return
	 */
	public boolean canPerformMove()
	{
		return !(getEffectController().isAbnormalState(EffectId.CANT_MOVE_STATE) || !isSpawned());
	}

	/**
	 * All abnormal effects are checked that disable attack
	 * @return
	 */
	public boolean canAttack()
	{
		return !(getEffectController().isAbnormalState(EffectId.CANT_ATTACK_STATE) || isCasting() || isInState(CreatureState.RESTING) || isInState(CreatureState.PRIVATE_SHOP));
	}

	/**
	 * @return seeState
	 */
	public int getSeeState()
	{
		return seeState;
	}

	/**
	 * @param seeState the seeState to set
	 */
	public void setSeeState(CreatureSeeState seeState)
	{
		this.seeState |= seeState.getId();
	}

	public void unsetSeeState(CreatureSeeState seeState)
	{
		this.seeState &= ~seeState.getId();
	}

	public boolean isInSeeState(CreatureSeeState seeState)
	{
		int isSeeState = this.seeState & seeState.getId();

		if (isSeeState == seeState.getId())
			return true;

		return false;
	}

	/**
	 * @return the transformedModelId
	 */
	public int getTransformedModelId()
	{
		return transformedModelId;
	}

	/**
	 * @param transformedModelId the transformedModelId to set
	 */
	public void setTransformedModelId(int transformedModelId)
	{
		this.transformedModelId = transformedModelId;
	}

	/**
	 * @return the aggroList
	 */
	public AggroList getAggroList()
	{
		return aggroList;
	}

	/**
	 * PacketBroadcasterMask
	 */
	private volatile byte	packetBroadcastMask;

	/**
	 * This is adding broadcast to player.
	 */
	public final void addPacketBroadcastMask(BroadcastMode mode)
	{
		packetBroadcastMask |= mode.mask();

		PacketBroadcaster.getInstance().add(this);

		// Debug
		if (log.isDebugEnabled())
			log.debug("PacketBroadcaster: Packet " + mode.name() + " added to player " + ((Player) this).getName());
	}

	/**
	 * This is removing broadcast from player.
	 */
	public final void removePacketBroadcastMask(BroadcastMode mode)
	{
		packetBroadcastMask &= ~mode.mask();

		// Debug
		if (log.isDebugEnabled())
			log.debug("PacketBroadcaster: Packet " + mode.name() + " removed from player " + ((Player) this).getName());
	}

	/**
	 * Broadcast getter.
	 */
	public final byte getPacketBroadcastMask()
	{
		return packetBroadcastMask;
	}

	/**
	 * @return the observeController
	 */
	public ObserveController getObserveController()
	{
		if (observeController == null)
			observeController = new ObserveController();
		return observeController;
	}

	public boolean isEnemy(Creature creature)
	{
		return isAggressiveTo(creature);
		/*		if (creature instanceof Npc)
					return isEnemyNpc((Npc) creature);
				else if (creature instanceof Player)
					return isEnemyPlayer((Player) creature);
				else if (creature instanceof Summon)
					return isEnemySummon((Summon) creature);

				return false;*/
	}

	/*	public boolean isEnemySummon(Summon summon)
		{
			return false;
		}

		public boolean isEnemyPlayer(Player player)
		{
			return false;
		}

		public boolean isEnemyNpc(Npc npc)
		{
			return false;
		}*/

	public TribeClass getTribe()
	{
		if (this instanceof ISummoned)
			return ((ISummoned) this).getMaster().getObjectTemplate().getTribe();
		else
			return getObjectTemplate().getTribe();
	}

	/**
	 * 
	 * @param creature
	 * @return
	 */
	public boolean isAggressiveTo(Creature creature)
	{
		return creature.isAggroFrom(this) || creature.isHostileFrom(this);
		//		return false;
	}

	public boolean isAggroFrom(Creature npc)
	{
		if (npc instanceof SiegeNpc || npc.getLevel() + 10 >= getLevel())
		{
			if (this instanceof ISummoned)
				return DataManager.TRIBE_RELATIONS_DATA.isAggressiveRelation(((ISummoned) npc).getMaster().getObjectTemplate().getTribe(), getTribe());
			else
				return DataManager.TRIBE_RELATIONS_DATA.isAggressiveRelation(npc.getObjectTemplate().getTribe(), getTribe());
		}
		return false;
	}

	public boolean isHostileFrom(Creature npc)
	{
		if (this instanceof ISummoned)
			return DataManager.TRIBE_RELATIONS_DATA.isHostileRelation(((ISummoned) npc).getMaster().getObjectTemplate().getTribe(), getTribe());
		else
			return DataManager.TRIBE_RELATIONS_DATA.isHostileRelation(npc.getObjectTemplate().getTribe(), getTribe());
		//		return false;
	}

	public boolean isSupportFrom(Creature npc)
	{
		if (this instanceof ISummoned)
			return DataManager.TRIBE_RELATIONS_DATA.isSupportRelation(((ISummoned) npc).getMaster().getObjectTemplate().getTribe(), getTribe());
		else
			return DataManager.TRIBE_RELATIONS_DATA.isSupportRelation(npc.getObjectTemplate().getTribe(), getTribe());
		//		return false;
	}

	/**
	 * 
	 * @param visibleObject
	 * @return
	 */
	public boolean canSee(VisibleObject visibleObject)
	{
		if (visibleObject instanceof Npc)
			return canSeeNpc((Npc) visibleObject);
		else if (visibleObject instanceof Player)
			return canSeePlayer((Player) visibleObject);

		return true;
	}

	/**
	 * @param visibleObject
	 * @return
	 */
	protected boolean canSeePlayer(Player visibleObject)
	{
		return true;
	}

	/**
	 * @param visibleObject
	 * @return
	 */
	protected boolean canSeeNpc(Npc visibleObject)
	{
		return true;
	}

	/**
	 * 
	 * @param skillId
	 * @return
	 */
	public boolean isSkillDisabled(int skillId)
	{
		if (skillCoolDowns == null)
			return false;

		Long coolDown = skillCoolDowns.get(skillId);
		if (coolDown == null)
			return false;

		if (coolDown < System.currentTimeMillis())
		{
			skillCoolDowns.remove(skillId);
			return false;
		}

		return true;
	}

	/**
	 * 
	 * @param skillId
	 * @return
	 */
	public long getSkillCoolDown(int skillId)
	{
		if (skillCoolDowns == null || !skillCoolDowns.containsKey(skillId))
			return 0;

		return skillCoolDowns.get(skillId);
	}

	/**
	 * 
	 * @param skillId
	 * @param time
	 */
	public void setSkillCoolDown(int skillId, long time)
	{
		if (skillCoolDowns == null)
			skillCoolDowns = new FastMap<Integer, Long>().shared();

		skillCoolDowns.put(skillId, time);
	}

	/**
	 * @return the skillCoolDowns
	 */
	public Map<Integer, Long> getSkillCoolDowns()
	{
		return skillCoolDowns;
	}

	/**
	 * 
	 * @param skillId
	 */
	public void removeSkillCoolDown(int skillId)
	{
		if (skillCoolDowns == null)
			return;
		skillCoolDowns.remove(skillId);
	}

	@Override
	public void delete()
	{
		cancelAllTasks();
		super.delete();
	}

	/**
	 * Perform tasks when Creature was attacked //TODO may be pass only Skill object - but need to add properties in it
	 */
	public void onAttack(Creature creature, int skillId, TYPE type, int damage)
	{
		if (getLifeStats().isAlreadyDead())
			return;

		// Reduce the damage to exactly what is required to ensure death.
		// - Important that we don't include 7k worth of damage when the
		//   creature only has 100 hp remaining. (For AggroList dmg count.)
		if (damage > getLifeStats().getCurrentHp())
			damage = getLifeStats().getCurrentHp() + 1;

		Skill skill = getCastingSkill();
		if (skill != null && skill.getSkillTemplate().getCancelRate() > 0)
		{
			int cancelRate = skill.getSkillTemplate().getCancelRate();
			int conc = getGameStats().getCurrentStat(StatEnum.CONCENTRATION) / 10;
			float maxHp = getGameStats().getCurrentStat(StatEnum.MAXHP);
			float cancel = (cancelRate - conc) + ((damage) / maxHp * 50);
			if (Rnd.get(100) < cancel)
				cancelCurrentSkill();
		}

		getObserveController().notifyAttackedObservers(creature);
		getAggroList().addDamage(creature, damage);
		getLifeStats().reduceHp(damage, creature);

		if (this instanceof Player)
			PacketSendUtility.broadcastPacket((Player) this, new SM_ATTACK_STATUS(this, type, skillId, damage), true);
		else
			PacketSendUtility.broadcastPacket(this, new SM_ATTACK_STATUS(this, type, skillId, damage));
	}

	/**
	 * Perform tasks when Creature was attacked
	 */
	public final void onAttack(Creature creature, int damage)
	{
		this.onAttack(creature, 0, TYPE.REGULAR, damage);
	}

	/** 
	* Cancel current skill and remove cooldown 
	*/
	public void cancelCurrentSkill()
	{
		Skill castingSkill = getCastingSkill();
		if (castingSkill != null)
		{
			removeSkillCoolDown(castingSkill.getSkillTemplate().getSkillId());
			setCasting(null);
			PacketSendUtility.broadcastPacketAndReceive(this, new SM_SKILL_CANCEL(this, castingSkill.getSkillTemplate().getSkillId()));
		}
	}

	/**
	 * 
	 * @param target
	 */
	public void attackTarget(Creature target)
	{
		/**
		 * Check all prerequisites
		 */
		if (target == null || !canAttack() || getLifeStats().isAlreadyDead() || !isSpawned() || !isEnemy(target))
			return;

		getObserveController().notifyAttackObservers(target);

		/**
		 * Calculate and apply damage
		 */
		List<AttackResult> attackList = AttackUtil.calculateAttackResult(this, target);

		int damage = 0;
		for (AttackResult result : attackList)
		{
			damage += result.getDamage();
		}

		long time = System.currentTimeMillis();
		int attackType = 0; // TODO investigate attack types (0 or 1)
		PacketSendUtility.broadcastPacket(this, new SM_ATTACK(this, target, gameStats.getAttackCounter(), (int) time, attackType, attackList));

		target.onAttack(this, damage);
		gameStats.increaseAttackCounter();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void notSee(VisibleObject object, boolean isOutOfRange)
	{
		super.notSee(object, isOutOfRange);
		if (object == getTarget())
		{
			setTarget(null);
			PacketSendUtility.broadcastPacket(this, new SM_LOOKATOBJECT(this));
		}
	}

	/**
	 * Perform tasks on Creature starting to move
	 */
	public void onStartMove()
	{
		getObserveController().notifyMoveObservers();
	}

	/**
	 * Perform tasks on Creature move in progress
	 */
	public void onMove()
	{

	}

	/**
	 * Perform tasks on Creature stop move
	 */
	public void onStopMove()
	{

	}

	/**
	 * Perform tasks on Creature respawn
	 */
	@Override
	public void onRespawn()
	{
		unsetState(CreatureState.DEAD);
		getAggroList().clear();
	}

	/**
	 * 
	 * @param hopType
	 * @param value
	 */
	public void onRestore(HealType hopType, int value)
	{
		switch (hopType)
		{
			case HP:
				getLifeStats().increaseHp(TYPE.HP, value);
				break;
			case MP:
				getLifeStats().increaseMp(TYPE.MP, value);
				break;
			case FP:
				getLifeStats().increaseFp(value);
				break;
		}
	}

	/**
	    * Stops movements
	    */
	public void stopMoving()
	{
		World.getInstance().updatePosition(this, getX(), getY(), getZ(), getHeading());
		PacketSendUtility.broadcastPacket(this, new SM_MOVE(getObjectId(), getX(), getY(), getZ(), getHeading(), MovementType.MOVEMENT_STOP));
	}

	/**
	 * Die by reducing HP to 0
	 */
	public void die()
	{
		getLifeStats().reduceHp(getLifeStats().getCurrentHp() + 1, null);
	}

	/**
	 * 
	 * @param skillId
	 */
	public void useSkill(int skillId)
	{
		Skill skill = SkillEngine.getInstance().getSkill(this, skillId, 1, getTarget());
		if (skill != null)
		{
			skill.useSkill();
		}
	}

	/**
	 *  Notify hate value to all visible creatures
	 *  
	 * @param value
	 */
	public void broadcastHate(int value)
	{
		for (VisibleObject visibleObject : getKnownList().getKnownObjects().values())
		{
			if (visibleObject instanceof Creature)
			{
				((Creature) visibleObject).getAggroList().notifyHate(this, value);
			}
		}
	}

	public void abortCast()
	{
		Skill skill = getCastingSkill();
		if (skill == null)
			return;
		setCasting(null);
	}

	/**
	 * @param npcId
	 */
	public void createSummon(int npcId, int skillLvl)
	{
		// TODO Auto-generated method stub

	}

	/**
	 * Perform tasks on Creature death
	 */
	public void onDie(Creature lastAttacker)
	{
		this.setCasting(null);
		this.getEffectController().removeAllEffects();

		this.setState(CreatureState.DEAD);

		if (this instanceof IReward)
			((IReward) this).doReward();

		if (this instanceof Player)
			PacketSendUtility.broadcastPacket((Player) this, new SM_EMOTION(this, EmotionType.DIE, 0, lastAttacker == null ? 0 : lastAttacker.getObjectId()),
					true);
		else
			PacketSendUtility.broadcastPacket(this, new SM_EMOTION(this, EmotionType.DIE, 0, lastAttacker == null ? 0 : lastAttacker.getObjectId()));

	}

	@Override
	public void see(VisibleObject object)
	{
		super.see(object);
		if (object instanceof Player)
		{
			boolean update = false;
			Player player = (Player) object;
			for (int questId : QuestEngine.getInstance().getNpcQuestData(this.getObjectTemplate().getTemplateId()).getOnQuestStart())
			{
				if (QuestService.checkStartCondition(new QuestEnv(this, player, questId, 0)))
				{
					if (!player.getNearbyQuests().contains(questId))
					{
						update = true;
						player.getNearbyQuests().add(questId);
					}
				}
			}
			if (update)
				player.updateNearbyQuestList();
		}
	}

	/**
	 * Schedule respawn of npc
	 * In instances - no npc respawn
	 */
	public void scheduleRespawn()
	{
		if (isInInstance())
			return;

		if (getSpawn().getInterval() > 0)
		{
			Future<?> respawnTask = RespawnService.scheduleRespawnTask(this);
			addTask(TaskId.RESPAWN, respawnTask);
		}
	}
}

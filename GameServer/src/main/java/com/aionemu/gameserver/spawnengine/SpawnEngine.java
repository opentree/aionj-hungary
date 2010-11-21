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
package com.aionemu.gameserver.spawnengine;

import org.apache.log4j.Logger;

import com.aionemu.gameserver.controllers.GroupGateController;
import com.aionemu.gameserver.controllers.KiskController;
import com.aionemu.gameserver.controllers.NpcController;
import com.aionemu.gameserver.controllers.PostmanController;
import com.aionemu.gameserver.controllers.ServantController;
import com.aionemu.gameserver.controllers.SummonController;
import com.aionemu.gameserver.controllers.effect.EffectController;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.dataholders.NpcData;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.GroupGate;
import com.aionemu.gameserver.model.gameobjects.Kisk;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.Servant;
import com.aionemu.gameserver.model.gameobjects.Summon;
import com.aionemu.gameserver.model.gameobjects.Trap;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.NpcTemplate;
import com.aionemu.gameserver.model.templates.WorldMapTemplate;
import com.aionemu.gameserver.model.templates.spawn.SpawnTemplate;
import com.aionemu.gameserver.model.templates.stats.SummonStatsTemplate;
import com.aionemu.gameserver.utils.idfactory.IDFactory;
import com.aionemu.gameserver.world.KnownList;
import com.aionemu.gameserver.world.StaticObjectKnownList;
import com.aionemu.gameserver.world.World;

/**
 * 
 * This class is responsible for NPCs spawn management. Current implementation is temporal and will be replaced in the
 * future.
 * 
 * @author Luno
 * 
 *         modified by ATracer
 * 
 */
public class SpawnEngine
{
	private static Logger				log					= Logger.getLogger(SpawnEngine.class);

	/** Counter counting number of npc spawns */
	private int							npcCounter			= 0;
	/** Counter counting number of gatherable spawns */
	private int							gatherableCounter	= 0;

	public static final SpawnEngine getInstance()
	{
		return SingletonHolder.instance;
	}

	private SpawnEngine()
	{
		this.spawnAll();
	}

	/**
	 * Creates VisibleObject instance and spawns it using given {@link SpawnTemplate} instance.
	 * 
	 * @param spawn
	 * @return created and spawned VisibleObject
	 */
	public VisibleObject spawnObject(SpawnTemplate spawn, int instanceIndex)
	{
		return null;
	}

	/**
	 * 
	 * @param spawn
	 * @param instanceIndex
	 * @param creator
	 * @return
	 */
	public Trap spawnTrap(SpawnTemplate spawn, int instanceIndex, Creature creator, int skillId)
	{
		NpcTemplate npcTemplate = DataManager.NPC_DATA.getNpcTemplate(spawn.getTemplateId());
		Trap trap = new Trap(IDFactory.getInstance().nextId(), new NpcController(), spawn,
			npcTemplate);
		trap.setKnownlist(new KnownList(trap));
		trap.setEffectController(new EffectController(trap));
		trap.setCreator(creator);
		trap.setSkillId(skillId);
		trap.getController().onRespawn();
		bringIntoWorld(trap, spawn, instanceIndex);
		return trap;
	}

	/**
	*
	 * @param spawn
	 * @param instanceIndex
	 * @param creator
	 * @return
	 */
	public GroupGate spawnGroupGate(SpawnTemplate spawn, int instanceIndex, Creature creator)
	{
		NpcTemplate npcTemplate = DataManager.NPC_DATA.getNpcTemplate(spawn.getTemplateId());
		GroupGate groupgate = new GroupGate(IDFactory.getInstance().nextId(), new GroupGateController(), spawn,
			npcTemplate);
		groupgate.setKnownlist(new StaticObjectKnownList(groupgate));
		groupgate.setEffectController(new EffectController(groupgate));
		groupgate.setCreator(creator);
		groupgate.getController().onRespawn();
		bringIntoWorld(groupgate, spawn, instanceIndex);
		return groupgate;
	}

	/**
	 * @param spawn
	 * @param instanceIndex
	 * @param creator
	 * @return
	 */
	public Kisk spawnKisk(SpawnTemplate spawn, int instanceIndex, Player creator)
	{
		NpcTemplate template = DataManager.NPC_DATA.getNpcTemplate(spawn.getTemplateId());
		Kisk kisk = new Kisk(IDFactory.getInstance().nextId(), new KiskController(),
			spawn, template, creator);
		kisk.setKnownlist(new StaticObjectKnownList(kisk));
		kisk.setEffectController(new EffectController(kisk));
		kisk.getController().onRespawn();
		bringIntoWorld(kisk, spawn, instanceIndex);
		return kisk;
	}
	/**
	 * @param recipient
	 * @author leo
	 * 
	 * Spawns postman for express mail
	 */
	public void spawnPostman(Player recipient)
	{
		NpcData	npcData = DataManager.NPC_DATA;
		NpcTemplate template = npcData.getNpcTemplate(798044);
		IDFactory iDFactory = IDFactory.getInstance();
		int worldId = recipient.getWorldId();
		int instanceId = recipient.getInstanceId();
		float x = recipient.getX();
		float y = recipient.getY();
		float z = recipient.getZ();
		byte heading = recipient.getHeading();
		SpawnTemplate spawn = addNewSpawn(worldId, instanceId, 798044, x, y, z, heading, 0, 0, false, true);
		Npc postman = new Npc(iDFactory.nextId(), new PostmanController(recipient.getObjectId()), spawn, template);
		postman.setKnownlist(new StaticObjectKnownList(postman));
		bringIntoWorld(postman, spawn, instanceId);
	}

	
	/**
	 * 
	 * @param spawn
	 * @param instanceIndex
	 * @param creator
	 * @param skillId
	 * @return
	 */
	public Servant spawnServant(SpawnTemplate spawn, int instanceIndex, Creature creator, int skillId, int hpRatio)
	{
		NpcTemplate npcTemplate = DataManager.NPC_DATA.getNpcTemplate(spawn.getTemplateId());
		Servant servant = new Servant(IDFactory.getInstance().nextId(), new ServantController(), spawn,
			npcTemplate);
		servant.setKnownlist(new KnownList(servant));
		servant.setEffectController(new EffectController(servant));
		servant.setCreator(creator);
		servant.setSkillId(skillId);
		servant.setTarget(creator.getTarget());
		servant.setHpRatio(hpRatio);
		servant.getController().onRespawn();
		bringIntoWorld(servant, spawn, instanceIndex);
		return servant;
	}
	
	/**
	 * 
	 * @param creator
	 * @param npcId
	 * @return
	 */
	public Summon spawnSummon(Player creator, int npcId, int skillLvl)
	{	
		float x = creator.getX();
		float y = creator.getY();
		float z = creator.getZ();
		byte heading = creator.getHeading();
		int worldId = creator.getWorldId();
		int instanceId = creator.getInstanceId();
		
		SpawnTemplate spawn = createSpawnTemplate(worldId, npcId, x, y, z, heading, 0, 0);
		NpcTemplate npcTemplate = DataManager.NPC_DATA.getNpcTemplate(npcId);
		
		byte level = (byte) (npcTemplate.getLevel() + skillLvl - 1);
		SummonStatsTemplate statsTemplate = DataManager.SUMMON_STATS_DATA.getSummonTemplate(npcId, level);
		Summon summon = new Summon(IDFactory.getInstance().nextId(), new SummonController(), spawn,
			npcTemplate, statsTemplate, level);
		summon.setKnownlist(new KnownList(summon));
		summon.setEffectController(new EffectController(summon));
		summon.setMaster(creator);

		bringIntoWorld(summon, spawn, instanceId);
		return summon;
	}

	/**
	 * 
	 * @param worldId
	 * @param objectId
	 * @param x
	 * @param y
	 * @param z
	 * @param heading
	 * @param walkerid
	 * @param randomwalk
	 * @return
	 */
	private SpawnTemplate createSpawnTemplate(int worldId, int templateId, float x, float y, float z, byte heading,
		int walkerid, int randomwalk)
	{
		SpawnTemplate spawnTemplate = new SpawnTemplate(worldId, templateId, x, y, z, heading);
		return spawnTemplate;
	}

	/**
	 * Should be used when need to define whether spawn will be deleted after death Using this method spawns will not be
	 * saved with //save_spawn command
	 * 
	 * @param worldId
	 * @param objectId
	 * @param x
	 * @param y
	 * @param z
	 * @param heading
	 * @param walkerid
	 * @param randomwalk
	 * @param noRespawn
	 * @return SpawnTemplate
	 */
	public SpawnTemplate addNewSpawn(int worldId, int instanceId, int objectId, float x, float y, float z,
		byte heading, int walkerid, int randomwalk, boolean noRespawn)
	{
		return this
			.addNewSpawn(worldId, instanceId, objectId, x, y, z, heading, walkerid, randomwalk, noRespawn, false);
	}

	/**
	 * 
	 * @param worldId
	 * @param instanceId
	 * @param objectId
	 * @param x
	 * @param y
	 * @param z
	 * @param heading
	 * @param walkerid
	 * @param randomwalk
	 * @param noRespawn
	 * @param isNewSpawn
	 * @return SpawnTemplate
	 */
	public SpawnTemplate addNewSpawn(int worldId, int instanceId, int objectId, float x, float y, float z,
		byte heading, int walkerid, int randomwalk, boolean noRespawn, boolean isNewSpawn)
	{
		SpawnTemplate spawnTemplate = createSpawnTemplate(worldId, objectId, x, y, z, heading, walkerid, randomwalk);

		if(spawnTemplate == null)
		{
			log.warn("Object couldn't be spawned");
			return null;
		}

		return spawnTemplate;
	}

	private void bringIntoWorld(VisibleObject visibleObject, SpawnTemplate spawn, int instanceIndex)
	{
		World world = World.getInstance();
		world.storeObject(visibleObject);
		world.setPosition(visibleObject, spawn.getMapId(), instanceIndex, spawn.getX(), spawn.getY(), spawn.getZ(),
			spawn.getHeading());
		world.spawn(visibleObject);
	}

	/**
	 * Spawn all NPC's from templates
	 */
	public void spawnAll()
	{
		this.npcCounter = 0;
		this.gatherableCounter = 0;
		
		for(WorldMapTemplate worldMapTemplate : DataManager.WORLD_MAPS_DATA)
		{
			if(worldMapTemplate.isInstance())
				continue;
			int maxTwin = worldMapTemplate.getTwinCount();
			final int mapId = worldMapTemplate.getMapId();
			int numberToSpawn = maxTwin > 0 ? maxTwin : 1;

			for(int i = 1; i <= numberToSpawn; i++)
			{
				spawnInstance(mapId, i);
			}
		}

		log.info("Loaded " + npcCounter + " npc spawns");
		log.info("Loaded " + gatherableCounter + " gatherable spawns");

		RiftSpawnManager.startRiftPool();
	}

	/**
	 * 
	 * @param worldId
	 * @param instanceIndex
	 */
	public void spawnInstance(int worldId, int instanceIndex)
	{
/*
		int instanceSpawnCounter = 0;
		for(SpawnGroup spawnGroup : worldSpawns)
		{
			spawnGroup.resetLastSpawnCounter(instanceIndex);
			if(spawnGroup.getHandler() == null)
			{
				int pool = spawnGroup.getPool();
				for(int i = 0; i < pool; i++)
				{
					spawnObject(spawnGroup.getNextAvailableTemplate(instanceIndex), instanceIndex);

					instanceSpawnCounter++;
				}
			}
			else
			{
				switch(spawnGroup.getHandler())
				{
					case RIFT:
						RiftSpawnManager.addRiftSpawnGroup(spawnGroup);
						break;
					case STATIC:
						StaticObjectSpawnManager.spawnGroup(spawnGroup, instanceIndex);
					default:
						break;
				}
			}
		}
		log.info("Spawned " + worldId + " [" + instanceIndex + "] : " + instanceSpawnCounter);
		*/
	}
	
	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder
	{
		protected static final SpawnEngine instance = new SpawnEngine();
	}
}

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

import gnu.trove.TIntObjectHashMap;

import java.lang.reflect.Constructor;

import org.apache.log4j.Logger;

import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Kisk;
import com.aionemu.gameserver.model.gameobjects.Servant;
import com.aionemu.gameserver.model.gameobjects.Summon;
import com.aionemu.gameserver.model.gameobjects.Trap;
import com.aionemu.gameserver.model.gameobjects.GroupGate;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.newmodel.gameobject.SpawnedObject;
import com.aionemu.gameserver.newmodel.templates.IObjectTemplate;
import com.aionemu.gameserver.newmodel.templates.spawn.SpawnTemplate;

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

	private TIntObjectHashMap<SpawnTemplate> spawnByMap = new TIntObjectHashMap<SpawnTemplate>();
	private TIntObjectHashMap<SpawnTemplate> spawnByNpc = new TIntObjectHashMap<SpawnTemplate>();

	public static final SpawnEngine getInstance()
	{
		return SingletonHolder.instance;
	}

	private SpawnEngine()
	{
		this.spawnAll();
	}

	public SpawnTemplate getFirstSpawnByNpcId(int objectTemplateId)
	{
		return spawnByNpc.get(objectTemplateId);
	}
	/**
	 * Creates VisibleObject instance and spawns it using given {@link SpawnTemplate} instance.
	 * 
	 * @param spawn
	 * @return created and spawned VisibleObject
	 */
	public SpawnedObject spawnObject(IObjectTemplate objectTemplate)
	{
		Class<?> clazz;
		try
		{
			clazz = Class.forName("com.aionemu.gameserver.newmodel.gameobject.SpawnedObject");
			Object[] parameters = {objectTemplate};
			Constructor<?> constructor = clazz.getConstructor(Integer.class);
			SpawnedObject object = (SpawnedObject) constructor.newInstance(parameters);
			return object;
		}
		catch (Exception e)
		{
			log.warn(e);
			return null;
		}

	}
	public SpawnedObject spawnObject(SpawnTemplate spawn, int instanceIndex)
	{
/*		
		if(objectId > 400000 && objectId < 499999)// gatherable
		{
			template = DataManager.GATHERABLE_DATA.getGatherableTemplate(objectId);
			if(template == null)
				return null;

			try
			{
				clazz = Class.forName("com.aionemu.gameserver.newmodel.gameobject.Gatherable");
			}
			catch (ClassNotFoundException e)
			{
				log.warn(e);
				return null;
			}
			gatherableCounter++;
		}
		else
		// npc
		{
			template = npcData.getNpcTemplate(objectId);
			if(template == null)
				return null;
			try
			{
				clazz = Class.forName("com.aionemu.gameserver.newmodel.gameobject.SpawnedObject");
			}
			catch (ClassNotFoundException e)
			{
				log.warn(e);
				return null;
			}
			npcCounter++;
		}
		IDFactory iDFactory = IDFactory.getInstance();
		
		try
		{
			Object[] parameters =
			{iDFactory.nextId()};
			Constructor<?> constructor = clazz.getConstructor(Integer.class);
			SpawnedObject object = (SpawnedObject) constructor.newInstance(parameters);
			object.spawn(spawn, instanceIndex);
		}
		catch (Exception e)
		{
		}
		/**
		if(template instanceof NpcTemplate)
		{
			NpcType npcType = ((NpcTemplate) template).getNpcType();
			Npc npc = null;

			-------Spawning Npcs,Monsters and other simple Npcs---------
			if (spawn.getSpawnGroup().getSiegeId()==0) 
			{
				switch(npcType)
				{
					case AGGRESSIVE:
					case ATTACKABLE:
						npc = new Monster(iDFactory.nextId(), new MonsterController(),
							spawn, template);
						npc.setKnownlist(new KnownList(npc));
						break;
					case ARTIFACT :
						npc = new Npc(iDFactory.nextId(), new ArtifactController(), spawn,
							template);
						npc.setKnownlist(new StaticObjectKnownList(npc));
						break;
					case POSTBOX:
						npc = new Npc(iDFactory.nextId(), new PostboxController(), spawn,
							template);
						npc.setKnownlist(new StaticObjectKnownList(npc));
						break;
					case RESURRECT:
						BindpointController bindPointController = new BindpointController();
						bindPointController.setBindPointTemplate(DataManager.BIND_POINT_DATA.getBindPointTemplate(objectId));
						npc = new Npc(iDFactory.nextId(), bindPointController, spawn, template);
						npc.setKnownlist(new StaticObjectKnownList(npc));
						break;
					case USEITEM:
						npc = new Npc(iDFactory.nextId(), new ActionitemController(),
							spawn, template);
						npc.setKnownlist(new StaticObjectKnownList(npc));
						break;
					case PORTAL:
						npc = new Npc(iDFactory.nextId(), new PortalController(), spawn,
							template);
						npc.setKnownlist(new StaticObjectKnownList(npc));
						break;
					default: // NON_ATTACKABLE
						npc = new Npc(iDFactory.nextId(), new NpcController(), spawn,
							template);
						npc.setKnownlist(new KnownList(npc));
	
				}

			}
            	----------Spawn siege npcs like Siege Guardians,Protectors,Vendors------------
			else
			{
				FastMap<Integer, SiegeLocation> locations = DataManager.SIEGE_LOCATION_DATA.getSiegeLocations();
				DAOManager.getDAO(SiegeDAO.class).loadSiegeLocations(locations);
				int spawnSiegeId = spawn.getSpawnGroup().getSiegeId();
				SiegeRace spawnRace = spawn.getSpawnGroup().getRace();
				SiegeLocation siegeLocation = locations.get(spawnSiegeId);
				SiegeRace siegeRace = siegeLocation.getRace();
				if ( (spawnSiegeId==siegeLocation.getLocationId()) && (spawnRace==siegeRace))
				{
					if(spawn.getSpawnGroup().getSiegeSpawnType()!=null)
						switch(spawn.getSpawnGroup().getSiegeSpawnType())
						{
							case PEACE:
								SiegeNpcController controller = new SiegeNpcController();
								npc = new SiegeNpc(iDFactory.nextId(), controller, spawn, template);
								npc.setKnownlist(new KnownList(npc));
								break;
							case ARTIFACT:
								ArtifactController artifactController= new ArtifactController();
								npc = new Artifact(iDFactory.nextId(), artifactController, spawn, template);
								npc.setKnownlist(new KnownList(npc));
								break;
							case PROTECTOR:
								SiegeGeneralController generalController = new SiegeGeneralController();
								npc = new SiegeGeneral(iDFactory.nextId(), generalController, spawn, template);
								npc.setKnownlist(new KnownList(npc));
								break;
							case MINE:
								SiegeMineController mineController = new SiegeMineController();
								npc = new SiegeMine(iDFactory.nextId(), mineController, spawn, template);
								npc.setKnownlist(new KnownList(npc));
								break;
						}
					else	//default: GUARD
					{
						SiegeGuardController guardController= new SiegeGuardController();
						npc = new SiegeGuard(iDFactory.nextId(), guardController, spawn, template);
						npc.setKnownlist(new KnownList(npc));
					}
				}
				else
					return null;
			}

			npc.setNpcSkillList(DataManager.NPC_SKILL_DATA.getNpcSkillList(template.getTemplateId()));
			npc.setEffectController(new EffectController(npc));
			npc.getController().onRespawn();
			bringIntoWorld(npc, spawn, instanceIndex);
			return npc;
		}
		else if(template instanceof GatherableTemplate)
		{
			Gatherable gatherable = new Gatherable(spawn, template, iDFactory.nextId(), new GatherableController());
			gatherable.setKnownlist(new StaticObjectKnownList(gatherable));
			bringIntoWorld(gatherable, spawn, instanceIndex);
			return gatherable;
		}
		*/
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
		/*
		int objectId = spawn.getSpawnGroup().getNpcid();
		NpcTemplate npcTemplate = DataManager.NPC_DATA.getNpcTemplate(objectId);
		Trap trap = new Trap(IDFactory.getInstance().nextId(), new NpcController(), spawn,
			npcTemplate);
		trap.setKnownlist(new KnownList(trap));
		trap.setEffectController(new EffectController(trap));
		trap.setCreator(creator);
		trap.setSkillId(skillId);
		trap.getController().onRespawn();
		bringIntoWorld(trap, spawn, instanceIndex);
		return trap;*/
		return null;
	}

	/**
	*
	 * @param spawn
	 * @param instanceIndex
	 * @param creator
	 * @return
	 */
	public GroupGate spawnGroupGate(SpawnTemplate spawn, int instanceIndex, Creature creator)
	{/*
		int objectId = spawn.getSpawnGroup().getNpcid();
		NpcTemplate npcTemplate = DataManager.NPC_DATA.getNpcTemplate(objectId);
		GroupGate groupgate = new GroupGate(IDFactory.getInstance().nextId(), new GroupGateController(), spawn,
			npcTemplate);
		groupgate.setKnownlist(new StaticObjectKnownList(groupgate));
		groupgate.setEffectController(new EffectController(groupgate));
		groupgate.setCreator(creator);
		groupgate.getController().onRespawn();
		bringIntoWorld(groupgate, spawn, instanceIndex);
		return groupgate;*/
		return null;
	}

	/**
	 * @param spawn
	 * @param instanceIndex
	 * @param creator
	 * @return
	 */
	public Kisk spawnKisk(SpawnTemplate spawn, int instanceIndex, Player creator)
	{/*
		int npcId = spawn.getSpawnGroup().getNpcid();
		NpcTemplate template = DataManager.NPC_DATA.getNpcTemplate(npcId);
		Kisk kisk = new Kisk(IDFactory.getInstance().nextId(), new KiskController(),
			spawn, template, creator);
		kisk.setKnownlist(new StaticObjectKnownList(kisk));
		kisk.setEffectController(new EffectController(kisk));
		kisk.getController().onRespawn();
		bringIntoWorld(kisk, spawn, instanceIndex);
		return kisk;*/
		return null;
	}
	/**
	 * @param recipient
	 * @author leo
	 * 
	 * Spawns postman for express mail
	 */
	public void spawnPostman(Player recipient)
	{/*
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
		bringIntoWorld(postman, spawn, instanceId);*/
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
		/*
		int objectId = spawn.getSpawnGroup().getNpcid();
		NpcTemplate npcTemplate = DataManager.NPC_DATA.getNpcTemplate(objectId);
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
		return servant;*/
		return null;
	}
	
	/**
	 * 
	 * @param creator
	 * @param npcId
	 * @return
	 */
	public Summon spawnSummon(Player creator, int npcId, int skillLvl)
	{	/*
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
		return summon;*/
		return null;
	}

/*
	private void bringIntoWorld(VisibleObject visibleObject, SpawnTemplate spawn, int instanceIndex)
	{
		World world = World.getInstance();
		world.storeObject(visibleObject);
		world.setPosition(visibleObject, spawn.getWorldId(), instanceIndex, spawn.getX(), spawn.getY(), spawn.getZ(),
			spawn.getHeading());
		world.spawn(visibleObject);
	}*/

	/**
	 * Spawn all NPC's from templates
	 */
	public void spawnAll()
	{}

	/**
	 * 
	 * @param worldId
	 * @param instanceIndex
	 */
	public void spawnInstance(int worldId, int instanceIndex)
	{
/*		List<SpawnGroup> worldSpawns = DataManager.SPAWNS_DATA.getSpawnsForWorld(worldId);

		if(worldSpawns == null || worldSpawns.size() == 0)
			return;

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
*/	}
	
	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder
	{
		protected static final SpawnEngine instance = new SpawnEngine();
	}

	/**
	 * @param worldId
	 * @param instanceId
	 * @param templateId
	 * @param x
	 * @param y
	 * @param z
	 * @param heading
	 * @param noRespawn
	 * @return
	 */
	public SpawnTemplate addNewSpawn(int worldId, int instanceId, int templateId, float x, float y, float z, byte heading, boolean noRespawn)
	{
		return null;
	}

	/**
	 * @param template
	 * @param spawnTime
	 * @param nextRespawnTime
	 */
	public void addSpawn(SpawnTemplate template, String nextRespawnTime)
	{
		this.spawnByMap.put(template.getMapId(), template);
		this.spawnByNpc.put(template.getTemplateId(), template);
	}
}

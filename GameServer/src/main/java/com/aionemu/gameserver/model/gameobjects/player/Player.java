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
package com.aionemu.gameserver.model.gameobjects.player;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import javolution.util.FastMap;

import org.apache.log4j.Logger;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.gameserver.configs.main.OptionsConfig;
import com.aionemu.gameserver.controllers.FlyController;
import com.aionemu.gameserver.controllers.ReviveController;
import com.aionemu.gameserver.controllers.effect.PlayerEffectController;
import com.aionemu.gameserver.dao.AbyssRankDAO;
import com.aionemu.gameserver.dao.PlayerDAO;
import com.aionemu.gameserver.dao.PlayerQuestListDAO;
import com.aionemu.gameserver.dao.PlayerSkillListDAO;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.Gender;
import com.aionemu.gameserver.model.PlayerClass;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.TaskId;
import com.aionemu.gameserver.model.alliance.PlayerAlliance;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.PersistentState;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.instance.Kisk;
import com.aionemu.gameserver.model.gameobjects.instance.Summon;
import com.aionemu.gameserver.model.gameobjects.instance.Summon.UnsummonType;
import com.aionemu.gameserver.model.gameobjects.interfaces.IDialogSelect;
import com.aionemu.gameserver.model.gameobjects.interfaces.IReward;
import com.aionemu.gameserver.model.gameobjects.interfaces.ISummoned;
import com.aionemu.gameserver.model.gameobjects.state.CreatureState;
import com.aionemu.gameserver.model.gameobjects.state.CreatureVisualState;
import com.aionemu.gameserver.model.gameobjects.stats.PlayerGameStats;
import com.aionemu.gameserver.model.gameobjects.stats.PlayerLifeStats;
import com.aionemu.gameserver.model.gameobjects.stats.StatEnum;
import com.aionemu.gameserver.model.items.ItemCooldown;
import com.aionemu.gameserver.model.legion.Legion;
import com.aionemu.gameserver.model.legion.LegionMember;
import com.aionemu.gameserver.model.team.alliance.PlayerAllianceEvent;
import com.aionemu.gameserver.model.team.group.GroupEvent;
import com.aionemu.gameserver.model.team.group.PlayerGroup;
import com.aionemu.gameserver.model.team.interfaces.ITeamProperties;
import com.aionemu.gameserver.model.templates.quest.QuestItems;
import com.aionemu.gameserver.model.templates.stats.PlayerStatsTemplate;
import com.aionemu.gameserver.network.aion.AionChannelHandler;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ATTACK_STATUS.TYPE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_LEVEL_UPDATE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_NEARBY_QUESTS;
import com.aionemu.gameserver.network.aion.serverpackets.SM_PET;
import com.aionemu.gameserver.network.aion.serverpackets.SM_PLAYER_STATE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_PRIVATE_STORE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SKILL_CANCEL;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SKILL_LIST;
import com.aionemu.gameserver.network.aion.serverpackets.SM_STATS_INFO;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SUMMON_PANEL;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SUMMON_UPDATE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.questEngine.QuestEngine;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.restrictions.RestrictionsManager;
import com.aionemu.gameserver.services.AllianceService;
import com.aionemu.gameserver.services.BrokerService;
import com.aionemu.gameserver.services.DropService;
import com.aionemu.gameserver.services.DuelService;
import com.aionemu.gameserver.services.ExchangeService;
import com.aionemu.gameserver.services.ItemService;
import com.aionemu.gameserver.services.LegionService;
import com.aionemu.gameserver.services.PlayerService;
import com.aionemu.gameserver.services.PvpService;
import com.aionemu.gameserver.services.QuestService;
import com.aionemu.gameserver.services.SkillLearnService;
import com.aionemu.gameserver.services.ZoneService;
import com.aionemu.gameserver.services.ZoneService.ZoneUpdateMode;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.skillengine.model.HealType;
import com.aionemu.gameserver.skillengine.model.Skill;
import com.aionemu.gameserver.skillengine.task.CraftingTask;
import com.aionemu.gameserver.spawnengine.SpawnEngine;
import com.aionemu.gameserver.taskmanager.tasks.PacketBroadcaster.BroadcastMode;
import com.aionemu.gameserver.utils.MathUtil;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.utils.rates.Rates;
import com.aionemu.gameserver.utils.rates.RegularRates;
import com.aionemu.gameserver.utils.stats.StatFunctions;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.WorldType;
import com.aionemu.gameserver.world.zone.ZoneInstance;

/**
 * This class is representing Player object, it contains all needed data.
 * 
 * 
 * @author -Nemesiss-
 * @author SoulKeeper
 * @author alexa026
 * @author IlBuono
 */
public class Player extends Creature implements IReward, IDialogSelect, ITeamProperties
{
	private static final Logger			log				= Logger.getLogger(Player.class);

	private PlayerAppearance			playerAppearance;
	private PlayerAppearance			savedPlayerAppearance;
	private LegionMember				legionMember;
	private MacroList					macroList;
	private SkillList					skillList;
	private FriendList					friendList;
	private BlockList					blockList;
	private final ResponseRequester		requester;
	private boolean						lookingForGroup	= false;
	private Storage						inventory;
	private final Storage[]				petBag			= new Storage[4];
	private Storage						regularWarehouse;
	private Storage						accountWarehouse;
	private Equipment					equipment;
	private Mailbox						mailbox;
	private PrivateStore				store;
	private PlayerStatsTemplate			playerStatsTemplate;
	private TitleList					titleList;
	private PlayerSettings				playerSettings;
	private QuestStateList				questStateList;
	private final List<Integer>			nearbyQuestList	= new ArrayList<Integer>();
	private ZoneInstance				zoneInstance;
	private PlayerGroup					playerGroup;
	private AbyssRank					abyssRank;
	private Rates						rates;
	private RecipeList					recipeList;
	private int							flyState		= 0;
	private boolean						isTrading;
	private long						prisonTimer		= 0;
	private long						startPrison;
	private boolean						invul;
	private FlyController				flyController;
	private ReviveController			reviveController;
	private CraftingTask				craftingTask;
	private int							flightTeleportId;
	private int							flightDistance;
	private Summon						summon;
	private ToyPet						toyPet;
	private Kisk						kisk;
	private final Prices				prices;
	private boolean						isGagged		= false;
	private boolean						edit_mode		= false;
	private boolean						isInShutdownProgress;

	/**
	 * Zone update mask
	 */
	private volatile byte				zoneUpdateMask;

	private long						lastAttackMilis	= 0;
	private Map<Integer, ItemCooldown>	itemCoolDowns;

	/**
	 * Static information for players
	 */
	private static final int			CUBE_SPACE		= 9;
	private static final int			WAREHOUSE_SPACE	= 8;

	/**
	 * Connection of this Player.
	 */
	private AionChannelHandler			clientConnection;

	public Player(PlayerCommonData plCommonData, PlayerAppearance appereance)
	{
		super(plCommonData.getPlayerObjId(), null);
		this.objectTemplate = plCommonData;
		this.playerAppearance = appereance;
		this.position = plCommonData.getPosition();
		this.prices = new Prices();
		this.requester = new ResponseRequester(this);
		this.questStateList = new QuestStateList();
		this.titleList = new TitleList();
	}

	public PlayerCommonData getCommonData()
	{
		return (PlayerCommonData) objectTemplate;
	}

	/**
	 * @param playerCommonData the playerCommonData to set
	 */
	public void setCommonData(PlayerCommonData playerCommonData)
	{
		this.objectTemplate = playerCommonData;
	}

	public PlayerAppearance getPlayerAppearance()
	{
		return playerAppearance;
	}

	public void setPlayerAppearance(PlayerAppearance playerAppearance)
	{
		this.playerAppearance = playerAppearance;
	}

	/**
	 * Only use for the Size admin command
	 * 
	 * @return PlayerAppearance : The saved player's appearance, to rollback his appearance
	 */
	public PlayerAppearance getSavedPlayerAppearance()
	{
		return savedPlayerAppearance;
	}

	/**
	 * Only use for the Size admin command
	 * 
	 * @param playerAppearance PlayerAppearance : The saved player's appearance, to rollback his appearance
	 */
	public void setSavedPlayerAppearance(PlayerAppearance savedPlayerAppearance)
	{
		this.savedPlayerAppearance = savedPlayerAppearance;
	}

	/**
	 * Set connection of this player.
	 * 
	 * @param clientConnection
	 */
	public void setClientConnection(AionChannelHandler clientConnection)
	{
		this.clientConnection = clientConnection;
	}

	/**
	 * Get connection of this player.
	 * 
	 * @return AionConnection of this player.
	 * 
	 */
	public AionChannelHandler getClientConnection()
	{
		return this.clientConnection;
	}

	public MacroList getMacroList()
	{
		return macroList;
	}

	public void setMacroList(MacroList macroList)
	{
		this.macroList = macroList;
	}

	public SkillList getSkillList()
	{
		return skillList;
	}

	public void setSkillList(SkillList skillList)
	{
		this.skillList = skillList;
	}

	/**
	 * @return the toyPet
	 */
	public ToyPet getToyPet()
	{
		return toyPet;
	}

	/**
	 * @param toyPet the toyPet to set
	 */
	public void setToyPet(ToyPet toyPet)
	{
		this.toyPet = toyPet;
	}

	/**
	 * Gets this players Friend List
	 * 
	 * @return FriendList
	 */
	public FriendList getFriendList()
	{
		return friendList;
	}

	/**
	 * Is this player looking for a group
	 * 
	 * @return true or false
	 */
	public boolean isLookingForGroup()
	{
		return lookingForGroup;
	}

	/**
	 * Sets whether or not this player is looking for a group
	 * 
	 * @param lookingForGroup
	 */
	public void setLookingForGroup(boolean lookingForGroup)
	{
		this.lookingForGroup = lookingForGroup;
	}

	/**
	 * Sets this players friend list. <br />
	 * Remember to send the player the <tt>SM_FRIEND_LIST</tt> packet.
	 * 
	 * @param list
	 */
	public void setFriendList(FriendList list)
	{
		this.friendList = list;
	}

	public BlockList getBlockList()
	{
		return blockList;
	}

	public void setBlockList(BlockList list)
	{
		this.blockList = list;
	}

	/**
	 * @return the playerLifeStats
	 */
	@Override
	public PlayerLifeStats getLifeStats()
	{
		return (PlayerLifeStats) super.getLifeStats();
	}

	/**
	 * @param lifeStats
	 *            the lifeStats to set
	 */
	public void setLifeStats(PlayerLifeStats lifeStats)
	{
		super.setLifeStats(lifeStats);
	}

	/**
	 * @return the gameStats
	 */
	@Override
	public PlayerGameStats getGameStats()
	{
		return (PlayerGameStats) super.getGameStats();
	}

	/**
	 * @param gameStats
	 *            the gameStats to set
	 */
	public void setGameStats(PlayerGameStats gameStats)
	{
		super.setGameStats(gameStats);
	}

	/**
	 * Gets the ResponseRequester for this player
	 * 
	 * @return ResponseRequester
	 */
	public ResponseRequester getResponseRequester()
	{
		return requester;
	}

	public boolean isOnline()
	{
		return getClientConnection() != null;
	}

	public int getCubeSize()
	{
		return getCommonData().getCubeSize();
	}

	public PlayerClass getPlayerClass()
	{
		return getCommonData().getPlayerClass();
	}

	public Gender getGender()
	{
		return getCommonData().getGender();
	}

	/**
	 * @return the inventory
	 */

	public Equipment getEquipment()
	{
		return equipment;
	}

	public void setEquipment(Equipment equipment)
	{

		this.equipment = equipment;
	}

	/**
	 * @return the player private store
	 */
	public PrivateStore getStore()
	{
		return store;
	}

	/**
	 * @param store the store that needs to be set
	 */
	public void setStore(PrivateStore store)
	{
		if (this.store != null && store == null)
			this.store.clear();
		this.store = store;
	}

	/**
	 * @return the questStatesList
	 */
	public QuestStateList getQuestStateList()
	{
		return questStateList;
	}

	/**
	 * @param questStateList
	 *            the QuestStateList to set
	 */
	public void setQuestStateList(QuestStateList questStateList)
	{
		this.questStateList = questStateList;
	}

	/**
	 * @return the playerStatsTemplate
	 */
	public PlayerStatsTemplate getPlayerStatsTemplate()
	{
		return playerStatsTemplate;
	}

	/**
	 * @param playerStatsTemplate
	 *            the playerStatsTemplate to set
	 */
	public void setPlayerStatsTemplate(PlayerStatsTemplate playerStatsTemplate)
	{
		this.playerStatsTemplate = playerStatsTemplate;
	}

	public List<Integer> getNearbyQuests()
	{
		return nearbyQuestList;
	}

	public RecipeList getRecipeList()
	{
		return recipeList;
	}

	public void setRecipeList(RecipeList recipeList)
	{
		this.recipeList = recipeList;
	}

	/**
	 * @param inventory
	 *            the inventory to set Inventory should be set right after player object is created
	 */
	public void setStorage(Storage storage, StorageType storageType)
	{
		if (storageType == StorageType.CUBE)
		{
			this.inventory = storage;
		}

		if (storageType.getId() > 31 && storageType.getId() < 36)
		{
			this.petBag[storageType.getId() - 32] = storage;
		}

		if (storageType == StorageType.REGULAR_WAREHOUSE)
		{
			this.regularWarehouse = storage;
		}

		if (storageType == StorageType.ACCOUNT_WAREHOUSE)
		{
			this.accountWarehouse = storage;
		}
	}

	/**
	 * 
	 * @param storageType
	 * @return
	 */
	public Storage getStorage(int storageType)
	{
		if (storageType == StorageType.REGULAR_WAREHOUSE.getId())
			return regularWarehouse;

		if (storageType == StorageType.ACCOUNT_WAREHOUSE.getId())
			return accountWarehouse;

		if (storageType == StorageType.LEGION_WAREHOUSE.getId())
			return getLegion().getLegionWarehouse();

		if (storageType > 31 && storageType < 36)
			return petBag[storageType - 32];

		if (storageType == StorageType.CUBE.getId())
			return inventory;
		else
			return null;
	}

	/**
	 *  Items from UPDATE_REQUIRED storages and equipment
	 *  
	 * @return
	 */
	public List<Item> getDirtyItemsToUpdate()
	{
		List<Item> dirtyItems = new ArrayList<Item>();

		Storage cubeStorage = getStorage(StorageType.CUBE.getId());
		if (cubeStorage.getPersistentState() == PersistentState.UPDATE_REQUIRED)
		{
			dirtyItems.addAll(cubeStorage.getAllItems());
			dirtyItems.addAll(cubeStorage.getDeletedItems());
			cubeStorage.setPersistentState(PersistentState.UPDATED);
		}

		Storage regularWhStorage = getStorage(StorageType.REGULAR_WAREHOUSE.getId());
		if (regularWhStorage.getPersistentState() == PersistentState.UPDATE_REQUIRED)
		{
			dirtyItems.addAll(regularWhStorage.getAllItems());
			dirtyItems.addAll(regularWhStorage.getDeletedItems());
			regularWhStorage.setPersistentState(PersistentState.UPDATED);
		}

		Storage accountWhStorage = getStorage(StorageType.ACCOUNT_WAREHOUSE.getId());
		if (accountWhStorage.getPersistentState() == PersistentState.UPDATE_REQUIRED)
		{
			dirtyItems.addAll(accountWhStorage.getAllItems());
			dirtyItems.addAll(accountWhStorage.getDeletedItems());
			accountWhStorage.setPersistentState(PersistentState.UPDATED);
		}

		for (int petBagId = 32; petBagId < 36; petBagId++)
		{
			Storage petBag = getStorage(petBagId);
			if (petBag != null && petBag.getPersistentState() == PersistentState.UPDATE_REQUIRED)
			{
				dirtyItems.addAll(petBag.getAllItems());
				dirtyItems.addAll(petBag.getDeletedItems());
				petBag.setPersistentState(PersistentState.UPDATED);
			}
		}

		Equipment equipment = getEquipment();
		if (equipment.getPersistentState() == PersistentState.UPDATE_REQUIRED)
		{
			dirtyItems.addAll(equipment.getEquippedItems());
			equipment.setPersistentState(PersistentState.UPDATED);
		}

		return dirtyItems;
	}

	/**
	 *  //TODO probably need to optimize here
	 *  
	 * @return
	 */
	public List<Item> getAllItems()
	{
		List<Item> allItems = new ArrayList<Item>();

		Storage cubeStorage = getStorage(StorageType.CUBE.getId());
		allItems.addAll(cubeStorage.getAllItems());

		Storage regularWhStorage = getStorage(StorageType.REGULAR_WAREHOUSE.getId());
		allItems.addAll(regularWhStorage.getStorageItems());

		Storage accountWhStorage = getStorage(StorageType.ACCOUNT_WAREHOUSE.getId());
		allItems.addAll(accountWhStorage.getStorageItems());

		for (int petBagId = 32; petBagId < 36; petBagId++)
		{
			Storage petBag = getStorage(petBagId);
			allItems.addAll(petBag.getAllItems());
		}

		Equipment equipment = getEquipment();
		allItems.addAll(equipment.getEquippedItems());

		return allItems;
	}

	public Storage getInventory()
	{
		return inventory;
	}

	/**
	 * @param CubeUpgrade
	 *            int Sets the cubesize
	 */
	public void setCubesize(int cubesize)
	{
		getCommonData().setCubesize(cubesize);
		getInventory().setLimit(getInventory().getLimit() + (cubesize * CUBE_SPACE));
	}

	/**
	 * @return the playerSettings
	 */
	public PlayerSettings getPlayerSettings()
	{
		return playerSettings;
	}

	/**
	 * @param playerSettings
	 *            the playerSettings to set
	 */
	public void setPlayerSettings(PlayerSettings playerSettings)
	{
		this.playerSettings = playerSettings;
	}

	/**
	 * @return the zoneInstance
	 */
	public ZoneInstance getZoneInstance()
	{
		return zoneInstance;
	}

	/**
	 * @param zoneInstance
	 *            the zoneInstance to set
	 */
	public void setZoneInstance(ZoneInstance zoneInstance)
	{
		this.zoneInstance = zoneInstance;
	}

	public TitleList getTitleList()
	{
		return titleList;
	}

	public void setTitleList(TitleList titleList)
	{
		this.titleList = titleList;
		titleList.setOwner(this);
	}

	/**
	 * @return the playerGroup
	 */
	public PlayerGroup getPlayerGroup()
	{
		return playerGroup;
	}

	/**
	 * @param playerGroup
	 *            the playerGroup to set
	 */
	public void setPlayerGroup(PlayerGroup playerGroup)
	{
		this.playerGroup = playerGroup;
	}

	/**
	 * @return the abyssRank
	 */
	public AbyssRank getAbyssRank()
	{
		return abyssRank;
	}

	/**
	 * @param abyssRank
	 *            the abyssRank to set
	 */
	public void setAbyssRank(AbyssRank abyssRank)
	{
		this.abyssRank = abyssRank;
	}

	@Override
	public PlayerEffectController getEffectController()
	{
		return (PlayerEffectController) super.getEffectController();
	}

	@Override
	public void initializeAi()
	{
		// Empty
	}

	/**
	 * <b><font color='red'>NOTICE: </font>this method is supposed to be called only from
	 * {@link PlayerService#playerLoggedIn(Player)}</b>
	 */
	public void onLoggedIn()
	{
		addTask(TaskId.PLAYER_UPDATE,
				ThreadPoolManager.getInstance().scheduleAtFixedRate(new GeneralUpdateTask(this), OptionsConfig.PLAYER_GENERAL * 1000,
						OptionsConfig.PLAYER_GENERAL * 1000));
	}

	/**
	 * <b><font color='red'>NOTICE: </font>this method is supposed to be called only from
	 * {@link PlayerService#playerLoggedOut(Player)}</b>
	 */
	public void onLoggedOut()
	{
		requester.denyAll();
		friendList.setStatus(FriendList.Status.OFFLINE);
		BrokerService.getInstance().removePlayerCache(this);
		ExchangeService.getInstance().cancelExchange(this);
	}

	/**
	 * Returns true if has valid LegionMember
	 */
	public boolean isLegionMember()
	{
		return legionMember != null;
	}

	/**
	 * @param legionMember
	 *            the legionMember to set
	 */
	public void setLegionMember(LegionMember legionMember)
	{
		this.legionMember = legionMember;
	}

	/**
	 * @return the legionMember
	 */
	public LegionMember getLegionMember()
	{
		return legionMember;
	}

	/**
	 * @return the legion
	 */
	public Legion getLegion()
	{
		if (legionMember != null)
			return legionMember.getLegion();
		else
			return null;
	}

	/**
	 * Checks if object id's are the same
	 * 
	 * @return true if the object id is the same
	 */
	public boolean sameObjectId(int objectId)
	{
		return this.getObjectId() == objectId;
	}

	/**
	 * @return true if a player has a store opened
	 */
	public boolean hasStore()
	{
		if (getStore() != null)
			return true;
		return false;
	}

	/**
	 * Removes legion from player
	 */
	public void resetLegionMember()
	{
		setLegionMember(null);
	}

	/**
	 * This method will return true if player is in a group
	 * 
	 * @return true or false
	 */
	public boolean isInGroup()
	{
		return playerGroup != null;
	}

	/**
	 * Access level of this player
	 * 
	 * @return byte
	 */
	public byte getAccessLevel()
	{
		return getClientConnection().getAccount().getAccessLevel();
	}

	/**
	 * accountName of this player
	 * 
	 * @return int
	 */
	public String getAcountName()
	{
		return getClientConnection().getAccount().getName();
	}

	/**
	 * @return the rates
	 */
	public Rates getRates()
	{
		if (rates == null)
			rates = new RegularRates();
		return rates;
	}

	/**
	 * @param rates
	 *            the rates to set
	 */
	public void setRates(Rates rates)
	{
		this.rates = rates;
	}

	/**
	 * @return warehouse size
	 */
	public int getWarehouseSize()
	{
		return getCommonData().getWarehouseSize();
	}

	/**
	 * @param warehouseSize
	 */
	public void setWarehouseSize(int warehouseSize)
	{
		getCommonData().setWarehouseSize(warehouseSize);
		getWarehouse().setLimit(getWarehouse().getLimit() + (warehouseSize * WAREHOUSE_SPACE));
	}

	/**
	 * @return regularWarehouse
	 */
	public Storage getWarehouse()
	{
		return regularWarehouse;
	}

	/**
	 * 0: regular, 1: fly, 2: glide
	 */
	public int getFlyState()
	{
		return this.flyState;
	}

	public void setFlyState(int flyState)
	{
		this.flyState = flyState;
	}

	/**
	 * @return the isTrading
	 */
	public boolean isTrading()
	{
		return isTrading;
	}

	/**
	 * @param isTrading the isTrading to set
	 */
	public void setTrading(boolean isTrading)
	{
		this.isTrading = isTrading;
	}

	/**
	 * @return the isInPrison
	 */
	public boolean isInPrison()
	{
		return prisonTimer != 0;
	}

	/**
	 * @param prisonTimer the prisonTimer to set
	 */
	public void setPrisonTimer(long prisonTimer)
	{
		if (prisonTimer < 0)
			prisonTimer = 0;

		this.prisonTimer = prisonTimer;
	}

	/**
	 * @return the prisonTimer
	 */
	public long getPrisonTimer()
	{
		return prisonTimer;
	}

	/**
	 * @return the time in ms of start prison
	 */
	public long getStartPrison()
	{
		return startPrison;
	}

	/**
	 * @param start : The time in ms of start prison
	 */
	public void setStartPrison(long start)
	{
		this.startPrison = start;
	}

	/**
	 * @return
	 */
	public boolean isProtectionActive()
	{
		return isInVisualState(CreatureVisualState.BLINKING);
	}

	/**
	 * Check is player is invul
	 * 
	 * @return boolean
	 **/
	public boolean isInvul()
	{
		return invul;
	}

	/**
	 * Sets invul on player
	 * 
	 * @param invul
	 *            - boolean
	 **/
	public void setInvul(boolean invul)
	{
		this.invul = invul;
	}

	public void setMailbox(Mailbox mailbox)
	{
		this.mailbox = mailbox;
	}

	public Mailbox getMailbox()
	{
		return mailbox;
	}

	/**
	 * @return the flyController
	 */
	public FlyController getFlyController()
	{
		return flyController;
	}

	/**
	 * @param flyController the flyController to set
	 */
	public void setFlyController(FlyController flyController)
	{
		this.flyController = flyController;
	}

	public ReviveController getReviveController()
	{
		return reviveController;
	}

	public void setReviveController(ReviveController reviveController)
	{
		this.reviveController = reviveController;
	}

	public int getLastOnline()
	{
		Timestamp lastOnline = getCommonData().getLastOnline();
		if (lastOnline == null || isOnline())
			return 0;

		return (int) (lastOnline.getTime() / 1000);
	}

	/**
	 * 
	 * @param craftingTask
	 */
	public void setCraftingTask(CraftingTask craftingTask)
	{
		this.craftingTask = craftingTask;
	}

	/**
	 * 
	 * @return
	 */
	public CraftingTask getCraftingTask()
	{
		return craftingTask;
	}

	/**
	 * 
	 * @param flightTeleportId
	 */
	public void setFlightTeleportId(int flightTeleportId)
	{
		this.flightTeleportId = flightTeleportId;
	}

	/**
	 * 
	 * @return flightTeleportId
	 */
	public int getFlightTeleportId()
	{
		return flightTeleportId;
	}

	/**
	 * 
	 * @param flightDistance
	 */
	public void setFlightDistance(int flightDistance)
	{
		this.flightDistance = flightDistance;
	}

	/**
	 * 
	 * @return flightDistance
	 */
	public int getFlightDistance()
	{
		return flightDistance;
	}

	/**
	 * @return
	 */
	public boolean isUsingFlyTeleport()
	{
		return isInState(CreatureState.FLIGHT_TELEPORT) && flightTeleportId != 0;
	}

	public boolean isGM()
	{
		return getAccessLevel() > 0;
	}

	/**
	 * Npc enemies:<br>
	 * - monsters<br>
	 * - aggressive npcs<br>
	 * @param npc
	 * @return
	 */
	/*	@Override
		public boolean isEnemyNpc(Npc npc)
		{
			return npc instanceof Monster || npc.isAggressiveTo(this);
		}*/

	/**
	 * Player enemies:<br>
	 * - different race<br>
	 * - duel partner<br>
	 * 
	 * @param player
	 * @return
	 */
	/*	@Override
		public boolean isEnemyPlayer(Player player)
		{
			return player.getCommonData().getRace() != getCommonData().getRace() || isDueling(player);
		}*/

	/**
	 * Summon enemies:<br>
	 * - master not null and master is enemy<br>
	 */
	/*	@Override
		public boolean isEnemySummon(Summon summon)
		{
			return summon.getMaster() != null && isEnemyPlayer(summon.getMaster());
		}*/

	/**
	 * Player-player friends:<br>
	 * - not in duel<br>
	 * - same race<br>
	 * 
	 * @param player
	 * @return
	 */
	public boolean isFriend(Player player)
	{
		return player.getCommonData().getRace() == getCommonData().getRace() && !isDueling(player);
	}

	/*	@Override
		public TribeClass getTribe()
		{
			switch (getCommonData().getRace())
			{
				case ELYOS:
					return TribeClass.PC;
				default:
					return TribeClass.PC_DARK;
			}
		}*/

	/*	@Override
		public boolean isAggressiveTo(Creature creature)
		{
			return creature.isAggroFrom(this);
		}*/

	/*	@Override
		public boolean isAggroFrom(Creature npc)
		{
			//siege npc are always aggro on players, without level limitation
			// npc's that are 10 or more levels lower don't get aggro on players
			if (!(npc instanceof SiegeNpc) && npc.getLevel() + 10 <= getLevel())
				return false;
			else
				return isAggroIconTo(npc);
		}*/

	/**
	 * Used in SM_NPC_INFO to check aggro irrespective to level
	 * 
	 * @param npcTribe
	 * @return
	 */
	/*	private boolean isAggroIconTo(Creature npc)
		{
			switch (getCommonData().getRace())
			{
				case ELYOS:
					if (npc.getObjectTemplate().getTribe().isGuard() && npc.getObjectTemplate().getRace() == Race.ASMODIANS)
						return true;
					return DataManager.TRIBE_RELATIONS_DATA.isAggressiveRelation(npc.getObjectTemplate().getTribe(), TribeClass.PC);
				case ASMODIANS:
					if (npc.getObjectTemplate().getTribe().isGuard() && npc.getObjectTemplate().getRace() == Race.ELYOS)
						return true;
					return DataManager.TRIBE_RELATIONS_DATA.isAggressiveRelation(npc.getObjectTemplate().getTribe(), TribeClass.PC_DARK);
			}
			return false;
		}*/

	@Override
	protected boolean canSeeNpc(Npc npc)
	{
		return true; //TODO
	}

	@Override
	protected boolean canSeePlayer(Player player)
	{
		return player.getVisualState() <= getSeeState();
	}

	/**
	 * @return the summon
	 */
	public Summon getSummon()
	{
		return summon;
	}

	/**
	 * @param summon the summon to set
	 */
	public void setSummon(Summon summon)
	{
		this.summon = summon;
	}

	/**
	 * @param new kisk to bind to (null if unbinding)
	 */
	public void setKisk(Kisk newKisk)
	{
		this.kisk = newKisk;
	}

	/**
	 * @return
	 */
	public Kisk getKisk()
	{
		return this.kisk;
	}

	/**
	 * 
	 * @param delayId
	 * @return
	 */
	public boolean isItemUseDisabled(int delayId)
	{
		if (itemCoolDowns == null || !itemCoolDowns.containsKey(delayId))
			return false;

		Long coolDown = itemCoolDowns.get(delayId).getReuseTime();
		if (coolDown == null)
			return false;

		if (coolDown < System.currentTimeMillis())
		{
			itemCoolDowns.remove(delayId);
			return false;
		}

		return true;
	}

	/**
	 * 
	 * @param itemMask
	 * @return
	 */
	public long getItemCoolDown(int itemMask)
	{
		if (itemCoolDowns == null || !itemCoolDowns.containsKey(itemMask))
			return 0;

		return itemCoolDowns.get(itemMask).getReuseTime();
	}

	/**
	 * @return the itemCoolDowns
	 */
	public Map<Integer, ItemCooldown> getItemCoolDowns()
	{
		return itemCoolDowns;
	}

	/**
	 * 
	 * @param delayId
	 * @param time
	 * @param useDelay
	 */
	public void addItemCoolDown(int delayId, long time, int useDelay)
	{
		if (itemCoolDowns == null)
			itemCoolDowns = new FastMap<Integer, ItemCooldown>().shared();

		itemCoolDowns.put(delayId, new ItemCooldown(time, useDelay));
	}

	/**
	 * 
	 * @param itemMask
	 */
	public void removeItemCoolDown(int itemMask)
	{
		if (itemCoolDowns == null)
			return;
		itemCoolDowns.remove(itemMask);
	}

	/**
	 * @return prices
	 */
	public Prices getPrices()
	{
		return this.prices;
	}

	/**
	 * @param isGagged the isGagged to set
	 */
	public void setGagged(boolean isGagged)
	{
		this.isGagged = isGagged;
	}

	/**
	 * @return the isGagged
	 */
	public boolean isGagged()
	{
		return isGagged;
	}

	private class GeneralUpdateTask implements Runnable
	{
		private final Player	player;

		private GeneralUpdateTask(Player player)
		{
			this.player = player;
		}

		@Override
		public void run()
		{
			try
			{
				DAOManager.getDAO(AbyssRankDAO.class).storeAbyssRank(player);
				DAOManager.getDAO(PlayerSkillListDAO.class).storeSkills(player);
				DAOManager.getDAO(PlayerQuestListDAO.class).store(player);
				DAOManager.getDAO(PlayerDAO.class).storePlayer(player);
			}
			catch (Exception ex)
			{
				log.error("Exception during periodic saving of player " + player.getName() + " " + ex.getCause() != null ? ex.getCause().getMessage() : "null");
			}
		}
	}

	public void setPlayerAlliance(PlayerAlliance playerAlliance)
	{
		this.playerAlliance = playerAlliance;
	}

	private PlayerAlliance	playerAlliance;

	/**
	 * @return
	 */
	public PlayerAlliance getPlayerAlliance()
	{
		return playerAlliance;
	}

	/**
	 * @return boolean value is in alliance
	 */
	public boolean isInAlliance()
	{
		return (this.playerAlliance != null);
	}

	/**
	 * @author IlBuono
	*/
	public void setEditMode(boolean edit_mode)
	{
		this.edit_mode = edit_mode;
	}

	/**
	 * @author IlBuono
	*/
	public boolean isInEditMode()
	{
		return edit_mode;
	}

	/**
	 * Cancel current skill and remove cooldown
	 */
	@Override
	public void cancelCurrentSkill()
	{
		Skill castingSkill = getCastingSkill();
		if (castingSkill != null)
		{
			int skillId = castingSkill.getSkillTemplate().getSkillId();
			removeSkillCoolDown(skillId);
			setCasting(null);
			PacketSendUtility.sendPacket(this, new SM_SKILL_CANCEL(this, skillId));
			PacketSendUtility.sendPacket(this, SM_SYSTEM_MESSAGE.STR_SKILL_CANCELED());
		}
	}

	@Override
	public void see(VisibleObject object)
	{
		//		super.see(object);
		if (object instanceof Player)
		{
			Player player = (Player) object;
			if (player.getToyPet() != null)
			{
				log.debug("Player " + getName() + " sees " + object.getName() + " that has toypet");
				PacketSendUtility.sendPacket(this, new SM_PET(3, player.getToyPet()));
			}
			{
				getEffectController().sendEffectIconsTo((Player) object);
			}
		}
	}

	@Override
	public void notSee(VisibleObject object, boolean isOutOfRange)
	{
		super.notSee(object, isOutOfRange);
		if (object instanceof Npc)
		{
			boolean update = false;
			for (int questId : QuestEngine.getInstance().getNpcQuestData(((Npc) object).getNpcId()).getOnQuestStart())
			{
				if (QuestService.checkStartCondition(new QuestEnv(object, this, questId, 0)))
				{
					if (getNearbyQuests().contains(questId))
					{
						update = true;
						getNearbyQuests().remove(getNearbyQuests().indexOf(questId));
					}
				}
			}
			if (update)
				updateNearbyQuestList();
		}
	}

	public void updateNearbyQuests()
	{
		getNearbyQuests().clear();
		for (VisibleObject obj : getKnownList().getKnownObjects().values())
		{
			if (obj instanceof Npc)
			{
				for (int questId : QuestEngine.getInstance().getNpcQuestData(((Npc) obj).getNpcId()).getOnQuestStart())
				{
					if (QuestService.checkStartCondition(new QuestEnv(obj, this, questId, 0)))
					{
						if (!getNearbyQuests().contains(questId))
						{
							getNearbyQuests().add(questId);
						}
					}
				}
			}
		}
		updateNearbyQuestList();
	}

	/**
	 * Will be called by ZoneManager when player enters specific zone
	 * 
	 * @param zoneInstance
	 */
	public void onEnterZone(ZoneInstance zoneInstance)
	{
		QuestEngine.getInstance().onEnterZone(new QuestEnv(null, this, 0, 0), zoneInstance.getTemplate().getName());
		WorldType worldType = World.getInstance().getWorldMap(getWorldId()).getWorldType();
		if (worldType == WorldType.ABYSS)
			checkEnemyAbyssBase();
	}

	/**
	 * Check if the player has entered the Protective Shield of the Enemy Abyss Base
	 *
	 */
	public void checkEnemyAbyssBase()
	{
		if (getLifeStats().isAlreadyDead() || isGM())
			return;
		if ((getCommonData().getRace() == Race.ELYOS) && (MathUtil.isInSphere(this, 878.6906f, 2950.4717f, 1646.4772f, 290)))
			die();
		else if ((getCommonData().getRace() == Race.ASMODIANS) && (MathUtil.isInSphere(this, 2979.622f, 906.5588f, 1540.2731f, 290)))
			die();
	}

	/**
	 * Will be called by ZoneManager when player leaves specific zone
	 * 
	 * @param zoneInstance
	 */
	public void onLeaveZone(ZoneInstance zoneInstance)
	{

	}

	/**
	 * Set zone instance as null (Where no zones defined)
	 */
	public void resetZone()
	{
		setZoneInstance(null);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * Should only be triggered from one place (life stats)
	 */
	@Override
	public void onDie(Creature lastAttacker)
	{

		Creature master = null;
		if (lastAttacker != null && lastAttacker instanceof ISummoned)
			master = ((ISummoned) lastAttacker).getMaster();

		if (master instanceof Player)
		{
			if (isDueling((Player) master))
			{
				DuelService.getInstance().onDie(this);
				return;
			}
		}

		// Effects removed with super.onDie()
		boolean hasSelfRezEffect = getReviveController().checkForSelfRezEffect(this);

		super.onDie(lastAttacker);

		if (master instanceof Npc || master == this)
		{
			if (getLevel() > 4)
				getCommonData().calculateExpLoss();
		}

		/**
		 * Release summon
		 */
		Summon summon = getSummon();
		if (summon != null)
			summon.release(UnsummonType.UNSPECIFIED);

		// SM_DIE Packet
		int kiskTimeRemaining = (getKisk() != null ? getKisk().getRemainingLifetime() : 0);
		boolean hasSelfRezItem = getReviveController().checkForSelfRezItem(this);
		PacketSendUtility.sendPacket(this, new SM_DIE(hasSelfRezEffect, hasSelfRezItem, kiskTimeRemaining));

		PacketSendUtility.sendPacket(this, SM_SYSTEM_MESSAGE.DIE);
		QuestEngine.getInstance().onDie(new QuestEnv(null, this, 0, 0));
	}

	@Override
	public void doReward()
	{
		PvpService.getInstance().doReward(this);

		// DP reward 
		// TODO: Figure out what DP reward should be for PvP
		//int currentDp = winner.getCommonData().getDp();
		//int dpReward = StatFunctions.calculateSoloDPReward(winner, getOwner());
		//winner.getCommonData().setDp(dpReward + currentDp);

	}

	@Override
	public void onRespawn()
	{
		unsetState(CreatureState.DEAD);
		startProtectionActiveTask();
	}

	@Override
	public void attackTarget(Creature target)
	{

		PlayerGameStats gameStats = getGameStats();

		// check player attack Z distance
		if (Math.abs(getZ() - target.getZ()) > 6)
			return;

		if (!RestrictionsManager.canAttack(this, target))
			return;

		int attackSpeed = gameStats.getCurrentStat(StatEnum.ATTACK_SPEED);
		long milis = System.currentTimeMillis();
		if (milis - lastAttackMilis < attackSpeed)
		{
			/**
			 * Hack!
			 */
			return;
		}
		lastAttackMilis = milis;

		/**
		 * notify attack observers
		 */
		super.attackTarget(target);
	}

	@Override
	public void onAttack(Creature creature, int skillId, TYPE type, int damage)
	{
		if (isInvul())
			damage = 0;

		super.onAttack(creature, skillId, type, damage);
	}

	/**
	 * @param skillId
	 * @param targetType
	 * @param x
	 * @param y
	 * @param z
	 */
	public void useSkill(int skillId, int targetType, float x, float y, float z)
	{

		Skill skill = SkillEngine.getInstance().getSkillFor(this, skillId, getTarget());

		if (skill != null)
		{
			skill.setTargetType(targetType, x, y, z);
			if (!RestrictionsManager.canUseSkill(this, skill))
				return;

			skill.useSkill();
		}
	}

	@Override
	public void onMove()
	{
		super.onMove();
		addZoneUpdateMask(ZoneUpdateMode.ZONE_UPDATE);
	}

	@Override
	public void onStopMove()
	{
		super.onStopMove();
	}

	@Override
	public void onStartMove()
	{
		cancelCurrentSkill();
		super.onStartMove();
	}

	/**
	 * 
	 */
	public void updatePassiveStats()
	{
		for (SkillListEntry skillEntry : getSkillList().getAllSkills())
		{
			Skill skill = SkillEngine.getInstance().getSkillFor(this, skillEntry.getSkillId(), getTarget());
			if (skill != null && skill.isPassive())
			{
				skill.useSkill();
			}
		}
	}

	@Override
	public void onRestore(HealType healType, int value)
	{
		super.onRestore(healType, value);
		switch (healType)
		{
			case DP:
				getCommonData().addDp(value);
				break;
		}
	}

	/**
	 * 
	 * @param player
	 * @return
	 */
	public boolean isDueling(Player player)
	{
		return DuelService.getInstance().isDueling(player.getObjectId(), getObjectId());
	}

	public void updateNearbyQuestList()
	{
		addPacketBroadcastMask(BroadcastMode.UPDATE_NEARBY_QUEST_LIST);
	}

	public void updateNearbyQuestListImpl()
	{
		PacketSendUtility.sendPacket(this, new SM_NEARBY_QUESTS(getNearbyQuests()));
	}

	public boolean isInShutdownProgress()
	{
		return isInShutdownProgress;
	}

	public void setInShutdownProgress(boolean isInShutdownProgress)
	{
		this.isInShutdownProgress = isInShutdownProgress;
	}

	/**
	 * Handle dialog
	 */
	@Override
	public void onDialogSelect(int dialogId, Player player, int questId)
	{
		switch (dialogId)
		{
			case 2:
				PacketSendUtility.sendPacket(player, new SM_PRIVATE_STORE(getStore()));
				break;
		}
	}

	/**
	 * @param level
	 */
	public void upgradePlayer(int level)
	{

		PlayerStatsTemplate statsTemplate = DataManager.PLAYER_STATS_DATA.getTemplate(this);
		setPlayerStatsTemplate(statsTemplate);

		// update stats after setting new template
		getGameStats().doLevelUpgrade();
		getLifeStats().synchronizeWithMaxStats();
		getLifeStats().updateCurrentStats();

		PacketSendUtility.broadcastPacket(this, new SM_LEVEL_UPDATE(getObjectId(), 0, level), true);

		QuestEngine.getInstance().onLvlUp(new QuestEnv(null, this, 0, 0));
		updateNearbyQuests();

		PacketSendUtility.sendPacket(this, new SM_STATS_INFO(this));

		if (level >= 10 && getSkillList().getSkillEntry(30001) != null)
		{
			int skillLevel = getSkillList().getSkillLevel(30001);
			getSkillList().removeSkill(this, 30001);
			PacketSendUtility.sendPacket(this, new SM_SKILL_LIST(this));
			getSkillList().addSkill(this, 30002, skillLevel, true);
		}
		// add new skills
		SkillLearnService.addNewSkills(this, false);

		/**
		 * Broadcast Update to all that may care.
		 */
		if (isInGroup())
			getPlayerGroup().updateGroupUIToEvent(this, GroupEvent.UPDATE);
		if (isInAlliance())
			AllianceService.getInstance().updateAllianceUIToEvent(this, PlayerAllianceEvent.UPDATE);
		if (isLegionMember())
			LegionService.getInstance().updateMemberInfo(this);
	}

	/**
	 * After entering game player char is "blinking" which means that it's in under some protection, after making an
	 * action char stops blinking. - Starts protection active - Schedules task to end protection
	 */
	public void startProtectionActiveTask()
	{
		setVisualState(CreatureVisualState.BLINKING);
		PacketSendUtility.broadcastPacket(this, new SM_PLAYER_STATE(this), true);
		Future<?> task = ThreadPoolManager.getInstance().schedule(new Runnable()
		{

			@Override
			public void run()
			{
				stopProtectionActiveTask();
			}
		}, 60000);
		addTask(TaskId.PROTECTION_ACTIVE, task);
	}

	/**
	 * Stops protection active task after first move or use skill
	 */
	public void stopProtectionActiveTask()
	{
		cancelTask(TaskId.PROTECTION_ACTIVE);
		if (isSpawned())
		{
			unsetVisualState(CreatureVisualState.BLINKING);
			PacketSendUtility.broadcastPacket(this, new SM_PLAYER_STATE(this), true);
		}
	}

	/**
	 * When player arrives at destination point of flying teleport
	 */
	public void onFlyTeleportEnd()
	{
		unsetState(CreatureState.FLIGHT_TELEPORT);
		setFlightTeleportId(0);
		setFlightDistance(0);
		setState(CreatureState.ACTIVE);
		addZoneUpdateMask(ZoneUpdateMode.ZONE_REFRESH);
	}

	/**
	 * Zone update mask management
	 * 
	 * @param mode
	 */
	public final void addZoneUpdateMask(ZoneUpdateMode mode)
	{
		zoneUpdateMask |= mode.mask();
		ZoneService.getInstance().add(this);
	}

	public final void removeZoneUpdateMask(ZoneUpdateMode mode)
	{
		zoneUpdateMask &= ~mode.mask();
	}

	public final byte getZoneUpdateMask()
	{
		return zoneUpdateMask;
	}

	/**
	 * Update zone taking into account the current zone
	 */
	public void updateZoneImpl()
	{
		ZoneService.getInstance().checkZone(this);
	}

	/**
	 * Refresh completely zone irrespective of the current zone
	 */
	public void refreshZoneImpl()
	{
		ZoneService.getInstance().findZoneInCurrentMap(this);
	}

	/**
	 * Check water level (start drowning) and map death level (die)
	 */
	public void checkWaterLevel()
	{
		World world = World.getInstance();
		float z = getZ();

		if (getLifeStats().isAlreadyDead())
			return;

		if (z < world.getWorldMap(getWorldId()).getDeathLevel())
		{
			die();
			return;
		}

		ZoneInstance currentZone = getZoneInstance();
		if (currentZone != null && currentZone.isBreath())
			return;

		//TODO need fix character height
		float playerheight = getPlayerAppearance().getHeight() * 1.6f;
		if (z < world.getWorldMap(getWorldId()).getWaterLevel() - playerheight)
			ZoneService.getInstance().startDrowning(this);
		else
			ZoneService.getInstance().stopDrowning(this);
	}

	@Override
	public void createSummon(int npcId, int skillLvl)
	{
		Summon summon = SpawnEngine.getInstance().spawnSummon(this, npcId, skillLvl);
		setSummon(summon);
		PacketSendUtility.sendPacket(this, new SM_SUMMON_PANEL(summon));
		PacketSendUtility.broadcastPacket(summon, new SM_EMOTION(summon, EmotionType.START_EMOTE2));
		PacketSendUtility.broadcastPacket(summon, new SM_SUMMON_UPDATE(summon));
	}

	public boolean addItems(int itemId, int count)
	{
		return ItemService.addItems(this, Collections.singletonList(new QuestItems(itemId, count)));
	}

	@Override
	public void getReward(Npc owner)
	{
		if (isInGroup())
			getPlayerGroup().getReward(owner);
		else
		{
			long expReward = StatFunctions.calculateSoloExperienceReward(this, owner);
			getCommonData().addExp(expReward);

			int currentDp = getCommonData().getDp();
			int dpReward = StatFunctions.calculateSoloDPReward(this, owner);
			getCommonData().setDp(dpReward + currentDp);

			WorldType worldType = World.getInstance().getWorldMap(getWorldId()).getWorldType();
			if (worldType == WorldType.ABYSS)
			{
				int apReward = StatFunctions.calculateSoloAPReward(this, owner);
				getCommonData().addAp(apReward);
			}

			QuestEngine.getInstance().onKill(new QuestEnv(owner, this, 0, 0));

			DropService.getInstance().registerDrop(owner, this, getLevel());
		}
	}
}

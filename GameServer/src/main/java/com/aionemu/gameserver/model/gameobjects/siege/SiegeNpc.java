/**
 * 
 */
package com.aionemu.gameserver.model.gameobjects.siege;

import com.aionemu.gameserver.controllers.siege.SiegeNpcController;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.siege.SiegeRace;
import com.aionemu.gameserver.model.templates.VisibleObjectTemplate;
import com.aionemu.gameserver.model.templates.spawn.SpawnTemplate;

/**
 * @author ViAl
 *
 */
public class SiegeNpc extends Npc {

	private int siegeId;
	SiegeRace siegeRace;
	
	/**
	 * @param objId
	 * @param controller
	 * @param spawnTemplate
	 * @param objectTemplate
	 * 
	 * SiegeNpc constructor
	 */
	public SiegeNpc	(int objId, SiegeNpcController controller, SpawnTemplate spawnTemplate,	VisibleObjectTemplate objectTemplate) 
	{
		super(objId, controller, spawnTemplate, objectTemplate);
		controller.setOwner(this);
		this.siegeId = spawnTemplate.getSpawnGroup().getSiegeId();
		this.siegeRace = spawnTemplate.getSpawnGroup().getRace();
	}
	
	public SiegeRace getSiegeRace() 
	{
		return siegeRace;
	}

	public int getSiegeId() 
	{
		return siegeId;
	}
	
	@Override
	public void initializeAi()
	{
	}
	
	@Override
	public SiegeNpcController getController()
	{
		return (SiegeNpcController) super.getController();
	}

}

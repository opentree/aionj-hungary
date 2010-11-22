/*
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, 
 * MA  02110-1301, USA.
 *
 * http://www.gnu.org/copyleft/gpl.html
 */
package com.aionemu.gameserver.model.gameobjects.instance;

import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.interfaces.IDialog;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.state.CreatureState;
import com.aionemu.gameserver.model.gameobjects.state.CreatureVisualState;
import com.aionemu.gameserver.model.gameobjects.stats.StaticNpcStats;
import com.aionemu.gameserver.model.templates.NpcTemplate;
import com.aionemu.gameserver.model.templates.spawn.SpawnTemplate;
import com.aionemu.gameserver.network.aion.serverpackets.SM_NPC_INFO;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.StaticObjectKnownList;
import com.aionemu.gameserver.world.WorldPosition;

/**
 * @author Mr. Poke
 *
 */
public class StaticNpc extends VisibleObject implements IDialog
{
	
	private int state = CreatureState.ACTIVE.getId();
	private int visualState = CreatureVisualState.VISIBLE.getId();
	private StaticNpcStats stats = new StaticNpcStats();

	/**
	 * @param objId
	 * @param spawnTemplate
	 * @param position
	 */
	public StaticNpc(int objId, SpawnTemplate spawnTemplate, WorldPosition position)
	{
		super(objId, spawnTemplate, position);
		this.objectTemplate = DataManager.NPC_DATA.getNpcTemplate(spawnTemplate.getTemplateId());
		this.setKnownlist(new StaticObjectKnownList(this));
	}

	/* (non-Javadoc)
	 * @see com.aionemu.gameserver.model.gameobjects.AionObject#getName()
	 */
	@Override
	public String getName()
	{
		return objectTemplate.getName();
	}

	/* (non-Javadoc)
	 * @see com.aionemu.gameserver.model.gameobjects.VisibleObject#getObjectTemplate()
	 */
	@Override
	public NpcTemplate getObjectTemplate()
	{
		return (NpcTemplate) super.getObjectTemplate();
	}
	/**
	 * @return Returns the stats.
	 */
	public StaticNpcStats getStats()
	{
		return stats;
	}

	/**
	 * @return state
	 */
	public int getState()
	{
		return state;
	}

	/**
	 * @param state the state to set
	 */
	public void setState(CreatureState state)
	{
		this.state |= state.getId();
	}
	
	/** 
	 * @param state taken usually from templates
	 */
	public void setState(int state)
	{
		this.state = state;
	}

	public void unsetState(CreatureState state)
	{
		this.state &= ~state.getId();
	}

	public boolean isInState(CreatureState state)
	{
		int isState = this.state & state.getId();

		if(isState == state.getId())
			return true;

		return false;
	}
	
	/**
	 * @return visualState
	 */
	public int getVisualState()
	{
		return visualState;
	}

	/**
	 * @param visualState the visualState to set
	 */
	public void setVisualState(CreatureVisualState visualState)
	{
		this.visualState |= visualState.getId();
	}

	public void unsetVisualState(CreatureVisualState visualState)
	{
		this.visualState &= ~visualState.getId();
	}

	public boolean isInVisualState(CreatureVisualState visualState)
	{
		int isVisualState = this.visualState & visualState.getId();

		if(isVisualState == visualState.getId())
			return true;

		return false;
	}

	public byte getLevel()
	{
		return this.getObjectTemplate().getLevel();
	}

	/* (non-Javadoc)
	 * @see com.aionemu.gameserver.model.gameobjects.interfaces.IDialog#onDialogRequest(com.aionemu.gameserver.model.gameobjects.player.Player)
	 */
	@Override
	public void onDialogRequest(Player player)
	{
	}
	
	/* (non-Javadoc)
	 * @see com.aionemu.gameserver.model.gameobjects.VisibleObject#see(com.aionemu.gameserver.model.gameobjects.VisibleObject)
	 */
	@Override
	public void see(VisibleObject object)
	{
		super.see(object);
		if (object instanceof Player)
		{
			PacketSendUtility.sendPacket((Player)object, new SM_NPC_INFO(this));
		}
	}
}

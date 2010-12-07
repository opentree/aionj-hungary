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
import com.aionemu.gameserver.model.gameobjects.knownList.StaticObjectKnownList;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.state.CreatureState;
import com.aionemu.gameserver.model.gameobjects.state.CreatureVisualState;
import com.aionemu.gameserver.model.gameobjects.stats.StaticNpcStats;
import com.aionemu.gameserver.model.templates.NpcTemplate;
import com.aionemu.gameserver.model.templates.PlayableTemplate;
import com.aionemu.gameserver.model.templates.spawn.SpawnTemplate;
import com.aionemu.gameserver.network.aion.serverpackets.SM_NPC_INFO;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * @author Mr. Poke, Lyahim
 *
 */
public class StaticNpc extends VisibleObject
{
	private int						state		= CreatureState.ACTIVE.getId();
	private final StaticNpcStats	stats		= new StaticNpcStats();
	private int						visualState	= CreatureVisualState.VISIBLE.getId();

	/**
	 * @param objId
	 * @param spawnTemplate
	 * @param position
	 */
	public StaticNpc(int objId, SpawnTemplate spawnTemplate)
	{
		super(objId, spawnTemplate);
		if (spawnTemplate != null)
			this.objectTemplate = DataManager.NPC_DATA.getNpcTemplate(spawnTemplate.getTemplateId());
		this.setKnownlist(new StaticObjectKnownList(this));

	}

	@Override
	public NpcTemplate getObjectTemplate()
	{
		return (NpcTemplate) super.getObjectTemplate();
	}

	public byte getLevel()
	{
		return ((PlayableTemplate) objectTemplate).getLevel();
	}

	@Override
	public void see(VisibleObject object)
	{
		if (object instanceof Player)
			PacketSendUtility.sendPacket((Player) object, new SM_NPC_INFO(this, (Player) object));
	}

	public StaticNpcStats getStats()
	{
		return stats;
	}

	public int getState()
	{
		return state;
	}

	public void setState(CreatureState state)
	{
		this.state |= state.getId();
	}

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

		if (isState == state.getId())
			return true;

		return false;
	}

	public int getVisualState()
	{
		return visualState;
	}

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

		if (isVisualState == visualState.getId())
			return true;

		return false;
	}
}

package com.aionemu.gameserver.network.aion.clientpackets;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.commons.network.netty.packet.AbstractClientPacket;
import com.aionemu.gameserver.network.aion.AionChannelHandler;

/**
 * @author Lyahim, dragoon112
 *
 */
public class CM_REMOVE_ALTERED_STATE extends AbstractClientPacket<AionChannelHandler>
{

	private int	skillid;

	/**
	 * @param opcode
	 */
	public CM_REMOVE_ALTERED_STATE(int opcode)
	{
		super(opcode);
	}

	/* (non-Javadoc)
	 * @see com.aionemu.commons.network.packet.BaseClientPacket#readImpl()
	 */
	@Override
	protected void readImpl()
	{
		skillid = readH();

	}

	/* (non-Javadoc)
	 * @see com.aionemu.commons.network.packet.BaseClientPacket#runImpl()
	 */
	@Override
	protected void runImpl()
	{
		Player player = getChannelHandler().getActivePlayer();
		player.getEffectController().removeEffect(skillid);
	}

}

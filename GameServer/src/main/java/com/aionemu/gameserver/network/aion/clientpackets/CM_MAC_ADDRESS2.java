package com.aionemu.gameserver.network.aion.clientpackets;

import com.aionemu.commons.network.netty.packet.AbstractClientPacket;
import com.aionemu.gameserver.network.aion.AionChannelHandler;

/**
 * Client sends this only once.
 * 
 * @author Lyahim, Luno
 * 
 */
public class CM_MAC_ADDRESS2 extends AbstractClientPacket<AionChannelHandler>
{
	/**
	 * Constructor
	 * @param opcode
	 */
	public CM_MAC_ADDRESS2(int opcode)
	{
		super(opcode);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void readImpl()
	{
		@SuppressWarnings("unused")
		int objectId = readD(); // lol NC
		@SuppressWarnings("unused")
		byte[] macAddress = readB(6);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void runImpl()
	{
		// TODO server should response - find out response packet.
	}
}

package com.aionemu.chatserver.network.gameserver;

import com.aionemu.chatserver.network.gameserver.clientpackets.CM_CS_AUTH;
import com.aionemu.chatserver.network.gameserver.clientpackets.CM_PLAYER_AUTH;
import com.aionemu.chatserver.network.gameserver.clientpackets.CM_PLAYER_LOGOUT;
import com.aionemu.chatserver.network.gameserver.serverpackets.SM_GS_AUTH_RESPONSE;
import com.aionemu.chatserver.network.gameserver.serverpackets.SM_PLAYER_AUTH_RESPONSE;
import com.aionemu.commons.network.netty.State;
import com.aionemu.commons.network.netty.handler.AbstractPacketHandlerFactory;

public class GameServerPacketHandlerFactory extends AbstractPacketHandlerFactory<GameServerChannelHandler>
{
	public GameServerPacketHandlerFactory()
	{
		super(null, null);
		addPacket(new CM_CS_AUTH(0x00), State.CONNECTED);
		addPacket(new CM_PLAYER_AUTH(0x01), State.AUTHED);
		addPacket(new CM_PLAYER_LOGOUT(0x02), State.AUTHED);
		
		addPacket(SM_GS_AUTH_RESPONSE.class, 0x00);
		addPacket(SM_PLAYER_AUTH_RESPONSE.class, 0x01);

	}
}

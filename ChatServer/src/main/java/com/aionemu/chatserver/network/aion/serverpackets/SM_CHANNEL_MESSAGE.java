package com.aionemu.chatserver.network.aion.serverpackets;

import com.aionemu.chatserver.model.message.Message;
import com.aionemu.chatserver.network.aion.AionChannelHandler;
import com.aionemu.commons.network.netty.packet.AbstractServerPacket;

/**
 * 
 * @author ATracer, Lyahim
 *
 */
public class SM_CHANNEL_MESSAGE extends AbstractServerPacket<AionChannelHandler>
{
	
	private Message message;
	
	public SM_CHANNEL_MESSAGE(Message message)
	{
		this.message = message;
	}

	@Override
	protected void writeImpl(AionChannelHandler cHandler)
	{
		writeC(0x00);
		writeD(message.getChannel().getChannelId());
		writeD(message.getSender().getClientId());
		writeD(0x00);
		writeH(message.getSender().getIdentifier().length / 2);
		writeB(message.getSender().getIdentifier());
		writeH(message.size() / 2);
		writeB(message.getText());		
	}

}

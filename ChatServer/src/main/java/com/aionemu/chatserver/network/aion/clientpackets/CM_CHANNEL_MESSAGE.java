/*
 * This file is part of aion-unique <aion-unique.org>.
 *
 *  aion-unique is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  aion-unique is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with aion-unique.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.aionemu.chatserver.network.aion.clientpackets;

import java.util.Arrays;

import com.aionemu.chatserver.model.channel.Channel;
import com.aionemu.chatserver.model.channel.Channels;
import com.aionemu.chatserver.model.message.Message;
import com.aionemu.chatserver.network.aion.AionChannelHandler;
import com.aionemu.chatserver.service.BroadcastService;
import com.aionemu.commons.network.packet.AbstractClientPacket;

/**
 * 
 * @author ATracer, Lyahim
 */
public class CM_CHANNEL_MESSAGE extends AbstractClientPacket<AionChannelHandler>
{
	private int					channelId;
	private byte[]				content;

	/**
	 * 
	 * @param channelBuffer
	 * @param gameChannelHandler
	 * @param opCode
	 */
	public CM_CHANNEL_MESSAGE(int opcode)
	{
		super(opcode);
	}

	@Override
	protected void readImpl()
	{
		readH();
		readC();
		readD();
		readD();
		channelId = readD();
		int lenght = readH() * 2;
		content = readB(lenght);
	}

	@Override
	protected void runImpl()
	{
		AionChannelHandler cch = getChannelHandler();
		Channel channel = Channels.getChannelById(channelId);
		Message message = new Message(channel, content, cch.getChatClient());
		BroadcastService.getInstance().broadcastMessage(message);
	}

	@Override
	public String toString()
	{
		return "CM_CHANNEL_MESSAGE [channelId=" + channelId + ", content=" + Arrays.toString(content) + "]";
	}
}

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
package com.aionemu.gameserver.network.aion;

import org.apache.log4j.Logger;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;

import com.aionemu.commons.network.netty.State;
import com.aionemu.commons.network.netty.handler.AbstractChannelHandler;
import com.aionemu.commons.network.netty.handler.AbstractPacketHandlerFactory;
import com.aionemu.commons.network.netty.handler.ICryptedChannelHandler;
import com.aionemu.gameserver.model.account.Account;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.Crypt;
import com.aionemu.gameserver.network.aion.serverpackets.SM_KEY;
import com.aionemu.gameserver.network.loginserver.LoginServer;
import com.aionemu.gameserver.services.PlayerService;

/**
 * @author lyahim
 *
 */
public class AionChannelHandler extends AbstractChannelHandler implements ICryptedChannelHandler
{
	private static final Logger	log		= Logger.getLogger(AionChannelHandler.class);

	/**
	 * Crypt that will encrypt/decrypt packets.
	 */
	private final Crypt			crypt	= new Crypt();

	/**
	 * AionClient is authenticating by passing to GameServer id of account.
	 */
	private Account				account;

	/**
	 * active Player that owner of this connection is playing [entered game]
	 */
	private Player				activePlayer;
	//	private String							lastPlayerName = "";
	private long				lastPingTimeMS;

	public AionChannelHandler(AbstractPacketHandlerFactory<AionChannelHandler> aphf)
	{
		super(aphf);
	}

	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception
	{
		super.channelConnected(ctx, e);
		sendPacket(new SM_KEY());
		log.info("Client connected Ip:" + inetAddress.getHostAddress());
	}

	public Player getActivePlayer()
	{
		return activePlayer;
	}

	public Account getAccount()
	{
		return account;
	}

	public boolean setActivePlayer(Player player)
	{
		if (activePlayer != null && player != null)
			return false;
		activePlayer = player;

		if (activePlayer == null)
			state = State.AUTHED;
		else
			state = State.ENTERED;

		/*		if(activePlayer != null)
					lastPlayerName = player.getName();*/

		return true;
	}

	public void setAccount(Account account)
	{
		this.account = account;
	}

	public long getLastPingTimeMS()
	{
		return lastPingTimeMS;
	}

	public void setLastPingTimeMS(long currentTimeMillis)
	{
		this.lastPingTimeMS = currentTimeMillis;
	}

	public final int enableCryptKey()
	{
		return crypt.enableKey();
	}

	@Override
	public void decrypt(ChannelBuffer buf)
	{
		crypt.decrypt(buf);
	}

	@Override
	public void encrypt(ChannelBuffer buf)
	{
		crypt.encrypt(buf);
	}

	@Override
	public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception
	{
		super.channelDisconnected(ctx, e);

		/**
		 * Client starts authentication procedure
		 */
		if (getAccount() != null)
			LoginServer.getInstance().aionClientDisconnected(getAccount().getId());
		if (getActivePlayer() != null)
		{
			Player player = getActivePlayer();

			if (player.isInShutdownProgress())
				PlayerService.playerLoggedOut(player);

			// prevent ctrl+alt+del / close window exploit
			else
			{
				int delay = 15;
				PlayerService.playerLoggedOutDelay(player, (delay * 1000));
			}
			log.info("Player disconnected! Name: " + player.getName());
		}
	}

}

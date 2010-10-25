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
package com.aionemu.gameserver.network.aion;

import java.nio.channels.ClosedChannelException;

import org.apache.log4j.Logger;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;

import com.aionemu.commons.netty.State;
import com.aionemu.commons.netty.handler.AbstractChannelHandler;
import com.aionemu.commons.netty.handler.PacketCrypter;
import com.aionemu.gameserver.model.account.Account;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.Crypt;
import com.aionemu.gameserver.network.aion.serverpackets.SM_KEY;
import com.aionemu.gameserver.network.loginserver.LoginServer;
import com.aionemu.gameserver.services.PlayerService;
import com.aionemu.gameserver.utils.ThreadPoolManager;

/**
 * Object representing connection between GameServer and Aion Client.
 * 
 * @author -Nemesiss-
 */
public class AionConnection extends AbstractChannelHandler implements PacketCrypter
{
	/**
	 * Logger for this class.
	 */
	private static final Logger								log			= Logger.getLogger(AionConnection.class);

	/**
	 * AionClient is authenticating by passing to GameServer id of account.
	 */
	private Account							account;

	/**
	 * Crypt that will encrypt/decrypt packets.
	 */
	private final Crypt						crypt			= new Crypt();

	/**
	 * active Player that owner of this connection is playing [entered game]
	 */
	private Player							activePlayer;


	private long                     		lastPingTimeMS;

	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception
	{
		super.channelConnected(ctx, e);

		/** Send SM_KEY packet */
		sendPacket(new SM_KEY());
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception
	{
		super.messageReceived(ctx, e);
		ChannelBuffer buf = (ChannelBuffer) e.getMessage();
		AionClientPacket clientPacket = AionPacketHandlerFactory.getInstance().getPacketHandler().handle(buf, this);
		if(clientPacket != null && clientPacket.read())
		{
			ThreadPoolManager.getInstance().execute(clientPacket);
		}
	}

	/**
	 * Enable crypt key - generate random key that will be used to encrypt second server packet [first one is
	 * unencrypted] and decrypt client packets. This method is called from SM_KEY server packet, that packet sends key
	 * to aion client.
	 * 
	 * @return "false key" that should by used by aion client to encrypt/decrypt packets.
	 */
	public final int enableCryptKey()
	{
		return crypt.enableKey();
	}

	/**
	 * Invoked when a Channel was disconnected from its remote peer
	 */
	@Override
	public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception
	{
		/**
		 * Client starts authentication procedure
		 */
		if(getAccount() != null)
			LoginServer.getInstance().aionClientDisconnected(getAccount().getId());
		if(getActivePlayer() != null)
		{
			Player player = getActivePlayer();
			
			if(player.getController().isInShutdownProgress())
				PlayerService.playerLoggedOut(player);
			
			// prevent ctrl+alt+del / close window exploit
			else
			{
				int delay = 15;
				PlayerService.playerLoggedOutDelay(player, (delay * 1000));
			}
		}
	}

	/**
	 * Encrypt packet.
	 * 
	 * @param buf
	 */
	@Override
	public void encrypt(ChannelBuffer buf)
	{
		crypt.encrypt(buf);
	}


	@Override
	public void decrypt(ChannelBuffer buf)
	{
		if (!crypt.decrypt(buf))
		{
			log.info("Decrypt failed!!"+toString());
			close();
		}
	}

	/**
	 * Returns account object associated with this connection
	 * 
	 * @return account object associated with this connection
	 */
	public Account getAccount()
	{
		return account;
	}

	/**
	 * Sets account object associated with this connection
	 * 
	 * @param account
	 *            account object associated with this connection
	 */
	public void setAccount(Account account)
	{
		this.account = account;
	}

	/**
	 * Sets Active player to new value. Update connection state to correct value.
	 * 
	 * @param player
	 * @return True if active player was set to new value.
	 */
	public boolean setActivePlayer(Player player)
	{
		if(activePlayer != null && player != null)
			return false;
		activePlayer = player;

		if(activePlayer == null)
			state = State.AUTHED;
		else
			state = State.IN_GAME;
		return true;
	}

	/**
	 * Return active player or null.
	 * 
	 * @return active player or null.
	 */
	public Player getActivePlayer()
	{
		return activePlayer;
	}

	/**
	 * @return the lastPingTimeMS
	 */
	public long getLastPingTimeMS()
	{
		return lastPingTimeMS;
	}

	/**
	 * @param lastPingTimeMS the lastPingTimeMS to set
	 */
	public void setLastPingTimeMS(long lastPingTimeMS)
	{
		this.lastPingTimeMS = lastPingTimeMS;
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception
	{
		if (e.getCause() instanceof ClosedChannelException)
			return;
		log.error("Game to login connection error:", e.getCause());
		e.getChannel().close();
	}

	@Override
	public String toString()
	{
		return "AionConnection " +
			(account != null?"[account=" + account.getId(): "") +
			(activePlayer != null? ", activePlayer=" + activePlayer.getName() + "]" : "");
	}
	
}

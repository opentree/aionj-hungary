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

package com.aionemu.loginserver.network.aion;

import java.security.interfaces.RSAPrivateKey;

import javax.crypto.SecretKey;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.MessageEvent;

import com.aionemu.commons.netty.handler.CrypedChannelHandler;
import com.aionemu.loginserver.controller.AccountController;
import com.aionemu.loginserver.controller.AccountTimeController;
import com.aionemu.loginserver.model.Account;
import com.aionemu.loginserver.network.aion.serverpackets.SM_INIT;
import com.aionemu.loginserver.network.ncrypt.CryptEngine;
import com.aionemu.loginserver.network.ncrypt.EncryptedRSAKeyPair;
import com.aionemu.loginserver.network.ncrypt.KeyGen;
import com.aionemu.loginserver.utils.FloodProtector;
import com.aionemu.loginserver.utils.ThreadPoolManager;

/**
 * Object representing connection between LoginServer and Aion Client.
 * 
 * @author -Nemesiss-
 */
public class AionConnection extends CrypedChannelHandler
{

	/**
	 * Unique Session Id of this connection
	 */
	private int					sessionId	= hashCode();

	/**
	 * Account object for this connection. if state = AUTHED_LOGIN account cant be null.
	 * 
	 */
	private Account				account;

	/**
	 * Crypt to encrypt/decrypt packets
	 */
	private CryptEngine			cryptEngine;

	/**
	 * True if this user is connecting to GS.
	 */
	private boolean				joinedGs;

	/**
	 * Scrambled key pair for RSA
	 */
	private EncryptedRSAKeyPair	encryptedRSAKeyPair;

	/**
	 * Session Key for this connection.
	 */
	private SessionKey			sessionKey;

	/**
	 * Constructor
	 */
	public AionConnection()
	{
		super();
	}

	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception
	{
		super.channelConnected(ctx, e);
		if (FloodProtector.getInstance().addIp(this.getIP()))
			close();
			
		encryptedRSAKeyPair = KeyGen.getEncryptedRSAKeyPair();

		SecretKey blowfishKey = KeyGen.generateBlowfishKey();

		cryptEngine = new CryptEngine();

		cryptEngine.updateKey(blowfishKey.getEncoded());

		/** Send Init packet */
		sendPacket(new SM_INIT(this, blowfishKey));
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception
	{
		super.messageReceived(ctx, e);
		ChannelBuffer buf = (ChannelBuffer) e.getMessage();
		AionClientPacket clientPacket = AionClientPacketHandlerFactory.getInstance().getPacketHandler().handle(buf, this);
		if(clientPacket != null && clientPacket.read())
		{
			ThreadPoolManager.getInstance().execute(clientPacket);
		}
	}

	/**
	 * Invoked when a Channel was disconnected from its remote peer
	 */
	@Override
	public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception
	{
		/**
		 * Remove account only if not joined GameServer yet.
		 */
		if((account != null) && !joinedGs)
		{
			AccountController.getInstance().removeAccountOnLS(account);
			AccountTimeController.updateOnLogout(account);
		}
	}

	/**
	 * Encrypt packet.
	 * 
	 * @param buf
	 * @return encrypted packet size.
	 */
	public final void encrypt(ChannelBuffer buf)
	{
		ChannelBuffer b = buf.slice();
		int size = b.readableBytes()-2 ;
		cryptEngine.encrypt(b.array(), size-2, size);
	}

	/**
	 * Return Scrambled modulus
	 * 
	 * @return Scrambled modulus
	 */
	public final byte[] getEncryptedModulus()
	{
		return encryptedRSAKeyPair.getEncryptedModulus();
	}

	/**
	 * Return RSA private key
	 * 
	 * @return rsa private key
	 */
	public final RSAPrivateKey getRSAPrivateKey()
	{
		return (RSAPrivateKey) encryptedRSAKeyPair.getRSAKeyPair().getPrivate();
	}

	/**
	 * Returns unique sessionId of this connection.
	 * 
	 * @return SessionId
	 */
	public final int getSessionId()
	{
		return sessionId;
	}

	/**
	 * Returns Account object that this client logged in or null
	 * 
	 * @return Account
	 */
	public final Account getAccount()
	{
		return account;
	}

	/**
	 * Set Account object for this connection.
	 * 
	 * @param account
	 */
	public final void setAccount(Account account)
	{
		this.account = account;
	}

	/**
	 * Returns Session Key of this connection
	 * 
	 * @return SessionKey
	 */
	public final SessionKey getSessionKey()
	{
		return sessionKey;
	}

	/**
	 * Set Session Key for this connection
	 * 
	 * @param sessionKey
	 */
	public final void setSessionKey(SessionKey sessionKey)
	{
		this.sessionKey = sessionKey;
	}

	/**
	 * Set joinedGs value to true
	 */
	public final void setJoinedGs()
	{
		joinedGs = true;
	}

	/**
	 * @return String info about this connection
	 */
	@Override
	public String toString()
	{
		return (account != null) ? account + " " + getIP() : "not loged " + getIP();
	}

	/**
	 * This method should no be modified, hashcode in this class is used to ensure that each connection hash unique id
	 * 
	 * @return unique identifier
	 */
	@Override
	public int hashCode()
	{
		return super.hashCode();
	}

	/*
	 * (non-Javadoc)
	 * @see com.aionemu.commons.netty.handler.AbstractChannelHandler#decrypt(org.jboss.netty.buffer.ChannelBuffer)
	 */
	@Override
	public void decrypt(ChannelBuffer buf)
	{
		int size = buf.readableBytes();
		cryptEngine.decrypt(buf.array(), 0, size);
	}
}

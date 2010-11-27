/*
 * This file is part of aion-lightning <aion-lightning.com>.
 *
 *  aion-lightning is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  aion-lightning is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with aion-lightning.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.aionemu.loginserver.network.aion;

import java.security.interfaces.RSAPrivateKey;

import javax.crypto.SecretKey;

import org.apache.log4j.Logger;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;

import com.aionemu.commons.network.netty.handler.AbstractChannelHandler;
import com.aionemu.commons.network.netty.handler.AbstractPacketHandlerFactory;
import com.aionemu.commons.network.netty.handler.ICryptedChannelHandler;
import com.aionemu.loginserver.controller.AccountController;
import com.aionemu.loginserver.controller.AccountTimeController;
import com.aionemu.loginserver.model.Account;
import com.aionemu.loginserver.network.aion.serverpackets.SM_INIT;
import com.aionemu.loginserver.network.ncrypt.CryptEngine;
import com.aionemu.loginserver.network.ncrypt.EncryptedRSAKeyPair;
import com.aionemu.loginserver.network.ncrypt.KeyGen;
import com.aionemu.loginserver.utils.FloodProtector;

/**
 * @author lyahim
 * 
 */
public class AionChannelHandler extends AbstractChannelHandler implements
		ICryptedChannelHandler {

	public AionChannelHandler(
			AbstractPacketHandlerFactory<AionChannelHandler> aphf) {
		super(aphf);
	}

	private static final Logger log = Logger
			.getLogger(AionChannelHandler.class);
	private Account account;

	/**
	 * Unique Session Id of this connection
	 */
	private int sessionId = hashCode();

	/**
	 * Crypt to encrypt/decrypt packets
	 */
	private CryptEngine cryptEngine;

	/**
	 * Scrambled key pair for RSA
	 */
	private EncryptedRSAKeyPair encryptedRSAKeyPair;

	/**
	 * Session Key for this connection.
	 */
	private SessionKey sessionKey;

	/**
	 * True if this user is connecting to GS.
	 */
	private boolean joinedGs;

	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e)
			throws Exception {
		super.channelConnected(ctx, e);

		if (FloodProtector.getInstance().addIp(this.getIP()))
			close();
		encryptedRSAKeyPair = KeyGen.getEncryptedRSAKeyPair();
		SecretKey blowfishKey = KeyGen.generateBlowfishKey();
		cryptEngine = new CryptEngine();
		cryptEngine.updateKey(blowfishKey.getEncoded());
		log.info("Client connected Ip:" + inetAddress.getHostName());
		sendPacket(new SM_INIT(this, blowfishKey));
	}

	@Override
	public void channelDisconnected(ChannelHandlerContext ctx,
			ChannelStateEvent e) throws Exception {
		super.channelDisconnected(ctx, e);

		if (account != null) {
			if (joinedGs)
				log.info("Client logged in IP: "
						+ inetAddress.getHostName().toString()
						+ ", AccountName: " + account.getName());
			else {
				AccountController.getInstance().removeAccountOnLS(account);
				AccountTimeController.updateOnLogout(account);
				log.info("Client can't logged in IP: "
						+ inetAddress.getHostName().toString());
			}
		} else
			log.info("Client can't logged in IP: "
					+ inetAddress.getHostName().toString());
	}

	// *********************************GETTERS-SETTERS*****************************************/

	public int getSessionId() {
		return sessionId;
	}

	public CryptEngine getCryptEngine() {
		return cryptEngine;
	}

	/**
	 * Return RSA private key
	 * 
	 * @return rsa private key
	 */
	public final RSAPrivateKey getRSAPrivateKey() {
		return (RSAPrivateKey) encryptedRSAKeyPair.getRSAKeyPair().getPrivate();
	}

	public SessionKey getSessionKey() {
		return sessionKey;
	}

	/**
	 * Set Session Key for this connection
	 * 
	 * @param sessionKey
	 */
	public final void setSessionKey(SessionKey sessionKey) {
		this.sessionKey = sessionKey;
	}

	/**
	 * Set joinedGs value to true
	 */
	public final void setJoinedGs() {
		joinedGs = true;
	}

	/**
	 * Return Scrambled modulus
	 * 
	 * @return Scrambled modulus
	 */
	public final byte[] getEncryptedModulus() {
		return encryptedRSAKeyPair.getEncryptedModulus();
	}

	/**
	 * This method should no be modified, hashcode in this class is used to
	 * ensure that each connection hash unique id
	 * 
	 * @return unique identifier
	 */
	@Override
	public int hashCode() {
		return super.hashCode();
	}

	public Account getAccount() {
		return account;
	}

	public final void setAccount(Account account) {
		this.account = account;
	}

	@Override
	public void decrypt(ChannelBuffer buf) {
		int size = buf.readableBytes();
		cryptEngine.decrypt(buf.array(), 0, size);
	}

	@Override
	public void encrypt(ChannelBuffer buf) {
		int size = buf.readableBytes() - 2;
		int offset = buf.arrayOffset() + buf.readerIndex() + 2;
		int lenght = cryptEngine.encrypt(buf.array(), offset, size);
		buf.writerIndex(offset + lenght);
	}
}

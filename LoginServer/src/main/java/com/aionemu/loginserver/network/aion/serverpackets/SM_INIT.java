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
package com.aionemu.loginserver.network.aion.serverpackets;

import java.util.Arrays;

import javax.crypto.SecretKey;

import com.aionemu.commons.network.netty.packet.AbstractServerPacket;
import com.aionemu.loginserver.network.aion.AionChannelHandler;

/**
 * @author lyahim
 * 
 *         Format: dd b dddd s d: session id d: protocol revision b: 0x90 bytes
 *         : 0x80 bytes for the scrambled RSA public key 0x10 bytes at 0x00 d:
 *         unknow d: unknow d: unknow d: unknow s: blowfish key
 */
public final class SM_INIT extends AbstractServerPacket<AionChannelHandler> {

	/**
	 * Session Id of this connection
	 */
	private final int sessionId;

	/**
	 * public Rsa key that client will use to encrypt login and password that
	 * will be send in RequestAuthLogin client packet.
	 */
	private final byte[] publicRsaKey;
	/**
	 * blowfish key for packet encryption/decryption.
	 */
	private final byte[] blowfishKey;

	/**
	 * Constructor
	 * 
	 * @param client
	 * @param blowfishKey
	 */
	public SM_INIT(AionChannelHandler client, SecretKey blowfishKey) {
		this(client.getEncryptedModulus(), blowfishKey.getEncoded(), client
				.getSessionId());
	}

	/**
	 * Creates new instance of <tt>SM_INIT</tt> packet.
	 * 
	 * @param publicRsaKey
	 *            Public RSA key
	 * @param blowfishKey
	 *            Blowfish key
	 * @param sessionId
	 *            Session identifier
	 */
	private SM_INIT(byte[] publicRsaKey, byte[] blowfishKey, int sessionId) {
		this.sessionId = sessionId;
		this.publicRsaKey = publicRsaKey;
		this.blowfishKey = blowfishKey;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void writeImpl(AionChannelHandler cHandler) {
		writeD(sessionId); // session id
		writeD(0x0000c621); // protocol revision
		writeB(publicRsaKey); // RSA Public Key
		// unk
		writeD(0x00);
		writeD(0x00);
		writeD(0x00);
		writeD(0x00);

		writeB(blowfishKey); // BlowFish key
		writeD(197635); // unk
		writeD(2097152); // unk
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SM_INIT [blowfishKey=" + Arrays.toString(blowfishKey)
				+ ", publicRsaKey=" + Arrays.toString(publicRsaKey)
				+ ", sessionId=" + sessionId + "]";
	}
}

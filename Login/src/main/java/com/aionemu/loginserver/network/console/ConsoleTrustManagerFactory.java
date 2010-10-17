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
package com.aionemu.loginserver.network.console;

import java.security.InvalidAlgorithmParameterException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.ManagerFactoryParameters;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactorySpi;
import javax.net.ssl.X509TrustManager;

/**
 * @author Mr. Poke
 * 
 */
public class ConsoleTrustManagerFactory extends TrustManagerFactorySpi
{

	private static final TrustManager	DUMMY_TRUST_MANAGER	= new X509TrustManager(){
																@Override
																public X509Certificate[] getAcceptedIssuers()
																{
																	return new X509Certificate[0];
																}

																@Override
																public void checkClientTrusted(X509Certificate[] chain,
																	String authType) throws CertificateException
																{
																	// Always trust - it is an example.
																	// You should do something in the real world.
																	// You will reach here only if you enabled client
																	// certificate auth,
																	// as described in SecureChatSslContextFactory.
																	System.err.println("UNKNOWN CLIENT CERTIFICATE: "
																		+ chain[0].getSubjectDN());
																}

																@Override
																public void checkServerTrusted(X509Certificate[] chain,
																	String authType) throws CertificateException
																{
																	// Always trust - it is an example.
																	// You should do something in the real world.
																	System.err.println("UNKNOWN SERVER CERTIFICATE: "
																		+ chain[0].getSubjectDN());
																}
															};

	public static TrustManager[] getTrustManagers()
	{
		return new TrustManager[] { DUMMY_TRUST_MANAGER };
	}

	@Override
	protected TrustManager[] engineGetTrustManagers()
	{
		return getTrustManagers();
	}

	@Override
	protected void engineInit(KeyStore keystore) throws KeyStoreException
	{
		// Unused
	}

	@Override
	protected void engineInit(ManagerFactoryParameters managerFactoryParameters)
		throws InvalidAlgorithmParameterException
	{
		// Unused
	}
}

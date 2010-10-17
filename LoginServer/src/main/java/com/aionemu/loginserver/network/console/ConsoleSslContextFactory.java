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

import java.security.KeyStore;
import java.security.Security;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;

/**
 * @author Mr. Poke
 * 
 */
public class ConsoleSslContextFactory
{

	private static final String		PROTOCOL	= "TLS";
	private static final SSLContext	SERVER_CONTEXT;
	private static final SSLContext	CLIENT_CONTEXT;

	static
	{
		String algorithm = Security.getProperty("ssl.KeyManagerFactory.algorithm");
		if(algorithm == null)
		{
			algorithm = "SunX509";
		}

		SSLContext serverContext = null;
		SSLContext clientContext = null;
		try
		{
			KeyStore ks = KeyStore.getInstance("JKS");
			ks.load(ConsoleKeyStore.asInputStream(), ConsoleKeyStore.getKeyStorePassword());

			// Set up key manager factory to use our key store
			KeyManagerFactory kmf = KeyManagerFactory.getInstance(algorithm);
			kmf.init(ks, ConsoleKeyStore.getCertificatePassword());

			// Initialize the SSLContext to work with our key managers.
			serverContext = SSLContext.getInstance(PROTOCOL);
			serverContext.init(kmf.getKeyManagers(), null, null);
		}
		catch(Exception e)
		{
			throw new Error("Failed to initialize the server-side SSLContext", e);
		}

		try
		{
			clientContext = SSLContext.getInstance(PROTOCOL);
			clientContext.init(null, ConsoleTrustManagerFactory.getTrustManagers(), null);
		}
		catch(Exception e)
		{
			throw new Error("Failed to initialize the client-side SSLContext", e);
		}

		SERVER_CONTEXT = serverContext;
		CLIENT_CONTEXT = clientContext;
	}

	public static SSLContext getServerContext()
	{
		return SERVER_CONTEXT;
	}

	public static SSLContext getClientContext()
	{
		return CLIENT_CONTEXT;
	}
}

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

import org.apache.log4j.Logger;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.handler.ssl.SslHandler;

import com.aionemu.commons.consoleCommand.ConsoleCommandHandler;
import com.aionemu.loginserver.configs.Config;
import com.aionemu.loginserver.controller.AccountController;
import com.aionemu.loginserver.model.Account;
import com.aionemu.loginserver.utils.AccountUtils;

/**
 * @author Mr. Poke
 * 
 */
public class ConsoleHandler extends SimpleChannelUpstreamHandler
{

	private static final Logger	log	= Logger.getLogger(ConsoleHandler.class.getName());
	/**
	 * Current state of this connection
	 */
	private static State		state;

	private String				user;

	public static enum State
	{
		CONNECTED,
		REQUEST_USER,
		REQUEST_PASS,
		AUTHED
	}

	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception
	{

		// Get the SslHandler in the current pipeline.
		// We added it in SecureChatPipelineFactory.
		final SslHandler sslHandler = ctx.getPipeline().get(SslHandler.class);

		// Get notified when SSL handshake is done.
		ChannelFuture handshakeFuture = sslHandler.handshake();
		handshakeFuture.addListener(new Greeter(sslHandler));
		state = State.CONNECTED;
	}

	/**
	 * Invoked when a Channel was disconnected from its remote peer
	 */
	@Override
	public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception
	{
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
	{
		// Convert to a String first.
		String request = (String) e.getMessage();
		switch(state)
		{
			case REQUEST_USER:
				user = request;
				e.getChannel().write("\255\251\1");
				e.getChannel().write("Password: ");
				state = State.REQUEST_PASS;
				return;
			case REQUEST_PASS:
				Account account = AccountController.getInstance().loadAccount(user);
				if(account == null || !account.getPasswordHash().equals(AccountUtils.encodePassword(request)))
				{
					e.getChannel().write("Bad pasworld or name!!\n").addListener(ChannelFutureListener.CLOSE);				
				}
				e.getChannel().write("Welcome!\n");
				state = State.AUTHED;
				return;
			case AUTHED:
				String txt = ConsoleCommandHandler.getInstance().handleCommand(request, this);
				if(txt != null)
					e.getChannel().write(txt);
				else
					e.getChannel().write("Incorect command!\n");
				return;
		}
		// Close the connection if the client has sent 'bye'.
		if(request.toLowerCase().equals("bye"))
		{
			e.getChannel().close();
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e)
	{
		log.debug("Unexpected exception from downstream.", e.getCause());
		e.getChannel().close();
	}

	/**
	 * @author <a href="http://www.jboss.org/netty/">The Netty Project</a>
	 * @author <a href="http://gleamynode.net/">Trustin Lee</a>
	 * @version $Rev: 2121 $, $Date: 2010-02-02 01:38:07 +0100 (Tue, 02 Feb 2010) $
	 */
	private static final class Greeter implements ChannelFutureListener
	{

		private final SslHandler	sslHandler;

		Greeter(SslHandler sslHandler)
		{
			this.sslHandler = sslHandler;
		}

		@Override
		public void operationComplete(ChannelFuture future) throws Exception
		{
			if(future.isSuccess())
			{
				// Once session is secured, send a greeting.
				future.getChannel().write("Welcome to " + Config.CONSOLE_ADDRESS + " secure chat service!\n");
				future.getChannel().write(
					"Your session is protected by " + sslHandler.getEngine().getSession().getCipherSuite()
						+ " cipher suite.\n");
				future.getChannel().write("Username: ");
				state = State.REQUEST_USER;
			}
			else
			{
				future.getChannel().close();
			}
		}
	}
}

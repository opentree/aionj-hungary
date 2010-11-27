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
package com.aionemu.commons.network.netty.packet;

import org.jboss.netty.buffer.ChannelBuffer;

/**
 * @author lyahim
 * 
 */
public abstract class AbstractPacket
{
	protected int			opCode;
	protected ChannelBuffer	buf;

	public AbstractPacket(int opCode)
	{
		this.opCode = opCode;
	}

	public AbstractPacket()
	{
	}

	public int getOpCode()
	{
		return opCode;
	}

	public ChannelBuffer getBuf()
	{
		return buf;
	}

	public void setBuf(ChannelBuffer buf)
	{
		this.buf = buf;
	}

	public void setOpCode(int opCode)
	{
		this.opCode = opCode;
	}
}

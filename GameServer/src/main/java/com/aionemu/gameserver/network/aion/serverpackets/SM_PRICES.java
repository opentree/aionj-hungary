/*
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
package com.aionemu.gameserver.network.aion.serverpackets;

import com.aionemu.gameserver.model.gameobjects.player.Prices;
import com.aionemu.gameserver.network.aion.AbstractAionServerPacket;
import com.aionemu.gameserver.network.aion.AionChannelHandler;

/**
 * @author Lyahim, xavier, Sarynth
 * Price/tax in Influence ration dialog
 */
public class SM_PRICES extends AbstractAionServerPacket<AionChannelHandler>
{
	private Prices	prices;

	/**
	 * @param prices
	 */
	public SM_PRICES(Prices prices)
	{
		this.prices = prices;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void writeImpl(AionChannelHandler cHandler)
	{
		writeC(prices.getGlobalPrices()); // Display Buying Price %
		writeC(prices.getGlobalPricesModifier()); // Buying Modified Price %
		writeC(prices.getTaxes()); // Tax = -100 + C %
	}
}

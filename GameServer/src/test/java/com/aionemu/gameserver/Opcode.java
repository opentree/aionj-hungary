/*
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, 
 * MA  02110-1301, USA.
 *
 * http://www.gnu.org/copyleft/gpl.html
 */
package com.aionemu.gameserver;

import org.junit.Test;

/**
 * @author Mr. Poke
 *
 */
public class Opcode
{
	@Test
	public void testStorageFull()
	{
		for (int op = 0; op <= 255; op++)
		{
			/*
			byte op1 = (byte)((op + 0xAE) ^ 0xEE);
			op1 = (byte)((~op1)+ 0x45);*/
			byte op1 = (byte) (((~op) + 0x45));
			//op1 = (byte)(~(op1- 0x44));
			System.out.println(Integer.toHexString(op).toUpperCase() + " > " + Integer.toHexString(op1).toUpperCase());
		}
	}
}

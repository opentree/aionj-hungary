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
package system.database;

import system.database.mysql5.*;

import com.aionemu.commons.database.dao.DAOManager;

/**
 * @author Mr. Poke
 * 
 */
public class MySQL5Loader {

	/**
	 * 
	 */
	public MySQL5Loader() {
		DAOManager.getInstance().registerDAO(new MySQL5AccountDAO());
		DAOManager.getInstance().registerDAO(new MySQL5AccountTimeDAO());
		DAOManager.getInstance().registerDAO(new MySQL5BannedIpDAO());
		DAOManager.getInstance().registerDAO(new MySQL5GameServersDAO());
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new MySQL5Loader();
	}

}

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
package system.database.mysql5;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.log4j.Logger;

import com.aionemu.commons.database.DatabaseFactory;
import com.aionemu.gameserver.dao.SpawnDAO;
import com.aionemu.gameserver.model.templates.spawn.SpawnTemplate;
import com.aionemu.gameserver.model.templates.spawn.SpawnTime;
import com.aionemu.gameserver.spawnengine.DayNightSpawnManager;
import com.aionemu.gameserver.spawnengine.SpawnEngine;

/**
 * @author Mr. Poke
 * 
 */
public class MYSQL5SpawnDAO extends SpawnDAO
{
	private static final Logger	log				= Logger.getLogger(MYSQL5SpawnDAO.class);

	private static final String	SELECT_QUERY	= "SELECT `id`, `world`, `templateId`, `x`, `y`, `z`, `heading`, `staticId`,  `interval`, `nextRespawnTime`, `spawnTime` FROM `spawn`";

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.aionemu.commons.database.dao.DAO#supports(java.lang.String, int,
	 * int)
	 */
	@Override
	public boolean supports(String databaseName, int majorVersion, int minorVersion)
	{
		return MySQL5DAOUtils.supports(databaseName, majorVersion, minorVersion);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.aionemu.gameserver.dao.SpawnDAO#load()
	 */
	@Override
	public void load()
	{
		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(SELECT_QUERY);
			ResultSet rset = stmt.executeQuery();
			while (rset.next())
			{
				int id = rset.getInt("id");
				int mapId = rset.getInt("world");
				int templateId = rset.getInt("templateId");
				float x = rset.getFloat("x");
				float y = rset.getFloat("y");
				float z = rset.getFloat("z");
				int staticId = rset.getInt("staticId");
				int interval = rset.getInt("interval");
				byte heading = rset.getByte("heading");
				String nextRespawnTime = rset.getString("nextRespawnTime");
				SpawnTime spawnTime = SpawnTime.valueOf(rset.getString("spawnTime"));
				SpawnTemplate template = new SpawnTemplate(id, templateId, mapId, x, y, z, heading, interval, staticId, spawnTime);
				if (spawnTime == SpawnTime.ALL)
					SpawnEngine.getInstance().addSpawn(template, nextRespawnTime);
				else
					DayNightSpawnManager.getInstance().addSpawnTemplate(template);
			}
		}
		catch (Exception e)
		{
			log.fatal("Could not restore spawn data!", e);
		}
		finally
		{
			DatabaseFactory.close(con);
		}
	}

}

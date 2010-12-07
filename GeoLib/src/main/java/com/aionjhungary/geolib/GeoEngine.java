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
package com.aionjhungary.geolib;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.templates.WorldMapTemplate;
import com.aionjhungary.geolib.loader.models.FileList;
import com.aionjhungary.geolib.loader.models.ModelLoader;
import com.aionjhungary.geolib.scene.Geometry;
import com.aionjhungary.geolib.scene.Mesh;
import com.aionjhungary.geolib.world.GeoModel;
import com.aionjhungary.geolib.world.GeoWorld;

/**
 * @author Mr. Poke
 *
 */
public class GeoEngine
{
	private Map<Integer, GeoWorld> geoMaps = new HashMap<Integer, GeoWorld>();
	
	
	public static final GeoEngine getInstance()
	{
		return SingletonHolder.instance;
	}
	
	GeoEngine()
	{
		Map<String, Mesh> meshs =ModelLoader.load();
		
		for (WorldMapTemplate worldData : DataManager.WORLD_MAPS_DATA)
		{
			int worldId = worldData.getMapId();
			int worldSize = worldData.getWorldSize();
			GeoWorld world = new GeoWorld(worldSize);
			List <Geometry> geoms=FileList.load("data/geodata/"+worldId+".bin", meshs);
			geoMaps.put(worldId, world);
			for (Geometry geom : geoms)
			{
				world.addModel(new GeoModel(geom));
			}
		}
	}

	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder
	{
		protected static final GeoEngine	instance	= new GeoEngine();
	}
}

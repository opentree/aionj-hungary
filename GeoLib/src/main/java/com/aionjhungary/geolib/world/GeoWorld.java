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
package com.aionjhungary.geolib.world;

import java.util.ArrayList;
import java.util.List;

import com.aionjhungary.geolib.collision.CollisionResults;
import com.aionjhungary.geolib.math.Ray;

/**
 * @author Mr. Poke
 *
 */
public class GeoWorld
{
	private List<GeoRegion> regions;

	/**
	 * @param worldSize
	 */
	public GeoWorld(int worldSize)
	{
		this.regions = new ArrayList<GeoRegion>();
		for (int x = 0; x < worldSize; x+=256)
			for (int y = 0; y < worldSize; y+=256)
				regions.add(new GeoRegion(x, y));
	}
	
	public void addModel(GeoModel model)
	{
		for (GeoRegion region : regions)
		{
			if (region.getBox().intersectsBoundingBox(model.getWorldBound()))
			{
				region.addModel(model);
			}
		}
	}
	
	public int collideWithRay(Ray r, CollisionResults results)
	{
		int count = 0;
		for (GeoRegion region : regions)
			if (region.getBox().intersects(r))
				count+=region.collideWithRay(r, results);
				
		return count;
	}
}

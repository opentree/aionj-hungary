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

import com.aionjhungary.geolib.bounding.BoundingBox;
import com.aionjhungary.geolib.collision.CollisionResults;
import com.aionjhungary.geolib.math.Ray;
import com.aionjhungary.geolib.math.Vector3f;

/**
 * @author Mr. Poke
 *
 */
public class GeoRegion
{
	private BoundingBox box;
	
	private List<GeoModel> models = new ArrayList<GeoModel>();

	GeoRegion(int x, int y)
	{
		box = new BoundingBox(new Vector3f(x,y,0), new Vector3f(x+256,y+256,1000));
	}
	
	/**
	 * @return Returns the box.
	 */
	public BoundingBox getBox()
	{
		return box;
	}

	public boolean addModel(GeoModel model)
	{
		return models.add(model);
	}
	
	public int collideWithRay(Ray r, CollisionResults results)
	{
		int count = 0;
		for (GeoModel model : models)
			count += model.collideWithRay(r, results);
		return count;
	}
}

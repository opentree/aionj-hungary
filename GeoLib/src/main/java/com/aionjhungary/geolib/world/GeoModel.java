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

import com.aionjhungary.geolib.bounding.BoundingBox;
import com.aionjhungary.geolib.collision.CollisionResults;
import com.aionjhungary.geolib.collision.bih.BIHTree;
import com.aionjhungary.geolib.math.Matrix4f;
import com.aionjhungary.geolib.math.Ray;
import com.aionjhungary.geolib.scene.Geometry;

/**
 * @author Mr. Poke
 *
 */
public class GeoModel
{
	private BoundingBox worldBound;
	private Matrix4f worldMatrix;
	private BIHTree bIHTree;
	private Geometry geom;

	/**
	 * @param worldBound
	 * @param worldMatrix
	 * @param bIHTree
	 */
	public GeoModel(Geometry geom)
	{
		this.geom = geom;
		worldBound = (BoundingBox) geom.getModelBound();
	}
	
	 /**
	 * @return Returns the worldBound.
	 */
	public BoundingBox getWorldBound()
	{
		return worldBound;
	}

	public int collideWithRay(Ray r, CollisionResults results)
	 {
		if (worldBound.intersects(r))
		{
			if (bIHTree == null)
			{
				bIHTree = new BIHTree(geom.getMesh());
				bIHTree.construct();
				worldMatrix = geom.getWorldMatrix();
				geom = null;
			}
			return bIHTree.collideWith(r, worldMatrix, worldBound, results);
		}
		return 0;
	 }
}

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
package com.aionjhungary.geolib.loader.models;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.aionjhungary.geolib.math.Matrix3f;
import com.aionjhungary.geolib.scene.Geometry;
import com.aionjhungary.geolib.scene.Mesh;

/**
 * @author Mr. Poke
 *
 */
public class FileList
{
	public static List <Geometry> load (String fileName, Map<String, Mesh> meshs)
	{
		List <Geometry> geomList = new ArrayList<Geometry>();
		File Geo = new File(fileName);
		int size, block = 0;
		FileChannel roChannel = null;

		try
		{
			// Create a read-only memory-mapped file
			roChannel = new RandomAccessFile(Geo, "r").getChannel();
			size = (int) roChannel.size();
			MappedByteBuffer geo = roChannel.map(FileChannel.MapMode.READ_ONLY, 0, size).load();
			geo.order(ByteOrder.LITTLE_ENDIAN);
			int modelCount = geo.getInt();
			for (int i=0; i < modelCount; i++)
			{
				int nameLenght = geo.getShort();
				byte[] nameByte = new byte[nameLenght];
				geo.get(nameByte);
				
				String modelName = new String (nameByte);
				modelName = modelName.replace("terrain_models\\", "");
				modelName = modelName.replace("models\\", "");
				Mesh m = meshs.get(modelName.toLowerCase());
				if (m == null)
					System.out.println("Missing model :"+modelName);
				
				Geometry geom = new Geometry("", m);
				geom.setLocalTranslation(geo.getFloat(), geo.getFloat(), geo.getFloat());
				Matrix3f matrix = new Matrix3f();
				float [][] tmp= new float[3][3];
				for (int x= 0; x<3 ; x++)
					for (int y= 0; y<3 ; y++)
						tmp[x][y] = geo.getFloat();
				matrix.set(tmp);
				if (!modelName.startsWith("terrain_models\\water_"))
					geomList.add(geom);
				geom.setLocalRotation(matrix);
				geom.updateGeometricState();
				geo.getInt();
			}
		}
		catch (Exception e)
		{
			System.out.println("Failed to Load GeoFile at block: " + block);
		}
		finally
		{
			try
			{
				if (roChannel != null)
					roChannel.close();
			}
			catch (Exception e)
			{
			}
		}
		return geomList;
	}
}

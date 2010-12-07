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

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import com.aionjhungary.geolib.bounding.BoundingBox;
import com.aionjhungary.geolib.math.Vector3f;
import com.aionjhungary.geolib.scene.Mesh;
import com.aionjhungary.geolib.scene.VertexBuffer.Type;
import com.aionjhungary.geolib.utils.BufferUtils;

/**
 * @author Mr. Poke
 *
 */
public class ModelLoader
{

	public static Map<String, Mesh> load()
	{
		Map<String, Mesh> meshs = new HashMap<String, Mesh>();
		try
		{
			InputStream in = new BufferedInputStream(new FileInputStream("data/geodata/models.zip"));
			ZipInputStream zin = new ZipInputStream(in);
			ZipEntry e;
			while ((e = zin.getNextEntry()) != null)
			{
				if (!e.isDirectory())
				{
					byte[] buffer = new byte[(int) e.getSize()];
					zin.read(buffer);
					ByteBuffer byteBuffer = ByteBuffer.wrap(buffer);
					byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
					byte[] nameByte = new byte[256];
					byteBuffer.get(nameByte);
					String modelName = e.getName().trim().toLowerCase();
					byteBuffer.getInt(); //type ???
					int vectorCount = byteBuffer.getInt();
					Vector3f[] vertices = new Vector3f[vectorCount];
					for (int i = 0; i < vectorCount; i++)
					{
						vertices[i] = new Vector3f(byteBuffer.getFloat(), byteBuffer.getFloat(), byteBuffer.getFloat());
					}
					int tringle = byteBuffer.getInt() * 3;
					int[] indexes = new int[tringle];
					for (int i = 0; i < tringle; i++)
					{
						indexes[i] = byteBuffer.getShort();
					}
					/*
					 // Texture coordinates
					Vector2f [] texCoord = new Vector2f[4];
					texCoord[0] = new Vector2f(0,0);
					texCoord[1] = new Vector2f(1,0);
					texCoord[2] = new Vector2f(0,1);
					texCoord[3] = new Vector2f(1,1);*/
					Mesh m = new Mesh();
					m.setBuffer(Type.Position, 3, BufferUtils.createFloatBuffer(vertices));
					// m.setBuffer(Type.TexCoord, 2, BufferUtils.createFloatBuffer(texCoord));
					m.setBuffer(Type.Index, 1, BufferUtils.createIntBuffer(indexes));
					m.setBound(new BoundingBox());

					m.updateBound();
					meshs.put(modelName, m);
				}
			}
			zin.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return meshs;
	}
}

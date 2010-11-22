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
package parser.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * @author Mr. Poke
 *
 */
public class FileDecoder
{
	private static String	clientDir	= "D:\\NCsoft\\AionEU\\";

	public static void decode(String pakFileName, String pakFileDir, String xmlDir, String xmlFileName, boolean disasm, String outputFileName)
	{

		String s = null;
		try
		{
			Process process = Runtime.getRuntime().exec("c:\\Python25\\python pak2zip.py " + clientDir + pakFileDir + pakFileName + ".pak tmp/zip/" + pakFileName + ".zip");
			BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
			BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));

			while ((s = stdInput.readLine()) != null)
			{
				System.out.println(s);
			}

			// read any errors from the attempted command
			System.out.println("Error:");
			while ((s = stdError.readLine()) != null)
			{
				System.out.println(s);
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		try
		{
			InputStream in = new BufferedInputStream(new FileInputStream("tmp/zip/" + pakFileName + ".zip"));
			ZipInputStream zin = new ZipInputStream(in);
			ZipEntry e;
			while ((e = zin.getNextEntry()) != null)
			{
				if (e.getName().equalsIgnoreCase(xmlDir + xmlFileName))
				{
					unzip(zin, outputFileName, disasm);
					break;
				}
			}
			zin.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		if (!disasm)
			return;
		try
		{
			Process process = Runtime.getRuntime().exec("AIONdisasm.exe tmp/xml/" + outputFileName + " xml/" + outputFileName);
			BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
			BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));

			while ((s = stdInput.readLine()) != null)
			{
				System.out.println(s);
			}

			// read any errors from the attempted command
			System.out.println("Error:");
			while ((s = stdError.readLine()) != null)
			{
				System.out.println(s);
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	private static void unzip(ZipInputStream zin, String s, boolean disasm) throws IOException
	{
		String outDir;
		if (disasm)
			outDir = "tmp\\xml\\" + s;
		else
			outDir = "xml\\" + s;
		FileOutputStream out = new FileOutputStream(outDir);
		byte[] b = new byte[1024];
		int len = 0;
		while ((len = zin.read(b)) != -1)
		{
			out.write(b, 0, len);
		}
		out.close();
	}
}

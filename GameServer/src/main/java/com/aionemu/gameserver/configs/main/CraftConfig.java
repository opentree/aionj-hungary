
package com.aionemu.gameserver.configs.main;

import com.aionemu.commons.configuration.Property;

public class CraftConfig	
{	
	/**
	* forwards in craft.properties. 
	* @author nerolory
	*/
	@Property(key = "gameserver.percents.craft", defaultValue = "25")
	public static int					PERCENT_CRAFT;
}

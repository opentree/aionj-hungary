/*
 * This file is part of aion-lightning <aion-lightning.com>.
 *
 *  aion-lightning is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  aion-lightning is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with aion-lightning.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.aionemu.commons.consoleCommand;

import java.lang.reflect.Modifier;

import org.apache.log4j.Logger;

import com.aionemu.commons.scripting.classlistener.ClassListener;
import com.aionemu.commons.scripting.classlistener.DefaultClassListener;
import com.aionemu.commons.utils.ClassUtils;

/**
 * @author Mr. Poke
 * 
 */
public class ConsoleCommandLoader extends DefaultClassListener implements ClassListener
{
	private static final Logger	log	= Logger.getLogger(ConsoleCommandLoader.class);

	public ConsoleCommandLoader()
	{
	}

	@SuppressWarnings("unchecked")
	@Override
	public void postLoad(Class<?>[] classes)
	{
		for(Class<?> c : classes)
		{
			if(log.isDebugEnabled())
				log.debug("Load class " + c.getName());

			if(!isValidClass(c))
				continue;

			if(ClassUtils.isSubclass(c, AbstractConsoleCommand.class))
			{
				try
				{
					Class<? extends AbstractConsoleCommand> tmp = (Class<? extends AbstractConsoleCommand>) c;
					if(tmp != null)
						ConsoleCommandHandler.getInstance().registerCommand(tmp.newInstance());
				}
				catch(InstantiationException e)
				{
					log.error("", e);
				}
				catch(IllegalAccessException e)
				{
					log.error("", e);
				}
			}
		}

		// call onClassLoad()
		super.postLoad(classes);

	}

	@Override
	public void preUnload(Class<?>[] classes)
	{
		if(log.isDebugEnabled())
			for(Class<?> c : classes)
				// debug messages
				log.debug("Unload class " + c.getName());

		// call onClassUnload()
		super.preUnload(classes);

		ConsoleCommandHandler.getInstance().clearHandlers();
	}

	public boolean isValidClass(Class<?> clazz)
	{
		final int modifiers = clazz.getModifiers();

		if(Modifier.isAbstract(modifiers) || Modifier.isInterface(modifiers))
			return false;

		if(!Modifier.isPublic(modifiers))
			return false;

		return true;
	}
}

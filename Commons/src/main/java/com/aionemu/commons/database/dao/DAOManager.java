/*
 * This file is part of aion-emu <aion-emu.com>.
 *
 * aion-emu is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * aion-emu is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with aion-emu.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.aionemu.commons.database.dao;

import static com.aionemu.commons.database.DatabaseFactory.getDatabaseMajorVersion;
import static com.aionemu.commons.database.DatabaseFactory.getDatabaseMinorVersion;
import static com.aionemu.commons.database.DatabaseFactory.getDatabaseName;

import java.util.HashMap;
import java.util.Map;

import javolution.text.TextBuilder;

import org.apache.log4j.Logger;

/**
 * This class manages {@link DAO} implementations, it resolves valid implementation for current database
 * 
 * @author SoulKeeper, Saelya
 */
public class DAOManager
{
	/**
	 * Logger for DAOManager class
	 */
	private static final Logger				log		= Logger.getLogger(DAOManager.class);

	/**
	 * Collection of registered DAOs
	 */
	private static final Map<String, DAO>	daoMap	= new HashMap<String, DAO>();

	public static final DAOManager getInstance()
	{
		return SingletonHolder.instance;
	}

	/**
	 * Shutdown DAOManager
	 */
	public static void shutdown()
	{
		daoMap.clear();
	}

	/**
	 * Returns DAO implementation by DAO class. Typical usage:
	 * 
	 * <pre>
	 * AccountDAO	dao	= DAOManager.getDAO(AccountDAO.class);
	 * </pre>
	 * 
	 * @param clazz
	 *            Abstract DAO class implementation of which was registered
	 * @param <T>
	 *            Subclass of DAO
	 * @return DAO implementation
	 * @throws DAONotFoundException
	 *             if DAO implementation not found
	 */
	@SuppressWarnings("unchecked")
	public static <T extends DAO> T getDAO(Class<T> clazz) throws DAONotFoundException
	{

		DAO result = daoMap.get(clazz.getName());

		if(result == null)
		{
			String s = "DAO for class " + clazz.getName() + " not implemented";
			log.error(s);
			throw new DAONotFoundException(s);
		}

		return (T) result;
	}

	/**
	 * Registers {@link DAO}.<br>
	 * First it creates new instance of DAO, then invokes {@link DAO#supports(String, int, int)} <br>
	 * . If the result was possitive - it associates DAO instance with
	 * {@link com.aionemu.commons.database.dao.DAO#getClassName()} <br>
	 * If another DAO was registed - {@link com.aionemu.commons.database.dao.DAOAlreadyRegisteredException} will be
	 * thrown
	 * 
	 * @param daoClass
	 *            DAO implementation
	 * @throws DAOAlreadyRegisteredException
	 *             if DAO is already registered
	 * @throws IllegalAccessException
	 *             if something went wrong during instantiation of DAO
	 * @throws InstantiationException
	 *             if something went wrong during instantiation of DAO
	 */
	public void registerDAO(DAO dao)
	{

		if(!dao.supports(getDatabaseName(), getDatabaseMajorVersion(), getDatabaseMinorVersion()))
		{
			return;
		}

		synchronized(DAOManager.class)
		{
			DAO oldDao = daoMap.get(dao.getClassName());
			if(oldDao != null)
			{
				TextBuilder sb = TextBuilder.newInstance();
				sb.append("DAO with className ").append(dao.getClassName()).append(" is used by ");
				sb.append(oldDao.getClass().getName()).append(". Can't override with ");
				sb.append(dao.getClass().getName()).append(".");
				String s = sb.toString();
				log.error(s);
				TextBuilder.recycle(sb);
			}
			else
			{
				daoMap.put(dao.getClassName(), dao);
			}
		}

		if(log.isDebugEnabled())
			log.debug("DAO " + dao.getClassName() + " was successfuly registered.");
	}

	/**
	 * Unregisters DAO class
	 * 
	 * @param daoClass
	 *            DAO implementation to unregister
	 */
	public static void unregisterDAO(DAO daoClass)
	{
		synchronized(DAOManager.class)
		{
			for(DAO dao : daoMap.values())
			{
				if(dao == daoClass)
				{
					daoMap.remove(dao.getClassName());

					if(log.isDebugEnabled())
						log.debug("DAO " + dao.getClassName() + " was successfuly unregistered.");

					break;
				}
			}
		}
	}

	private DAOManager()
	{
		// empty
	}

	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder
	{
		protected static final DAOManager	instance	= new DAOManager();
	}
}

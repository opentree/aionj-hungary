/*
 * This file is part of aion-unique <aion-unique.org>.
 *
 *  aion-unique is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  aion-unique is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with aion-unique.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.aionemu.chatserver;

import com.aionemu.chatserver.configs.Config;
import com.aionemu.chatserver.network.NettyChatServer;
import com.aionemu.chatserver.service.BroadcastService;
import com.aionemu.chatserver.service.ChatService;
import com.aionemu.chatserver.service.GameServerService;
import com.aionemu.chatserver.utils.Util;
import com.aionemu.commons.services.LoggingService;
import com.aionemu.commons.utils.AEInfos;

/**
 * @author ATracer
 */
public class ChatServer
{
    /**
     * @param args
     */
    public static void main(String[] args)
    {
        LoggingService.init();     
		Config.load();
		
		Util.printSection("BroadcastService");
		BroadcastService.getInstance();
		
		Util.printSection("ChatService");
		ChatService.getInstance();
		
		Util.printSection("GameServerService");
		GameServerService.getInstance();
		
        Util.printSection("Netty - IOServer");
        NettyChatServer.getInstance();       
        		
        Util.printSection("System");
        AEInfos.printAllInfos();
    }
}

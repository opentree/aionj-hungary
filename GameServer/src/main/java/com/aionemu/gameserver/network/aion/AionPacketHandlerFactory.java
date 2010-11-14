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
package com.aionemu.gameserver.network.aion;

import com.aionemu.commons.network.netty.State;
import com.aionemu.commons.network.netty.handler.AbstractPacketHandlerFactory;
import com.aionemu.gameserver.network.aion.clientpackets.*;
import com.aionemu.gameserver.network.aion.serverpackets.*;

/**
 * This factory is responsible for creating {@link AionPacketHandler} object. It also initializes created handler with a
 * set of packet prototypes.<br>
 * Object of this classes uses <tt>Injector</tt> for injecting dependencies into prototype objects.<br>
 * <br>
 * 
 * @author Luno
 * 
 */
public class AionPacketHandlerFactory extends AbstractPacketHandlerFactory<AionChannelHandler>
{
	
	/**
	 * Creates new instance of <tt>AionPacketHandlerFactory</tt><br>
	 */
	public AionPacketHandlerFactory()
	{
		super(null, new AionClientPacketHandler<AionChannelHandler>());
		
		//Client Packet's
		addPacket(new CM_CRAFT(0x00), State.ENTERED);// 1.9
		addPacket(new CM_CLIENT_COMMAND_LOC(0x01), State.ENTERED);// 1.9
		addPacket(new CM_RESTORE_CHARACTER(0x04), State.AUTHED);// 1.9
		addPacket(new CM_START_LOOT(0x05), State.ENTERED);// 1.9
		addPacket(new CM_LOOT_ITEM(0x06), State.ENTERED);// 1.9
		addPacket(new CM_MOVE_ITEM(0x07), State.ENTERED);// 1.9
		addPacket(new CM_L2AUTH_LOGIN_CHECK(0x08), State.CONNECTED);// 1.9
		addPacket(new CM_CHARACTER_LIST(0x09), State.AUTHED);// 1.9
		addPacket(new CM_CREATE_CHARACTER(0x0A), State.AUTHED);// 1.9
		addPacket(new CM_DELETE_CHARACTER(0x0B), State.AUTHED);// 1.9
		addPacket(new CM_LEGION_UPLOAD_EMBLEM(0x0C), State.ENTERED);// testing
		addPacket(new CM_SPLIT_ITEM(0x10), State.ENTERED);// 1.9
		addPacket(new CM_PLAYER_SEARCH(0x12), State.ENTERED);// 1.9
		addPacket(new CM_LEGION_UPLOAD_INFO(0x13), State.ENTERED);// testing
		addPacket(new CM_FRIEND_STATUS(0x15), State.ENTERED);// 1.9
		addPacket(new CM_CHANGE_CHANNEL(0x17), State.ENTERED);// 1.9
		addPacket(new CM_BLOCK_ADD(0x19), State.ENTERED);// 1.9
		addPacket(new CM_BLOCK_DEL(0x1A), State.ENTERED);// 1.9
		addPacket(new CM_SHOW_BLOCKLIST(0x1B), State.ENTERED);// 1.9
		addPacket(new CM_CHECK_NICKNAME(0x1C), State.AUTHED);// 1.9
		addPacket(new CM_REPLACE_ITEM(0x1D), State.ENTERED);// testing
		addPacket(new CM_BLOCK_SET_REASON(0x1E), State.ENTERED);// 1.9
		addPacket(new CM_MAC_ADDRESS2(0x21), State.ENTERED);// 1.9
		addPacket(new CM_MACRO_CREATE(0x22), State.ENTERED);// 1.9
		addPacket(new CM_MACRO_DELETE(0x23), State.ENTERED);// 1.9
		addPacket(new CM_DISTRIBUTION_SETTINGS(0x24), State.ENTERED);// 1.9
		addPacket(new CM_MAY_LOGIN_INTO_GAME(0x25), State.AUTHED);// 1.9
		addPacket(new CM_SHOW_BRAND(0x28), State.ENTERED);// 1.9
		addPacket(new CM_RECONNECT_AUTH(0x2A), State.AUTHED);// 1.9
		addPacket(new CM_GROUP_LOOT(0x2B), State.ENTERED);	
		addPacket(new CM_SHOW_MAP(0x2F), State.ENTERED);// 1.9
		addPacket(new CM_MAC_ADDRESS(0x30), State.CONNECTED, State.AUTHED, State.ENTERED);// 1.9
		addPacket(new CM_REPORT_PLAYER(0x32), State.ENTERED);// need to find wrong code
		addPacket(new CM_GROUP_RESPONSE(0x33), State.ENTERED);// 1.9
		addPacket(new CM_SUMMON_MOVE(0x34), State.ENTERED);// 1.9
		addPacket(new CM_SUMMON_EMOTION(0x35), State.ENTERED);// 1.9
		addPacket(new CM_SUMMON_ATTACK(0x36), State.ENTERED);// 1.9
		addPacket(new CM_DELETE_QUEST(0x43), State.ENTERED);// 1.9
		addPacket(new CM_ITEM_REMODEL(0x45), State.ENTERED);// 1.9			
		addPacket(new CM_GODSTONE_SOCKET(0x46), State.ENTERED);// 1.9
		addPacket(new CM_INVITE_TO_GROUP(0x4C), State.ENTERED);// 1.9
		addPacket(new CM_ALLIANCE_GROUP_CHANGE(0x4D), State.ENTERED);// 1.9
		addPacket(new CM_VIEW_PLAYER_DETAILS(0x4F), State.ENTERED);// 1.9
		addPacket(new CM_PLAYER_STATUS_INFO(0x53), State.ENTERED);// 1.9
		addPacket(new CM_CLIENT_COMMAND_ROLL(0x56), State.ENTERED);// 1.9			
		addPacket(new CM_GROUP_DISTRIBUTION(0x57), State.ENTERED);// 1.9
		addPacket(new CM_PING_REQUEST(0x5A), State.ENTERED);// 1.9
		addPacket(new CM_DUEL_REQUEST(0x5D), State.ENTERED);// 1.9
		addPacket(new CM_DELETE_ITEM(0x5F), State.ENTERED);// 1.9
		addPacket(new CM_SHOW_FRIENDLIST(0x61), State.ENTERED);// 1.9
		addPacket(new CM_FRIEND_ADD(0x62), State.ENTERED);// 1.9
		addPacket(new CM_FRIEND_DEL(0x63), State.ENTERED);// 1.9
		addPacket(new CM_SUMMON_COMMAND(0x64), State.ENTERED);// 1.9
		addPacket(new CM_BROKER_LIST(0x66), State.ENTERED);// 1.9
		addPacket(new CM_BROKER_SEARCH(0x67), State.ENTERED);// 2.0
		addPacket(new CM_PRIVATE_STORE(0x6A), State.ENTERED);// 1.9
		addPacket(new CM_PRIVATE_STORE_NAME(0x6B), State.ENTERED);// 1.9
		addPacket(new CM_BROKER_SETTLE_LIST(0x6C), State.ENTERED);// 1.9
		addPacket(new CM_BROKER_SETTLE_ACCOUNT(0x6D), State.ENTERED);// 1.9
		addPacket(new CM_SEND_MAIL(0x6F), State.ENTERED);// 1.9
		addPacket(new CM_BROKER_REGISTERED(0x70), State.ENTERED);// 1.9
		addPacket(new CM_BUY_BROKER_ITEM(0x71), State.ENTERED);// 1.9
		addPacket(new CM_REGISTER_BROKER_ITEM(0x72), State.ENTERED);// 1.9
		addPacket(new CM_BROKER_CANCEL_REGISTERED(0x73), State.ENTERED);// 1.9
		addPacket(new CM_DELETE_MAIL(0x74),State.ENTERED);// 1.9
		addPacket(new CM_TITLE_SET(0x76), State.ENTERED);// 1.9
		addPacket(new CM_READ_MAIL(0x79), State.ENTERED);// 1.9
		addPacket(new CM_GET_MAIL_ATTACHMENT(0x7B), State.ENTERED);// 1.9
		addPacket(new CM_TELEPORT_SELECT(0x7F), State.ENTERED);// 1.9
		addPacket(new CM_PETITION(0x85), State.ENTERED);// 1.9
		addPacket(new CM_CHAT_MESSAGE_PUBLIC(0x86), State.ENTERED);// 1.9
		addPacket(new CM_CHAT_MESSAGE_WHISPER(0x87), State.ENTERED);// 1.9
		addPacket(new CM_PET_MOVE(0x88), State.ENTERED);// 2.0
		addPacket(new CM_PET(0x89), State.ENTERED);// 2.0
		addPacket(new CM_OPEN_STATICDOOR(0x8A), State.ENTERED);// 1.9
		addPacket(new CM_CASTSPELL(0x8C), State.ENTERED);// 1.9
		addPacket(new CM_SKILL_DEACTIVATE(0x8D), State.ENTERED);// 1.9
		addPacket(new CM_REMOVE_ALTERED_STATE(0x8E), State.ENTERED);// 1.9
		addPacket(new CM_TARGET_SELECT(0x92), State.ENTERED);// 1.9
		addPacket(new CM_ATTACK(0x93), State.ENTERED);// 1.9
		addPacket(new CM_EMOTION(0x96), State.ENTERED);// 1.9
		addPacket(new CM_PING(0x97), State.AUTHED, State.ENTERED);// 1.9
		addPacket(new CM_USE_ITEM(0x98), State.ENTERED);// 1.9
		addPacket(new CM_EQUIP_ITEM(0x99), State.ENTERED);// 1.9
		addPacket(new CM_FLIGHT_TELEPORT(0x9C), State.ENTERED);// 1.9
		addPacket(new CM_QUESTION_RESPONSE(0x9D), State.ENTERED);// 1.9
		addPacket(new CM_BUY_ITEM(0x9E), State.ENTERED);// 1.9
		addPacket(new CM_SHOW_DIALOG(0x9F), State.ENTERED);// 1.9
		addPacket(new CM_LEGION(0xA0), State.ENTERED);// 1.9
		addPacket(new CM_MOVE(0xA3), State.ENTERED);// 1.9
		addPacket(new CM_SET_NOTE(0xA5), State.ENTERED);// 1.9
		addPacket(new CM_LEGION_MODIFY_EMBLEM(0xA6), State.ENTERED);// 1.9
		addPacket(new CM_CLOSE_DIALOG(0xA8), State.ENTERED);// 1.9
		addPacket(new CM_DIALOG_SELECT(0xA9), State.ENTERED);// 1.9
		addPacket(new CM_LEGION_TABS(0xAA), State.ENTERED);// 1.9
		addPacket(new CM_EXCHANGE_ADD_KINAH(0xAD), State.ENTERED);// 1.9
		addPacket(new CM_EXCHANGE_LOCK(0xAE), State.ENTERED);// 1.9
		addPacket(new CM_EXCHANGE_OK(0xAF), State.ENTERED);// 1.9
		addPacket(new CM_EXCHANGE_REQUEST(0xB2), State.ENTERED);// 1.9
		addPacket(new CM_EXCHANGE_ADD_ITEM(0xB3), State.ENTERED);// 1.9
		addPacket(new CM_MANASTONE(0xB5), State.ENTERED);// 1.9
		addPacket(new CM_EXCHANGE_CANCEL(0xB8), State.ENTERED);// 1.9
		addPacket(new CM_PLAY_MOVIE_END(0xBC), State.ENTERED);// 1.9
		addPacket(new CM_SUMMON_CASTSPELL(0xC0), State.ENTERED);// 1.9
		addPacket(new CM_FUSION_WEAPONS(0xC1), State.ENTERED);// 1.9
		addPacket(new CM_BREAK_WEAPONS(0xC2), State.ENTERED);// 1.9
		addPacket(new CM_LEGION_SEND_EMBLEM(0xD3), State.ENTERED);// 1.9
		addPacket(new CM_DISCONNECT(0xED), State.ENTERED);// seems wrong
		addPacket(new CM_QUIT(0xEE), State.AUTHED, State.ENTERED);// 1.9
		addPacket(new CM_MAY_QUIT(0xEF), State.AUTHED, State.ENTERED);// 1.9
		addPacket(new CM_VERSION_CHECK(0xF3), State.CONNECTED); // 1.9
		addPacket(new CM_LEVEL_READY(0xF4), State.ENTERED);// 1.9
		addPacket(new CM_UI_SETTINGS(0xF5), State.ENTERED);// 1.9
		addPacket(new CM_OBJECT_SEARCH(0xF6), State.ENTERED);// 1.9
		addPacket(new CM_CUSTOM_SETTINGS(0xF7), State.ENTERED);// 1.9
		addPacket(new CM_REVIVE(0xF8), State.ENTERED);// 1.9
		addPacket(new CM_CHARACTER_EDIT(0xFA), State.AUTHED);// 1.9
		addPacket(new CM_ENTER_WORLD(0xFB), State.AUTHED); // 1.9
		addPacket(new CM_TIME_CHECK(0xFD), State.CONNECTED, State.AUTHED, State.ENTERED);// 1.9
		addPacket(new CM_GATHER(0xFE), State.ENTERED);// 1.9
		addPacket(new CM_QUESTIONNAIRE(0x7c), State.ENTERED); // 1.9
		// opcode 70 broker sell page
		// opcode 6c broker sold items page
		
		//Server Packet's
		addPacket(SM_STATUPDATE_MP.class, 0x00);// 1.9
		addPacket(SM_STATUPDATE_HP.class, 0x01);// 1.9
		addPacket(SM_CHAT_INIT.class, 0x02); // 1.9
		addPacket(SM_CHANNEL_INFO.class, 0x03);// 1.9
		addPacket(SM_MACRO_RESULT.class, 0x04); // 1.9
		addPacket(SM_MACRO_LIST.class, 0x05);// 1.9
		addPacket(SM_NICKNAME_CHECK_RESPONSE.class, 0x07);// 1.9
		addPacket(SM_RIFT_ANNOUNCE.class, 0x08);// 1.9
		addPacket(SM_SET_BIND_POINT.class, 0x09);// 1.9
		addPacket(SM_ABYSS_RANK.class, 0x0B);// 1.9
		addPacket(SM_FRIEND_UPDATE.class, 0x0C);
		addPacket(SM_PETITION.class, 0x0D);// 1.9
		addPacket(SM_RECIPE_DELETE.class, 0x0E); // 1.9
		addPacket(SM_LEARN_RECIPE.class, 0x0F);// 1.9
		addPacket(SM_FLY_TIME.class, 0x10);// 1.9
		addPacket(SM_DELETE.class, 0x12);// 1.9
		addPacket(SM_PLAYER_MOVE.class, 0x13);// 1.9
		addPacket(SM_MESSAGE.class, 0x14);// 1.9
		addPacket(SM_LOGIN_QUEUE.class, 0x15); // 1.9
		addPacket(SM_INVENTORY_INFO.class, 0x16);// 1.9
		addPacket(SM_SYSTEM_MESSAGE.class, 0x17);// 1.9
		addPacket(SM_DELETE_ITEM.class, 0x18);
		addPacket(SM_ADD_ITEMS.class, 0x19);// 1.9
		addPacket(SM_UI_SETTINGS.class, 0x1A);// 1.9
		addPacket(SM_UPDATE_ITEM.class, 0x1B);// 1.9
		addPacket(SM_PLAYER_INFO.class, 0x1C);// 1.9
		addPacket(SM_GATHER_STATUS.class, 0x1E);// 1.9
		addPacket(SM_CASTSPELL.class, 0x1F);// 1.9
		addPacket(SM_UPDATE_PLAYER_APPEARANCE.class, 0x20);// 1.9
		addPacket(SM_GATHER_UPDATE.class, 0x21);// 1.9
		addPacket(SM_STATUPDATE_DP.class, 0x22);// 1.9
		addPacket(SM_ATTACK_STATUS.class, 0x23);// 1.9
		addPacket(SM_STATUPDATE_EXP.class, 0x24);// 1.9
		addPacket(SM_DP_INFO.class, 0x25);// 1.9
		addPacket(SM_LEGION_TABS.class, 0x28);// 1.9
		addPacket(SM_LEGION_UPDATE_NICKNAME.class, 0x29);// 1.9
		addPacket(SM_NPC_INFO.class, 0x2A);// 1.9
		addPacket(SM_ENTER_WORLD_CHECK.class, 0x2B);// 1.9
		addPacket(SM_PLAYER_SPAWN.class, 0x2D);// 1.9
		addPacket(SM_GATHERABLE_INFO.class, 0x2F);// 1.9
		addPacket(SM_TELEPORT_LOC.class, 0x30);// 1.9
		addPacket(SM_ATTACK.class, 0x32);// 1.9
		addPacket(SM_MOVE.class, 0x35);// 1.9
		addPacket(SM_TRANSFORM.class, 0x36);// 1.9
		addPacket(SM_DIALOG_WINDOW.class, 0x38);// 1.9
		addPacket(SM_SELL_ITEM.class, 0x3A);// 1.9
		addPacket(SM_VIEW_PLAYER_DETAILS.class, 0x3F);
		addPacket(SM_PLAYER_STATE.class, 0x40);// 1.9
		addPacket(SM_WEATHER.class, 0x41);// 1.9
		addPacket(SM_GAME_TIME.class, 0x42);// 1.9
		addPacket(SM_EMOTION.class, 0x43);// 1.9
		addPacket(SM_LOOKATOBJECT.class, 0x44);// 1.9
		addPacket(SM_TIME_CHECK.class, 0x45);// 1.9
		addPacket(SM_SKILL_CANCEL.class, 0x46);// 1.9
		addPacket(SM_TARGET_SELECTED.class, 0x47);// 1.9
		addPacket(SM_SKILL_LIST.class, 0x48);// 1.9
		addPacket(SM_CASTSPELL_END.class, 0x49);// 1.9
		addPacket(SM_SKILL_ACTIVATION.class, 0x4A);// 1.9
		addPacket(SM_SKILL_REMOVE.class, 0x4B);// 1.9
		addPacket(SM_ABNORMAL_EFFECT.class, 0x4E);// 1.9
		addPacket(SM_ABNORMAL_STATE.class, 0x4F);// 1.9
		addPacket(SM_QUESTION_WINDOW.class, 0x50);// 1.9
		addPacket(SM_SKILL_COOLDOWN.class, 0x51);// 1.9
		addPacket(SM_INFLUENCE_RATIO.class, 0x53);// 1.9
		addPacket(SM_NAME_CHANGE.class, 0x54);// 1.9
		addPacket(SM_GROUP_INFO.class, 0x56);// 1.9
		addPacket(SM_SHOW_NPC_ON_MAP.class, 0x57);// 1.9
		addPacket(SM_GROUP_MEMBER_INFO.class, 0x59);// 1.9
		addPacket(SM_QUIT_RESPONSE.class, 0x5E);// 1.9
		addPacket(SM_LEVEL_UPDATE.class, 0x62);// 1.9
		addPacket(SM_KEY.class, 0x64); // 1.9
		addPacket(SM_EXCHANGE_REQUEST.class, 0x66);// 1.9
		addPacket(SM_SUMMON_PANEL_REMOVE.class, 0x67);// testing
		addPacket(SM_EXCHANGE_ADD_ITEM.class, 0x69);// 1.9
		addPacket(SM_EXCHANGE_CONFIRMATION.class, 0x6A);// 1.9
		addPacket(SM_EXCHANGE_ADD_KINAH.class, 0x6B);// 1.9
		addPacket(SM_EMOTION_LIST.class, 0x6D);// 1.9
		addPacket(SM_TARGET_UPDATE.class, 0x6F);// 1.9
		addPacket(SM_PLASTIC_SURGERY.class, 0x71);// 1.9
		addPacket(SM_LEGION_UPDATE_SELF_INTRO.class, 0x75);// 1.9
		addPacket(SM_RIFT_STATUS.class, 0x76); // 1.9
		addPacket(SM_QUEST_ACCEPTED.class, 0x78);// 1.9
		addPacket(SM_QUEST_LIST.class, 0x79); // 1.9
		addPacket(SM_PING_RESPONSE.class, 0x7C);// 1.9
		addPacket(SM_NEARBY_QUESTS.class, 0x7D); // 1.9
		addPacket(SM_CUBE_UPDATE.class, 0x7E); // 1.9
		addPacket(SM_FRIEND_LIST.class, 0x80);// 1.9
		addPacket(SM_PET.class, 0x83);// 2.0
		addPacket(SM_UPDATE_NOTE.class, 0x84); // 1.9
		addPacket(SM_ITEM_COOLDOWN.class, 0x85);// 1.9
		addPacket(SM_PLAY_MOVIE.class, 0x87); // 1.9
		addPacket(SM_LEGION_INFO.class, 0x8A);// 1.9
		addPacket(SM_LEGION_LEAVE_MEMBER.class, 0x8C);// 1.9
		addPacket(SM_LEGION_ADD_MEMBER.class, 0x8D);// 1.9
		addPacket(SM_LEGION_UPDATE_TITLE.class, 0x8E);// 1.9
		addPacket(SM_LEGION_UPDATE_MEMBER.class, 0x8F);// 1.9
		//addPacket(SM_BROKER_REGISTRATION_SERVICE.class, 0x93); with 2.0 merged in SM_BROKER_SERVICE
		//addPacket(SM_BROKER_SETTLED_LIST.class, 0x95); with 2.0 merged in SM_BROKER_SERVICE
		addPacket(SM_SUMMON_OWNER_REMOVE.class, 0x96);// testing
		addPacket(SM_SUMMON_PANEL.class, 0x97);// testing
		addPacket(SM_SUMMON_UPDATE.class, 0x99);// testing
		addPacket(SM_LEGION_EDIT.class, 0x9A);// 1.9
		addPacket(SM_LEGION_MEMBERLIST.class, 0x9B);// 1.9
		addPacket(SM_SUMMON_USESKILL.class, 0x9E);// testing
		addPacket(SM_MAIL_SERVICE.class, 0x9F);// 1.9
		addPacket(SM_PRIVATE_STORE.class, 0xA2);
		addPacket(SM_ABYSS_RANK_UPDATE.class, 0xA4);// testing
		addPacket(SM_GROUP_LOOT.class, 0xA5);
		addPacket(SM_MAY_LOGIN_INTO_GAME.class, 0xA7);// 1.9
		addPacket(SM_PONG.class, 0xAA);// 1.9
		addPacket(SM_PLAYER_ID.class, 0xAB);// 1.9
		addPacket(SM_KISK_UPDATE.class, 0xAC);// 1.9
		addPacket(SM_BROKER_SERVICE.class, 0xAE);// 1.9
		addPacket(SM_PRIVATE_STORE_NAME.class, 0xAF);// 1.9
		//addPacket(SM_BROKER_REGISTERED_LIST.class, 0xB1); with 2.0 merged in SM_BROKER_SERVICE
		addPacket(SM_ASCENSION_MORPH.class, 0xB2);// 1.9
		addPacket(SM_CRAFT_UPDATE.class, 0xB3);// was CD
		addPacket(SM_CUSTOM_SETTINGS.class, 0xB4);// 1.9
		addPacket(SM_ITEM_USAGE_ANIMATION.class, 0xB5);// 1.9
		addPacket(SM_DUEL.class, 0xB7);// 1.9
		addPacket(SM_PET_MOVE.class, 0xB9);// 2.0
		addPacket(SM_RESURRECT.class, 0xBE);// 1.9
		addPacket(SM_DIE.class, 0xBF);// 1.9
		addPacket(SM_TELEPORT_MAP.class, 0xC0);// 1.9
		addPacket(SM_FORCED_MOVE.class, 0xC1);// 1.9		
		addPacket(SM_WAREHOUSE_INFO.class, 0xC4);// 1.9
		addPacket(SM_DELETE_WAREHOUSE_ITEM.class, 0xC6);// 1.9
		addPacket(SM_WAREHOUSE_UPDATE.class, 0xC7);// 1.9
		addPacket(SM_UPDATE_WAREHOUSE_ITEM.class, 0xC9);// 1.9		
		addPacket(SM_TITLE_LIST.class, 0xCC);// 1.9
		addPacket(SM_TITLE_SET.class, 0xCF);// 1.9
		addPacket(SM_CRAFT_ANIMATION.class, 0xD0);
		addPacket(SM_TITLE_UPDATE.class, 0xD1);// 1.9
		addPacket(SM_LEGION_SEND_EMBLEM.class, 0xD3);// 1.9
		addPacket(SM_LEGION_UPDATE_EMBLEM.class, 0xD5);// testing
		addPacket(SM_FRIEND_RESPONSE.class, 0xDA);// 1.9
		addPacket(SM_BLOCK_LIST.class, 0xDC);// 1.9
		addPacket(SM_BLOCK_RESPONSE.class, 0xDD);// 1.9
		addPacket(SM_FRIEND_NOTIFY.class, 0xDF);// 1.9
		addPacket(SM_USE_OBJECT.class, 0xE3);// 1.9
		addPacket(SM_CHARACTER_LIST.class, 0xE4);// 1.9
		addPacket(SM_L2AUTH_LOGIN_CHECK.class, 0xE5);// 1.9
		addPacket(SM_DELETE_CHARACTER.class, 0xE6);
		addPacket(SM_CREATE_CHARACTER.class, 0xE7);// 1.9
		addPacket(SM_TARGET_IMMOBILIZE.class, 0xE8);
		addPacket(SM_RESTORE_CHARACTER.class, 0xE9);// 1.9
		addPacket(SM_LOOT_ITEMLIST.class, 0xEA);// 1.9
		addPacket(SM_LOOT_STATUS.class, 0xEB);// 1.9
		addPacket(SM_MANTRA_EFFECT.class, 0xEC);// 1.9		
		addPacket(SM_RECIPE_LIST.class, 0xED);// testing
		addPacket(SM_SIEGE_LOCATION_INFO.class, 0xEF);// 1.9
		addPacket(SM_PLAYER_SEARCH.class, 0xF1);// 1.9
		addPacket(SM_ALLIANCE_MEMBER_INFO.class, 0xF2);// 1.9
		addPacket(SM_ALLIANCE_INFO.class, 0xF3);// 1.9
		addPacket(SM_LEAVE_GROUP_MEMBER.class, 0xF5);// 1.9
		addPacket(SM_ALLIANCE_READY_CHECK.class, 0xF6);// 1.9
		addPacket(SM_SHOW_BRAND.class, 0xF7);// 1.9
		addPacket(SM_PRICES.class, 0xF8);// 1.9
		addPacket(SM_TRADELIST.class, 0xFB);// 1.9
		addPacket(SM_VERSION_CHECK.class, 0xFC);// 1.9
		addPacket(SM_RECONNECT_KEY.class, 0xFD);// 1.9
		addPacket(SM_STATS_INFO.class, 0xFF);// 1.9
		addPacket(SM_QUESTIONNAIRE.class, 0xBD); // 1.9
		addPacket(SM_CUSTOM_PACKET.class, 99999); // fake packet

		// Unrecognized Opcodes from 1.5.4:
		// addPacket(SM_BUY_LIST.class, 0x7E);

		// Unrecognized Opcodes from 1.5.0:
		// addPacket(SM_VIRTUAL_AUTH.class, 0xE4);
		// addPacket(SM_WAITING_LIST.class, 0x18);

	}
}

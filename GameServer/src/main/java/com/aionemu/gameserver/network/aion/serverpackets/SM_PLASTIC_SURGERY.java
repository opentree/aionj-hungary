package com.aionemu.gameserver.network.aion.serverpackets;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

/**
 * @author IlBuono
 */
public class SM_PLASTIC_SURGERY extends AionServerPacket
{
	private int		playerObjId;
    private boolean    check_ticket; //2 no ticket, 1 have ticket
    private boolean    change_sex; 	 //0 plastic surgery, 1 gender switch

	public SM_PLASTIC_SURGERY(Player player, boolean check_ticket, boolean change_sex)
	{
            this.playerObjId = player.getObjectId();
            this.check_ticket = check_ticket;
            this.change_sex = change_sex;
	}


	@Override
	protected void writeImpl(AionConnection con)
	{
            writeD(playerObjId);
            writeC( check_ticket ? 1 : 2);
            writeC( change_sex ? 1 : 0);
	}
}

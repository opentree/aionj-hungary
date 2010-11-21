package parser.util;

import java.util.Map;

import parser.clientData.clientStrings.ClientString;

public class SystemMessagesParser 
{
	public static void writeFile(String dir)
	{		
		Map<String, ClientString> strings = DataManager.getInstance().getStringNameMap();
		
		XmlDocument messageDoc = new XmlDocument(dir + "system_messages.xml","system_messages");
		System.out.println("Wait for finish!");
		for(ClientString mes : strings.values())
		{
			if(mes.getName().contains("STR_MSG") || 
					mes.getName().contains("STR_CHAT")||
					mes.getName().contains("STR_SKILL")||
					mes.getName().contains("STR_CANNOT")||
					mes.getName().contains("STR_ENSLAVE")||
					mes.getName().contains("STR_DUEL")||
					mes.getName().contains("STR_GUILD")||
					mes.getName().contains("STR_FORCE")||
					mes.getName().contains("STR_PARTY"))
			{
//				System.out.println(mes.getName());
				int count = 0;
				if(mes.getBody() != null)
					count = mes.getBody().split("%").length - 1;
				int id1 = messageDoc.addElement("system_message",null,mes.getBody(),null);
				messageDoc.addElement("id",String.valueOf(mes.getId()),null,id1);
				messageDoc.addElement("name",mes.getName(),null,id1);
				messageDoc.addElement("param",String.valueOf(count),null,id1);
			}
		}
		System.out.println("Saving!");
		messageDoc.save();
		System.out.println("Finish!");
	}
}

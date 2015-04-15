package Packets;

import Server.Server;
import Client.Screens.GameScreen;

public class ChatPacket extends Packet{

	private static final long serialVersionUID = -1467244113040215472L;

	public Long cid;
	public String chatMessage;
	public String name;
	public String chatChannel;

	public ChatPacket(Long connectionID, String channel, String charName, String message){
		chatChannel = channel;
		cid = connectionID;
		chatMessage = message;
		name = charName;
	}

	@Override
	public void processClient(){
		if(GameScreen.getInstance().chatDialog.tabs.get(chatChannel) == null){
			GameScreen.getInstance().chatDialog.chatBuffer.put(name+chatMessage, this);
			GameScreen.getInstance().chatDialog.clearBuffer();
		}else{
			GameScreen.getInstance().chatDialog.chatBuffer.put(name+chatMessage, this);
			GameScreen.getInstance().chatDialog.clearBuffer();
		}
	}

	@Override
	public void processServer(){
		Server.relayChat(cid, chatChannel, name, chatMessage);
	}
}

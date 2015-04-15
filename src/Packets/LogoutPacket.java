package Packets;

import Client.Players.PlayerInfo;
import Server.Server;

public class LogoutPacket extends Packet{

	private static final long serialVersionUID = 4268726398613885026L;
	
	public String name;
	public Long cid;

	public LogoutPacket(Long clientID, String charName){
		cid = clientID;
		name = charName;
	}
	
	@Override
	public void processServer(){
		Server.clients.get(cid).information = new PlayerInfo();
		Server.tellEveryoneThisGuyIsGone(cid, name);
	}
}

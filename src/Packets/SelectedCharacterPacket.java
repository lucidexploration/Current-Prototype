package Packets;

import Server.Server;

public class SelectedCharacterPacket extends Packet{

	private static final long serialVersionUID = -1450631319178712219L;

	public String selectedCharacter;
	public Long cid;
	
	public SelectedCharacterPacket(Long id, String charName){
		cid = id;
		selectedCharacter = charName;
	}
	
	@Override
	public void processServer(){
		Server.loginWithThisCharacter(cid, selectedCharacter);
	}
}

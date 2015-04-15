package Packets;

import Server.Server;

public class CharacterDeletionPacket extends Packet{

	private static final long serialVersionUID = -8003228524350501035L;
	
	public String username;
	public String password;
	public String characterName;
	
	public long cid;
	
	public CharacterDeletionPacket(long id, String user, String pass, String characterToDelete){
		cid = id;
		username = user;
		password = pass;
		characterName = characterToDelete;
	}
	
	@Override
	public void processServer(){
		Server.deleteSelectedCharacter(cid, username, password, characterName);
	}
}

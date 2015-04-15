package Packets;

import Server.Server;

public class LoginPacket extends Packet{

	private static final long serialVersionUID = -4405747286636957827L;
	
	public String account;
	public String password;
	public Long clientID;
	
	public LoginPacket(Long id, String acc, String pass){
		account = acc;
		password = pass;
		clientID = id;
	}
	
	@Override
	public void processServer(){
		Server.verifyAndProcessLogin(clientID,account,password);
	}
}

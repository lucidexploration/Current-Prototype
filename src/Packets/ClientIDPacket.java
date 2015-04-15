package Packets;

import Client.Client;

public class ClientIDPacket extends Packet{

	private static final long serialVersionUID = -6367797262971950469L;
	
	public Long cidp;
	
	public ClientIDPacket(Long id){
		cidp = id;
	}

	@Override
	public void processClient(){
		Client.connectionID = cidp;
		System.out.println("Client ID = "+cidp);
	}
}

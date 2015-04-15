package Packets;

import Client.Hunted;
import Client.Players.Player;

public class PlayerGonePacket extends Packet{

	private static final long serialVersionUID = 1327716345239895066L;
	
	String name;
	
	public PlayerGonePacket(String charName){
		name = charName;
	}
	
	@Override
	public void processClient(){
		if(Hunted.people.containsKey(name) && !Player.getInstance().information.name.equals(name)){
			Hunted.people.remove(name);
		}
	}
}

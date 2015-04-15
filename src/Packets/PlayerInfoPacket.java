package Packets;

import Client.Hunted;
import Client.Players.Person;
import Client.Players.Player;
import Client.Players.PlayerInfo;
import Client.Screens.GameScreen;

public class PlayerInfoPacket extends Packet{

	private static final long serialVersionUID = -1062104920536388173L;
	
	public PlayerInfo playerInfo;

	public PlayerInfoPacket(PlayerInfo info){
		playerInfo = info;
	}
	
	@Override
	public void processClient(){
		System.out.println(playerInfo.name + " - "+ playerInfo.xCoord+" - "+ playerInfo.yCoord +" - " + playerInfo.zCoord);
		if(Player.getInstance().information.name.equals(playerInfo.name)){
			if(!Hunted.people.containsKey(playerInfo.name)){
				Hunted.people.put(playerInfo.name, Player.getInstance());
			}
			Player.getInstance().information = playerInfo;
			GameScreen.getInstance().friendDialog.updateFriends(playerInfo.friendList);
			return;
		}
		
		if(Hunted.people.containsKey(playerInfo.name)){
			Hunted.people.get(playerInfo.name).information = playerInfo;
			return;
		}
		Hunted.people.put(playerInfo.name, new Person(playerInfo));
		System.out.println(playerInfo.name+" is now on your screen.");
	}

	/*
	 * Removes information from the player that anyone outside that player doesnt need to know.
	 */
	public PlayerInfoPacket stripped() {
		playerInfo.friendList = null;
		playerInfo.currentExperience = 0;
		return this;
	}
}
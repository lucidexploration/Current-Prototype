package Packets;

import Client.Hunted;
import Client.Screens.SelectScreen;

public class CharacterSelectPacket extends Packet{

	private static final long serialVersionUID = 6847662375159446423L;
	
	public String[] list;

	public CharacterSelectPacket(String[] characters) {
		list = characters;
	}
	
	@Override
	public void processClient(){
		SelectScreen.characterList = list;
		SelectScreen.fillCharacterBox();
		
		Hunted.getInstance().setScreen(SelectScreen.getInstance());
	}
}

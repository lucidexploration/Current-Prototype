package Packets;

public class NameCheckPacket extends Packet{

	private static final long serialVersionUID = -6489475823573931815L;
	
	public String charName;
	
	public NameCheckPacket(String name){
		charName = name;
	}
}

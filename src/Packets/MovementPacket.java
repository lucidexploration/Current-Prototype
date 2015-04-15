package Packets;

import Server.Server;

public class MovementPacket extends Packet{

	private static final long serialVersionUID = 848414931575397839L;
	
	public long cid;
	public String name;
	public int direction;

	public MovementPacket(Long id, String characterName, int movementDirection){
		cid = id;
		name = characterName;
		direction = movementDirection;
	}
	
	@Override
	public void processServer(){
		Server.verifyAndHandlePlayerMovement(cid,name,direction);
	}
}

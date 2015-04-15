package Packets;

import Client.Utilities.Loader;

public class VersionVerificationPacket extends Packet{

	private static final long serialVersionUID = -3562129636242447114L;

	String md5;
	
	public VersionVerificationPacket(String hash){
		md5 = hash;
	}
	
	@Override
	public void processClient(){
		Loader.verifyMapIntegrity(md5);
	}
	
	@Override
	public void processServer(){
	}
}

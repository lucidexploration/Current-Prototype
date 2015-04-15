package Packets;

import Server.Server;

public class RegistrationPacket extends Packet{

	private static final long serialVersionUID = 1405132022049770319L;
	
	public long connectionID;
	
	public String username;
	public String password;
	public String email;
	public String hint;
	public String characterName;
	
	public int health;
	public int mana;
	
	public int strength;
	public int intelligence;
	public int wisdom;
	public int agility;
	public int speed;
	
	public boolean existingAccount;

	public RegistrationPacket(Long cid, String user, String pass, String emailAddress, String emailHint, String charName, int hp, int mp, int str, int intel, int wis, int agi, int spd, boolean exists){
		connectionID = cid;
		username = user;
		password = pass;
		email = emailAddress;
		hint = emailHint;
		characterName = charName;
		
		health = hp;
		mana = mp;
		
		strength = str;
		wisdom = wis;
		intelligence = intel;
		speed = spd;
		agility = agi;
		
		existingAccount = exists;
	}
	
	@Override
	public void processServer(){
		Server.clients.get(connectionID).dbHandler.accountAndCharacterCreation(username, password, email, hint, characterName, health, mana, strength, intelligence, wisdom, agility, speed, existingAccount);
	}
}

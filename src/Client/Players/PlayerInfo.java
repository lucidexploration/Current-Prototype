package Client.Players;

import java.io.Serializable;

public class PlayerInfo implements Serializable{

	private static final long serialVersionUID = -2088291681910432592L;

	public String name = "";
	public int hp = 1;
	public int mp = 1;
	
	public int hpmax = 1;
	public int mpmax = 1;
	
	public int strength = 1;
	public int intelligence = 1;
	public int wisdom = 1;
	public int agility = 1;
	public int speed = 1;
	
	public int xCoord = 1;
	public int yCoord = 1;
	public int zCoord = 1;
	
	public int currentExperience = 1;
	public int totalExperience = 1;
	
	public String[] friendList;
	
	public PlayerInfo(){
	}
	
	public PlayerInfo(String charName, int health, int healthMax, int mana, int manaMax, int str, int intel, int wis, int agi, int spd, int x, int y, int z, int currentExp, int maxExp, String[] friends){
		name = charName;
		hp = health;
		hpmax = healthMax;
		mp = mana;
		mpmax = manaMax;
		strength = str;
		intelligence = intel;
		wisdom = wis;
		agility = agi;
		speed = spd;
		xCoord = x;
		yCoord = y;
		zCoord =z;
		currentExperience = currentExp;
		totalExperience = maxExp;
		friendList = friends;
	}
}

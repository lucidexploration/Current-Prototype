package World.AI;

import java.io.Serializable;

public class MonsterInfo implements Serializable{

	private static final long serialVersionUID = -1526962960757344304L;

	public String name;
	public int hp;
	public int mp;
	
	public int hpmax;
	public int mpmax;
	
	public int strength;
	public int intelligence;
	public int wisdom;
	public int agility;
	public int speed;
	
	public int xCoord;
	public int yCoord;
	public int zCoord;
	
	public MonsterInfo(){
		
	}
}

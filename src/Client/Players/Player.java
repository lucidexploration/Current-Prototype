package Client.Players;

public class Player extends Person{
	
	private static Player instance;
	
	private Player(){
		super();
	}
	
	public static Player getInstance(){
		if(instance == null){
			instance = new Player();
		}
		return instance;
	}
}

package World.AI;

public class MonsterOverLord implements Runnable{

	private static MonsterOverLord instance;
	
	private MonsterOverLord(){
		
	}
	
	public static MonsterOverLord getInstance(){
		if(instance == null){
			instance = new MonsterOverLord();
		}
		return instance;
	}
	
	@Override
	public void run() {
		while(true){
			
		}
	}

}

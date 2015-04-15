package World.AI;

import com.badlogic.gdx.graphics.g2d.Batch;

public class Monster {

	public MonsterInfo information;

	public Monster(MonsterInfo monsterInfo){
		information = monsterInfo;
	}
	
	public void render(@SuppressWarnings("unused") Batch b){
		if(information == null){
			return;
		}
	}
}

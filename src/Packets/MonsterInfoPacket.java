package Packets;

import World.AI.Monster;
import World.AI.MonsterInfo;
import Client.Hunted;

public class MonsterInfoPacket extends Packet {

	private static final long serialVersionUID = 8100206614649341456L;
	public MonsterInfo monsterInfo;

	public MonsterInfoPacket(MonsterInfo info){
		monsterInfo = info;
	}

	@Override
	public void processClient(){
		if(Hunted.monsters.containsKey(monsterInfo.name)){
			Hunted.monsters.get(monsterInfo.name).information = monsterInfo;
			return;
		}
		Hunted.monsters.put(monsterInfo.name, new Monster(monsterInfo));
	}
}

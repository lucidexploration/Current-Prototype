package Client.Players;

import Client.Players.Parts.NameBar;
import Client.Utilities.Loader;
import Notes.Constants;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

public class Person {
	
	public PlayerInfo information;
	private Texture texture;
	public NameBar nameBar;
	
	public Person(){
		information = new PlayerInfo();
		texture = Loader.assets.get("sprites/players/testtoon.png", Texture.class);
		
		nameBar = new NameBar(this, information.name);
	}
	
	public Person(PlayerInfo playerInfo){
		information =  playerInfo;
		texture = Loader.assets.get("sprites/players/testtoon.png", Texture.class);
		
		nameBar = new NameBar(this, information.name);
	}
	
	public void render(Batch b, int xCoord, int yCoord, int width, int height){
		int tileHeight = Gdx.graphics.getHeight() / Constants.NUMBEROFVERTICALTILES;
		int tileWidth = Gdx.graphics.getWidth() / Constants.NUMBEROFHORIZONTALTILES;
		
		double maxHp = information.hpmax;
		double currentHp = information.hp;
		double hpPercent = currentHp / maxHp;
		double pixelsPerHP = tileWidth * hpPercent;
		
		double maxMp = information.mpmax;
		double currentMp = information.mp;
		double mpPercent = currentMp / maxMp;
		double pixelsPerMP = tileWidth * mpPercent;
		
		b.draw(texture, xCoord, yCoord, width, height);
		nameBar.draw(b, xCoord, yCoord);
		b.draw(Loader.assets.get("sprites/players/parts/manabar.png",Texture.class), xCoord, yCoord, (float) pixelsPerMP,tileHeight);
		b.draw(Loader.assets.get("sprites/players/parts/healthbar.png",Texture.class), xCoord, yCoord, (float) pixelsPerHP,tileHeight);
	}

	public Texture getTexture() {
		if(texture == null){
			return Loader.assets.get("sprites/players/testtoon.png", Texture.class);
		}
		return texture;
	}
	
	@Override
	public boolean equals(Object o){
		if(o instanceof Person){
			if(((Person) o).information.name.equals(this.information.name)){
				return true;
			}
		}
		return false;
	}
}

package Client.Players.Parts;

import Client.Players.Person;
import Client.Utilities.Loader;
import Notes.Constants;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class NameBar extends Actor{

	private BitmapFont wrapper = Loader.wrapper;
	private Person belongsTo;
	
	private String displayText = "";

	public NameBar(Person p, String charName){
		belongsTo = p;
		displayText = charName;
	}

	@Override
	public void act (float delta) {
	}

	public void draw(Batch b, int xCoord, int yCoord){
		int tileHeight = Gdx.graphics.getHeight() / Constants.NUMBEROFVERTICALTILES;
		wrapper.draw(b, belongsTo.information.name, xCoord + 5, yCoord + tileHeight + (tileHeight / 7));
	}
}
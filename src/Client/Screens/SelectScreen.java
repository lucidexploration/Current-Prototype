package Client.Screens;

import java.io.IOException;

import Client.Client;
import Client.Hunted;
import Packets.SelectedCharacterPacket;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class SelectScreen implements Screen{

	private static SelectScreen instance;

	public static String[] characterList;
	public static SelectBox<String> charSelectBox;

	//user interface
	private Stage stage;
	private Skin skin;
	private TextButton selectButton;
	private TextButton exitButton;
	private TextButton deleteButton;
	
	private Table table;

	private SelectScreen(){
		stage = new Stage();
		skin = new Skin(Gdx.files.internal("uiskin.json"));
		
		table = new Table();
		table.setPosition((Gdx.graphics.getWidth() / 2) - (table.getWidth()/2), Gdx.graphics.getHeight() / 2);

		charSelectBox = new SelectBox<String>(skin);

		selectButton = new TextButton("Login",skin);
		selectButton.addListener(new ChangeListener(){
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if(charSelectBox.getSelected()!=null){
					try {
						Hunted.player.information.name = charSelectBox.getSelected();
						Gdx.graphics.setTitle("Hunt(ed) Experimental - " + charSelectBox.getSelected());
						Client.connection.oos.writeObject(new SelectedCharacterPacket(Client.connectionID,charSelectBox.getSelected()));
						Client.connection.oos.flush();
						Hunted.getInstance().setScreen(GameScreen.getInstance());
					} catch (IOException e) {
						e.printStackTrace();
						System.exit(-1);
					}
				}
			}
		});
		exitButton = new TextButton("Exit",skin);
		exitButton.addListener(new ChangeListener(){
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				event.handle();
				Hunted.getInstance().setScreen(MainMenuScreen.getInstance());
			}
		});
		
		deleteButton = new TextButton("Delete Selected Character",skin);
		deleteButton.addListener(new ChangeListener(){
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				event.handle();
				Client.connection.sendDeleteCharacterPacket(charSelectBox.getSelected());
			}
		});
		
		table.add(charSelectBox).padBottom(80);
		table.add(deleteButton).padBottom(80);
		table.row();
		table.add(selectButton).colspan(2);
		table.row();
		table.add(exitButton).colspan(2);

		stage.addActor(table);
	}

	public static SelectScreen getInstance(){
		if(instance == null){
			instance = new SelectScreen();
		}
		return instance;
	}

	@Override
	public void show() {
	}

	@Override
	public void render(float delta) {
		Gdx.input.setInputProcessor(stage);
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act();
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void hide() {
	}

	@Override
	public void dispose() {
	}

	public static void fillCharacterBox(){
		if(characterList != null){
			charSelectBox.setItems(characterList);
		}
	}
}

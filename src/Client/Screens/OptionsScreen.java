package Client.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class OptionsScreen implements Screen{
	
	private static OptionsScreen instance;
	
	//user interface
	private Stage stage;
	private Skin skin;

	private OptionsScreen(){
		stage = new Stage();
		skin = new Skin(Gdx.files.internal("uiskin.json"));
	}
	
	public static OptionsScreen getInstance(){
		if(instance == null){
			instance = new OptionsScreen();
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
}

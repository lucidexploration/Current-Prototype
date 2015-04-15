package Client.Screens;

import Client.Client;
import Client.Hunted;
import Client.Utilities.Loader;
import Utilities.Encrypter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class MainMenuScreen implements Screen{
	
	private static MainMenuScreen instance;
	
	//user interface
	private Stage stage;
	private Skin skin;
	
	private TextButton loginButton;
	private TextButton exitButton;
	private TextButton optionsButton;
	private TextButton registerButton;
	
	private TextField accountField;
	private TextField passwordField;

	private MainMenuScreen(){
		stage = new Stage();
		
		Texture bgTexture = Loader.assets.get("ui/bg.png");
		Image background = new Image(bgTexture);
		stage.addActor(background);
		
		skin = new Skin(Gdx.files.internal("uiskin.json"));
		layoutEverything();
	}
	
	private void layoutEverything() {
		//buttons
		loginButton = new TextButton("Login",skin);
		loginButton.addListener(new ChangeListener(){//send login info when button pressed
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				event.handle();
				
				String accountname = accountField.getText();
				String pass = passwordField.getText();
				pass = Encrypter.base64encode(pass);
				
				Client.connection.sendLoginPacket(Client.connectionID,accountname,pass);
				Client.username = accountname;
				Client.password = pass;
			}
		});
		exitButton = new TextButton("Exit",skin);
		exitButton.addListener(new ChangeListener(){
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				event.handle();
				Client.closeEverything();
			}
		});
		optionsButton = new TextButton("Options",skin);
		optionsButton.addListener(new ChangeListener(){
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				event.handle();
				Hunted.getInstance().setScreen(OptionsScreen.getInstance());
			}
		});
		registerButton = new TextButton("Register",skin);
		registerButton.addListener(new ChangeListener(){
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				event.handle();
				Hunted.getInstance().setScreen(RegistrationScreen.getInstance());
			}
		});
		
		//text fields
		accountField = new TextField("Account",skin);
		accountField.setMaxLength(20);
		passwordField = new TextField("Password",skin);
		passwordField.setMaxLength(20);
		passwordField.setPasswordCharacter('*');
		passwordField.setPasswordMode(true);
		
		//arrange everything
		accountField.setPosition((stage.getWidth() / 2) - (accountField.getWidth() / 2), stage.getHeight() / 3);
		passwordField.setPosition((stage.getWidth() / 2) - (passwordField.getWidth() / 2), accountField.getY() - passwordField.getHeight());
		loginButton.setPosition((stage.getWidth() / 2) - (loginButton.getWidth() / 2), passwordField.getY() - (loginButton.getHeight() * 2));
		registerButton.setPosition((stage.getWidth() / 2) - (registerButton.getWidth() / 2), loginButton.getY() - registerButton.getHeight());
		optionsButton.setPosition((stage.getWidth() / 2) - optionsButton.getWidth() / 2, registerButton.getY() - optionsButton.getHeight());
		exitButton.setPosition((stage.getWidth() / 2) - (registerButton.getWidth() / 2), optionsButton.getY() - exitButton.getHeight());
		
		stage.addActor(accountField);
		stage.addActor(passwordField);
		stage.addActor(loginButton);
		stage.addActor(registerButton);
		stage.addActor(optionsButton);
		stage.addActor(exitButton);
	}

	public static MainMenuScreen getInstance(){
		if(instance == null){
			instance = new MainMenuScreen();
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

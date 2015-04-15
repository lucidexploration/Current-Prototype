package Client.Screens;

import java.io.IOException;

import Client.Client;
import Client.Hunted;
import Packets.NameCheckPacket;
import Utilities.Encrypter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class RegistrationScreen implements Screen{

	private static RegistrationScreen instance;

	//user interface
	private Stage stage;
	private Skin skin;

	//character creation ui elements
	private boolean nameIsGood = false;

	private Table table;

	private CheckBox existingAccountCheck;
	private TextField usernameField;
	private TextField passwordField;
	private TextField emailField;
	private TextField hintField;

	private TextField characterNameField;
	private TextButton nameCheckButton;

	private Label nameCheckLabel;
	private Label pointsLeftLabel;
	private Label healthLabel;
	private Label manaLabel;
	private Label strengthLabel;
	private Label speedLabel;
	private Label intelligenceLabel;
	private Label wisdomLabel;
	private Label agilityLabel;

	private Slider healthSlider;
	private Slider manaSlider;
	private Slider strengthSlider;
	private Slider speedSlider;
	private Slider intelligenceSlider;
	private Slider wisdomSlider;
	private Slider agilitySlider;

	private TextButton createCharacterButton;

	private TextButton exitButton;

	private RegistrationScreen(){
		stage = new Stage();
		skin = new Skin(Gdx.files.internal("uiskin.json"));

		//instantiation and customization
		table = new Table();

		existingAccountCheck = new CheckBox("Already have an account.",skin);
		usernameField = new TextField("username",skin);
		usernameField.setMaxLength(20);
		passwordField = new TextField("password",skin);
		passwordField.setMaxLength(20);
		emailField = new TextField("email",skin);
		emailField.setMaxLength(40);
		hintField = new TextField("recovery hint",skin);
		hintField.setMaxLength(20);

		characterNameField = new TextField("character name",skin);
		characterNameField.setMaxLength(20);
		nameCheckButton = new TextButton("Check name",skin);
		nameCheckButton.addListener(new ChangeListener(){
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				event.handle();
				try {
					Client.connection.oos.writeObject(new NameCheckPacket(characterNameField.getText()));
					Client.connection.oos.flush();
				} catch (IOException e) {
					e.printStackTrace();
					System.exit(-1);
				}
			}
		});

		nameCheckLabel = new Label( "Check if you have an existing account, then fill in your information.",skin);
		pointsLeftLabel = new Label(" - points left.",skin);
		healthLabel = new Label("10 - health points",skin);
		manaLabel = new Label("10 - mana points",skin);
		strengthLabel = new Label("10 - strength points",skin);
		speedLabel = new Label("10 - speed points",skin);
		intelligenceLabel = new Label("10 - intelligence points",skin);
		wisdomLabel = new Label("10 - wisdom points",skin);
		agilityLabel = new Label("10 - agility points",skin);
		
		healthSlider = new Slider(10, 100, 1, false, skin);
		manaSlider = new Slider(10, 100, 1, false, skin);
		strengthSlider  = new Slider(10, 100, 1, false, skin);
		speedSlider = new Slider(10, 100, 1, false, skin);
		intelligenceSlider = new Slider(10, 100, 1, false, skin);
		wisdomSlider = new Slider(10, 100, 1, false, skin);
		agilitySlider = new Slider(10, 100, 1, false, skin);

		createCharacterButton = new TextButton("Submit",skin);
		createCharacterButton.addListener(new ChangeListener(){
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				event.handle();
				
				//gather values
				String username = usernameField.getText();
				String password = Encrypter.base64encode(passwordField.getText());
				String characterName = characterNameField.getText();
				String email = emailField.getText();
				String hint = hintField.getText();
				
				int health = (int) healthSlider.getValue();
				int mana = (int) manaSlider.getValue();
				
				int strength = (int) strengthSlider.getValue();
				int intelligence = (int) intelligenceSlider.getValue();
				int wisdom = (int) wisdomSlider.getValue();
				int agility = (int) agilitySlider.getValue();
				int speed = (int) speedSlider.getValue();
				
				boolean exists = existingAccountCheck.isChecked();
				
				Client.connection.sendRegistrationPacket(username, password, email, hint, characterName, health, mana, strength, intelligence, wisdom, agility, speed, exists);
			}
		});
		exitButton = new TextButton("Back",skin);
		exitButton.addListener(new ChangeListener(){
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				event.handle();
				Hunted.getInstance().setScreen(MainMenuScreen.getInstance());
			}
		});

		table.defaults().fill();
		table.add(nameCheckLabel).colspan(2);
		table.row();
		table.add(existingAccountCheck).padLeft(175).padTop(40).padBottom(40);
		table.row();
		table.add(usernameField);
		table.row();
		table.add(passwordField);
		table.row();
		table.add(emailField);
		table.row();
		table.add(hintField);
		table.row();
		table.add(characterNameField);
		table.add(nameCheckButton);
		table.row();
		table.add(pointsLeftLabel).padTop(80).padLeft(175).padBottom(40).colspan(2);
		table.row();
		table.add(healthSlider);
		table.add(healthLabel);
		table.row();
		table.add(manaSlider);
		table.add(manaLabel);
		table.row();
		table.add(strengthSlider);
		table.add(strengthLabel);
		table.row();
		table.add(speedSlider);
		table.add(speedLabel);
		table.row();
		table.add(intelligenceSlider);
		table.add(intelligenceLabel);
		table.row();
		table.add(wisdomSlider);
		table.add(wisdomLabel);
		table.row();
		table.add(agilitySlider);
		table.add(agilityLabel);
		table.row();
		table.add(createCharacterButton);
		table.row();
		table.add(exitButton);

		table.setPosition((Gdx.graphics.getWidth() / 2) - (table.getWidth() / 2), 400);

		stage.addActor(table);
	}

	public static RegistrationScreen getInstance(){
		if(instance == null){
			instance = new RegistrationScreen();
		}
		return instance;
	}

	@Override
	public void show() {
	}

	@Override
	public void render(float delta) {
		if(existingAccountCheck.isChecked()){
			emailField.setVisible(false);
			hintField.setVisible(false);
		}else{
			emailField.setVisible(true);
			hintField.setVisible(true);
		}
		//calculations to do
		calculatePointsLeft();

		//rendering
		Gdx.input.setInputProcessor(stage);
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act();
		stage.draw();
	}

	private void calculatePointsLeft() {
		healthLabel.setText(healthSlider.getValue()+ " - health points");
		manaLabel.setText(manaSlider.getValue()+ " - mana points");
		strengthLabel.setText(strengthSlider.getValue()+ " - strength points");
		intelligenceLabel.setText(intelligenceSlider.getValue()+ " - intelligence points");
		wisdomLabel.setText(wisdomSlider.getValue()+ " - wisdom points");
		agilityLabel.setText(agilitySlider.getValue()+ " - agility points");
		speedLabel.setText(speedSlider.getValue()+ " - speed points");
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

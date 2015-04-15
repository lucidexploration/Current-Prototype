package Client;

import java.util.concurrent.ConcurrentHashMap;

import Client.Players.Person;
import Client.Players.Player;
import Client.Screens.GameScreen;
import Client.Screens.MainMenuScreen;
import Client.Screens.OptionsScreen;
import Client.Screens.RegistrationScreen;
import Client.Screens.SelectScreen;
import Client.Utilities.Loader;
import World.AI.Monster;

import com.badlogic.gdx.Game;

public class Hunted extends Game{
	
	private static Hunted instance;
	
	public static ConcurrentHashMap<String, Person> people;
	public static ConcurrentHashMap<String, Monster> monsters;
	
	public static String username = "";
	public static String password = "";
	public static Player player;
	
	private Hunted(){
		loadAssets();
		
		people = new ConcurrentHashMap<String, Person>();
		monsters = new ConcurrentHashMap<String, Monster>();
		player = Player.getInstance();

		initializePlayer();
		initializeScreens();
		
		setScreen(MainMenuScreen.getInstance());
	}
	
	public static Hunted getInstance(){
		if(instance == null){
			instance = new Hunted();
		}
		return instance;
	}

	private void loadAssets(){
		Loader.getInstance();
	}
	
	private void initializePlayer(){
		Player.getInstance();
	}
	
	private void initializeScreens() {
		GameScreen.getInstance();
		MainMenuScreen.getInstance();
		OptionsScreen.getInstance();
		RegistrationScreen.getInstance();
		SelectScreen.getInstance();
	}

	@Override
	public void create() {
	}

	@Override
	public void render() {
        super.render();
    }
}

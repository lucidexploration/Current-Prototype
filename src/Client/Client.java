package Client;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import Client.Server.ConnectionToServer;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Audio;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class Client extends ApplicationAdapter {
	
	//game
	public static Hunted game;
	public static Preferences prefs;
	public static Audio audio;
	
	//networking
	public static ConnectionToServer connection;
	public static Socket socket;
	private final String serverAdress = "76.165.144.8";
	private final int serverPort = 7171;
	
	public static Long connectionID;
	public static String username;
	public static String password;
	
	@Override
	public void create () {
		prefs = Gdx.app.getPreferences("Hunt(ed)-Experimental");
		audio = Gdx.audio;
		
		loadPreferences();
		startConnection();
		
		game = Hunted.getInstance();
	}

	private void loadPreferences() {
	}
	
	private void startConnection(){
		try {
			//start the socket. create the wrapper, and start it's thread.
			socket = new Socket(serverAdress,serverPort);
			connection = new ConnectionToServer(socket);
			(new Thread(connection)).start();
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
			System.exit(-1);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}

	@Override
	public void render () {
		game.render();
	}

	public static void closeEverything() {
		System.exit(0);
	}
}

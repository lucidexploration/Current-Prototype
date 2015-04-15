package Client.Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import Client.Client;
import Client.Players.Player;
import Packets.CharacterDeletionPacket;
import Packets.ChatPacket;
import Packets.LoginPacket;
import Packets.LogoutPacket;
import Packets.MovementPacket;
import Packets.Packet;
import Packets.RegistrationPacket;

public class ConnectionToServer implements Runnable{
	
	public static Socket socket;
	public ObjectInputStream ois;
	public ObjectOutputStream oos;
	
	public ConnectionToServer(Socket sock){
		socket = sock;
		createStreams();
	}
	
	private void createStreams() {
		try {
			oos = new ObjectOutputStream(socket.getOutputStream());
			ois = new ObjectInputStream(socket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}

	@Override
	public void run() {
		while(true){
			try {
				Packet p = (Packet) ois.readObject();
				p.processClient();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				System.exit(-1);
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(-1);
			}
		}
	}
	
	public void sendLoginPacket(Long id, String acc, String pass){
		try {
			oos.writeObject(new LoginPacket(id,acc,pass));
			oos.flush();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}

	public void sendRegistrationPacket(String username, String password, String email, String hint, String characterName, int health, int mana, int strength, int intelligence, int wisdom, int agility, int speed, boolean exists) {
		try {
			oos.writeObject(new RegistrationPacket(Client.connectionID, username, password, email, hint, characterName, health, mana, strength, intelligence, wisdom, agility, speed, exists));
			oos.flush();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}

	public void sendDeleteCharacterPacket(String characterToDelete) {
		try{
			Long cid = Client.connectionID;
			String username = Client.username;
			String password = Client.password;
			oos.writeObject(new CharacterDeletionPacket(cid,username,password,characterToDelete));
			oos.flush();
		}catch(IOException e){
			e.printStackTrace();
			System.exit(-1);
		}
	}
	
	/*
	 * movementDirection
	 * 		1 = west
	 * 		2 = east
	 * 		3 = north
	 * 		4 = south
	 */
	public void sendMovementPacket(int movementDirection){
		try{
			Long cid = Client.connectionID;
			String characterName = Player.getInstance().information.name;
			oos.writeObject(new MovementPacket(cid,characterName,movementDirection));
			oos.flush();
		}catch(IOException e){
			e.printStackTrace();
			System.exit(-1);
		}
	}

	public void sendLogoutPacket() {
		try {
			oos.writeObject(new LogoutPacket(Client.connectionID, Player.getInstance().information.name));
			oos.flush();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}

	public void sendChatPacket(String channel,String text) {
		try{
			oos.writeObject(new ChatPacket(Client.connectionID, channel, Player.getInstance().information.name, text));
			oos.flush();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}
}

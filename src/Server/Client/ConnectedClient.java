package Server.Client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

import Client.Players.PlayerInfo;
import Packets.Packet;
import Server.Server;
import Server.Database.DBHandler;

public class ConnectedClient implements Runnable{
	
	//pulls info from database
	public DBHandler dbHandler;

	//character information
	public PlayerInfo information = new PlayerInfo();
	
	//connection
	public Socket socket;
	public ObjectInputStream ois;
	public ObjectOutputStream oos;
	public Long clientID;
	
	//timing objects
	private long lastMovementTime = 0;
	
	private ConcurrentHashMap<String,String> subscribedChannels = new ConcurrentHashMap<String,String>();
	
	/*
	 * Constructor
	 */
	public ConnectedClient(Socket sock){
		socket = sock;
		createStreams();
		dbHandler = new DBHandler();
		
		System.out.println("Connection from : "+socket.getRemoteSocketAddress().toString());
	}

	private void createStreams() {
		try {
			ois = new ObjectInputStream(socket.getInputStream());
			oos = new ObjectOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}

	/*
	 * Main loop
	 */
	@Override
	public void run() {
		while(true){
			try {
				Packet p = (Packet) ois.readObject();
				p.processServer();;
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				System.exit(-1);
			} catch (IOException e) {//only happens when client shuts down abruptly. aka client was force quit.
				Server.clients.remove(clientID);//remove this client from the client list
				Server.tellEveryoneThisGuyIsGone(clientID, information.name);
				System.out.println(socket.getRemoteSocketAddress().toString() + " - has disconnected.");
				break;
			}
		}
	}

	public boolean canWalk() {
		if(System.currentTimeMillis() -lastMovementTime > movementRate()){
			System.out.println(information.name + " can walk.");
			return true;
		}
		System.out.println(information.name + " can't walk.");
		return false;
	}

	private long movementRate() {
		return 500 - information.speed;
	}
	
	private void updateInfo(){
		information = dbHandler.getPlayerInformation(information.name);
	}

	/*
	 * movementDirection
	 * 		1 = west
	 * 		2 = east
	 * 		3 = north
	 * 		4 = south
	 */
	public void walk(int direction) {
		if(direction == 1){
			dbHandler.updatePlayerLocation(information.name,information.xCoord-1,information.yCoord,information.zCoord);
			updateInfo();
			lastMovementTime = System.currentTimeMillis();
			return;
		}
		if(direction == 2){
			dbHandler.updatePlayerLocation(information.name,information.xCoord+1,information.yCoord,information.zCoord);
			updateInfo();
			lastMovementTime = System.currentTimeMillis();
			return;
		}
		if(direction == 3){
			dbHandler.updatePlayerLocation(information.name,information.xCoord,information.yCoord - 1,information.zCoord);
			updateInfo();
			lastMovementTime = System.currentTimeMillis();
			return;
		}
		if(direction == 4){
			dbHandler.updatePlayerLocation(information.name,information.xCoord,information.yCoord + 1,information.zCoord);
			updateInfo();
			lastMovementTime = System.currentTimeMillis();
			return;
		}
	}

	public void subscribeToThisChannel(String channel) {
		if(!subscribedChannels.containsKey(channel)){
			subscribedChannels.put(channel, channel);
		}
	}

	public boolean isSubscribedToThisChannel(String channel) {
		return subscribedChannels.containsKey(channel);
	}
}

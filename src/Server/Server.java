package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;

import Client.Players.PlayerInfo;
import Notes.Constants;
import Packets.CharacterSelectPacket;
import Packets.ChatPacket;
import Packets.ClientIDPacket;
import Packets.PlayerGonePacket;
import Packets.PlayerInfoPacket;
import Packets.VersionVerificationPacket;
import Server.Client.ConnectedClient;

public class Server implements Runnable {

	private final int port = 7171;
	private ServerSocket clientListener;

	public static ConcurrentHashMap<Long,ConnectedClient> clients;

	private static Long currentClientID = 0L;

	public Server(){
		clients = new ConcurrentHashMap<Long, ConnectedClient>();

		try {
			clientListener = new ServerSocket(port);
		} catch (IOException ex) {
			ex.printStackTrace();
			System.exit(-1);
		}
	}

	@Override
	public void run() {
		try{
			while(true){
				ConnectedClient cc = new ConnectedClient(clientListener.accept());
				cc.clientID = currentClientID;

				try{
					//send client its identifier
					cc.oos.writeObject(new ClientIDPacket(currentClientID));
					cc.oos.flush();
					//verify client is up to date.
					cc.oos.writeObject(new VersionVerificationPacket(""));
					cc.oos.flush();
				}catch(IOException e){
					e.printStackTrace();
					System.exit(-1);
				}

				//add client to list
				clients.put(currentClientID, cc);

				//increment client id
				currentClientID++;

				//start the clients separate thread
				(new Thread(cc)).start();
			}
		}catch(IOException e){
			e.printStackTrace();
			System.exit(-1);
		}
	}

	public static void relayChat(Long cid, String channel, String charName, String chatMessage) {
		System.out.println(cid + " - " + channel + " - " + charName + " - " + chatMessage);
		
		clients.get(cid).subscribeToThisChannel(channel);
		
		Enumeration<Long> it = clients.keys();
		while(it.hasMoreElements()){
			Long currentKey = it.nextElement();
			if(clients.get(currentKey).clientID != cid){
				if(clients.get(currentKey).isSubscribedToThisChannel(channel)){
					try {
						clients.get(currentKey).oos.writeObject(new ChatPacket(0l, channel, charName,chatMessage));
						clients.get(currentKey).oos.flush();
					} catch (IOException e) {
						e.printStackTrace();
						System.exit(-1);
					}
				}
			}
		}
	}

	public static void verifyAndProcessLogin(Long id, String account, String pass) {
		if(clients.containsKey(id)){
			if(clients.get(id).dbHandler.loginInformationCorrect(account,pass)){
				String[] characters = clients.get(id).dbHandler.getCharacters(account);
				try {
					System.out.println("Sending character list to client - "+id);
					clients.get(id).oos.writeObject(new CharacterSelectPacket(characters));
					clients.get(id).oos.flush();
				} catch (IOException e) {
					e.printStackTrace();
					System.exit(-1);
				}
			}
		}
	}

	public static void loginWithThisCharacter(Long cid, String selectedCharacter) {
		if(clients.containsKey(cid)){
			//update server client information
			clients.get(cid).information.name = selectedCharacter;

			//retrieve information from database
			PlayerInfo info = clients.get(cid).dbHandler.getPlayerInformation(selectedCharacter);
			clients.get(cid).information = info;

			//retrieve info on surrounding players
			tellEveryoneThatCanSeeThisClientAboutHim(cid);
			sendThisClientEverythingAroundHim(cid);

			//send retrieved info to client
			try{
				clients.get(cid).oos.writeObject(new PlayerInfoPacket(info));
				clients.get(cid).oos.flush();
			}catch(IOException e){
				e.printStackTrace();
				System.exit(-1);
			}
			
			//now subscribe him to default channels
			clients.get(cid).subscribeToThisChannel("Default");
			clients.get(cid).subscribeToThisChannel(selectedCharacter);
		}
	}

	/*
	 * Returns true if the otherPlayer is within Screen range of the centralPlayer.
	 */
	private static boolean inViewRange(PlayerInfo centralPlayer, PlayerInfo otherPlayers) {
		if(centralPlayer.name.equals(otherPlayers.name)){
			return false;
		}
		
		int minX = centralPlayer.xCoord - Constants.NUMBEROFHORIZONTALTILES / 2;
		int maxX = centralPlayer.xCoord + Constants.NUMBEROFHORIZONTALTILES / 2;
		int minY = centralPlayer.yCoord - Constants.NUMBEROFVERTICALTILES / 2;
		int maxY = centralPlayer.yCoord + 1 + Constants.NUMBEROFVERTICALTILES / 2;
		
		if(otherPlayers.xCoord >= minX && otherPlayers.xCoord <= maxX && otherPlayers.yCoord >= minY && otherPlayers.yCoord <= maxY){
			System.out.println(otherPlayers.name + " is in range of and can see "+centralPlayer.name);
			return true;
		}
		return false;
	}

	public static void deleteSelectedCharacter(Long cid, String username, String password, String characterName) {
		if(clients.containsKey(cid)){
			@SuppressWarnings("unused")
			boolean result = clients.get(cid).dbHandler.deleteCharacter(username,password,characterName);
			verifyAndProcessLogin(cid,username,password);//resends character list
		}
	}

	public static void verifyAndHandlePlayerMovement(long cid, String name, int direction) {
		if(clients.get(cid).information.name.equals(name)){
			if(clients.get(cid).canWalk()){
				clients.get(cid).walk(direction);
				sendThisClientHisNewLocation(cid);
				sendThisClientEverythingAroundHim(cid);
				tellEveryoneThatCanSeeThisClientAboutHim(cid);
			}
		}
	}

	private static void sendThisClientHisNewLocation(long cid){
		try {
			clients.get(cid).oos.writeObject(new PlayerInfoPacket(clients.get(cid).information));
			clients.get(cid).oos.flush();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}

	private static void tellEveryoneThatCanSeeThisClientAboutHim(long cid) {
		Enumeration<Long> it = clients.keys();

		PlayerInfo tellEveryoneAboutMe = clients.get(cid).information;

		while(it.hasMoreElements()){
			long currentID = it.nextElement();
			if(currentID != cid){
				try {
					clients.get(currentID).oos.writeObject(new PlayerInfoPacket(tellEveryoneAboutMe).stripped());
					clients.get(currentID).oos.flush();
				} catch (IOException e) {
					e.printStackTrace();
					System.exit(-1);
				}
			}
		}
	}

	private static void sendThisClientEverythingAroundHim(long cid) {
		Enumeration<Long> it = clients.keys();

		while(it.hasMoreElements()){
			long currentID = it.nextElement();
			PlayerInfo giveThisInformationToHim = clients.get(currentID).information;

			if(currentID != cid){
				try {
					clients.get(cid).oos.writeObject(new PlayerInfoPacket(giveThisInformationToHim));
					clients.get(cid).oos.flush();
				} catch (IOException e) {
					e.printStackTrace();
					System.exit(-1);
				}
			}
		}
	}

	public static void tellEveryoneThisGuyIsGone(Long cid, String name) {
		Enumeration<Long> it = clients.keys();

		while(it.hasMoreElements()){
			long currentID = it.nextElement();

			if(currentID != cid){
				try {
					clients.get(currentID).oos.writeObject(new PlayerGonePacket(name));
					clients.get(currentID).oos.flush();
				} catch (IOException e) {
					e.printStackTrace();
					System.exit(-1);
				}
			}
		}
	}
}
package Server.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import Client.Players.PlayerInfo;

public class DBHandler {

	private String driver = "com.mysql.jdbc.Driver";
	private String url = "jdbc:mysql://localhost";
	private String dbusername = "hunted";
	private String dbpassword = "Iwillpk4ever!";

	private Connection con;

	public DBHandler(){
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, dbusername, dbpassword);
		} catch (SQLException e) {
			System.out.println("Couldnt start connection to DB. Error message follows.");
			e.printStackTrace();
			System.exit(-1);
		} catch (ClassNotFoundException e) {
			System.out.println("Couldnt start connection to DB. Error message follows.");
			e.printStackTrace();
			System.exit(-1);
		} 
	}

	public void shutDown(){
		try {
			if(!con.isClosed()){
				con.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}

	@SuppressWarnings("resource")
	public String[] getCharacters(String username) {
		PreparedStatement stmt = null;
		String query = "SELECT charactername FROM hunted.characters WHERE username=?";
		try{
			stmt = con.prepareStatement(query);
			stmt.setString(1, username);

			ResultSet rs = stmt.executeQuery();

			Vector<String> character = new Vector<String>(2);
			while(rs.next()){
				String characters = rs.getString("charactername");
				character.add(characters);
			}

			rs.close();
			stmt.close();

			String[] result = new String[character.size()];

			return character.toArray(result);
		}catch(SQLException e){
			e.printStackTrace();
			System.exit(-1);
		}
		return null;
	}

	@SuppressWarnings("resource")
	public PlayerInfo getPlayerInformation(String selectedCharacter) {
		PreparedStatement stmt = null;
		String getStats = "SELECT hp, hpmax, mp, mpmax, strength, speed, intelligence, wisdom, agility, currentexperience, totalexperience FROM hunted.characterinfo WHERE charactername=?";
		String getLocation = "SELECT x, y, z FROM hunted.characterlocation WHERE charactername=?";
		String getUsername = "SELECT username FROM hunted.characters WHERE charactername=?";
		String getFriends = "SELECT charactername FROM hunted.characterfriends WHERE username=?";
		
		String username;
		
		try{
			stmt = con.prepareStatement(getStats);
			stmt.setString(1, selectedCharacter);

			ResultSet rs = stmt.executeQuery();
			rs.next();

			int hp = rs.getInt("hp");
			int hpmax = rs.getInt("hpmax");
			int mp = rs.getInt("mp");
			int mpmax = rs.getInt("mpmax");
			int strength = rs.getInt("strength");
			int speed = rs.getInt("speed");
			int intelligence = rs.getInt("intelligence");
			int wisdom = rs.getInt("wisdom");
			int agility = rs.getInt("agility");
			int currentExp = rs.getInt("currentexperience");
			int totalExp = rs.getInt("totalexperience");

			rs.close();
			stmt.close();

			stmt = con.prepareStatement(getLocation);
			stmt.setString(1, selectedCharacter);

			rs = stmt.executeQuery();
			rs.next();

			int x = rs.getInt("x");
			int y = rs.getInt("y");
			int z = rs.getInt("z");

			rs.close();
			stmt.close();
			
			stmt = con.prepareStatement(getUsername);
			stmt.setString(1, selectedCharacter);
			
			rs = stmt.executeQuery();
			rs.next();
			
			username = rs.getString("username");
			
			rs.close();
			stmt.close();
			
			stmt = con.prepareStatement(getFriends);
			stmt.setString(1, username);
			
			rs = stmt.executeQuery();
			
			List<String> friendList = new ArrayList<String>();
			
			while(rs.next()){
				friendList.add(rs.getString("charactername"));
			}
			
			String[] friendResult = new String[friendList.size()];
			
			int i = 0;
			for(@SuppressWarnings("unused") String s: friendList){
				friendResult[i]=friendList.get(i);
				i++;
			}
			
			rs.close();
			stmt.close();

			return new PlayerInfo(selectedCharacter, hp, hpmax, mp, mpmax, strength, intelligence, wisdom, agility, speed, x, y, z, currentExp, totalExp, friendResult);
		}catch(SQLException e){
			e.printStackTrace();
			System.exit(-1);
		}

		return null;
	}
	
	@SuppressWarnings("resource")
	public void accountAndCharacterCreation(String username, String password, String email, String emailHint, String characterName, int health, int mana, int strength, int intelligence, int wisdom, int agility, int speed, boolean existingAccount) {
		PreparedStatement stmt = null;

		String query = "INSERT INTO hunted.accounts VALUES(?,?,?,?)";
		String query2 = "INSERT INTO hunted.characters VALUES(?,?)";
		String query3 = "INSERT INTO hunted.characterinfo VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";
		String query4 = "INSERT INTO hunted.characterlocation VALUES(?,?,?,?)";

		if(existingAccount && !characterExists(characterName)){
			try{
				con.setAutoCommit(false);

				//characters
				stmt = con.prepareStatement(query2);

				stmt.setString(1, username);
				stmt.setString(2, characterName);

				stmt.executeUpdate();
				stmt.close();

				//characterinfo
				stmt = con.prepareStatement(query3);

				stmt.setString(1, characterName);
				stmt.setInt(2, health);
				stmt.setInt(3, health);
				stmt.setInt(4, mana);
				stmt.setInt(5, mana);
				stmt.setInt(6, strength);
				stmt.setInt(7, speed);
				stmt.setInt(8, intelligence);
				stmt.setInt(9, wisdom);
				stmt.setInt(10, agility);
				stmt.setInt(11, 1);
				stmt.setInt(12, 1);

				stmt.executeUpdate();
				stmt.close();

				//characterlocation
				stmt = con.prepareStatement(query4);

				stmt.setString(1, characterName);
				stmt.setInt(2, 250);
				stmt.setInt(3, 250);
				stmt.setInt(4, 0);

				stmt.executeUpdate();
				stmt.close();

				con.commit();
				con.setAutoCommit(true);

				System.out.println(username+" - has just made a new character.");
				return;
			}catch(SQLException e){
				try {
					con.rollback();
					con.setAutoCommit(true);
				} catch (SQLException ex) {
					ex.printStackTrace();
					System.exit(-1);
				}
				e.printStackTrace();
				System.exit(-1);
			}
		}
		if(!loginInformationCorrect(username,password) && !characterExists(characterName)){
			try{
				con.setAutoCommit(false);

				//accounts
				stmt = con.prepareStatement(query);

				stmt.setString(1, username);
				stmt.setString(2, password);
				stmt.setString(3, email);
				stmt.setString(4, emailHint);

				stmt.executeUpdate();
				stmt.close();

				//characters
				stmt = con.prepareStatement(query2);

				stmt.setString(1, username);
				stmt.setString(2, characterName);

				stmt.executeUpdate();
				stmt.close();

				//characterinfo
				stmt = con.prepareStatement(query3);

				stmt.setString(1, characterName);
				stmt.setInt(2, health);
				stmt.setInt(3, health);
				stmt.setInt(4, mana);
				stmt.setInt(5, mana);
				stmt.setInt(6, strength);
				stmt.setInt(7, speed);
				stmt.setInt(8, intelligence);
				stmt.setInt(9, wisdom);
				stmt.setInt(10, agility);
				stmt.setInt(11, 1);
				stmt.setInt(12, 1);

				stmt.executeUpdate();
				stmt.close();

				//character location
				stmt = con.prepareStatement(query4);

				stmt.setString(1, characterName);
				stmt.setInt(2, 250);
				stmt.setInt(3, 250);
				stmt.setInt(4, 0);

				stmt.executeUpdate();
				stmt.close();

				con.commit();
				con.setAutoCommit(true);

				System.out.println(username+" - has just made an account.");
				return;
			} catch(SQLException e){
				try {
					con.rollback();
					con.setAutoCommit(true);
				} catch (SQLException ex) {
					ex.printStackTrace();
					System.exit(-1);
				}
				e.printStackTrace();
				System.exit(-1);
			}
		}

		System.out.println("Nothing was done. Character must already exist.");
	}

	@SuppressWarnings("resource")
	private boolean characterExists(String characterName) {
		PreparedStatement stmt = null;
		String query = "SELECT charactername FROM hunted.characters WHERE charactername = ?";
		try{
			stmt = con.prepareStatement(query);

			stmt.setString(1, characterName);

			ResultSet rs = stmt.executeQuery();

			boolean result = rs.next();

			rs.close();
			stmt.close();

			return result;
		}catch(SQLException e){
			e.printStackTrace();
			System.exit(-1);
		}
		return false;
	}

	@SuppressWarnings("resource")
	public boolean loginInformationCorrect(String username, String password) {
		PreparedStatement stmt = null;
		String query = "SELECT username, password FROM hunted.accounts WHERE username=? AND password=?";

		try{
			stmt = con.prepareStatement(query);
			stmt.setString(1, username);
			stmt.setString(2, password);

			ResultSet rs = stmt.executeQuery();
			boolean result = rs.next();

			rs.close();
			stmt.close();

			return result;
		}catch(SQLException ex){
			ex.printStackTrace();
		}
		return false;
	}

	@SuppressWarnings("resource")
	public boolean deleteCharacter(String username, String password, String characterName) {
		
		PreparedStatement stmt = null;
		String characters = "DELETE FROM hunted.characters WHERE charactername=?";
		String characterinfo = "DELETE FROM hunted.characterinfo WHERE charactername=?";
		String characterlocations = "DELETE FROM hunted.characterlocation WHERE charactername=?";
		String characteritems = "DELETE FROM hunted.characteritems WHERE charactername=?";
		String characterfriends = "DELETE FROM hunted.characterfriends WHERE charactername=?";
		String guildmembers = "DELETE FROM hunted.guildmembers WHERE charactername=?";
		
		System.out.println(username +" - "+ password +" - "+ characterName);
		
		if(!loginInformationCorrect(username,password)){
			return false;
		}
		if(!characterExists(characterName)){
			return false;
		}
		
		try{
			con.setAutoCommit(false);
			
			//characters
			stmt = con.prepareStatement(characters);
			stmt.setString(1, characterName);
			stmt.executeUpdate();
			stmt.close();
			
			//character info
			stmt = con.prepareStatement(characterinfo);
			stmt.setString(1, characterName);
			stmt.executeUpdate();
			stmt.close();
			
			//locations
			stmt = con.prepareStatement(characterlocations);
			stmt.setString(1, characterName);
			stmt.executeUpdate();
			stmt.close();
			
			//items
			stmt = con.prepareStatement(characteritems);
			stmt.setString(1, characterName);
			stmt.executeUpdate();
			stmt.close();
			
			//friends
			stmt = con.prepareStatement(characterfriends);
			stmt.setString(1, characterName);
			stmt.executeUpdate();
			stmt.close();
			
			//guild members
			stmt = con.prepareStatement(guildmembers);
			stmt.setString(1, characterName);
			stmt.executeUpdate();
			stmt.close();
			
			con.commit();
			con.setAutoCommit(true);
			
			return true;
			
		}catch(SQLException e){
			try {
				con.rollback();
				con.setAutoCommit(true);
				System.exit(-1);
			} catch (SQLException ex) {
				ex.printStackTrace();
				System.exit(-1);
			}
			e.printStackTrace();
			System.exit(-1);
		}
		
		return false;
	}

	@SuppressWarnings("resource")
	public void updatePlayerLocation(String name, int x, int y, int z) {
		PreparedStatement stmt = null;
		String query = "UPDATE hunted.characterlocation SET x = ?, y = ?, z = ? WHERE charactername = ?";
		
		try{
			stmt = con.prepareStatement(query);
			stmt.setInt(1, x);
			stmt.setInt(2, y);
			stmt.setInt(3, z);
			stmt.setString(4, name);
			stmt.executeUpdate();
			stmt.close();
		}catch(SQLException e){
			e.printStackTrace();
			System.exit(-1);
		}
	}
}

package Client.Screens;

import java.awt.MouseInfo;

import Client.Client;
import Client.Hunted;
import Client.Players.Player;
import Client.UI.EscapeDialog;
import Client.UI.Chat.ChatDialog;
import Client.UI.Custom.CustomWindow.WindowStyle;
import Client.UI.VIP.FriendDialog;
import Client.Utilities.Loader;
import Notes.Constants;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class GameScreen implements Screen{

	private static GameScreen instance;

	//user interface
	public Stage stage;
	public Skin skin;
	private SpriteBatch batch;

	//menu items
	private EscapeDialog menuDialog;
	private boolean showingMenu = false;

	//chat items
	public ChatDialog chatDialog;
	private boolean showingChat = false;
	private boolean stillTyping = false;

	//friend list items
	public FriendDialog friendDialog;
	private boolean showingFriendList = false;

	//world elements
	private TiledMap map;
	public static MapProperties prop;

	//targeting renderer
	private ShapeRenderer shapes;

	private Long lastMovementTime = (long) 0;

	private GameScreen(){
		//ui elements
		stage = new Stage();
		
		skin = new Skin(Gdx.files.internal("uiskin.json"));
		WindowStyle ws = new WindowStyle();
		ws.titleFont = new BitmapFont();
		// other ws properties here
		skin.add("default", ws);
		
		batch = new SpriteBatch();

		//world elements
		Loader.getInstance();
		map = Loader.assets.get("world/surface.tmx", TiledMap.class);
		prop = map.getProperties();

		//in game menu
		menuDialog = new EscapeDialog("Menu",skin);
		//chat window
		chatDialog = new ChatDialog("Chat",skin);
		//friend dialo
		friendDialog = new FriendDialog("VIP List",skin);

		//shape renderer for targeting
		shapes = new ShapeRenderer();
	}

	public static GameScreen getInstance(){
		if(instance == null){
			instance = new GameScreen();
		}
		return instance;
	}

	@Override
	public void show() {
	}

	@Override
	public void render(float delta) {
		removeOutOfRangePeople();

		Gdx.input.setInputProcessor(stage);
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.begin();
		renderWorld();
		batch.end();

		//drawTargetedSquare();

		stage.act();
		stage.draw();

		handleInput();
	}

	private void removeOutOfRangePeople() {
		int minX = Player.getInstance().information.xCoord - Constants.NUMBEROFHORIZONTALTILES / 2;
		int maxX = Player.getInstance().information.xCoord + Constants.NUMBEROFHORIZONTALTILES / 2;
		int minY = Player.getInstance().information.yCoord - Constants.NUMBEROFVERTICALTILES / 2;
		int maxY = Player.getInstance().information.yCoord + 1 + Constants.NUMBEROFVERTICALTILES / 2;

		int mapWidth = prop.get("width", Integer.class);
		int mapHeight = prop.get("height", Integer.class);

		if(minX < 0){
			minX = 0;
		}
		if(minY < 0){
			minY = 0;
		}
		if(maxX >= mapWidth){
			maxX = mapWidth-1;
		}
		if(maxY >= mapHeight){
			maxY = mapHeight-1;
		}

		for(String key : Hunted.people.keySet()){
			if(Hunted.people.get(key).information.xCoord < minX || Hunted.people.get(key).information.yCoord < minY 
					|| Hunted.people.get(key).information.xCoord > maxX || Hunted.people.get(key).information.yCoord > maxY){
				stage.getActors().removeValue(Hunted.people.get(key).nameBar, false);
				Hunted.people.remove(key);
			}
		}
	}

	private void drawTargetedSquare(){
		int mouseX = MouseInfo.getPointerInfo().getLocation().x;
		int mouseY = MouseInfo.getPointerInfo().getLocation().y;

		if(mouseX < 0 || mouseX > Gdx.graphics.getWidth() || mouseY < 0 || mouseY > Gdx.graphics.getHeight() ){
			return;
		}

		int x = mouseX / (Gdx.graphics.getWidth() / Constants.NUMBEROFHORIZONTALTILES);
		int y = mouseY / (Gdx.graphics.getHeight() / Constants.NUMBEROFVERTICALTILES);

		shapes.begin(ShapeType.Line);
		shapes.setColor(Color.RED);
		shapes.rect(x*(Gdx.graphics.getWidth() / Constants.NUMBEROFHORIZONTALTILES), y*(Gdx.graphics.getHeight() / Constants.NUMBEROFVERTICALTILES), Gdx.graphics.getWidth() / Constants.NUMBEROFHORIZONTALTILES, Gdx.graphics.getHeight() / Constants.NUMBEROFVERTICALTILES);
		shapes.end();
	}

	private void renderWorld(){
		int creatureLayer = 3;
		int currentLayer = -1;

		int mapWidth = prop.get("width", Integer.class);
		int mapHeight = prop.get("height", Integer.class);
		//		int tilePixelWidth = prop.get("tilewidth", Integer.class);
		//		int tilePixelHeight = prop.get("tileheight", Integer.class);
		//		int mapPixelWidth = mapWidth * tilePixelWidth;
		//		int mapPixelHeight = mapHeight * tilePixelHeight;

		for (MapLayer layer : map.getLayers()) {

			//increment current layer to zero to start
			currentLayer++;
			if (layer.isVisible()) {

				if (layer instanceof TiledMapTileLayer) {

					int minX = Player.getInstance().information.xCoord - Constants.NUMBEROFHORIZONTALTILES / 2;
					int maxX = Player.getInstance().information.xCoord + Constants.NUMBEROFHORIZONTALTILES / 2;
					int minY = Player.getInstance().information.yCoord - Constants.NUMBEROFVERTICALTILES / 2;
					int maxY = Player.getInstance().information.yCoord + 1 + Constants.NUMBEROFVERTICALTILES / 2;

					if(minX < 0){
						minX = 0;
					}
					if(minY < 0){
						minY = 0;
					}
					if(maxX >= mapWidth){
						maxX = mapWidth-1;
					}
					if(maxY >= mapHeight){
						maxY = mapHeight-1;
					}

					while(minX<=maxX){
						while(minY<=maxY){
							TiledMapTileLayer.Cell cell = ((TiledMapTileLayer) layer).getCell(minX, minY);

							if(cell != null){
								TiledMapTile tile = cell.getTile();

								if(tile != null){
									Texture region = tile.getTextureRegion().getTexture();

									int col = Math.abs(Constants.tileWidth - (maxX - minX));
									int row = Math.abs(Constants.tileHeight - (maxY - minY));

									int tileWidth = Gdx.graphics.getWidth() / Constants.NUMBEROFHORIZONTALTILES;
									int tileHeight = Gdx.graphics.getHeight() / Constants.NUMBEROFVERTICALTILES;

									if(currentLayer == creatureLayer){
										for(String key : Hunted.people.keySet()){
											if(Hunted.people.get(key).information.xCoord == minX && Hunted.people.get(key).information.yCoord == minY){
												Hunted.people.get(key).render(batch, col * tileWidth, row * tileHeight, tileWidth, tileHeight);
											}
										}
									}
									batch.draw(region, col*tileWidth, row*tileHeight, tileWidth, tileHeight);
								}
							}else{
								int col = Math.abs(Constants.tileWidth - (maxX - minX));
								int row = Math.abs(Constants.tileHeight - (maxY - minY));

								int tileWidth = Gdx.graphics.getWidth() / Constants.NUMBEROFHORIZONTALTILES;
								int tileHeight = Gdx.graphics.getHeight() / Constants.NUMBEROFVERTICALTILES;

								if(currentLayer == creatureLayer){
									for(String key : Hunted.people.keySet()){
										if(Hunted.people.get(key).information.xCoord == minX && Hunted.people.get(key).information.yCoord == minY){
											Hunted.people.get(key).render(batch, col * tileWidth, row * tileHeight, tileWidth, tileHeight);
										}
									}
								}
							}
							minY++;
						}

						minY = Player.getInstance().information.yCoord - Constants.NUMBEROFVERTICALTILES / 2;
						if(minY < 0){
							minY = 0;
						}
						minX++;
					}
				}
			}
		}
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

	private Long movementSpeed(){
		return (long) (500 - Player.getInstance().information.speed);
	}

	/*
	 * movementDirection
	 * 		1 = west
	 * 		2 = east
	 * 		3 = north
	 * 		4 = south
	 */
	private void handleInput(){
		if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)){
			if(stillTyping){
				stillTyping = false;
				stage.unfocusAll();
				return;
			}
			if(showingFriendList){
				closeFriendDialog();
				return;
			}
			if(showingChat){
				closeChatDialog();
				return;
			}
			if(!showingMenu){
				showMenu();
				return;
			}
			else{
				closeMenu();
				return;
			}
		}
		if(Gdx.input.isKeyJustPressed(Input.Keys.O)){
			if(!showingChat && !stillTyping){
				showingFriendList = true;
				friendDialog.show(stage);
			}
		}
		if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){
			if(System.currentTimeMillis()-lastMovementTime > movementSpeed()){
				lastMovementTime = System.currentTimeMillis();
				Client.connection.sendMovementPacket(1);
			}
		}
		if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
			if(System.currentTimeMillis()-lastMovementTime > movementSpeed()){
				lastMovementTime = System.currentTimeMillis();
				Client.connection.sendMovementPacket(2);
			}
		}
		if(Gdx.input.isKeyPressed(Input.Keys.UP)){
			if(System.currentTimeMillis()-lastMovementTime > movementSpeed()){
				lastMovementTime = System.currentTimeMillis();
				Client.connection.sendMovementPacket(4);
			}
		}
		if(Gdx.input.isKeyPressed(Input.Keys.DOWN)){
			if(System.currentTimeMillis()-lastMovementTime > movementSpeed()){
				lastMovementTime = System.currentTimeMillis();
				Client.connection.sendMovementPacket(3);
			}
		}
		if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER)){
			if(!showingChat){
				showChatDialog();
			}
		}
	}

	public void closeFriendDialog() {
		showingFriendList = false;
		friendDialog.hide();
	}

	private void closeMenu() {
		showingMenu=false;
		menuDialog.hide();
	}

	private void closeChatDialog(){
		showingChat = false;
		chatDialog.hide();
	}

	private void showMenu() {
		showingMenu = true;
		menuDialog.show(stage);
	}

	public void showChatDialog(){
		stillTyping = true;
		showingChat = true;
		chatDialog.show(stage);
		stage.setKeyboardFocus(chatDialog.chatInput);
	}
}

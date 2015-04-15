package Client.Utilities;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

public class Loader {
	
	private static Loader instance = null;
	public static AssetManager assets;

	private TiledMap map;
	public static BitmapFont wrapper;
	
	private Loader(){
		assets = new AssetManager();
		loadResources();
	}

	public static Loader getInstance(){
		if(instance == null){
			instance = new Loader();
		}
		return instance;	
	}
	
	private void loadResources() {
		wrapper = new BitmapFont();
		
		assets.load("ui/bg.png",Texture.class);
		assets.load("sprites/players/testtoon.png", Texture.class);
		assets.load("sprites/monsters/goblin/goblin.png",Texture.class);
		assets.load("sprites/players/parts/healthbar.png",Texture.class);
		assets.load("sprites/players/parts/manabar.png",Texture.class);
		//lastly load maps
		assets.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
		assets.load("world/surface.tmx", TiledMap.class);
		
		//finally finish
		assets.finishLoading();
	}

	public static void verifyMapIntegrity(String md5) {
		// TODO Auto-generated method stub
	}
}

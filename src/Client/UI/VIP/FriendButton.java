package Client.UI.VIP;

import Client.Screens.GameScreen;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class FriendButton extends TextButton{

	String friend = "";
	
	public FriendButton(String text, Skin skin) {
		super(text, skin);

		friend = text;
		
		addCustomListener();
	}

	private void addCustomListener() {
		addListener(new ChangeListener(){
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				event.handle();
				
				GameScreen.getInstance().chatDialog.addChat(friend, "");
				
				GameScreen.getInstance().closeFriendDialog();
				GameScreen.getInstance().showChatDialog();
			}
		});
	}
}
				

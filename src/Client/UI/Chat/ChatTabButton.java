package Client.UI.Chat;

import Client.Screens.GameScreen;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class ChatTabButton extends TextButton{

	public Table chatLines;
	public ScrollPane chatPane;

	public String channelName = "";

	public ChatTabButton(String text, Skin skin) {
		super(text, skin);
		
		channelName = text;
		
		chatLines = new Table(skin);
		chatLines.defaults().align(Align.left).fill().expand();

		chatPane = new ScrollPane(chatLines);

		chatPane.setOverscroll(false, false);
		chatPane.setSize(300, 200);
		
		chatPane.validate();

		addCustomListener();
	}

	private void addCustomListener() {
		addListener(new ChangeListener(){
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				event.handle();

				if(GameScreen.getInstance().chatDialog.currentChannel.equals(channelName)){
					return;
				}
				
				GameScreen.getInstance().chatDialog.row1.getCell(
						GameScreen.getInstance().chatDialog.tabs.get(
								GameScreen.getInstance().chatDialog.currentChannel).chatPane).setActor(chatPane);
				
				GameScreen.getInstance().chatDialog.setTitle(channelName + " - Chat");
				GameScreen.getInstance().chatDialog.currentChannel = channelName;
			}
		});
	}
}

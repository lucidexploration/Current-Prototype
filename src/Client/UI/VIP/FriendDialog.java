package Client.UI.VIP;

import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Align;

public class FriendDialog extends Dialog{
	
	private Skin friendSkin;
	private ScrollPane friendButtonPane;
	private Table buttonTable;
	
	String[] friendList;

	public FriendDialog(String title, Skin skin) {
		super(title, skin);
		
		friendSkin = skin;
		
		buttonTable = new Table(skin);
		buttonTable.defaults().align(Align.center).expand().fill();
		
		friendButtonPane = new ScrollPane(buttonTable,skin);
		
		getButtonTable().add(friendButtonPane);
	}

	public void updateFriends(String[] newFriends){
		if(newFriends == null){
			return;
		}
		
		friendList = newFriends;
		
		buttonTable.clearChildren();
		
		for(String friend : friendList){
			buttonTable.add(new FriendButton(friend,friendSkin)).row();
		}
		
		buttonTable.validate();
		getButtonTable().validate();
	}
}

package Client.UI.Chat;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

import Client.Client;
import Client.Screens.GameScreen;
import Client.UI.InputListeners.ChatInputListener;
import Packets.ChatPacket;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class ChatDialog extends Dialog{

	private Float xCoord = 0f;
	private Float yCoord = 0f;

	public TextField chatInput;
	private ScrollPane buttonTabPane;
	public Table buttonTabTable;
	private TextButton sendButton;
	
	protected Table row1;
	private Table row2;
	private Table row3;
	
	public ConcurrentHashMap<String,ChatPacket> chatBuffer = new ConcurrentHashMap<String,ChatPacket>();
	public ConcurrentHashMap<String,ChatTabButton> tabs = new ConcurrentHashMap<String,ChatTabButton>();

	private Skin chatSkin;
	
	public String currentChannel = "Default";
	private int numberOfChannels = 0;

	public ChatDialog(String title, Skin skin) {
		super(title, skin);
		chatSkin = skin;
		
		row1 = new Table(skin);
		row2 = new Table(skin);
		row3 = new Table(skin);
		
		addDefaultChannel();
		
		getButtonTable().add(row1).size(300,200);
		getButtonTable().add(row2).size(100,200).align(Align.bottom);
		getButtonTable().add(row3).size(100,200).align(Align.bottom);
		
		addInputAndSend();
		
		row1.validate();
		row2.validate();
		row3.validate();
		getButtonTable().validate();
		validate();
	}

	private void addDefaultChannel() {
		ChatTabButton button = new ChatTabButton("Default",chatSkin);
		
		buttonTabTable = new Table(chatSkin);
		buttonTabPane = new ScrollPane(buttonTabTable);
		
		buttonTabTable.defaults().fill().expand();
		
		tabs.put("Default",button);
		buttonTabTable.add(tabs.get("Default")).row();;
		numberOfChannels++;
		
		row1.add(button.chatPane).size(300,180).row();
	}

	private void addInputAndSend() {
		chatInput = new TextField("Talk here",chatSkin);
		chatInput.setTextFieldListener(new ChatInputListener());

		sendButton = new TextButton("Send", chatSkin);
		sendButton.addListener(new ChangeListener(){
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				
				event.handle();
				
				if(chatInput.getText().length() <=0 ){
					return;
				}
				
				Client.connection.sendChatPacket(currentChannel,chatInput.getText());
				chatInput.setText("");
			}
		});

		row1.add(chatInput).align(Align.bottom).size(300, 30);
		row2.add(sendButton).fill().expand();
		row3.add(buttonTabPane).size(100,200).align(Align.top).fill().expand();
	}

	@Override
	public void hide(){
		xCoord = this.getX();
		yCoord = this.getY();
		hide(sequence(fadeOut(0.4f, Interpolation.fade), Actions.removeListener(ignoreTouchDown, true), Actions.removeActor()));
	}

	@Override
	public Dialog show (Stage stage) {
		show(stage, sequence(Actions.alpha(0), Actions.fadeIn(0.4f, Interpolation.fade)));
		setPosition(xCoord, yCoord);
		return this;
	}

	public void addChat(String channel,String text){
		if(tabs.containsKey(channel)){
			tabs.get(channel).chatLines.row();
			tabs.get(channel).chatLines.add(text);
			tabs.get(channel).chatLines.validate();
			tabs.get(channel).chatPane.validate();
			tabs.get(channel).chatPane.scrollTo(0, 0, 0, 0, true, true);
		}else{
			ChatTabButton button = new ChatTabButton(channel,chatSkin);
			tabs.put(channel, button);
			
			buttonTabTable.add(button).row();
			swapToThisTab(channel, button.chatPane);
			numberOfChannels++;
			
			buttonTabTable.validate();
			validate();
			
			tabs.get(channel).chatLines.row();
			tabs.get(channel).chatLines.add(text);
			tabs.get(channel).chatLines.validate();
			tabs.get(channel).chatPane.scrollTo(0, 0, 0, 0, true, true);
		}
	}
	
	public void clearBuffer(){
		Iterator<ChatPacket> chatBufferIterator = chatBuffer.values().iterator();
		while(chatBufferIterator.hasNext()){
			ChatPacket p = chatBufferIterator.next();
			addChat(p.chatChannel, p.name+"-"+p.chatMessage);
			chatBuffer.remove(p.name+p.chatMessage);
		}
	}
	
	public void swapToThisTab(String chan, ScrollPane pane){
		if(GameScreen.getInstance().chatDialog.currentChannel.equals(chan)){
			return;
		}
		
		row1.getCell(
				GameScreen.getInstance().chatDialog.tabs.get(
						GameScreen.getInstance().chatDialog.currentChannel).chatPane).setActor(pane);
		
		GameScreen.getInstance().chatDialog.setTitle(chan + " - Chat");
		GameScreen.getInstance().chatDialog.currentChannel = chan;
	}
}

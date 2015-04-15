package Client.UI.InputListeners;

import Client.Client;
import Client.Screens.GameScreen;

import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldListener;

public class ChatInputListener implements TextFieldListener{

	public String channel = "Default";

	@Override
	public void keyTyped(TextField textField, char c) {
		if(!textField.getText().equals("")){
			if(c == '\r'){
				if(textField.getText().length()==0){
					return;
				}
				if(textField.getText().startsWith("/m ")){
					String[] spaceSplits = textField.getText().split(" ");
					String[] quoteSplits = textField.getText().split("\"");

					if(spaceSplits.length <= 2 || quoteSplits.length <= 2){
						textField.setText("");
						return;
					}

					String channel = quoteSplits[1];
					String message = quoteSplits[2];

					Client.connection.sendChatPacket(channel, message);

					textField.setText("");
					return;
				}
				
				// TODO: let users close tabs.... probably a button for this
				if(textField.getText().startsWith("/close")){

					return;
				}

				Client.connection.sendChatPacket(GameScreen.getInstance().chatDialog.currentChannel, textField.getText());
				textField.setText("");
				return;
			}
		}
	}
}

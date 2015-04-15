package Client.UI;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;
import Client.Client;
import Client.Hunted;
import Client.Screens.MainMenuScreen;
import Client.Screens.OptionsScreen;
import Client.Screens.SelectScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class EscapeDialog extends Dialog{
	
	private Float xCoord = (float) (Gdx.graphics.getWidth() / 2);
	private Float yCoord =(float) (Gdx.graphics.getHeight() / 2);
	
	private Skin escapeSkin;
	private TextButton optionsButton;
	private TextButton logoutButton;
	private TextButton exitButton;

	public EscapeDialog(String title, Skin skin) {
		super(title, skin);
		escapeSkin = skin;
		addCustomElements();
		validate();
	}

	private void addCustomElements() {
		optionsButton = new TextButton("Options",escapeSkin);
		optionsButton.addListener(new ChangeListener(){
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				event.handle();
				Hunted.getInstance().setScreen(OptionsScreen.getInstance());
			}
		});
		logoutButton = new TextButton("Go To Character Select",escapeSkin);
		logoutButton.addListener(new ChangeListener(){
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				event.handle();
				Hunted.getInstance().setScreen(SelectScreen.getInstance());
				Client.connection.sendLogoutPacket();
			}
		});
		exitButton = new TextButton("Go To Main Menu",escapeSkin);
		exitButton.addListener(new ChangeListener(){
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				event.handle();
				Hunted.getInstance().setScreen(MainMenuScreen.getInstance());
				Client.connection.sendLogoutPacket();
			}
		});

		getButtonTable().add(optionsButton);
		getButtonTable().row();
		getButtonTable().add(logoutButton);
		getButtonTable().row();
		getButtonTable().add(exitButton);
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
}

import org.lwjgl.opengl.DisplayMode;

import cs.lucioben.game.base.Game;
import cs.lucioben.game.base.Scene;

public class Application {
	
	/**
	 * The main class for the game. 
	 * @param args
	 */
	public static void main(String[] args) {
		Scene[] scenes = {
					new TitleScene(),
					new StartScene(),
				};
		Game game = Game.getContext();
		Game.setDisplayMode(new DisplayMode(900, 600));
		Game.setFontPath("res/fonts/Plump.ttf");
		game.start(scenes);
	}
}
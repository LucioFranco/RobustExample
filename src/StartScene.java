/** 
 * The first scene displayed after the title screen
 * 
 * @author Lucio Franco and Benjamin Snoha
 * @version 1.0 
 * @since June 2, 2015
 */


import org.lwjgl.util.vector.Vector2f;

import GameObjects.Health;
import GameObjects.Player;
import cs.lucioben.game.GameObjects.base.MapTile;
import cs.lucioben.game.GameObjects.base.TextObject;
import cs.lucioben.game.base.GameObjectType;
import cs.lucioben.game.base.Scene;

public class StartScene extends Scene {

	/**
	 * Sets up the scene
	 */
	@Override
	public void setup() {
		MapTile wall = new MapTile(96, 96, "res/images/wall.png", GameObjectType.COLLISON),
				floor =  new MapTile(96, 96, "res/images/floor.png", GameObjectType.NON_COLLISON),
				end = new MapTile(96, 96, "res/images/end.png", GameObjectType.END);
		this.setMapFileName("res/maps/StartScene.txt");
		this.addMapTile("-", wall);
		this.addMapTile("`", floor);
		this.addMapTile("F", end);

		Player player = new Player(96, 96, 0, new Vector2f(200, 100), "res/images/player.png");
		player.setType(GameObjectType.PLAYER);
		this.add(player);

		TextObject text = new TextObject("Level 1", new Vector2f(10, 10));
		this.add(text);
		
		Health health = new Health("res/images/health.png", player);
		this.add(health);
	}
}

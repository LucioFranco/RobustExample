/** 
 * The player object
 * 
 * @author Lucio Franco and Benjamin Snoha
 * @version 1.0 
 * @since June 2, 2015
 */

package GameObjects;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;

import cs.lucioben.game.base.Game;
import cs.lucioben.game.base.GameObject;
import cs.lucioben.game.base.GameObjectType;

public class Player extends GameObject implements Healthable {
	private float SPEED = 10;
	private boolean isColliding = false;
	private boolean mouseClicked = false;
	private int health = 100;
	
	/**
	 * The constructor for player. 
	 * @param i the width
	 * @param j the height
	 * @param rotation the rotation
	 * @param position the starting position.
	 */
	public Player(int i, int j, float rotation, Vector2f position) {
		super(i, j, rotation, position);
		
		this.setBoundingBoxWidth(30);
		this.setBoundingBoxHeight(30);
	}
	
	/**
	 * The constructor for player. 
	 * @param i the width
	 * @param j the height
	 * @param rotation the rotation
	 * @param position the starting position.
	 * @param path the path of the file location. 
	 */
	public Player(int i, int j, float rotation, Vector2f position, String path) {
		super(i, j, rotation, position, path);
		
		this.setBoundingBoxWidth(64);
		this.setBoundingBoxHeight(64);
	}

	/**
	 * Updates the player object.
	 */
	@Override
	public void update() {
		this.SPEED = 10;
		
		if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
			this.SPEED = 13;
		}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_W)) {
			setPosition(new Vector2f(0,-1), SPEED);
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_A)) {
			setPosition(new Vector2f(-1,0), SPEED);
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_S)) {
			setPosition(new Vector2f(0,1), SPEED);
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_D)) {
			setPosition(new Vector2f(1,0), SPEED);
		}
		if(Mouse.isButtonDown(0) && !mouseClicked){
			shoot();
		}
		
		mouseClicked = Mouse.isButtonDown(0);
		
		this.setRotation(-(float)(Math.atan2(Mouse.getY() - Game.getScreenHeight()/2, Mouse.getX() - Game.getScreenWidth()/2) * (180/Math.PI)));
	}
	
	/**
	 * Makes the player shoot a bullet in the direction he is facing. 
	 */
	public void shoot(){
		Game.getCurrentScene().add(new Bullet(this.getPosition(), (-(float)(Math.atan2(Mouse.getY() - Game.getScreenHeight()/2, Mouse.getX() - Game.getScreenWidth()/2) * (180/Math.PI)))));
	}
	
	/**
	 * Inflict damage on the player.
	 * @param amount The amount of damage to inflict. 
	 */
	public void takeDamage(float amount){
		health -= amount;
	}
	
	/**
	 * Sets the position of the player. 
	 * @param direction the direction to move in.
	 * @param distance the distance to move.
	 */
	public void setPosition(Vector2f direction, float distance){	
		Vector2f futurePosition = this.getPosition();
		Vector2f previousPosition = this.getPosition();
		
		while(distance > 0){
			previousPosition = futurePosition;
			futurePosition  = new Vector2f(futurePosition.x + direction.x, futurePosition.y + direction.y);
			
			boolean collision = false;
			for(GameObject GameObj : Game.getCurrentScene().getSceneObjects()) {	
				if(GameObj.getType().equals(GameObjectType.COLLISON) || GameObj.getType().equals(GameObjectType.END) || GameObj.getType().equals(GameObjectType.ENEMY)){
					if( futurePosition.x - this.getBoundingBoxWidth()/2 < GameObj.getPosition().x + GameObj.getBoundingBoxWidth()/2 &&
						futurePosition.x + this.getBoundingBoxWidth()/2 > GameObj.getPosition().x - GameObj.getBoundingBoxWidth()/2 &&
						futurePosition.y - this.getBoundingBoxHeight()/2 < GameObj.getPosition().y + GameObj.getBoundingBoxHeight()/2 &&
						futurePosition.y + this.getBoundingBoxHeight()/2 > GameObj.getPosition().y - GameObj.getBoundingBoxHeight()/2){
						
						collision = true;
						
						if(GameObj.getType().equals(GameObjectType.END)){
							Game.getContext().state.next();
						}
					}
				}
			}
			
			if(collision){
				break;
			}
			
			distance--;
		}
		
		this.setPosition(previousPosition);
	}

	/**
	 * Draws the player
	 */
	@Override
	public void draw() {
		GL11.glRotatef(this.getRotation(), 0, 0, 1);
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glTexCoord2f(0,0); 
		GL11.glVertex2f(-this.getWidth()/2, -this.getHeight()/2);
		
		GL11.glTexCoord2f(0,1); 
		GL11.glVertex2f(-this.getWidth()/2, this.getHeight()/2);

		GL11.glTexCoord2f(1,1); 
		GL11.glVertex2f(this.getWidth()/2, this.getHeight()/2);
		
		GL11.glTexCoord2f(1,0); 
		GL11.glVertex2f(this.getWidth()/2, -this.getHeight()/2);
		GL11.glEnd();
	}
		
	/**
	 * Checks if the player is colliding.
	 * @return if the player is colliding with another object. 
	 */
	public boolean isColliding(){
		return isColliding;
	}
	
	/**
	 * Gets the health of the player.
	 * @return the health of the player. 
	 */
	public int getHealth(){
		return health;
	}
}
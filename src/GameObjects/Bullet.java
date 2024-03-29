/** 
 * The bullet class, represents a bullet object.
 * 
 * @author Lucio Franco and Benjamin Snoha
 * @version 1.0 
 * @since June 2, 2015
 */

package GameObjects;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import cs.lucioben.game.base.Game;
import cs.lucioben.game.base.GameObject;
import cs.lucioben.game.base.GameObjectType;

public class Bullet extends GameObject{
	private final float SPEED = 50f; 
	private static final int WIDTH = 4;
	private static final int HEIGHT = 10;
	private Vector2f velocity;
	private boolean alive = true; 
	private boolean enemyBullet = false; 
	private float damage = 10;
	
	/**
	 * The constructor for the bullet
	 * @param startingPosition The position to start the bullet at. 
	 * @param rotation The rotation to face the the bullet.
	 */
	public Bullet(Vector2f startingPosition, float rotation){
		super(WIDTH, HEIGHT, 0, startingPosition);

		this.setTexture("res/images/bullet.png");
		this.setRotation(90 + rotation);
		this.setType(GameObjectType.TEMP_OBJECT);
		velocity = new Vector2f((float)Math.cos(Math.toRadians(rotation)), (float)Math.sin(Math.toRadians(rotation))); 
	}
	
	/**
	 * Updates the bullet
	 */
	@Override
	public void update() {
		this.setPosition(velocity, SPEED);
		
		if(!alive){
			Game.getCurrentScene().remove(this);
		}
	}
	
	/**
	 * Sets a bullet as an enemy bullet
	 * @param t the value of enemyBullet
	 */
	public void setEnemyBullet(boolean t){
		enemyBullet = t;
	}
	
	/**
	 * Gets the enemy bullet
	 * @return if the bullet is an enemy bullet. 
	 */
	public boolean getEnemyBullet(){
		return enemyBullet; 
	}
	
	/**
	 * Gets the damage of the bullet
	 * @return the bullet's damage. 
	 */
	public float getDamage(){
		return damage;
	}
	
	/**
	 * Sets the position of the bullet
	 * @param direction the direction of movement.
	 * @param distance the distance the bullet must move. 
	 */
	public void setPosition(Vector2f direction, float distance){	
		Vector2f futurePosition = this.getPosition();
		Vector2f previousPosition = this.getPosition();
		
		while(distance > 0){
			previousPosition = futurePosition;
			futurePosition = new Vector2f(futurePosition.x + direction.x, futurePosition.y + direction.y);
			
			boolean collision = false;
			for(GameObject GameObj : Game.getCurrentScene().getSceneObjects()) {	
				if(GameObj.getType().equals(GameObjectType.COLLISON) || 
						GameObj.getType().equals(GameObjectType.ENEMY) || 
						GameObj.getType().equals(GameObjectType.PLAYER)){
					if( futurePosition.x - this.getBoundingBoxWidth()/2 < GameObj.getPosition().x + GameObj.getBoundingBoxWidth()/2 &&
						futurePosition.x + this.getBoundingBoxWidth()/2 > GameObj.getPosition().x - GameObj.getBoundingBoxWidth()/2 &&
						futurePosition.y - this.getBoundingBoxHeight()/2 < GameObj.getPosition().y + GameObj.getBoundingBoxHeight()/2 &&
						futurePosition.y + this.getBoundingBoxHeight()/2 > GameObj.getPosition().y - GameObj.getBoundingBoxHeight()/2){
												
						if(GameObj.getType().equals(GameObjectType.ENEMY) && !this.getEnemyBullet()){
							collision = true;

							Enemy e = (Enemy)GameObj;
							e.takeDamage(this.getDamage());
						}
						if(GameObj.getType().equals(GameObjectType.COLLISON)){
							collision = true; 
						}
						if(GameObj.getType().equals(GameObjectType.PLAYER) && this.getEnemyBullet()){
							collision = true;
							
							Player player = (Player)GameObj;
							player.takeDamage(this.getDamage());
						}
					}
				}
			}
			
			if(collision){
				alive = false;
				break;
			}
			
			distance--;
			this.setPosition(previousPosition);
		}
	}
	
	/**
	 * Draws the bullet. 
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
}

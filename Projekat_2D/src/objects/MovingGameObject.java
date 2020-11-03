package objects;

import javafx.geometry.Point2D;

public abstract class MovingGameObject extends GameObject {
	protected float speedX;
	protected float speedY;
	
	public MovingGameObject ( Point2D position ) {
		super ( position );
	}
	
	protected MovingGameObject ( ) {
		super ( );
	}
	
	public float getSpeedX ( ) {
		return speedX;
	}
	
	public void setSpeedX ( float speedX ) {
		this.speedX = speedX;
	}
	
	public float getSpeedY ( ) {
		return speedY;
	}
	
	public void setSpeedY ( float speedY ) {
		this.speedY = speedY;
	}
	
	protected abstract void handleCollisions ( );
	
	protected abstract void updatePosition ( );
}

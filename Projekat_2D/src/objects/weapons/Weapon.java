package objects.weapons;

import javafx.geometry.Point2D;
import models.GameModel;
import objects.MovingGameObject;

import static models.GameModel.GAME_SCENE_MARGIN_Y;
import static models.GameModel.GAME_SCENE_UNIVERSAL_SCALE_FACTOR;

public abstract class Weapon extends MovingGameObject {
	public enum WEAPON_TYPE {BULLET, HARPOON}
	public static WEAPON_TYPE ACTIVE_WEAPON = WEAPON_TYPE.HARPOON; //default

	@Override
	protected void handleCollisions ( ) {
		// if the weapon get's out of the window, remove it from the scene
		if ( position.getY ( ) <=  GAME_SCENE_MARGIN_Y ) {
			GameModel.getInstance ( ).setWeapon ( null );
			GameModel.getInstance ( ).getRoot ( ).getChildren ( ).remove ( this );
		}
	}
	
	public Weapon ( Point2D position ) {
		super ( position );
	}
	
	public Weapon ( ) { }
	
	@Override
	public void updatePosition ( ) {
		position = new Point2D ( position.getX ( ) + speedX, position.getY ( ) + speedY );
		//setTranslateX(getTranslateX() + speedX);
		setTranslateY ( getTranslateY ( ) + speedY );
		
		handleCollisions ( );
	}

}

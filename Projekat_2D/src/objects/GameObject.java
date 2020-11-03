package objects;


import javafx.geometry.Point2D;
import javafx.scene.Group;
import models.GameModel;

import static models.GameModel.GAME_SCENE_MARGIN_X;
import static models.GameModel.GAME_SCENE_MARGIN_Y;

public abstract class GameObject extends Group {
	
	protected Point2D position;
	
	public GameObject ( ) {
		this.position = new Point2D (
				( float ) GameModel.getInstance ( ).getScreenHeight ( ) / 2,
				( float ) GameModel.getInstance ( ).getScreenWidth ( ) / 2
		);
	}
	
	public GameObject ( Point2D position ) {
		//if error
		this.position = position;
		this.setTranslateX ( position.getX() );
		this.setTranslateY ( position.getY() );
	}
	
	public Point2D getPosition ( ) {
		return position;
	}
	
	public void setPosition ( Point2D position ) {
		this.position = position;
	}
}

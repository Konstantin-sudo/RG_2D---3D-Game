package objects.weapons;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import models.GameModel;

public class Bullet extends Weapon {
	public static final float  BULLET_SPEED    = -13;
	public static final double BULLET_DIAMETER = GameModel.getInstance ( ).getSceneWidth ( ) * 0.004;

	{
		ACTIVE_WEAPON = WEAPON_TYPE.BULLET;

		Circle bullet = new Circle ( BULLET_DIAMETER );
		
		bullet.setFill ( Color.BLUE );
		
		this.getChildren ( ).addAll ( bullet );
	}
	
	public Bullet ( Point2D position ) {
		super ( position );
	}
	
	public Bullet ( ) { }

	// initialize speed
	{
		super.speedX = 0;
		super.speedY = BULLET_SPEED;
	}


}

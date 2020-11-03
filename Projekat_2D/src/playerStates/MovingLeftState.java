package playerStates;

import javafx.geometry.Point2D;
import objects.Player;

public class MovingLeftState extends State {
	public MovingLeftState ( Player player ) {
		super ( player );
	}
	
	@Override
	public void update ( ) {
		player.setPosition ( new Point2D ( player.getPosition ( ).getX ( ) - player.getSpeedX ( ), player.getPosition ( ).getY ( ) ) );
		player.setTranslateX ( player.getTranslateX ( ) - player.getSpeedX ( ) );
	}
}

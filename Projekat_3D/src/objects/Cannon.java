package objects;

import constants.Constants;
import javafx.animation.TranslateTransition;
import javafx.event.EventHandler;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.*;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.util.Duration;
import objects.movable.weapon.CannonBall;
import objects.movable.weapon.Weapon;
import subscenes.AmmunitionView;
import subscenes.GameMap;
import subscenes.InfoBar;
import timer.MyAnimationTimer;

public class Cannon extends Group implements EventHandler<MouseEvent> {
	private double           sceneWidth;
	private double           sceneHeight;
	private Rotate           rotateX;
	private Rotate           rotateY;
	private MyAnimationTimer timer;
	private double           width;
	private Color            cannonColor;
	private double           height;
	private double           ySpeed;
	private double           gravity;
	private Group            root;
	private double           ventHeight;
	private Camera 			 cannonBallCamera;
	private int 			 ammunition;
	private double 			 health;
	private AmmunitionView 	 ammunitionView;
	private InfoBar 		 infoBar;
	private GameMap 		 gameMap;

	private Group cannon;
	private Group vent;

	private Group getBody ( double width, double height, double depth, Color cannonColor ) {
		Group body = new Group ( );
		
		float xUnit = ( float ) ( width / 3 );
		float yUnit = ( float ) ( height / 8 );
		float zUnit = ( float ) ( depth / 6 );
		
		Box box = new Box ( 3 * xUnit, 8 * yUnit, 2 * zUnit );
		box.getTransforms ( ).addAll (
				new Translate ( 0, 0, -1 * zUnit )
		);
		box.setMaterial ( new PhongMaterial ( cannonColor ) );
		body.getChildren ( ).addAll ( box );
		
		float points[] = {
				1.5f * xUnit,  4 * yUnit, 0 * zUnit,
				1.5f * xUnit,  4 * yUnit, 2 * zUnit,
				1.5f * xUnit,  2 * yUnit, 4 * zUnit,
				1.5f * xUnit, -4 * yUnit, 0 * zUnit,
				
				-1.5f * xUnit,  4 * yUnit, 0 * zUnit,
			    -1.5f * xUnit,  4 * yUnit, 2 * zUnit,
			    -1.5f * xUnit,  2 * yUnit, 4 * zUnit,
			    -1.5f * xUnit, -4 * yUnit, 0 * zUnit,
		};
		
		float texels[] = {
				0.5f, 0.5f
		};
		
		int faces[] = {
			0, 0, 1, 0, 3, 0,
			1, 0, 2, 0, 3, 0,
			4, 0, 7, 0, 5, 0,
			5, 0, 7, 0, 6, 0,
			0, 0, 4, 0, 5, 0,
			0, 0, 5, 0, 1, 0,
			1, 0, 5, 0, 6, 0,
			1, 0, 6, 0, 2, 0,
			2, 0, 6, 0, 7, 0,
			2, 0, 7, 0, 3, 0,
		};
		
		TriangleMesh triangleMesh = new TriangleMesh ( );
		triangleMesh.getPoints ( ).addAll ( points );
		triangleMesh.getTexCoords ( ).addAll ( texels );
		triangleMesh.getFaces ( ).addAll ( faces );
		MeshView meshView = new MeshView ( triangleMesh );
		meshView.setDrawMode ( DrawMode.FILL );
		meshView.setMaterial ( new PhongMaterial ( cannonColor ) );
		body.getChildren ( ).addAll ( meshView );
		
		return body;
	}
	
	private Group getVent ( double radius, double ventHeight, Color cannonColor ) {
		Group vent = new Group ( );
		
		Cylinder neck = new Cylinder ( 0.5 * radius, 0.9 * ventHeight );
		neck.setMaterial ( new PhongMaterial ( cannonColor ) );
		neck.getTransforms ( ).addAll (
				new Translate ( 0, 0.05 * ventHeight, 0 )
		);
		vent.getChildren ( ).addAll ( neck );
		
		Cylinder top = new Cylinder ( radius, 0.1 * ventHeight );
		top.setMaterial ( new PhongMaterial ( cannonColor ) );
		top.getTransforms ( ).addAll (
				new Translate ( 0, -0.45 * ventHeight, 0 )
		);
		vent.getChildren ( ).addAll ( top );
		
		return vent;
	}
	
	public Cannon (Group root, double width, double height, double depth, double islandHeight, double ventHeight, Color cannonColor, double sceneWidth, double sceneHeight, double ySpeed, double gravity, MyAnimationTimer timer, Camera camera, Camera cannonBallCamera, AmmunitionView ammunitionView, InfoBar infoBar, GameMap gameMap) {
		this.root = root;
		this.gameMap = gameMap;
		this.infoBar = infoBar;
		this.ammunitionView = ammunitionView;
		this.ammunition = Constants.CANNON_MAX_AMMUNITION;
		this.health = 100;
		this.sceneHeight = sceneHeight;
		this.sceneWidth = sceneWidth;
		this.timer = timer;
		this.width = width;
		this.cannonColor = cannonColor;
		this.height = height;
		this.ySpeed = ySpeed;
		this.gravity = gravity;
		this.ventHeight = ventHeight;
		this.cannonBallCamera = cannonBallCamera;
		this.rotateX = new Rotate ( );
		this.rotateY = new Rotate ( );
		this.rotateX.setAxis ( Rotate.X_AXIS );
		this.rotateY.setAxis ( Rotate.Y_AXIS );

		//CANNON
		cannon = new Group ( );
		super.getChildren ( ).addAll ( cannon );
		// Box podium = new Box ( width, height, depth );
		// podium.setMaterial ( new PhongMaterial ( cannonColor ) );
		Group podium = this.getBody ( width, height, depth, cannonColor );
		podium.getTransforms ( ).addAll (
				new Translate ( 0, -( height + islandHeight ) / 2, 0 )
		);
		cannon.getChildren ( ).addAll ( podium );
		cannon.getTransforms ( ).addAll (
				this.rotateY
		);
		//VENT
		// Cylinder vent = new Cylinder ( width / 2, ventHeight );
		// vent.setMaterial ( new PhongMaterial ( cannonColor ) );
		vent = this.getVent ( width / 2, ventHeight, cannonColor );
		vent.getTransforms ( ).addAll (
				new Translate ( 0, -height / 2, 0 ),
				this.rotateX,
				new Translate ( 0, -ventHeight / 2, 0 )
		);
		cannon.getChildren ( ).addAll ( vent );
		//CANNON CAMERA
		camera.getTransforms ( ).addAll (
				new Translate ( 0, -3 * height, -6 * depth ),
				new Rotate ( -10, Rotate.X_AXIS )
				// new Rotate ( 180, Rotate.Y_AXIS )
		);
		cannon.getChildren ( ).addAll ( camera );
		

	}

    public boolean handleCollision(Weapon other)
    {
        if ( this.getBoundsInParent ( ).intersects ( other.getBoundsInParent ( ) ) ) {
            this.onCollision ( other );
            return true;
        } else {
            return false;
        }
    }

    public void onCollision ( Weapon other ) {
		this.root.getChildren ( ).remove ( other );
		this.health -= other.getPower();
		this.infoBar.updateHealth(this.health);
		if(this.health <= 0)
		{

		}
    }

    public void addAmmunition (int new_ammo)
	{
		int freeSpace = Constants.CANNON_MAX_AMMUNITION - this.ammunition;
		int newBullets;
		if(new_ammo >= freeSpace)
			newBullets = freeSpace;
		else
			newBullets = new_ammo;
		this.ammunition += newBullets;
		this.ammunitionView.addBullets(newBullets);
	}

	public double getHealth() {
		return health;
	}

	private void fireAnimation()
	{
		double d = -2;
		Duration t1 = Duration.seconds(0.1);
		Duration t2 = Duration.seconds(0.8);
		TranslateTransition a1 = new TranslateTransition(t1, this.vent);
		a1.setFromZ(1);
		a1.setToZ(d);
		TranslateTransition a2 = new TranslateTransition(t2, this.vent);
		a2.setFromZ(d);
		a2.setToZ(1);
		a1.setOnFinished(e->a2.play());
		a1.play();
	}

	@Override public void handle (MouseEvent event ) {
		if ( MouseEvent.MOUSE_MOVED.equals ( event.getEventType ( ) ) ) {
			double xRatio = event.getSceneX ( ) / this.sceneWidth;
			double yRatio = event.getSceneY ( ) / this.sceneHeight;
			
			this.rotateX.setAngle ( -120 * yRatio );
			this.rotateY.setAngle ( 360 * xRatio );
			gameMap.rotateCannon( 360 * xRatio );
		} else if ( MouseEvent.MOUSE_PRESSED.equals ( event.getEventType ( ) ) && this.timer.canAddWeapon ( ) ) {
			if(this.ammunition > 0)
			{
				fireAnimation();
				CannonBall cannonBall = new CannonBall (
						root,
						Constants.CANNON_BALL_RADIUS,
						this.cannonColor,
						this.height / 2,
						this.ventHeight,
						this.rotateX.getAngle ( ),
						this.rotateY.getAngle ( ),
						this.ySpeed,
						this.gravity,
						timer,
						this.cannonBallCamera
				);
				root.getChildren ( ).addAll ( cannonBall );
				--this.ammunition;
				this.ammunitionView.removeBullets(1 );
			}
		}
	}
}

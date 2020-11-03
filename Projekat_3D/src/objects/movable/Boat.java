package objects.movable;

import cameraController.CameraController;
import constants.Constants;
import javafx.animation.TranslateTransition;
import javafx.geometry.Point3D;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.util.Duration;
import objects.movable.weapon.Projectile;
import subscenes.GameMap;
import subscenes.InfoBar;
import timer.MyAnimationTimer;

import java.util.Random;

public class Boat extends MovableObject {

	private double  Yangle;
	private double  height;
	private double  mySpeed;
	private GameMap gameMap;
	private InfoBar infoBar;

	public static class BoatDestination implements Destination {
		private Point3D destination;
		private double delta;
		
		public BoatDestination ( Point3D destination, double delta ) {
			this.destination = destination;
			this.delta = delta;
		}
		
		@Override public boolean reached ( double x, double y, double z ) {
			double dx = Math.abs ( this.destination.getX ( ) - x );
			double dy = Math.abs ( this.destination.getY ( ) - y );
			double dz = Math.abs ( this.destination.getZ ( ) - z );
			
			if ( dx <= this.delta && dy <= this.delta && dz <= this.delta ) {
				return true;
			} else {
				return false;
			}
		}
	}
	
	private static Affine getPosition ( double angle, double distance ) {
		Affine identity = new Affine (  );
		
		identity.appendRotation ( angle, new Point3D ( 0, 0, 0 ), new Point3D ( 0, 1, 0 ) );
		identity.appendTranslation ( 0, 0, distance );
		
		return identity;
	}
	
	private static Point3D getSpeed ( double angle, double distance, double speed ) {
		Affine position = Boat.getPosition ( angle, distance );
		
		return new Point3D (
				-position.getTx ( ),
				-position.getTy ( ),
				-position.getTz ( )
		).normalize ( ).multiply ( speed );
	}
	
	private static BoatDestination getBoatDestination ( double angle, double zDestination, double delta ) {
		Affine position = Boat.getPosition ( angle, zDestination );
		Point3D destination = new Point3D (
				position.getTx ( ),
				position.getTy ( ),
				position.getTz ( )
		);
		
		return new BoatDestination ( destination, delta );
	}
	
	@Override public void onDestinationReached ( ) {
		infoBar.incReachedIslandCnt();
	}
	
	public Boat (Group parent, double body_width, double body_height, double body_depth, double cabin_width, double cabin_height, double cabin_depth, double nose_length, double distance, double angle, double speed, double destination, double delta, CameraController cameraController, InfoBar infoBar, GameMap gameMap) {
		super (
				parent,
				Boat.getPosition ( angle, distance ),
				Boat.getSpeed ( angle, distance, speed),
				new Point3D ( 0, 0, 0 ),
				Boat.getBoatDestination ( angle, destination + (body_depth / 2 + nose_length), delta )
		);

		this.mySpeed = speed;
		this.gameMap = gameMap;
		this.infoBar = infoBar;
		this.Yangle = angle;
		this.height = body_height + cabin_height;
		Group boat_ = buildBoat(body_width, body_height, body_depth, cabin_width,cabin_height,cabin_depth,nose_length);
		super.getChildren ( ).addAll ( boat_ );

		Camera newBoatCamera  = new PerspectiveCamera(true);
		newBoatCamera.setNearClip(Constants.CAMERA_NEAR_CLIP);
		newBoatCamera.setFarClip(Constants.CAMERA_FAR_CLIP);
		newBoatCamera.getTransforms ( ).addAll (
				new Translate( 0, -1 * height, 1 * (body_depth + nose_length) ),
				//new Rotate( -5, Rotate.X_AXIS ),
				new Rotate ( 180, Rotate.Y_AXIS )
		);
		this.setCamera( newBoatCamera );
		cameraController.addBoatCamera( newBoatCamera, this.id );
	}

	private Group buildBoat(double body_width, double body_height, double body_depth, double cabin_width, double cabin_height, double cabin_depth, double nose_length)
	{
		Group ret = new Group();

		Box body = new Box(body_width, body_height, body_depth);
		PhongMaterial body_material = new PhongMaterial(Color.LIGHTGRAY);
		body.setMaterial(body_material);
		ret.getChildren().addAll(body);

		float[] points = {
				-(float)body_width/2, -(float)body_height/2, -(float)body_depth/2, //a 0
				 (float)body_width/2, -(float)body_height/2, -(float)body_depth/2, //b 1
				-(float)body_width/2,  (float)body_height/2, -(float)body_depth/2, //c 2
				 (float)body_width/2,  (float)body_height/2, -(float)body_depth/2, //d 3
						0, 			  -(float)body_height/2, -(float)(body_depth/2 + nose_length), //ostale 4
		};
		float[] texCoords = {
				0.5f,	0f,  		//g 0
				0f, 	0.35f,		//h 1
				0.31f, 	0.35f, //a 2
				0.69f,	0.35f, //b 3
				1f, 	0.35f, 		//f 4
				0.31f, 	0.58f, //c 5
				0.69f, 	0.58f, //d 6
				0.5f, 	1f 			//e 7
		};
		int[] faces = {
				0,2, 1,3, 2,5, //abc
				0,2, 2,5, 1,3,

				1,3, 2,5, 3,6, //bcd
				1,3, 3,6, 2,5,

				0,2, 1,3, 4,0, //abg
				0,2, 4,0, 1,3,

				4,1, 0,2, 2,5, //hac
				4,1, 2,5,  0,2,

				1,3, 4,4, 3,6, //bfd
				1,3, 3,6, 4,4,

				2,5, 3,6, 4,7,  //cde
				2,5, 4,7, 3,6
		};
		TriangleMesh mesh = new TriangleMesh();
		mesh.getPoints().addAll(points);
		mesh.getTexCoords().addAll(texCoords);
		mesh.getFaces().addAll(faces);

		MeshView nose = new MeshView();
		nose.setMesh(mesh);
		nose.setMaterial(body_material);
		ret.getChildren().addAll(nose);
		nose.getTransforms().addAll(
			new Translate(0, 0, 0)
		);

		Box cabin = new Box(cabin_width, cabin_height, cabin_depth);
		PhongMaterial cabin_material = new PhongMaterial(Color.GRAY);
		cabin.setMaterial(cabin_material);
		ret.getChildren().addAll(cabin);
		cabin.getTransforms().addAll(
				new Translate(0, -(body_height/2 + cabin_height/2), body_depth/2 - 4 - cabin_depth/2)
		);

		return ret;
	}

	@Override
	public void onCollision ( ) {
		gameMap.removeBoat(this.id);
		infoBar.incSunkenShipsCnt();
		TranslateTransition sinkAnimation = new TranslateTransition(Duration.seconds(5),this);
		sinkAnimation.setFromY(this.position.getTy());
		sinkAnimation.setToY(this.position.getTy() + this.height/2);
		sinkAnimation.setOnFinished( v -> this.parent.getChildren ( ).remove ( this ) );
		sinkAnimation.play();
	}

	@Override
	public double getMyYAngle()
	{
		return this.Yangle;
	}

	public static void tryToFireFromBoat(MovableObject boat, Group root, MyAnimationTimer timer)
	{
		//boat.position;
        Random rand = new Random();
        double chance = rand.nextDouble() * 100;
		//System.out.println("chance: " + chance);
        if(chance <= Constants.BOAT_CHANCE_TO_FIRE && chance > 0)
        {
			//System.out.println("boat fire!");
        	//calculate random yAngle
			double xAngle = -Constants.BOAT_FIRE_ANGLE_RANGE_VERTICAL * ( new Random().nextDouble() * 0.9 + 0.1); // from 10 degree to 60
			double dy = new Random().nextDouble() * (Constants.BOAT_FIRE_ANGLE_RANGE_HORIZONTAL / 2);
			if( new Random().nextDouble() <= 0.5)
				dy *= -1;
			double yAngle = (boat.getMyYAngle() + 180) + dy;

            Projectile projectile = new Projectile(root, Constants.PROJECTILE_RADIUS, Color.YELLOW,Constants.BOAT_BODY_HEIGHT + Constants.BOAT_CABIN_HEIGHT,xAngle, yAngle, Constants.Y_SPEED, Constants.GRAVITY, boat.position, timer );
        	root.getChildren().addAll( projectile );
            timer.addProjectile(projectile);
        }

	}

	@Override
	public boolean update(long now) {
		boolean ret =  super.update(now);
		this.gameMap.updateBoatPosition(this.id, this.mySpeed);
		return ret;
	}
}

package objects.movable;

import javafx.geometry.Point3D;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.transform.Affine;

public abstract class MovableObject extends Group {
	public static interface Destination {
		public boolean reached ( double x, double y, double z );
	}
	private static int globID = -1;
	protected int id;
	protected Camera camera;
	protected Group parent;
	protected Affine position;
	private Point3D speed;
	private Point3D damp;
	private Destination destination;
	
	public MovableObject ( Group parent, Affine position, Point3D speed, Point3D damp, Destination destination ) {
		this.parent      = parent;
		this.position    = position;
		this.speed       = speed;
		this.damp        = damp;
		this.destination = destination;
		this.id = ++globID;

		super.getTransforms ( ).addAll (
				this.position
		);
	}

	public int getMyId() { return id; }
	public double getMyYAngle(){ return 0; }

	public boolean update ( long now ) {
		double x = this.position.getTx ( );
		double y = this.position.getTy ( );
		double z = this.position.getTz ( );
		
		double newX = x + this.speed.getX ( );
		double newY = y + this.speed.getY ( );
		double newZ = z + this.speed.getZ ( );
		
		this.position.setTx ( newX );
		this.position.setTy ( newY );
		this.position.setTz ( newZ );

		this.rotateCamera(x, y, z, newX, newY, newZ);

		this.speed = this.speed.add ( this.damp );
		
		boolean destinationReached = this.destination.reached ( newX, newY, newZ );
		
		if ( destinationReached ) {
			this.onDestinationReached ( );
			return true;
		} else {
			return false;
		}
	}
	
	public boolean handleCollision ( MovableObject other ) {
		if ( this.getBoundsInParent ( ).intersects ( other.getBoundsInParent ( ) ) ) {
			this.onCollision ( );
			return true;
		} else {
			return false;
		}
	}
	
	public void onDestinationReached ( ) { this.parent.getChildren ( ).remove ( this ); }

	public void onCollision ( ) {
		this.parent.getChildren ( ).remove ( this );
	}

	public void setCamera(Camera camera) {
		this.camera = camera;
		this.getChildren().add(this.camera);
	}

	public void removeCamera() {
		this.getChildren().remove(this.camera);
		this.camera = null;
	}

	public void rotateCamera(double x, double y, double z, double newX, double newY, double newZ)
	{

	}
}

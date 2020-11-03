package objects.movable.weapon;

import constants.Constants;
import javafx.animation.*;
import javafx.geometry.Point3D;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.util.Duration;
import objects.movable.MovableObject;
import timer.MyAnimationTimer;

public class CannonBall extends Weapon {

	private double xAngle;

	public static class CannonBallDestination implements Destination {
		private double radius;
		private double landingPointHeight;
		private Group root;
		
		public CannonBallDestination ( double radius, double landingPointHeight, Group root ) {
			this.radius = radius;
			this.landingPointHeight = landingPointHeight;
			this.root = root;
		}
		
		@Override public boolean reached ( double x, double y, double z ) {
			boolean reached = ( y - radius ) >= 0;
			if ( reached )
			    this.makeSplash( x, y, z );

			return reached;
		}

        private void makeSplash(double x, double y, double z )
        {
            Cylinder landingPoint = new Cylinder( this.radius, this.landingPointHeight);
            PhongMaterial LandingPointMaterial = new PhongMaterial ( Color.BLUE );
            landingPoint.setMaterial ( LandingPointMaterial );
            landingPoint.getTransforms ( ).addAll (
                    new Translate ( x, this.landingPointHeight/2, z )
            );
            this.root.getChildren ( ).addAll ( landingPoint );

            KeyValue startHeight = new KeyValue(landingPoint.translateYProperty(),0.0);
            KeyValue maxHeight = new KeyValue(landingPoint.translateYProperty(), -this.landingPointHeight, Interpolator.EASE_OUT);
            KeyValue endHeight = new KeyValue(landingPoint.translateYProperty(), 0.0, Interpolator.EASE_IN);

            KeyValue startColor = new KeyValue(LandingPointMaterial.diffuseColorProperty(), Color.BLUE);
            KeyValue middleColor = new KeyValue(LandingPointMaterial.diffuseColorProperty(), Color.WHITE, Interpolator.EASE_OUT);
            KeyValue endColor = new KeyValue(LandingPointMaterial.diffuseColorProperty(), Color.BLUE, Interpolator.EASE_IN);

            KeyFrame startFrame = new KeyFrame(Duration.seconds(0), startHeight, startColor);
            KeyFrame middleFrame = new KeyFrame(Duration.seconds(1), maxHeight, middleColor);
            KeyFrame endFrame = new KeyFrame(Duration.seconds(2), endHeight, endColor);

            Timeline splashAnimation = new Timeline(startFrame, middleFrame, endFrame);
            splashAnimation.setOnFinished ( value -> this.root.getChildren ( ).remove ( landingPoint ) );
            splashAnimation.play();
        }

	}

	private static Point3D getSpeed ( double ySpeed, double xAngle, double yAngle ) {
		Point3D speedVector = new Point3D ( 0, ySpeed, 0 );
		Rotate rotateX = new Rotate ( xAngle, Rotate.X_AXIS );
		Rotate rotateY = new Rotate ( yAngle, Rotate.Y_AXIS );
		speedVector = rotateX.transform ( speedVector );
		speedVector = rotateY.transform ( speedVector );
		return speedVector;
	}
	
	private static Affine getPosition ( double cannonHeight, double ventHeight, double xAngle, double yAngle ) {
		Affine identity = new Affine ( );
		
		identity.appendRotation ( yAngle, Point3D.ZERO, Rotate.Y_AXIS );
		identity.appendTranslation ( 0, -cannonHeight, 0 );
		identity.appendRotation ( xAngle, Point3D.ZERO, Rotate.X_AXIS );
		identity.appendTranslation ( 0, -ventHeight, 0 );
		
		return identity;
	}
	
	public CannonBall ( Group root, double radius, Color color, double cannonHeight, double ventHeight, double xAngle, double yAngle, double ySpeed, double gravity, MyAnimationTimer timer, Camera camera ) {
		super (
				root,
				CannonBall.getPosition ( cannonHeight, ventHeight, xAngle, yAngle ),
				CannonBall.getSpeed ( ySpeed, xAngle, yAngle ),
				new Point3D ( 0, gravity, 0 ),
				new CannonBallDestination ( radius, 4 * radius, root ),
				timer,
				Constants.WeaponType.CannonBall
		);
		Sphere ball = new Sphere (radius );
		ball.setMaterial ( new PhongMaterial ( color ) );
		super.getChildren ( ).addAll ( ball );

		this.xAngle = xAngle;
		this.setCamera(camera);
	}

	@Override public boolean update ( long now )
	{
		boolean ret = super.update(now);
		if(ret)
		{
			this.timer.changeWeaponCameraToCannonCamera();
			this.removeCamera();
		}
		return ret;
	}

	@Override
	public void onCollision ( )
	{
		super.onCollision();
		this.timer.changeWeaponCameraToCannonCamera();
		this.removeCamera();
	}

	@Override
	public void rotateCamera(double x, double y, double z, double newX, double newY, double newZ)
	{
		// nesto ne radi
		double dx = newX - x;
		double dy = newY - y;
		double dz = newZ - z;
		double newXangle = Math.atan(Math.abs(dy)/Math.abs(dz));
		if(dy != 0)
		{
			if (dy < 0)
			{
				if (dz < 0)
					newXangle *= -1;
			}
			else
			{
				if (dz > 0)
					newXangle *= -1;
			}
		}
		double rotationAngle = (newXangle - this.xAngle) * 1;
		this.xAngle = newXangle;
		this.position.appendRotation(rotationAngle, Point3D.ZERO, Rotate.X_AXIS);
	}

}

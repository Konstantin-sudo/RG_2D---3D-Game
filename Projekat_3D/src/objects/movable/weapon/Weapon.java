package objects.movable.weapon;

import constants.Constants;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Rotate;
import objects.movable.MovableObject;
import timer.MyAnimationTimer;

public abstract class Weapon extends MovableObject {
	protected MyAnimationTimer timer;
	private Constants.WeaponType type;
	protected double power;

	public Weapon ( Group parent, Affine position, Point3D speed, Point3D damp, Destination destination, MyAnimationTimer timer, Constants.WeaponType type) {
		super ( parent, position, speed, damp, destination );
		
		this.timer = timer;
		this.type = type;
		this.power = 100;
		switch (this.type)
		{
			case CannonBall:
				this.timer.setWeapon ( this );
				break;
			case BoatProjectile:
				break;
		}
	}

	public double getPower() {
		return power;
	}

	@Override public boolean update ( long now ) {
		boolean returnValue = super.update ( now );
		if ( returnValue ) {
			switch (this.type)
			{
				case CannonBall:
					this.timer.setWeapon ( null );
					break;
				case BoatProjectile:
					//this.timer.removeProjectile(this);
					break;
			}
		}
		
		return returnValue;
	}
}

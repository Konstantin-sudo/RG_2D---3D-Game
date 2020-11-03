package timer;

import cameraController.CameraController;
import constants.Constants;
import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.stage.Stage;
import objects.Cannon;
import objects.movable.Boat;
import objects.movable.MovableObject;
import objects.movable.weapon.Weapon;
import subscenes.GameMap;
import subscenes.InfoBar;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MyAnimationTimer extends AnimationTimer {
	private List<MovableObject> movableObjects;
	private MovableObject       weapon;
	private List<Weapon> projectiles = new ArrayList<>( );

	private GameEventListener listener;
	private boolean gameOver;

    private Group root3D;
	private CameraController cameraController;
	private long last_time_boat_fired;
	private Cannon cannon;
	private int newWaveCnt;
	private double lastWaveBoatSpeed;
	private InfoBar infoBar;
	private GameMap gameMap;
	private Stage stage;

	public MyAnimationTimer (Stage stage, List<MovableObject> movableObjects, GameEventListener listener, CameraController cameraController, Group root3D, InfoBar infoBar, GameMap gameMap) {
		this.movableObjects = movableObjects;
		this.listener = listener;
		this.cameraController = cameraController;
		this.cannon = null;
		this.root3D = root3D;
		this.newWaveCnt = Constants.NUMBER_OF_NEW_WAVES;
		this.lastWaveBoatSpeed = Constants.BOAT_SPEED;
		this.infoBar = infoBar;
		this.gameMap = gameMap;
		this.stage = stage;
	}

	@Override public synchronized void handle ( long now ) {

		if ( this.gameOver == false ) {
			boolean islandHit = this.movableObjects.removeIf ( object -> object.update ( now  ) );
			if ( islandHit ) {
				this.listener.onGameLost ( this.stage.getWidth(), this.stage.getHeight() );
				this.gameOver = true;
			} else {
				//projectiles--start
				long diff = (now - last_time_boat_fired) / 1000000000;
				double time_to_wait = 1 / Constants.BOAT_FIRE_RATE;
				boolean is_time_to_fire = diff >= time_to_wait;
				if( is_time_to_fire )
				{
					this.movableObjects.forEach(movableObject -> Boat.tryToFireFromBoat(movableObject, root3D, this) );
					last_time_boat_fired = now;
				}
				boolean projectileMiss = this.projectiles.removeIf ( object -> object.update ( now  ) );
				if(projectiles.size() != 0)
				{
					boolean isCannonHit = this.projectiles.removeIf(object -> this.cannon.handleCollision(object) );
					if(isCannonHit)
					{
						if( cannon.getHealth() <= 0)
						{
							this.root3D.getChildren().remove(this.cannon);
							this.cannon = null;
							if(this.cameraController.isActiveCannonCamera())
								this.cameraController.setDefaultCamera();
							this.cameraController.removeCannonCamera();
							this.gameMap.removeCannon();
							this.listener.onGameLost ( this.stage.getWidth(), this.stage.getHeight() );
							this.gameOver = true;
						}
					}
				}
				//projectiles--end
				if (this.weapon != null) {
					boolean boatHit = this.movableObjects.removeIf(object -> {
						boolean collision = object.handleCollision(this.weapon);
						if (collision) {
							int boatId = object.getMyId();
							if (this.cameraController.isActiveCameraOnBoat(boatId))
								this.cameraController.setDefaultCamera();
							object.removeCamera();
							this.cameraController.removeCameraOnBoat(boatId);
						}
						return collision;
					});
					if (boatHit) {
						this.weapon.onCollision();
						this.setWeapon(null);
						if (this.movableObjects.size() == 0) {
							if(newWaveCnt > 0){
								this.newWave();
								--newWaveCnt;
							}
							else
							{
								this.listener.onGameWon( this.stage.getWidth(), this.stage.getHeight() );
								this.gameOver = true;
							}
						}
					}
				}
			}
		}
		this.projectiles.removeIf(object -> object.update (now) );
		if ( this.weapon != null ) {
			this.weapon.update ( now );
		}
	}

	@Override
	public void start()
	{
		super.start();
		this.last_time_boat_fired = System.currentTimeMillis() / (1000 * 1000); //miliseconds -> nanoseconds
	}

	public synchronized boolean canAddWeapon ( ) {
		return this.gameOver == false && this.weapon == null;
	}
	
	public synchronized void setWeapon ( MovableObject weapon ) {
		this.weapon = weapon;
		if(this.weapon == null)
			cameraController.setIsThereCannonBall(false);
		else
			cameraController.setIsThereCannonBall(true);
	}

	public void changeWeaponCameraToCannonCamera()
	{
		if( this.cameraController.isActiveCannonBallCamera() )
			this.cameraController.setCannonCamera();
	}

	public void addProjectile(Weapon projectile) {
		projectiles.add(projectile);
	}

	public void setCannon(Cannon cannon) { this.cannon = cannon; }

	private void newWave()
	{
		//double waveNumber = Constants.NUMBER_OF_NEW_WAVES - this.newWaveCnt + 1;
		double newSpeed = this.lastWaveBoatSpeed * Constants.NEW_WAVE_BOAT_SPEED_INCREASE_FACTOR;
		if(newSpeed > 0.5) newSpeed = 0.5;
		this.lastWaveBoatSpeed = newSpeed;
		this.cannon.addAmmunition(Constants.NEW_WAVE_AMMUNITION);
		for ( int i = 0; i < Constants.NUMBER_OF_BOATS; ++i ) {
			double attackArea = 360 * 1.0 / Constants.NUMBER_OF_BOATS;
			Random rand = new Random();
			double angleInAttackArea = rand.nextDouble() * attackArea;
			double angle = angleInAttackArea + i * attackArea;
			Boat boat = new Boat (
                    root3D,
					Constants.BOAT_BODY_WIDTH,
					Constants.BOAT_BODY_HEIGHT,
					Constants.BOAT_BODY_DEPTH,
					Constants.BOAT_CABIN_WIDTH,
					Constants.BOAT_CABIN_HEIGHT,
					Constants.BOAT_CABIN_DEBT,
					Constants.BOAT_NOSE_LENGTH,
					Constants.BOAT_DISTANCE,
					angle,
					newSpeed,
					Constants.ISLAND_RADIUS,
					Constants.DELTA,
					this.cameraController,
					this.infoBar,
					this.gameMap
			);
			root3D.getChildren ( ).addAll ( boat );
			movableObjects.add ( boat );
			this.gameMap.addBoat(boat.getMyId(), angle);
		}
	}

}

package cameraController;

import constants.Constants;
import javafx.event.EventHandler;
import javafx.scene.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import subscenes.GameMap;

public class CameraController implements EventHandler<KeyEvent> {
	private static final double CAMERA_MOVING_SPEED = 1.0;
	private static Camera activeCamera = null;
	private SubScene subScene;
	private Camera fixedCamera;
	private Camera cannonCamera;
	private BoatCamera[] arrayOfBoatCamera;
	private Camera cannonBallCamera;
	private boolean isThereCannonBall;
	private boolean isWeaponCameraOn;
	private int spaceBtnCnt;
	private int tBtnCnt;
	private boolean isGameMapOn;
	private Group mainRoot;
	private GameMap gameMap;

	private class BoatCamera{
		private Camera camera;
		private int boatId;

		public BoatCamera(Camera camera, int boatId)
		{
			this.camera = camera;
			this.boatId = boatId;
		}

		public Camera getCamera() {
			return camera;
		}

		public int getBoatId() {
			return boatId;
		}
	}

	public CameraController (double nearClip, double farClip, SubScene subScene, Group mainRoot, GameMap gameMap) {
		this.subScene = subScene;

		this.fixedCamera = new PerspectiveCamera ( true );
		this.fixedCamera.setNearClip ( nearClip );
		this.fixedCamera.setFarClip ( farClip );
		activeCamera = this.fixedCamera;
		this.subScene.setCamera ( this.fixedCamera );

		this.cannonCamera = new PerspectiveCamera ( true );
		this.cannonCamera.setNearClip ( nearClip );
		this.cannonCamera.setFarClip ( farClip );

		arrayOfBoatCamera = new BoatCamera[Constants.NUMBER_OF_BOATS];

		this.isWeaponCameraOn = false;
		this.isThereCannonBall = false;
		this.cannonBallCamera = new PerspectiveCamera ( true );
		this.cannonBallCamera.setNearClip ( nearClip );
		this.cannonBallCamera.setFarClip ( farClip );
		this.spaceBtnCnt = 0;

		this.gameMap = gameMap;
		this.mainRoot = mainRoot;
		this.isGameMapOn = true;
		this.tBtnCnt = 0;
	}

	//GET
	public Camera getFixedCamera ( ) {
		return fixedCamera;
	}
	
	public Camera getCannonCamera ( ) {
		return cannonCamera;
	}

	public Camera getBoatCamera(int boatId) {
		for(int i = 0; i < arrayOfBoatCamera.length; ++i)
		{
			BoatCamera boatCamera = arrayOfBoatCamera[i];
			if(boatCamera.getBoatId() == boatId)
			{
				return boatCamera.getCamera();
			}
		}
		return null;
	}

	public Camera getCannonBallCamera() { return cannonBallCamera; }

	//SET
	public void setDefaultCamera()
	{
		activeCamera = this.fixedCamera;
		this.subScene.setCamera(this.fixedCamera);
	}
	public void setCannonCamera()
	{
		activeCamera = this.cannonCamera;
		this.subScene.setCamera(this.cannonCamera);
	}

	//CANNON CAMERA
	public boolean isActiveCannonCamera() { return CameraController.activeCamera == this.cannonCamera; }

	public void removeCannonCamera() { this.cannonCamera = null; }

	//BOAT CAMERA
	public void addBoatCamera(Camera camera, int boatId)
	{
		for(int i = 0; i < arrayOfBoatCamera.length; ++i)
		{
			if( arrayOfBoatCamera[i] == null)
			{
				arrayOfBoatCamera[i] = new BoatCamera(camera, boatId);
				break;
			}
		}
	}
	public void removeCameraOnBoat(int boatId)
	{
		for (int i = 0; i < arrayOfBoatCamera.length; i++) {
			if(arrayOfBoatCamera[i] != null)
			{
				if (arrayOfBoatCamera[i].getBoatId() == boatId)
				{
					arrayOfBoatCamera[i] = null;
					break;
				}
			}
		}
	}
	public boolean isActiveCameraOnBoat(int boatId)
	{
		for (int i = 0; i < arrayOfBoatCamera.length; i++)
		{
			if(arrayOfBoatCamera[i] != null)
			{
				BoatCamera boatCamera = arrayOfBoatCamera[i];
				if(boatCamera.getBoatId() == boatId)
				{
					if(CameraController.activeCamera == boatCamera.getCamera())
						return true;
				}
			}
		}
		return false;
	}
	private boolean isActiveCameraBoatCamera()
	{
		for(int i = 0; i < arrayOfBoatCamera.length; ++i)
		{
			if(arrayOfBoatCamera[i] != null)
			{
				if( activeCamera == arrayOfBoatCamera[i].getCamera() )
				return true;
			}
		}
		return false;
	}
	private void moveCamera(double dx,double dy,double dz)
	{
		activeCamera.getTransforms().addAll(
				new Translate(dx, dy, dz)
		);
	}

	//CANNON BALL CAMERA
	public boolean getIsWeaponCameraOn() { return isWeaponCameraOn; }

	public void setIsThereCannonBall(boolean IsThereCannonBall) {
		this.isThereCannonBall = IsThereCannonBall;
		if(this.isThereCannonBall == true && this.isWeaponCameraOn)
		{
			activeCamera = this.cannonBallCamera;
			this.subScene.setCamera(this.cannonBallCamera);
		}
	}

	public boolean isActiveCannonBallCamera() {	return activeCamera == cannonBallCamera; }

	//KEY EVENTS
	@Override public void handle (KeyEvent event ) {
		switch ( event.getCode ( ) ) {
			case DIGIT0:
			case NUMPAD0: {
				activeCamera = this.fixedCamera;
				this.subScene.setCamera ( this.fixedCamera );
				break;
			}
			
			case DIGIT5:
			case NUMPAD5: {
				if(this.cannonCamera != null)
				{
					activeCamera = this.cannonCamera;
					this.subScene.setCamera ( this.cannonCamera );
				}
				break;
			}

			case DIGIT1:
			case NUMPAD1:
			{
				if(this.arrayOfBoatCamera[0] != null) {
					activeCamera = this.arrayOfBoatCamera[0].getCamera();
					this.subScene.setCamera(this.arrayOfBoatCamera[0].getCamera());
				}
				break;
			}
			case DIGIT2:
			case NUMPAD2:
			{
				if(this.arrayOfBoatCamera[1] != null) {
					activeCamera = this.arrayOfBoatCamera[1].getCamera();
					this.subScene.setCamera(this.arrayOfBoatCamera[1].getCamera());
				}
				break;
			}
			case DIGIT3:
			case NUMPAD3:
			{
				if(this.arrayOfBoatCamera[2] != null) {
					activeCamera = this.arrayOfBoatCamera[2].getCamera();
					this.subScene.setCamera(this.arrayOfBoatCamera[2].getCamera());
				}
				break;
			}
			case DIGIT4:
			case NUMPAD4:
			{
				if(this.arrayOfBoatCamera[3] != null) {
					activeCamera = this.arrayOfBoatCamera[3].getCamera();
					this.subScene.setCamera(this.arrayOfBoatCamera[3].getCamera());
				}
				break;
			}

			case LEFT:
			{
				if(isActiveCameraBoatCamera())
					moveCamera( -CAMERA_MOVING_SPEED,0,0);
				break;
			}
			case RIGHT:
			{
				if(isActiveCameraBoatCamera())
					moveCamera( CAMERA_MOVING_SPEED,0,0);
				break;
			}
			case UP:
			{
				if(isActiveCameraBoatCamera())
					moveCamera(0, -CAMERA_MOVING_SPEED,0);
				break;
			}
			case DOWN:
			{
				if(isActiveCameraBoatCamera())
					moveCamera(0, CAMERA_MOVING_SPEED,0);
				break;
			}
			case PAGE_UP:
			{
				if(isActiveCameraBoatCamera())
					moveCamera(0, 0, CAMERA_MOVING_SPEED);
				break;
			}
			case PAGE_DOWN:
			{
				if(isActiveCameraBoatCamera())
					moveCamera(0, 0, -CAMERA_MOVING_SPEED);
				break;
			}
			case SPACE: {
				++spaceBtnCnt;
				if(spaceBtnCnt % 2 != 0) //do it on press, not on release
				{
					if (this.isWeaponCameraOn)
						isWeaponCameraOn = false;
					else
						isWeaponCameraOn = true;

					if( isThereCannonBall && isWeaponCameraOn)
					{
						this.subScene.setCamera(cannonBallCamera);
						activeCamera = cannonBallCamera;
					}
					else
					{
						if(activeCamera == cannonBallCamera && !isWeaponCameraOn)
						{
							setCannonCamera();
						}
					}
				}
				else
				{
					spaceBtnCnt = 0;
				}
				break;
			}
			case T:
				++tBtnCnt;
				if(tBtnCnt % 2 != 0) //do it on press, not on release
				{
					if(isGameMapOn)
					{
						this.mainRoot.getChildren().remove(this.gameMap);
						isGameMapOn = false;
					}
					else
					{
						this.mainRoot.getChildren().addAll(this.gameMap);
						isGameMapOn = true;
					}
				}
				else
					tBtnCnt = 0;
				break;
		}

	}

}

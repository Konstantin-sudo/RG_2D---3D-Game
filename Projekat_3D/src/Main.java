import cameraController.CameraController;
import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Cylinder;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import objects.Cannon;
import objects.DistanceMap;
import objects.movable.Boat;
import objects.movable.MovableObject;
import subscenes.AmmunitionView;
import subscenes.GameMap;
import subscenes.InfoBar;
import timer.GameEventListener;
import timer.MyAnimationTimer;
import constants.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main extends Application implements GameEventListener {

	private MyAnimationTimer timer;
	
	public static void main ( String[] args ) {
		launch ( args );
	}
	private Group mainRoot = new Group ( );

	@Override public void onGameWon ( double width, double height) {
		Group gameOverRoot = new Group();
		double subSceneWidth  = width;
		double subSceneHeight = height;
		//LABEL
		Label gameWon = new Label("YOU WON!");
		gameWon.setTextFill(Color.GREEN);
		gameWon.getTransforms().addAll(
				new Translate(subSceneWidth / 4, subSceneHeight / 4),
				new Scale( subSceneWidth / 100, subSceneHeight / 100 )
		);
		gameOverRoot.getChildren().addAll(gameWon);
		//SUB SCENE
		SubScene gameOver = new SubScene(gameOverRoot, subSceneWidth, subSceneHeight);
		gameOver.setCursor(Cursor.NONE);
		//MAIN SCENE
		mainRoot.getChildren().addAll(gameOver);
	}
	
	@Override public void onGameLost ( double width, double height ) {
		Group gameOverRoot = new Group();
		double subSceneWidth  = width;
		double subSceneHeight = height;

		//LABEL
		Label gameLost = new Label("YOU LOST!");
		gameLost.setTextFill(Color.RED);
		gameLost.getTransforms().addAll(
				new Translate(subSceneWidth / 4, subSceneHeight / 4),
				new Scale( subSceneWidth / 100, subSceneHeight / 100 )
		);
		gameOverRoot.getChildren().addAll(gameLost);
		//SUB SCENE
		SubScene gameOver = new SubScene(gameOverRoot, subSceneWidth, subSceneHeight);
		gameOver.setCursor(Cursor.NONE);
		//MAIN SCENE
		mainRoot.getChildren().addAll(gameOver);
	}

	@Override
	public void start ( Stage primaryStage ) throws Exception {
		List<MovableObject> movableObjects = new ArrayList<> ( );
		Scene               scene          = new Scene ( mainRoot, Constants.SCENE_WIDTH, Constants.SCENE_HEIGHT, true, SceneAntialiasing.BALANCED );


		//GAME_MAP
		Group gameMapRoot = new Group();
		GameMap gameMap = new GameMap(
				gameMapRoot,
				Constants.GAME_MAP_SUB_SCENE_WIDTH,
				Constants.GAME_MAP_SUB_SCENE_HEIGHT
		);


		//AMMUNITION VIEW SUB SCENE
		Group ammunitionViewRoot = new Group();
		AmmunitionView ammunitionView = new AmmunitionView(
				ammunitionViewRoot,
				Constants.AMMUNITION_VIEW_SUB_SCENE_WIDTH,
				Constants.AMMUNITION_VIEW_SUB_SCENE_HEIGHT,
				true,
				SceneAntialiasing.BALANCED,
				Constants.CANNON_MAX_AMMUNITION,
				Constants.SCENE_WIDTH - Constants.AMMUNITION_VIEW_SUB_SCENE_WIDTH,
				Constants.SCENE_HEIGHT - Constants.AMMUNITION_VIEW_SUB_SCENE_HEIGHT
		);


		//INFO BAR
		Group infoBarRoot = new Group();
		InfoBar infoBar = new InfoBar(infoBarRoot,Constants.INFO_BAR_SUB_SCENE_WIDTH,Constants.INFO_BAR_SUB_SCENE_HEIGHT);


		//3D GAME SUB SCENE
		Group               root3D           = new Group ( );
		SubScene            subScene3D       = new SubScene ( root3D, Constants.SCENE_WIDTH, Constants.SCENE_HEIGHT, true, SceneAntialiasing.BALANCED );
		subScene3D.setFill ( Color.LIGHTBLUE );
		subScene3D.setCursor ( Cursor.NONE );

		CameraController cameraController = new CameraController (
				Constants.CAMERA_NEAR_CLIP,
				Constants.CAMERA_FAR_CLIP,
				subScene3D,
				mainRoot,
				gameMap
		);
		scene.addEventHandler ( KeyEvent.ANY, cameraController );
		Camera camera = cameraController.getFixedCamera ( );
		camera.getTransforms ( ).addAll (
				new Rotate ( Constants.CAMERA_X_ANGLE, Rotate.X_AXIS ),
				new Translate ( 0, 0, Constants.CAMERA_Z )
		);

		Box ocean = new Box ( Constants.OCEAN_WIDTH, Constants.OCEAN_HEIGHT, Constants.OCEAN_DEPTH );
		PhongMaterial oceanMaterial = new PhongMaterial ( Color.BLUE );
		ocean.setMaterial ( oceanMaterial );
		root3D.getChildren ( ).addAll ( ocean );

		Cylinder island = new Cylinder ( Constants.ISLAND_RADIUS, Constants.ISLAND_HEIGHT );
		island.setMaterial ( new PhongMaterial ( Color.BROWN ) );
		root3D.getChildren ( ).addAll ( island );

		DistanceMap distanceMap = new DistanceMap(Constants.DISTANCE_MAP_RADIUS, Constants.OCEAN_HEIGHT, Color.RED);
		root3D.getChildren().addAll(distanceMap);

		for ( int i = 0; i < Constants.NUMBER_OF_BOATS; ++i ) {
			//double angle = 360 * 1.0 / Constants.NUMBER_OF_BOATS * i;
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
					Constants.BOAT_SPEED,
					Constants.ISLAND_RADIUS,
					Constants.DELTA,
					cameraController,
					infoBar,
					gameMap
			);
			root3D.getChildren ( ).addAll ( boat );
			movableObjects.add ( boat );
			gameMap.addBoat(boat.getMyId(), angle);
		}
		
		this.timer = new MyAnimationTimer (primaryStage, movableObjects, this , cameraController, root3D, infoBar, gameMap);
		
		Cannon cannon = new Cannon (
				root3D,
				Constants.CANNON_WIDTH,
				Constants.CANNON_HEIGHT,
				Constants.CANNON_DEPTH,
				Constants.ISLAND_HEIGHT,
				Constants.CANNON_VENT_LENGTH,
				Color.GREEN,
				Constants.SCENE_WIDTH,
				Constants.SCENE_HEIGHT,
				Constants.Y_SPEED,
				Constants.GRAVITY,
				this.timer,
				cameraController.getCannonCamera(),
				cameraController.getCannonBallCamera(),
				ammunitionView,
				infoBar,
				gameMap
		);
		root3D.getChildren ( ).addAll ( cannon );
		scene.addEventHandler ( MouseEvent.ANY, cannon );
		this.timer.setCannon(cannon);


		//MAIN SCENE
		mainRoot.getChildren().addAll(
				subScene3D,
				gameMap,
				ammunitionView,
				infoBar
		);

		primaryStage.setScene ( scene );
		primaryStage.setTitle ( Constants.TITLE );
		primaryStage.setResizable(true);
		primaryStage.setMinWidth( Constants.SCENE_WIDTH );
		primaryStage.setMinHeight( Constants.SCENE_HEIGHT );
		primaryStage.widthProperty().addListener( ( obs, oldVal, newVal ) -> {
            double oldWidth = oldVal.doubleValue();
		    double newWidth = newVal.doubleValue();
		    subScene3D.setWidth( newWidth );
            if( oldWidth >= Constants.SCENE_WIDTH )
            {
                ammunitionView.getTransforms().addAll(
                        new Translate(newWidth - oldWidth,0,0)
                );
            }
        });
        primaryStage.heightProperty().addListener( ( obs, oldVal, newVal ) -> {
            double oldHeight = oldVal.doubleValue();
            double newHeight =  newVal.doubleValue();
            subScene3D.setHeight( newHeight );
            if(oldHeight >= Constants.SCENE_HEIGHT)
            {
                gameMap.getTransforms().addAll(
                        new Translate(0, newHeight - oldHeight,0)
                );
                ammunitionView.getTransforms().addAll(
                  new Translate(0,newHeight - oldHeight,0)
                );
            }
        });
		primaryStage.show ( );
		timer.start ( );
	}
}

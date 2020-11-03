package main;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import models.GameModel;
import objects.*;
import objects.bonuses.Bonus;
import objects.bonuses.Dollar;
import objects.weapons.Bullet;
import objects.weapons.Harpoon;

import java.sql.Time;
import java.util.Random;

import static models.GameModel.GAME_SCENE_MARGIN_X;
import static models.GameModel.GAME_SCENE_MARGIN_Y;
import static objects.Player.PLAYER_HEIGHT;
import static objects.Player.PLAYER_WIDTH;
import static objects.PlayersLife.LIVES;
import static objects.PlayersLife.NUMBER_OF_LIVES;
import static objects.weapons.Weapon.ACTIVE_WEAPON;

public class Main extends Application {
	
	private AnimationTimer timer;
	
	@Override
	public void start ( Stage primaryStage ) throws Exception {
		Group root = new Group ( );
		primaryStage.setTitle ( "Deoba mehura" );
		Scene scene = new Scene ( root, GameModel.getInstance ( ).getSceneWidth ( ), GameModel.getInstance ( ).getSceneHeight ( ) );
		primaryStage.setScene ( scene );
		
		// disable resizing and maximize button
		primaryStage.setResizable ( false );
		primaryStage.sizeToScene ( );

		Ball ball = new Ball ( new Point2D ( 100 + GAME_SCENE_MARGIN_X, 100 + GAME_SCENE_MARGIN_Y ) );
		GameModel.getInstance ( ).getBalls ( ).add ( ball );
		
		Player player = new Player ( new Point2D ( 100 + GAME_SCENE_MARGIN_X, GameModel.getInstance ( ).getSceneHeight ( ) - GAME_SCENE_MARGIN_Y - PLAYER_HEIGHT ) );
		GameModel.getInstance ( ).setPlayer ( player );

       	GameTimer gametimer = new GameTimer( new Point2D(
       				GameModel.getInstance().getSceneWidth() / 2,
					GameModel.getInstance().getSceneHeight() - 3*GAME_SCENE_MARGIN_Y/4
			)
		);
       	GameModel.getInstance().setGameTimer(gametimer);

		Score score = new Score( new Point2D(scene.getWidth() - 150, 25) );
		GameModel.getInstance().setScore( score );

		root.getChildren ( ).addAll ( new Background ( ), ball, player, gametimer, score);

		for(int i = 0; i < NUMBER_OF_LIVES; i++){
			PlayersLife life = new PlayersLife(new Point2D(GAME_SCENE_MARGIN_X / 2 + i * 40, GAME_SCENE_MARGIN_Y / 2 + 10));
			GameModel.getInstance().getLives().add(life);
			LIVES[i] = life;
			root.getChildren().add(life);
		}
		GameModel.getInstance ( ).setRoot ( root );

		scene.setOnKeyPressed ( event -> {
			if ( event.getCode ( ) == KeyCode.SPACE ) {
				switch (ACTIVE_WEAPON){
                    case BULLET:
                        Bullet bullet = new Bullet ( player.getPosition ( ) .add ( 0.5 * PLAYER_WIDTH, 0 ) );
                        root.getChildren ( ).remove ( GameModel.getInstance ( ).getWeapon ( ) );
                        GameModel.getInstance ( ).setWeapon ( bullet );
                        root.getChildren ( ).addAll ( bullet );
                        break;
                    default: //case Harpoon
                        Harpoon harpoon = new Harpoon( player.getPosition ( ) .add ( 0.5 * PLAYER_WIDTH, 0 ) );
                        root.getChildren().remove(GameModel.getInstance().getWeapon());
                        GameModel.getInstance().setWeapon(harpoon);
                        root.getChildren().add( harpoon );
                }
			}
		} );
		primaryStage.show ( );

		//random create fake balls
		Timeline fake_ball_generator = new Timeline();
		fake_ball_generator.setCycleCount(Timeline.INDEFINITE);
		Random rand = new Random();
		int repeat = rand.nextInt(11) + 5; // 5 <= repeat <= 15 seconds
		fake_ball_generator.getKeyFrames().add(
				new KeyFrame(Duration.seconds(repeat),
						new EventHandler() {
							// KeyFrame event handler
							@Override
							public void handle(Event event) {
								double x_random = GAME_SCENE_MARGIN_X + Ball.BALL_DIAMETER + rand.nextInt( Math.round(GameModel.getInstance().getSceneWidth() - GAME_SCENE_MARGIN_X * 2  - (int)Ball.BALL_DIAMETER * 2) );
								Ball fake_ball = new Ball(new Point2D ( x_random, 100 + GAME_SCENE_MARGIN_Y ),true);
								GameModel.getInstance().getBalls().add(fake_ball);
								GameModel.getInstance().getRoot().getChildren().add(fake_ball);
							}
						}
				)
		);
		fake_ball_generator.playFromStart();

		timer = new AnimationTimer ( ) {
			@Override
			public void handle ( long l ) {
				for ( Ball ball : GameModel.getInstance ( ).getBalls ( ) ) {
					ball.updatePosition ( );
				}

				for ( Bonus bonus : GameModel.getInstance ( ).getBonuses() ) {
					bonus.updatePosition ( );
					/*GameModel.getInstance().getRoot().getChildren().remove( );
					GameModel.getInstance().getRoot().getChildren().add(this.dollar_bill);*/
				}

				GameModel.getInstance ( ).getPlayer ( ).updatePosition ( );
				if ( GameModel.getInstance ( ).getWeapon ( ) != null ) {
					GameModel.getInstance ( ).getWeapon ( ).updatePosition ( );
				}

				if ( GameModel.getInstance ( ).isGameLost ( )  ) {
					Text textLost = new Text(GameModel.getInstance().getSceneWidth()/2,GameModel.getInstance().getSceneHeight()/2,"You Lost!");
					textLost.setFill(Color.RED);
					textLost.setScaleX(10);
					textLost.setScaleY(10);
					root.getChildren().add(textLost);
					fake_ball_generator.stop();
					gametimer.end();
					GameModel.getInstance().getPlayer().endAnimations();
					timer.stop ( );
				}
				if( GameModel.getInstance ().isGameWon () ){
					Text textWon = new Text(GameModel.getInstance().getSceneWidth()/2,GameModel.getInstance().getSceneHeight()/2,"You Won!");
					textWon.setFill(Color.GREEN);
					textWon.setScaleX(10);
					textWon.setScaleY(10);
					root.getChildren().add(textWon);
					fake_ball_generator.stop();
					gametimer.end();
					GameModel.getInstance().getPlayer().endAnimations();
					timer.stop ( );
				}
				
			}
		};

		timer.start ( );
	}
	
	public static void main ( String[] args ) {
		launch ( args );
	}
}

package objects;

import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.shape.*;
import javafx.util.Duration;
import models.GameModel;
import playerStates.MovingLeftState;
import playerStates.MovingRightState;
import playerStates.StandingState;
import playerStates.State;

import static models.GameModel.GAME_SCENE_MARGIN_X;
import static models.GameModel.GAME_SCENE_UNIVERSAL_SCALE_FACTOR;


public class Player extends MovingGameObject {
	//private GameModel model = GameModel.getInstance();
	private             State state         = new StandingState ( this );
	public static final float PLAYER_WIDTH  = ( float ) ( GameModel.getInstance ( ).getScreenSize ( ).width * 0.01 );
	public static final float PLAYER_HEIGHT =  PLAYER_WIDTH / 2;
	public static final float PLAYER_SPEED  = 10;
	private static final int RESPAWNING_DURATION = 3; // in seconds
	private boolean is_vulnerable = true;


	private FadeTransition respawn_effect;
	//shield
	private boolean is_active_shield = false;
	private Timeline active_shield_timer;
	private Timeline disabling_shield_timer;
	FadeTransition disabling_effect;
	private static final int ACTIVE_SHIELD_DURATION = 10; //in seconds
	private static final int DISABLING_SHIELD_DURATION = 5; //in seconds
	private int active_shield_time_left = ACTIVE_SHIELD_DURATION;

	{
		// this is necessary in order for this class to respond to key events
		this.setFocusTraversable ( true );
	}
	
	{
		speedX = PLAYER_SPEED;
		speedY = 0;
		/*
		Rectangle player = new Rectangle ( PLAYER_WIDTH, PLAYER_HEIGHT );
		player.setFill ( Color.LIGHTBLUE );
		this.getChildren ( ).addAll ( player );
		*/

		Group player = new Group();

		//COMPLETE HEAD
		Group complete_head = new Group();

		//head
		Circle head = new Circle(0,0,20);
		head.setFill(Color.BURLYWOOD);
		head.setStroke(Color.BLACK);
		head.setStrokeWidth(2);
		complete_head.getChildren().add(head);

		//eyes
		Polygon lefteye = new Polygon(-10,-5, 10,-5, 0,5);
		Polygon righteye = new Polygon(-10,-5, 10,-5, 0,5);
		lefteye.setTranslateX(-10);
		lefteye.setTranslateY(-4);
		righteye.setTranslateX(10);
		righteye.setTranslateY(-4);
		lefteye.setScaleX(0.9);
		lefteye.setScaleY(0.9);
		righteye.setScaleX(0.9);
		righteye.setScaleY(0.9);
		lefteye.setFill(Color.BLACK );
		righteye.setFill(Color.BLACK);
		complete_head.getChildren ( ).addAll ( lefteye, righteye);

		//mouth
		Arc mouth = new Arc( 0,0,10,7,0,-180);
		mouth.setTranslateY(5);
		mouth.setFill(Color.DARKRED);
		complete_head.getChildren().add(mouth);

		//hat
		Group hat = new Group();
		Rectangle hat_part1 = new Rectangle(0,0,55,5);
		hat_part1.setFill(Color.BLACK);
		hat_part1.setTranslateX(-55/2);
		hat_part1.setTranslateY(-20);
		Rectangle hat_part2 = new Rectangle(0,0,30,20);
		hat_part2.setFill(Color.BLACK);
		hat_part2.setTranslateX(-15);
		hat_part2.setTranslateY(-39);
		hat.setTranslateY(2);
		hat.getChildren().addAll(hat_part1, hat_part2);
		complete_head.getChildren().add(hat);

		complete_head.setTranslateY(-40);

		//BODY
		Path body = new Path();

		MoveTo moveTo = new MoveTo(-25,11);
		LineTo lineTo1 = new LineTo(-15+10,-5-10*Math.sqrt(3));
		LineTo lineTo2 = new LineTo(20+12.5*Math.sqrt(3),-10+12.5);
		CubicCurveTo cubicCurveTo = new CubicCurveTo();
		cubicCurveTo.setX(-25);
		cubicCurveTo.setY(11);
		cubicCurveTo.setControlX2(0);
		cubicCurveTo.setControlY2(-15);
		cubicCurveTo.setControlX1(15);
		cubicCurveTo.setControlY1(10);
		body.getElements().addAll(moveTo,lineTo1,lineTo2,cubicCurveTo);

		body.setScaleY(1.3);
		body.setTranslateY(5);
		body.setRotate(10);
		body.setFill(Color.RED);

		//assemble player
		player.getChildren().addAll(body,complete_head);
		player.setScaleX(GAME_SCENE_UNIVERSAL_SCALE_FACTOR);
		player.setScaleY(GAME_SCENE_UNIVERSAL_SCALE_FACTOR);
		player.setTranslateY(5);
		this.getChildren ( ).addAll ( player );

		this.addEventFilter ( KeyEvent.KEY_PRESSED, event -> {
			switch ( event.getCode ( ) ) {
				case RIGHT:
					state = new MovingRightState ( GameModel.getInstance ( ).getPlayer ( ) );
					break;
				case LEFT:
					state = new MovingLeftState ( GameModel.getInstance ( ).getPlayer ( ) );
					break;
			}
		} );
		
		this.addEventFilter (
				KeyEvent.KEY_RELEASED, event -> {
					if ( event.getCode ( ) == KeyCode.LEFT || event.getCode ( ) == KeyCode.RIGHT ) {
						state = new StandingState ( GameModel.getInstance ( ).getPlayer ( ) );
					}
				}
		);
	}
	
	public Player ( Point2D position ) {
		super ( position );
	}
	
	@Override
	protected void handleCollisions ( ) {
		if ( position.getX ( ) < GAME_SCENE_MARGIN_X || position.getX ( ) > GameModel.getInstance ( ).getSceneWidth ( ) - GAME_SCENE_MARGIN_X - PLAYER_WIDTH ) {
			state = new StandingState ( this );
			if ( position.getX ( ) < GAME_SCENE_MARGIN_X ) {
				setPosition ( new Point2D ( GAME_SCENE_MARGIN_X, getPosition ( ).getY ( ) ) );
				setTranslateX ( GAME_SCENE_MARGIN_X );
			}
			
			if ( position.getX ( ) > GameModel.getInstance ( ).getSceneWidth ( ) - GAME_SCENE_MARGIN_X - PLAYER_WIDTH ) {
				setPosition ( new Point2D ( GameModel.getInstance ( ).getSceneWidth ( ) - GAME_SCENE_MARGIN_X - PLAYER_WIDTH, getPosition ( ).getY ( ) ) );
				setTranslateX ( GameModel.getInstance ( ).getSceneWidth ( ) - GAME_SCENE_MARGIN_X - PLAYER_WIDTH );
			}
		}
	}
	
	@Override
	public void updatePosition ( ) {
		state.update ( );
		handleCollisions ( );
	}

	public boolean getIsVulnerable(){
		return is_vulnerable;
	}

	public boolean getIsActiveShield(){
		return is_active_shield;
	}

	public void respawn(){
		is_vulnerable = false;
		respawn_effect = new FadeTransition(Duration.seconds(RESPAWNING_DURATION),this);
		respawn_effect.setCycleCount(1);
		respawn_effect.setFromValue(0.1);
		respawn_effect.setToValue(1);
		respawn_effect.setOnFinished(e->{
			if( !is_active_shield ) is_vulnerable = true;
		});
		respawn_effect.play();
	}

	public void activateShield(){
		if(respawn_effect != null){
			if(respawn_effect.getStatus() == Animation.Status.RUNNING) respawn_effect.stop();
			respawn_effect = null;
		}
		if(active_shield_timer != null) {
			if(active_shield_timer.getStatus() == Animation.Status.RUNNING) active_shield_timer.stop();
		}
		if(disabling_shield_timer != null){
			if(disabling_shield_timer.getStatus() == Animation.Status.RUNNING) disabling_shield_timer.stop();
		}
		if(disabling_effect != null){
			if(disabling_effect.getStatus() == Animation.Status.RUNNING) disabling_effect.stop();
		}
		//this.setOpacity(1);//ovo ne radi?
		FadeTransition instant_transparency = new FadeTransition(Duration.millis(1),this);
		instant_transparency.setFromValue(1);
		instant_transparency.setToValue(0.5);
		instant_transparency.setCycleCount(1);
		instant_transparency.play();
		is_vulnerable = false;
		is_active_shield = true;
		active_shield_time_left = ACTIVE_SHIELD_DURATION;
		active_shield_timer = new Timeline();
		active_shield_timer.setCycleCount(Timeline.INDEFINITE);
		active_shield_timer.getKeyFrames().add(
				new KeyFrame(Duration.seconds(1),
						new EventHandler() {
							// KeyFrame event handler
							@Override
							public void handle(Event event) {
								active_shield_time_left--;
								if (active_shield_time_left <= 0) {
									active_shield_timer.stop();
									disableShield();
								}
							}
						}));
		active_shield_timer.playFromStart();
	}

	public void disableShield(){
		if(disabling_shield_timer != null){
			if(disabling_shield_timer.getStatus() == Animation.Status.RUNNING) return;
		}
		if(disabling_effect != null){
			if(disabling_effect.getStatus() == Animation.Status.RUNNING) return;
		}

		if(active_shield_timer != null) active_shield_timer.stop();
		disabling_effect = new FadeTransition(Duration.seconds(0.2),this);
		disabling_effect.setCycleCount(Timeline.INDEFINITE);
		disabling_effect.setFromValue(0);
		disabling_effect.setToValue(1);
		disabling_effect.playFromStart();

		disabling_shield_timer = new Timeline();
		disabling_shield_timer.setCycleCount(1);
		disabling_shield_timer.getKeyFrames().add(new KeyFrame(Duration.seconds(DISABLING_SHIELD_DURATION)));
		disabling_shield_timer.setOnFinished(e->{
			//GameModel.getInstance().getPlayer().setOpacity(100); ne radi?
			FadeTransition instant_transparency = new FadeTransition(Duration.millis(1),this);
			instant_transparency.setFromValue(0.5);
			instant_transparency.setToValue(1);
			instant_transparency.setCycleCount(1);
			instant_transparency.play();
			is_vulnerable = true;
			is_active_shield = false;
			disabling_effect.stop();
			disabling_shield_timer.stop();
		});
		disabling_shield_timer.playFromStart();
	}

	public void endAnimations(){
		if(is_active_shield){
			if ( active_shield_timer != null ) active_shield_timer.stop();
			if ( disabling_shield_timer != null ) disabling_shield_timer.stop();
			if ( disabling_effect != null ) disabling_effect.stop();
		}
	}
}

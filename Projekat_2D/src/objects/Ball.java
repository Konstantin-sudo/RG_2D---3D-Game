package objects;


import javafx.animation.*;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.util.Duration;
import models.GameModel;
import objects.bonuses.Bonus;

import static objects.PlayersLife.LIVES;
import static objects.PlayersLife.NUMBER_OF_LIVES;

import java.util.Random;

import static models.GameModel.*;
//import static objects.bonuses.Bonus.NUMBER_OF_BONUS_TYPES;


public class Ball extends MovingGameObject {
	private static final double ComboSpeedScreenScale = 10.5f / 720;
	private static final double GravityScreenScale = ( 0.22 * (1 / GAME_SCENE_UNIVERSAL_SCALE_FACTOR) ) / 720; // 0.17 /0.33 /0.25
	private static final double GravityFactorScreenScale = 0.05 / 720;

	//private GameModel model = GameModel.getInstance();
	private float ballSpeedX = 7.5f;
	private float ballSpeedY = ballSpeedX * 1.4f;
	
	public static final double BALL_DIAMETER = ( float ) ( GameModel.getInstance ( ).getScreenSize ( ).width * 0.025  * GAME_SCENE_UNIVERSAL_SCALE_FACTOR) ;
	private static final double BALL_DIAMETER_FACTOR = 0.8;

	private static final float FAKE_BALLS_TRANSPARENCY = 0.5f; // 50 %
	private boolean isFake = false;
	private boolean displayComboTxt = false;
	private boolean combo1 = false;
	private boolean combo2 = false;
	private static final float COMBO_SPEED = (float) ( - ComboSpeedScreenScale * GameModel.getInstance().getSceneHeight() ) ;
	private static int BALL_LEFT_CNT = 15;
	private enum BALL_SIZE_TYPES { SMALL,MEDIUM,LARGE,EXTRA_LARGE}
	private BALL_SIZE_TYPES size = BALL_SIZE_TYPES.EXTRA_LARGE;
	private double GRAVITY = GravityScreenScale * GameModel.getInstance().getSceneHeight();
	private static final double GRAVITY_FACTOR = GravityFactorScreenScale*GameModel.getInstance().getSceneHeight(); //0.04
	private Circle ball;

	// initialize ball
	{
		ball = new Circle ( BALL_DIAMETER );
		ball.setFill ( Color.RED );
		this.getChildren ( ).addAll ( ball );
	}

	// initialize speed
	{
		super.speedX = ballSpeedX;
		super.speedY = ballSpeedY;
	}

	public Ball ( Point2D position ) { super ( position ); }

	public Ball(Point2D position, boolean fake) {
		super(position);
		this.isFake = fake;
		if(fake) {
			Random rand= new Random();
			ball.setFill(Color.rgb(
					rand.nextInt(256),
					rand.nextInt(256),
					rand.nextInt(256)
			));
			//ball.setOpacity(FAKE_BALLS_TRANSPARENCY); ne radi?
			FadeTransition instant_transparency = new FadeTransition(Duration.millis(1),this);
			instant_transparency.setFromValue(1);
			instant_transparency.setToValue(FAKE_BALLS_TRANSPARENCY);
			instant_transparency.setCycleCount(1);
			instant_transparency.play();
		}
	}
	
	@Override
	public void updatePosition ( ) {
		handleCollisions ( );
		double x = position.getX() + speedX;
		double y = position.getY() + speedY;
		/*//if error
		if(x < GAME_SCENE_MARGIN_X) {
			x = GAME_SCENE_MARGIN_X + 1 + this.ball.getRadius();
		}
		if( x > GameModel.getInstance().getSceneWidth() - GAME_SCENE_MARGIN_X){
			x = GameModel.getInstance().getSceneWidth() - GAME_SCENE_MARGIN_X - this.ball.getRadius();
		}
		if ( y > GameModel.getInstance().getSceneHeight() - GAME_SCENE_MARGIN_Y ){
			y = GameModel.getInstance().getSceneHeight() - GAME_SCENE_MARGIN_Y - this.ball.getRadius();
		}
		if( y < GAME_SCENE_MARGIN_Y){
			y = GAME_SCENE_MARGIN_Y + this.ball.getRadius();
		}
		//end if_error*/
		position = new Point2D ( x, y );
		setTranslateX ( x );
		setTranslateY ( y );
		
		if ( speedY < 0 ) {
			speedY += GRAVITY;  // slowdown speed
		} else {
			speedY = ballSpeedY; // falling speed
		}
	}
	
	@Override
	protected void handleCollisions ( ) {
		handleBorderCollisions ( );
		handlePlayerCollisions ( );
		handleBulletCollisions ( );
	}
	
	private void handlePlayerCollisions ( ) {
		if ( this.getBoundsInParent ( ).intersects ( GameModel.getInstance ( ).getPlayer ( ).getBoundsInParent ( ) ) ) {
			if(isFake){
				GameModel.getInstance ( ).getRoot ( ).getChildren ( ).remove ( this );
				GameModel.getInstance().getBalls().remove(this);
			}else{
				if(GameModel.getInstance().getPlayer().getIsVulnerable()){
					NUMBER_OF_LIVES--;
					if(NUMBER_OF_LIVES >= 0) GameModel.getInstance().getRoot().getChildren().remove(LIVES[NUMBER_OF_LIVES]); // if(NUMBER_OF_LIVES >= 0)  if-for imortality
					if(NUMBER_OF_LIVES >= 0) GameModel.getInstance().getLives().remove(LIVES[NUMBER_OF_LIVES]);
					if(NUMBER_OF_LIVES == 0){
						GameModel.getInstance ( ).setGameLost ( true );
					}else{
						 GameModel.getInstance().getPlayer().respawn();
					}
				}else{
					if(GameModel.getInstance().getPlayer().getIsActiveShield()){
						GameModel.getInstance().getPlayer().disableShield();
					}
				}
			}
		}
	}

	private void handleBorderCollisions ( ) {
		if (	this.position.getX ( ) - this.ball.getRadius() <= GAME_SCENE_MARGIN_X ||
				this.position.getX ( ) + this.ball.getRadius() >= GameModel.getInstance ( ).getSceneWidth ( ) - GAME_SCENE_MARGIN_X
		) {
			speedX = -speedX;
		}

		if ( this.position.getY ( ) + this.ball.getRadius() >= GameModel.getInstance ( ).getSceneHeight ( ) - GAME_SCENE_MARGIN_Y  ) {
			speedY = -speedY;
			combo1 = false;
			combo2 = false;
			//speedY = Math.signum(speedY) * GameModel.getInstance().ballSpeedY;
		}

		if( position.getY ( ) - this.ball.getRadius() <= GAME_SCENE_MARGIN_Y ){
			//remove this ball
			GameModel.getInstance ( ).getRoot ( ).getChildren ( ).remove ( this );
			GameModel.getInstance().getBalls().remove(this);
			GameModel.getInstance().getScore().addScore(10); // 2x5=10 points - 5 per ball // ?
			if(!isFake) {
				//reduce ball cnt if ball is'nt fake
				BALL_LEFT_CNT--; //this ball
				int kids = 0;    //and all his children
				switch (this.size) {
					case EXTRA_LARGE:
						kids = 14;
						break;
					case LARGE:
						kids = 6;
						break;
					case MEDIUM:
						kids = 2;
						break;
					default:
						kids = 0;
				}
				BALL_LEFT_CNT -= kids;
			}
			if(combo2 == true){
				//update points as combo hit
				GameModel.getInstance().getScore().addScore(50); // 2x50=100 points - 50 per ball
				//display combo! txt
				if(this.displayComboTxt) {
					Text combo = new Text(this.position.getX(), 25, "Combo!");
					combo.setFill(Color.RED);
					combo.setScaleX(5);
					combo.setScaleY(5);
					FadeTransition fade = new FadeTransition(Duration.seconds(3), combo);
					fade.setFromValue(1);
					fade.setToValue(0);
					fade.play();
					GameModel.getInstance().getRoot().getChildren().addAll(combo);
				}
			}else{
				//update points as border hit
				//GameModel.getInstance().getScore().addScore(10); // 2x10=20 points - 10 per ball // ?
				//don't display combo txt
			}

			if( BALL_LEFT_CNT == 0){
				GameModel.getInstance ( ).setGameWon ( true );
			}

		}
	}

	private void handleBulletCollisions ( ) {
		if ( GameModel.getInstance ( ).getWeapon ( ) == null ) {
			return;
		}
		if ( this.getBoundsInParent ( ) .intersects ( GameModel.getInstance ( ).getWeapon ( ).getBoundsInParent ( ) ) ) {
			//remove this ball and weapon
			GameModel.getInstance().getRoot().getChildren().remove(GameModel.getInstance().getWeapon());
			GameModel.getInstance().setWeapon(null);
			GameModel.getInstance ( ).getRoot ( ).getChildren ( ).remove ( this );
			GameModel.getInstance().getBalls().remove(this);

			if(!isFake) BALL_LEFT_CNT--;
			if(BALL_LEFT_CNT == 0){
				GameModel.getInstance().getScore().addScore(10);
				GameModel.getInstance ( ).setGameWon ( true );
			}else {
				//maybe generate bonus
				Bonus.generateBonus(this.position);

				if(this.size != BALL_SIZE_TYPES.SMALL) { // if it's not the smallest ball than break it in two smaller balls
					//update score
					GameModel.getInstance().getScore().addScore(5);
					toTwoSmallerBalls();
				}else{ // if it is the smallest ball that is not the last one then just add score and remove it from scene
					GameModel.getInstance().getScore().addScore(10);
				}
			}

		}
	}

	private void toTwoSmallerBalls(){
		//new balls
		Ball ball1 = new Ball(this.getPosition(), this.isFake);
		Ball ball2 = new Ball(this.getPosition(), this.isFake);

		//new sizes
		ball1.ball.setRadius(this.ball.getRadius() * BALL_DIAMETER_FACTOR);
		ball2.ball.setRadius(this.ball.getRadius() * BALL_DIAMETER_FACTOR);
		BALL_SIZE_TYPES new_size;
		switch (this.size){
			case EXTRA_LARGE:
				new_size = BALL_SIZE_TYPES.LARGE;
				break;
			case LARGE:
				new_size = BALL_SIZE_TYPES.MEDIUM;
				break;
			default: //case MEDIUM:
				new_size = BALL_SIZE_TYPES.SMALL;
		}
		ball1.size = new_size;
		ball2.size = new_size;

		//set speed
		ball1.speedX = this.speedX;
		ball1.speedY = COMBO_SPEED;
		ball2.speedX = -this.speedX;
		ball2.speedY = COMBO_SPEED;

		//new max height
		ball1.GRAVITY = this.GRAVITY - GRAVITY_FACTOR;
		ball2.GRAVITY = this.GRAVITY - GRAVITY_FACTOR;

		//new color
		Random r = new Random();
		Color newColor = Color.rgb(
				r.nextInt(256),
				r.nextInt(256),
				r.nextInt(256)
		);
		ball1.ball.setFill(newColor);
		ball2.ball.setFill(newColor);

		//set combo
		ball1.displayComboTxt = true;
		ball2.displayComboTxt = false;
		if(this.combo1==true){
			ball1.combo1=true;
			ball1.combo2=true;
			ball2.combo1=true;
			ball2.combo2=true;
		}else{
			ball1.combo1=true;
			ball1.combo2=false; // should be false;
			ball2.combo1=true;
			ball2.combo2=false; // should be false;
		}

		//add new balls
		GameModel.getInstance().getBalls().add(ball1);
		GameModel.getInstance().getBalls().add(ball2);
		GameModel.getInstance().getRoot().getChildren().add(ball1);
		GameModel.getInstance().getRoot().getChildren().add(ball2);
	}
}

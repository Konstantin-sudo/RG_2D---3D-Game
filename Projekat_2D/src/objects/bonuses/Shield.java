package objects.bonuses;

import javafx.animation.FadeTransition;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.util.Duration;
import models.GameModel;

import static models.GameModel.GAME_SCENE_MARGIN_Y;
import static models.GameModel.GAME_SCENE_UNIVERSAL_SCALE_FACTOR;

public class Shield extends Bonus {
    private static final double SHIELD_TRANSPARENCY = 0.5; // 50 %

    {
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

        //body_parts
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
        //player.setOpacity(SHIELD_TRANSPARENCY); ne radi?
        FadeTransition instant_transparency = new FadeTransition(Duration.millis(1),this);
        instant_transparency.setFromValue(1);
        instant_transparency.setToValue(SHIELD_TRANSPARENCY);
        instant_transparency.setCycleCount(1);
        instant_transparency.play();

        this.getChildren ( ).addAll ( player );
    }

    public Shield( Point2D position ) { super(position); }

    @Override
    protected void handleCollisions() {
        // if the shield gets out of the window, remove it from the scene
        if ( position.getY ( ) >= GameModel.getInstance().getSceneHeight() - GAME_SCENE_MARGIN_Y ) {
            GameModel.getInstance ( ).getBonuses().remove(this);
            GameModel.getInstance ( ).getRoot ( ).getChildren ( ).remove ( this );
        }
        //if it hits player
        if(this.getBoundsInParent ( ) .intersects ( GameModel.getInstance ( ).getPlayer().getBoundsInParent ( ) )){
            GameModel.getInstance ( ).getBonuses().remove(this);
            GameModel.getInstance ( ).getRoot ( ).getChildren ( ).remove ( this );

            GameModel.getInstance().getPlayer().activateShield();
        }
    }
}

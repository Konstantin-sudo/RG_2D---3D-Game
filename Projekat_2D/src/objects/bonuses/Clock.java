package objects.bonuses;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import models.GameModel;
import objects.Background;

import java.awt.*;

import static models.GameModel.GAME_SCENE_MARGIN_Y;

public class Clock extends Bonus {
    private static final double CLOCK_SIZE_X = 20;
    private static final double CLOCK_SIZE_Y = 20;
    public static final int BONUS_TIME = 10; // in seconds

    {
        //background
        Rectangle background = new Rectangle(0,0, CLOCK_SIZE_X, CLOCK_SIZE_Y);
        Stop[] stops = new Stop[]{
                new Stop(0, Color.YELLOW),
                new Stop(1, Color.BLACK),
        };
        LinearGradient background_color = new LinearGradient(0,0,1,1, true, CycleMethod.NO_CYCLE , stops);
        background.setFill(background_color);
        background.setTranslateX(-CLOCK_SIZE_X/2);
        background.setTranslateY(-CLOCK_SIZE_Y/2);

        //clock
        Circle circle1 = new Circle(0,0,CLOCK_SIZE_X/2 - 1);
        circle1.setFill(Color.RED);
        Circle circle2 = new Circle(0,0,CLOCK_SIZE_X/2 - 3);
        circle2.setFill(Color.WHITE);

        Line hour_hand = new Line(0,0,0,-CLOCK_SIZE_Y/2 + 4);
        hour_hand.setStrokeWidth(2);
        Line minute_hand = new Line(0,0,CLOCK_SIZE_Y/2 - 5,0);
        hour_hand.setStrokeWidth(2);

        this.getChildren().addAll(background,circle1,circle2,hour_hand,minute_hand);
    }

    public Clock(Point2D position) { super(position); }

    @Override
    protected void handleCollisions() {
        // if the clock gets out of the window, remove it from the scene
        if ( position.getY ( ) >= GameModel.getInstance().getSceneHeight() - GAME_SCENE_MARGIN_Y - CLOCK_SIZE_Y/2 ) {
            GameModel.getInstance ( ).getBonuses().remove(this);
            GameModel.getInstance ( ).getRoot ( ).getChildren ( ).remove ( this );
        }
        //if it hits player
        if(this.getBoundsInParent ( ) .intersects ( GameModel.getInstance ( ).getPlayer().getBoundsInParent ( ) )){
            GameModel.getInstance ( ).getBonuses().remove(this);
            GameModel.getInstance ( ).getRoot ( ).getChildren ( ).remove ( this );
            GameModel.getInstance().getGameTimer().addTime(BONUS_TIME);
            GameModel.getInstance().getGameTimer().updateTimeBarAnimation();
        }
    }
}

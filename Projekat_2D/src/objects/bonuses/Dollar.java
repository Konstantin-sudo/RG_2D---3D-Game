package objects.bonuses;

import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import models.GameModel;

import static models.GameModel.GAME_SCENE_MARGIN_Y;
import static models.GameModel.GAME_SCENE_UNIVERSAL_SCALE_FACTOR;


public class Dollar extends Bonus {
    private Label dollar_bill;
    private static final float DOLLAR_SCALE_FACTOR = 2 * GAME_SCENE_UNIVERSAL_SCALE_FACTOR;

    public Dollar( Point2D position ) {
        super(position);

        dollar_bill = new Label();
        dollar_bill.setText("$");

        dollar_bill.setTextFill(Color.BLACK);
        dollar_bill.setBackground(new Background(new BackgroundFill(Color.GREEN, CornerRadii.EMPTY, Insets.EMPTY)));

        dollar_bill.setScaleX(DOLLAR_SCALE_FACTOR);
        dollar_bill.setScaleY(DOLLAR_SCALE_FACTOR);

        this.getChildren ( ).addAll ( dollar_bill );

        FadeTransition fade = new FadeTransition(Duration.seconds(3), dollar_bill);
        fade.setFromValue(1);
        fade.setToValue(0);
        fade.setOnFinished(event->{
            //System.out.println("EVENT ON FINISHED");
            GameModel.getInstance ( ).getBonuses().remove(this);
            GameModel.getInstance ( ).getRoot ( ).getChildren ( ).remove ( this );
        });
        fade.play();
    }

    @Override
    protected void handleCollisions ( ) {
        // if the dollar get's out of the window, remove it from the scene
        if ( position.getY ( ) >= GameModel.getInstance().getSceneHeight() - GAME_SCENE_MARGIN_Y - dollar_bill.getHeight() ) {
            GameModel.getInstance ( ).getBonuses().remove(this);
            GameModel.getInstance ( ).getRoot ( ).getChildren ( ).remove ( this );
        }
        //if it hits player
        if(this.getBoundsInParent ( ) .intersects ( GameModel.getInstance ( ).getPlayer().getBoundsInParent ( ) )){
            GameModel.getInstance ( ).getBonuses().remove(this);
            GameModel.getInstance ( ).getRoot ( ).getChildren ( ).remove ( this );
            GameModel.getInstance().getScore().doubleScore();
        }
    }


}

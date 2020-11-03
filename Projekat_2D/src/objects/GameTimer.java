package objects;

import javafx.animation.*;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import models.GameModel;

import static models.GameModel.*;
import static objects.bonuses.Clock.BONUS_TIME;

public class GameTimer extends GameObject {
    private static final Integer START_TIME = 60;
    private Integer timeSeconds = START_TIME;
    private static final double TIME_BAR_START_WIDTH = GameModel.getInstance ( ).getSceneWidth ( ) - GAME_SCENE_MARGIN_X * 2;

    private Rectangle time_bar;
    private ParallelTransition time_bar_animation;
    private Timeline timeline;
    private Label timerLabel = new Label();

    public GameTimer(Point2D position){
        super(position);

        //time label
        timerLabel.setText(timeSeconds.toString());
        timerLabel.setTextFill(Color.BLUE);
        timerLabel.setScaleX(2.5);
        timerLabel.setScaleY(2.5);
        timerLabel.setTranslateX(-5);
        timerLabel.setTranslateY(5);

        //time bar
        time_bar = new Rectangle(
                0,
                0,
                TIME_BAR_START_WIDTH + 0,
                GAME_SCENE_MARGIN_Y / 2
        );
        time_bar.setFill(Color.RED);
        time_bar.setTranslateX(-TIME_BAR_START_WIDTH /2);

        this.getChildren().addAll(time_bar, timerLabel);

        //time label animation
        timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.getKeyFrames().add(
                new KeyFrame(Duration.seconds(1),
                        new EventHandler() {
                            // KeyFrame event handler
                            @Override
                            public void handle(Event event) {
                                timeSeconds--;
                                // update timerLabel
                                timerLabel.setText(timeSeconds.toString());
                                if (timeSeconds <= 0) {
                                    timeline.stop();
                                    GameModel.getInstance().setGameLost(true);
                                }
                            }
                        }));
        timeline.playFromStart();

        //time bar animation
        TranslateTransition time_bar_translate_animation = new TranslateTransition(Duration.seconds(timeSeconds),time_bar);
        time_bar_translate_animation.setFromX(-TIME_BAR_START_WIDTH/2);
        time_bar_translate_animation.setToX(-TIME_BAR_START_WIDTH);

        ScaleTransition time_bar_scale_animation = new ScaleTransition(Duration.seconds(timeSeconds),time_bar);
        time_bar_scale_animation.setFromX(1);
        time_bar_scale_animation.setToX(0);
        time_bar_scale_animation.setFromY(1);
        time_bar_scale_animation.setToY(1);

        time_bar_animation = new ParallelTransition(time_bar_translate_animation,time_bar_scale_animation);
        time_bar_animation.play();

    }

    public void end() {
        if(timeline!=null) timeline.stop();
        if(time_bar_animation!=null)time_bar_animation.stop();
    }

    public void addTime(int time){
        timeSeconds += time;
    }

    public void updateTimeBarAnimation(){
        time_bar_animation.stop();

        this.getChildren().remove(timerLabel);
        this.getChildren().remove(time_bar);
        //time bar
        double bar_k = ((double)timeSeconds) / ((double)START_TIME);
        if(bar_k > 1 ) bar_k = 1;
        double new_width = bar_k * TIME_BAR_START_WIDTH;
        time_bar = new Rectangle(
                0,
                0,
                new_width + 0,
                GAME_SCENE_MARGIN_Y / 2
        );
        time_bar.setFill(Color.RED);
        time_bar.setTranslateX(-TIME_BAR_START_WIDTH /2);
        this.getChildren().addAll(time_bar, timerLabel);

        //animation
        TranslateTransition time_bar_translate_animation = new TranslateTransition(Duration.seconds(timeSeconds),time_bar);
        time_bar_translate_animation.setToX(-new_width - (TIME_BAR_START_WIDTH/2 - new_width/2 ));

        ScaleTransition time_bar_scale_animation = new ScaleTransition(Duration.seconds(timeSeconds),time_bar);
        time_bar_scale_animation.setFromX(1);
        time_bar_scale_animation.setToX(0);
        time_bar_scale_animation.setFromY(1);
        time_bar_scale_animation.setToY(1);

        time_bar_animation = new ParallelTransition(time_bar_translate_animation,time_bar_scale_animation);
        time_bar_animation.play();


    }

}

package objects;

import javafx.geometry.Point2D;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;

public class Score extends GameObject{
    private Integer score = 0;
    private Label scorelabel = new Label();

    public Score(Point2D position){
        super(position);

        scorelabel.setText("Score: " + score.toString());
        scorelabel.setTextFill(Color.RED);

        scorelabel.setScaleX(3);
        scorelabel.setScaleY(3);

        this.getChildren().add(scorelabel);
    }

    public void addScore(int add){
        score+=add;
        scorelabel.setText("Score: " + score.toString());
    }

    public void doubleScore(){
        score*=2;
        scorelabel.setText("Score: " + score.toString());
    }



}

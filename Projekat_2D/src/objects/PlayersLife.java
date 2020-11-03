package objects;

import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;

public class PlayersLife extends GameObject {
    public static int NUMBER_OF_LIVES = 5;
    public static PlayersLife[] LIVES = new PlayersLife[NUMBER_OF_LIVES];
    private static double HEAD_SCALE = 0.5;

    private Group onelife;
    {
        onelife = new Group();

        //head
        Circle head = new Circle(0,0,20);
        head.setFill(Color.BURLYWOOD);
        head.setStroke(Color.BLACK);
        head.setStrokeWidth(2);
        onelife.getChildren().add(head);

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
        onelife.getChildren ( ).addAll ( lefteye, righteye);

        //mouth
        Arc mouth = new Arc( 0,0,10,7,0,-180);
        mouth.setTranslateY(5);
        mouth.setFill(Color.DARKRED);
        onelife.getChildren().add(mouth);

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
        onelife.getChildren().add(hat);

        onelife.setScaleX(HEAD_SCALE);
        onelife.setScaleY(HEAD_SCALE);
        this.getChildren().add(onelife);

    }

    public PlayersLife(Point2D position){
        super(position);
    }

}

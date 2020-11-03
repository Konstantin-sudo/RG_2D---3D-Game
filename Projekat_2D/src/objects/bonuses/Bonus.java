package objects.bonuses;

import javafx.geometry.Point2D;
import models.GameModel;
import objects.MovingGameObject;

import java.util.Random;

import static javafx.scene.input.KeyCode.DOLLAR;

public abstract class Bonus extends MovingGameObject {
    public static final float  BONUS_SPEED = 2;
    //public static final int NUMBER_OF_BONUS_TYPES = 3;

    {
        super.speedX = 0;
        super.speedY = BONUS_SPEED;
    }

    public Bonus ( Point2D position ) {
        super ( position );
    }

    public Bonus ( ) { super(); }

    @Override
    public void updatePosition ( ) {
        position = new Point2D ( position.getX ( ) + speedX, position.getY ( ) + speedY );
        //setTranslateX(getTranslateX() + speedX);
        setTranslateY ( getTranslateY ( ) + speedY );

        handleCollisions ( );
    }

    public static void generateBonus(Point2D position){
        //create dollar with 1/6 chance
        Random rand = new Random();
        int chance = rand.nextInt(6);
        //System.out.println("CHANCE: "+ res);
        if(chance == 1){
            Bonus bonus;
            int type = rand.nextInt(3); // 3 = number of bonus types
            switch (type){
                case 0:
                    bonus = new Dollar(position);
                    break;
                case 1:
                    bonus = new Shield(position);
                    break;
                default:
                    bonus = new Clock(position);
            }
            GameModel.getInstance().getBonuses().add(bonus);
            GameModel.getInstance().getRoot().getChildren().add(bonus);

        }
    }



}

package objects.weapons;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import models.GameModel;

import static models.GameModel.GAME_SCENE_MARGIN_Y;
import static models.GameModel.GAME_SCENE_UNIVERSAL_SCALE_FACTOR;
import static objects.Player.PLAYER_HEIGHT;


public class Harpoon extends Weapon {
    public static final float  HARPOON_SPEED = -12;
    private static final float ARROW_SCALE_FACTOR = 1.3f * GAME_SCENE_UNIVERSAL_SCALE_FACTOR;
    private int tail_part_next_direction = -1; // right == -1 , left == 1
    private int tail_part_next_position = 0;
    private enum HARPOON_TYPE {STRAIT, WINDING}
    private HARPOON_TYPE type = HARPOON_TYPE.WINDING;

    {
        ACTIVE_WEAPON = WEAPON_TYPE.HARPOON;
        Polygon arrow = new Polygon(-10,0, 10,0, 0,-20);
        arrow.setScaleX(ARROW_SCALE_FACTOR);
        arrow.setScaleY(ARROW_SCALE_FACTOR);
        arrow.setFill(Color.BLUE);
        this.getChildren().add(arrow);
    }

    public Harpoon ( ) { }

    public Harpoon ( Point2D position ) { super ( position ); }

    public Harpoon (Point2D position, boolean is_strait_harpoon ) {
        super (position);
        if(is_strait_harpoon){
            type = HARPOON_TYPE.STRAIT;
        }else{
            type = HARPOON_TYPE.WINDING;
        }
    }

    // initialize speed
    {
        super.speedX = 0;
        super.speedY = HARPOON_SPEED;
    }

    @Override
    public void updatePosition ( ) {
        position = new Point2D ( position.getX ( ) + speedX, position.getY ( ) + speedY );

        //add  new tail
        double arrow_distance_from_ground = GameModel.getInstance().getSceneHeight() - GAME_SCENE_MARGIN_Y - this.position.getY();
        switch (type){
            case STRAIT:
                Line tail = new Line(0,0,0, arrow_distance_from_ground+0);
                tail.setFill(Color.RED);
                this.getChildren().add(tail);
                break;
            default: //case WINDING:
                double tail_part_diameter_Y = 16;
                double tail_part_diameter_X = 4;
                int n = (int)(arrow_distance_from_ground / tail_part_diameter_Y);
                Arc[] tail_new_part = new Arc[n];
                for(int i = 0; i < n ; i++){
                    if((position.getY() + tail_part_diameter_Y * tail_part_next_position) >= (GameModel.getInstance().getSceneHeight() - GAME_SCENE_MARGIN_Y) ) break;
                    tail_new_part[i] = new Arc(
                            0,
                            0,
                            tail_part_diameter_X / 2,
                            tail_part_diameter_Y / 2,
                            90,
                            tail_part_next_direction * 180
                    );
                    tail_new_part[i].setTranslateY(tail_part_diameter_Y * tail_part_next_position);
                    tail_part_next_direction *= -1;
                    tail_part_next_position++;
                    tail_new_part[i].setFill(null);
                    //tail_new_part[i].setStrokeWidth(1);
                    tail_new_part[i].setStroke(Color.DARKRED );
                    this.getChildren().add(tail_new_part[i]);
                }
        }
        Polygon arrow = new Polygon(-10,0, 10,0, 0,-20);
        arrow.setScaleX(ARROW_SCALE_FACTOR);
        arrow.setScaleY(ARROW_SCALE_FACTOR);
        arrow.setFill(Color.BLUE);
        this.getChildren().add(arrow);

        setTranslateY ( getTranslateY ( ) + speedY );

        handleCollisions ( );
    }



}

package constants;

import javafx.stage.Screen;
import objects.movable.weapon.CannonBall;

public class Constants {
    public static final String TITLE = "Island defense";

    public static final double CAMERA_NEAR_CLIP = 0.1;
    public static final double CAMERA_FAR_CLIP  = 100000;
    public static final double CAMERA_X_ANGLE   = -45;
    public static final double CAMERA_Z         = -2000;

    public static final double SCENE_WIDTH  = Screen.getPrimary().getBounds().getWidth() * 0.8;
    public static final double SCENE_HEIGHT = Screen.getPrimary().getBounds().getHeight() * 0.8;

    public static final double GAME_MAP_SUB_SCENE_WIDTH  = SCENE_WIDTH / 4;
    public static final double GAME_MAP_SUB_SCENE_HEIGHT = SCENE_HEIGHT / 3;

    public static final double AMMUNITION_VIEW_SUB_SCENE_WIDTH   = SCENE_WIDTH / 2;
    public static final double AMMUNITION_VIEW_SUB_SCENE_HEIGHT  = 30;

    public static final double INFO_BAR_SUB_SCENE_WIDTH  = SCENE_WIDTH / 6;
    public static final double INFO_BAR_SUB_SCENE_HEIGHT = SCENE_HEIGHT * 0.2;

    public static final double OCEAN_WIDTH  = 10000;
    public static final double OCEAN_DEPTH  = 10000;
    public static final double OCEAN_HEIGHT = 2;

    public static final double ISLAND_RADIUS = 100;
    public static final double ISLAND_HEIGHT = 6;

    public static final double[] DISTANCE_MAP_RADIUS = {
            50, 150, 300, 500, 1000
    };

    public static final double CANNON_WIDTH            = 10;
    public static final double CANNON_HEIGHT           = 50;
    public static final double CANNON_DEPTH            = 50;
    public static final double CANNON_VENT_LENGTH      = 50;
    public static final double CANNON_BALL_RADIUS      = CANNON_WIDTH / 2;
    public static final int    CANNON_MAX_AMMUNITION   = 20;

    public static final int    NUMBER_OF_NEW_WAVES 	          = 2;
    public static final int    NEW_WAVE_AMMUNITION            = 10;
    public static final double NEW_WAVE_BOAT_SPEED_INCREASE_FACTOR = 1 + 0.5 / NUMBER_OF_NEW_WAVES;

    public static final int    NUMBER_OF_BOATS 		= 4;
    public static final double BOAT_BODY_WIDTH      = 15;
    public static final double BOAT_BODY_HEIGHT     = 8;
    public static final double BOAT_BODY_DEPTH      = 35;
    public static final double BOAT_CABIN_WIDTH     = 10;
    public static final double BOAT_CABIN_HEIGHT    = 7;
    public static final double BOAT_CABIN_DEBT      = 14;
    public static final double BOAT_NOSE_LENGTH     = 12;
    public static final double BOAT_DISTANCE   		= 500;
    public static final double BOAT_SPEED      		= 0.1;
    public static final double BOAT_FIRE_RATE       = 0.33;   //bullets per second
    public static final double BOAT_CHANCE_TO_FIRE  = 30;    // in percentages
    public static final double BOAT_FIRE_ANGLE_RANGE_VERTICAL = 60;
    public static final double BOAT_FIRE_ANGLE_RANGE_HORIZONTAL = 45;

    public static final double PROJECTILE_RADIUS           = CANNON_BALL_RADIUS / 2;
    public static final double PROJECTILE_FIREPOWER_FACTOR = 1 / (Constants.NUMBER_OF_BOATS * (1 + NUMBER_OF_NEW_WAVES) + BOAT_CHANCE_TO_FIRE/10 + BOAT_FIRE_RATE );


    public static final double DELTA = 0.1;

    public static final double Y_SPEED = -2;

    public static final double GRAVITY = 0.01;

    public enum WeaponType { CannonBall, BoatProjectile }

}

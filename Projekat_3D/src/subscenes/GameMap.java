package subscenes;

import constants.Constants;
import javafx.event.EventHandler;
import javafx.scene.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;

import java.util.ArrayList;
import java.util.List;

public class GameMap extends SubScene implements EventHandler<MouseEvent> {
    private class GameMapBoat extends Rectangle{
        int id;
        GameMapBoat(int id, double x, double y)
        {
            super(x, y);
            this.id = id;
        }
        public int getMyId() {
            return id;
        }
    }
    private static final double GAME_MAP_SIZE_FACTOR = 2 * ( (Constants.GAME_MAP_SUB_SCENE_WIDTH * Constants.GAME_MAP_SUB_SCENE_HEIGHT) / (Constants.SCENE_WIDTH * Constants.SCENE_HEIGHT) );
    private Group myRoot;
    private Circle island;
    private Polygon cannon;
    private Rotate cannonRotate;
    private List<GameMapBoat> boats = new ArrayList<>();

    public GameMap(Group myRoot, double width, double height)
    {
        //SUB SCENE
        super(myRoot, width, height);
        super.getTransforms().addAll(
                new Translate(0, Constants.SCENE_HEIGHT - height)
        );
        super.setFill(Color.DARKBLUE);
        super.setCursor(Cursor.NONE);
        this.myRoot = myRoot;

        //ISLAND
        island = new Circle(Constants.ISLAND_RADIUS * GAME_MAP_SIZE_FACTOR );
        island.setFill(Color.BROWN);
        island.getTransforms().addAll(
                new Translate(width/2,height/2)
        );
        this.myRoot.getChildren().addAll(island);

        //CANNON
        cannon = new Polygon();
        double cannonLength = ( (Constants.CANNON_DEPTH + Constants.CANNON_VENT_LENGTH) / 2) * GAME_MAP_SIZE_FACTOR;
        cannon.getPoints().addAll(new Double[]{
                         0.0,        -cannonLength,
                     cannonLength/2,  cannonLength,
                    -cannonLength/2,  cannonLength,
        });
        cannon.setFill(Color.GREEN);
        this.cannonRotate = new Rotate();
        cannon.getTransforms().addAll(
                new Translate(width/2,height/2),
                this.cannonRotate
        );
        this.myRoot.getChildren().addAll(cannon);

        for(int i = 0; i < Constants.DISTANCE_MAP_RADIUS.length; ++i)
        {
            Circle circle = new Circle(Constants.DISTANCE_MAP_RADIUS[i] * GAME_MAP_SIZE_FACTOR);
            circle.setStroke(Color.RED);
            circle.setFill(null);
            circle.getTransforms().addAll(
                    new Translate(width / 2, height / 2)
            );
            this.myRoot.getChildren().addAll(circle);
        }

    }

    public void addBoat(int boatId, double angle)
    {
        double boatLength = (Constants.BOAT_BODY_DEPTH + Constants.BOAT_NOSE_LENGTH) * GAME_MAP_SIZE_FACTOR;
        double boatWidth  = Constants.BOAT_BODY_WIDTH * GAME_MAP_SIZE_FACTOR;
        GameMapBoat boat = new GameMapBoat(
                boatId,
                boatWidth,
                boatLength
        );
        boat.setFill(Color.LIGHTGRAY);
        boat.getTransforms().addAll(
                new Translate(Constants.GAME_MAP_SUB_SCENE_WIDTH / 2,Constants.GAME_MAP_SUB_SCENE_HEIGHT / 2),
                new Rotate(angle + 180),
                new Translate(0,-boatLength / 2),
                new Translate(0,Constants.BOAT_DISTANCE * GAME_MAP_SIZE_FACTOR)
        );
        this.myRoot.getChildren().addAll(boat);
        this.boats.add(boat);
    }

    public void removeBoat(int boatId)
    {
        boats.removeIf(boat->{
            boolean match = boat.getMyId() == boatId;
            if(match)
                this.myRoot.getChildren().remove(boat);
            return match;
        });
    }

    public void updateBoatPosition(int id, double speed)
    {
        boats.forEach(boat->{
            if(boat.getMyId() == id)
            {
                boat.getTransforms().addAll(
                        new Translate(0, -speed * GAME_MAP_SIZE_FACTOR)
                );
            }
        });
    }

    public void removeCannon()
    {
        this.myRoot.getChildren().remove(this.cannon);
        this.cannon = null;
    }

    public void rotateCannon(double angle)
    {
        if(this.cannon != null)
            this.cannonRotate.setAngle(angle);
    }

    @Override
    public void handle(MouseEvent mouseEvent) {

    }
}

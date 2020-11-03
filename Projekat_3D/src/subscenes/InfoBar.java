package subscenes;

import constants.Constants;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.SubScene;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;

public class InfoBar extends SubScene {
    public static final Color BACKGROUND_COLOR = Color.BLACK;
    public static final Color TEXT_COLOR = Color.LIGHTGOLDENRODYELLOW;

    private Group myRoot;
    private double health;
    private int sunkenShipsCnt;
    private int reachedIslandCnt;
    private Label healthText;
    private Label sunkenShipsCntText;
    private Label reachedIslandCntText;


    private double scaleX;
    private double scaleY;

    public InfoBar(Group myRoot, double sceneWidth, double sceneHeight) {
        super(myRoot, sceneWidth, sceneHeight);

        super.setFill(BACKGROUND_COLOR);
        super.setCursor ( Cursor.NONE );

        this.scaleX = this.scaleY = 1;
        double subSceneRatio = ( Constants.SCENE_WIDTH * 6  ) / ( 8 * Constants.SCENE_HEIGHT);
        if(subSceneRatio > 1)
            this.scaleX = subSceneRatio;
        else
            this.scaleY = subSceneRatio;

        this.myRoot = myRoot;
        this.health = 100;
        this.sunkenShipsCnt = 0;
        this.reachedIslandCnt = 0;

        this.healthText = new Label( "Health: " + Double.toString( Math.round(health) ) + " %" );
        this.healthText.setTextFill(TEXT_COLOR);
        this.healthText.getTransforms().addAll(
                new Translate(this.scaleX * 20,this.scaleY * 20)
        );
        this.healthText.setScaleX(scaleX);
        this.healthText.setScaleY(scaleY);
        this.myRoot.getChildren().addAll(healthText);

        this.sunkenShipsCntText = new Label( "Sunken ships: " +  Integer.toString(this.sunkenShipsCnt) );
        this.sunkenShipsCntText.setTextFill(TEXT_COLOR);
        this.sunkenShipsCntText.getTransforms().addAll(
                new Translate(this.scaleX * 20,this.scaleY * 50)
        );
        this.sunkenShipsCntText.setScaleX(scaleX);
        this.sunkenShipsCntText.setScaleY(scaleY);
        this.myRoot.getChildren().addAll(sunkenShipsCntText);

        this.reachedIslandCntText = new Label( "Reached island: " + Integer.toString(this.reachedIslandCnt) );
        this.reachedIslandCntText.setTextFill(TEXT_COLOR);
        this.reachedIslandCntText.getTransforms().addAll(
                new Translate(this.scaleX * 20,this.scaleY * 80)
        );
        this.reachedIslandCntText.setScaleX(scaleX);
        this.reachedIslandCntText.setScaleY(scaleY);
        this.myRoot.getChildren().addAll(reachedIslandCntText);
    }

    public void updateHealth(double newHealth)
    {
        this.health = newHealth;
        if(this.health < 0) this.health = 0;
        healthText.setText( "Health: " + Double.toString( Math.round(this.health) ) + " %"  );
    }

    public void incSunkenShipsCnt()
    {
        ++this.sunkenShipsCnt;
        this.sunkenShipsCntText.setText( "Sunken ships: " +  Integer.toString(this.sunkenShipsCnt) );
    }

    public void incReachedIslandCnt()
    {
        ++reachedIslandCnt;
        this.reachedIslandCntText.setText( "Reached island: " + Integer.toString(this.reachedIslandCnt) );
    }

}

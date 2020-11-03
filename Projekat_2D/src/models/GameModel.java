package models;

import javafx.scene.Group;
import objects.*;
import objects.bonuses.Bonus;
import objects.weapons.Weapon;

import java.awt.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class GameModel {
    public static float GAME_SCENE_MARGIN_SCALE_FACTOR = 0.1f;
    public static float GAME_SCENE_MARGIN_X = (float) GameModel.getInstance ( ).getSceneWidth ( ) * GAME_SCENE_MARGIN_SCALE_FACTOR;
    public static float GAME_SCENE_MARGIN_Y = (float) GameModel.getInstance ( ).getSceneHeight ( ) * GAME_SCENE_MARGIN_SCALE_FACTOR;
    public static float GAME_SCENE_UNIVERSAL_SCALE_FACTOR =
              ( ( GameModel.getInstance ( ).getSceneWidth ( ) - GAME_SCENE_MARGIN_X * 2 ) *
                ( GameModel.getInstance ( ).getSceneHeight ( ) - GAME_SCENE_MARGIN_Y * 2) ) /
              ( GameModel.getInstance().getSceneWidth() * GameModel.getInstance().getSceneHeight() );

    private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private final double SCENE_SCALE_FACTOR = 0.8;
    private float sceneWidth = (float) (screenSize.getWidth() * SCENE_SCALE_FACTOR);
    private float sceneHeight = (float) (screenSize.getHeight() * SCENE_SCALE_FACTOR);

    private static GameModel thisInstance = null;

    private CopyOnWriteArrayList<Ball> balls = new CopyOnWriteArrayList<>();
    private Player player;
    private Weapon weapon;
    private boolean gameLost;
    private boolean gameWon;
    private Group root;
//~~~~~~~~~~~~~
    private GameTimer gameTimer;
    private Score score;
    private CopyOnWriteArrayList<Bonus> bonuses = new CopyOnWriteArrayList<>();
    private CopyOnWriteArrayList<PlayersLife> lives = new CopyOnWriteArrayList<>();
//~~~~~~~~~~~~~
    public static GameModel getInstance() {
        if (thisInstance == null) {
            thisInstance = new GameModel();
        }
        return thisInstance;
    }

    public float getSceneWidth() {
        return sceneWidth;
    }

    public float getSceneHeight() {
        return sceneHeight;
    }

    public double getScreenWidth() {
        return screenSize.getWidth();
    }

    public double getScreenHeight() {
        return screenSize.getHeight();
    }

    public void setGameLost(boolean gameLost) {
        this.gameLost = gameLost;
    }

    public boolean isGameLost() {
        return gameLost;
    }

    public CopyOnWriteArrayList<Ball> getBalls() {
        return balls;
    }

    public void setBalls(CopyOnWriteArrayList<Ball> balls) {
        this.balls = balls;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Weapon getWeapon() {
        return weapon;
    }

    public void setWeapon(Weapon weapon) {
        this.weapon = weapon;
    }

    public Group getRoot() {
        return root;
    }

    public void setRoot(Group root) {
        this.root = root;
    }

    public boolean isGameWon() {
        return gameWon;
    }

    public void setGameWon(boolean gameWon) {
        this.gameWon = gameWon;
    }

    public Dimension getScreenSize() {
        return screenSize;
    }

    public void setScreenSize(Dimension screenSize) {
        this.screenSize = screenSize;
    }

    //~~~~~~~~~~~~~~

    public GameTimer getGameTimer(){
        return gameTimer;
    }

    public void setGameTimer(GameTimer gt){
        this.gameTimer = gt;
    }

    public Score getScore(){
        return score;
    }

    public void setScore(Score s){
        this.score = s;
    }

    public CopyOnWriteArrayList<Bonus> getBonuses() {
        return bonuses;
    }

    public void setBonuses(CopyOnWriteArrayList<Bonus> bonuses) {
        this.bonuses = bonuses;
    }

    public CopyOnWriteArrayList<PlayersLife> getLives() { return lives; }

    public void setLives(CopyOnWriteArrayList<PlayersLife> lives) { this.lives = lives; }

}

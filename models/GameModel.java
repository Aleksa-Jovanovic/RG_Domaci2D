package models;

import javafx.geometry.Point2D;
import javafx.scene.Group;
import objects.*;
import objects.weapons.Weapon;
import javafx.scene.shape.Rectangle;
import java.awt.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class GameModel {
    private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    public final double GAME_SCREEN_SCALE_FACTOR = 0.8;
    private final double GAME_SCREEN_OFFSTE = 0.1;
    private static Integer GAME_LENGTH;

    private final double SCENE_SCALE_FACTOR = 0.7;
    private float sceneWidth = (float) (screenSize.getWidth() * SCENE_SCALE_FACTOR);
    private float sceneHeight = (float) (screenSize.getHeight() * SCENE_SCALE_FACTOR);
    public final double GAME_SCREEN_HORIZONTAL_OFFSTE = sceneWidth*GAME_SCREEN_OFFSTE;
    public final double GAME_SCREEN_VERTICAL_OFFSTE = sceneHeight*GAME_SCREEN_OFFSTE;
    
    private static GameModel thisInstance = null;

    private CopyOnWriteArrayList<Ball> balls = new CopyOnWriteArrayList<>();
    private CopyOnWriteArrayList<Dollar> dollars = new CopyOnWriteArrayList<>();
    private CopyOnWriteArrayList<Shield> shields = new CopyOnWriteArrayList<Shield>();
    private CopyOnWriteArrayList<Life> lives = new CopyOnWriteArrayList<>();
    private CopyOnWriteArrayList<Clock> clocks = new CopyOnWriteArrayList<>();
    private Player player;
    private Weapon weapon;
    private boolean gameLost;
    private boolean gameWon;
    private Group root;
    private TextDisplay comboText;
    private boolean combo=false;
    private TextDisplay scoreText;
    private int lastDivider =10;
    private TextDisplay timeLeft;
    private Rectangle timeLeftBar;

    //Score Value
    public static final int BALL_DESTROYED = 5;
    public static final int SMALLEST_BALL_DESTROYED = 10;
    public static final int HIGH_BALL_DESTROYED = 10;
    public static final int COMBO_BALL_DESTROYED = 100;

    public static GameModel getInstance() {
        if (thisInstance == null) {
            thisInstance = new GameModel();
        }
        return thisInstance;
    }

    public static void setGameLength(Integer gameLength){
        GAME_LENGTH = gameLength;
    }

    public static Integer getGameLength(){
        return GAME_LENGTH;
    }

    public void setTimeLeftText(TextDisplay timeLeft){
        this.timeLeft=timeLeft;
    }

    public TextDisplay getTimeLeftText(){
        return timeLeft;
    }

    public void setTimeLeftBar(Rectangle timeLeftBar){
        this.timeLeftBar=timeLeftBar;
    }

    public Rectangle getTimeLeftBar(){
        return timeLeftBar;
    }

    public void updateScore(int change){

        int last =Integer.parseInt(scoreText.getTextClass().getText().toString().substring(7));
        last+=change;
        scoreText.getTextClass().setText("Score: " + Integer.toString(last));
    }

    public void doubleTheScore(){
        int last =Integer.parseInt(scoreText.getTextClass().getText().toString().substring(7));
        updateScore(last);
    }

    public TextDisplay getScoreText(){
        return scoreText;
    }

    public void setScoreText(TextDisplay score){
        scoreText=score;
    }

    public void setComboFinished(){
        combo=false;
    }

    public boolean getComboStatus(){
        return combo;
    }

    public TextDisplay getComboText(){
        return comboText;
    }

    public void setComboText(TextDisplay text){
        comboText=text;
    }

    public void startComboTextAnimation(){
        comboText.startAnimation();
        combo=true;
    }

    public CopyOnWriteArrayList<Life> getLifeIcons(){
        return lives;
    }
    public CopyOnWriteArrayList<Shield> getShields(){
        return shields;
    }
    public CopyOnWriteArrayList<Clock> getClocks(){
        return clocks;
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

    public CopyOnWriteArrayList<Dollar> getDollars(){
        return dollars;
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
}

package objects;

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Point2D;
import javafx.util.Duration;
import models.GameModel;

public abstract class BonusObject extends MovingGameObject {

    protected static final float FALLING_SPEED = 5;
    protected static final float OBJECT_WIDTH = Player.PLAYER_WIDTH*(float)1.5;
    protected static final float OBJECT_HEIGHT = Player.PLAYER_HEIGHT*(float)1.5;

    private FadeTransition fadeAnimation;
    private TranslateTransition moveAnimation;

    protected  abstract  void playerCollisionEffect();
    protected abstract void removeObject();
    protected  abstract  void drawObject();
    protected abstract void addObject();

    public BonusObject(Point2D position){
        super(position);
        super.speedX = 0;
        super.speedY = FALLING_SPEED;

        drawObject();
        addFadeAnimation(position);
        addObject();
    }

    private void addFadeAnimation(Point2D position){
        double distanceToFall = GameModel.getInstance().getSceneHeight() - position.getY() - GameModel.getInstance().GAME_SCREEN_VERTICAL_OFFSTE;
        double animationTime = distanceToFall/FALLING_SPEED;

        fadeAnimation = new FadeTransition(new Duration(animationTime*100/5),this);
        fadeAnimation.setFromValue(1);
        fadeAnimation.setToValue(0);
        fadeAnimation.play();

        moveAnimation = new TranslateTransition(new Duration(animationTime*100/5),this);
        moveAnimation.setFromY(position.getY());
        moveAnimation.setToY(GameModel.getInstance().getSceneHeight()+1);
        moveAnimation.play();

        //SelfDelete at end of animation
        moveAnimation.setOnFinished(event -> this.removeObject());
    }

    @Override
    public void updatePosition ( ){
        handleCollisions();
    }

    //Handling collision with player
    @Override
    protected void handleCollisions ( ){
        handlePlayerCollisions();
        handleGroundCollisions();
    }

    private void handlePlayerCollisions ( ){
        if(this.getBoundsInParent().intersects(GameModel.getInstance().getPlayer().getBoundsInParent())){
            playerCollisionEffect();
            moveAnimation.stop();
            fadeAnimation.stop();
            this.removeObject();
        }
    }

    private void handleGroundCollisions(){
        if(this.getPosition().getY() >=  GameModel.getInstance().getSceneHeight() - GameModel.getInstance().GAME_SCREEN_VERTICAL_OFFSTE - OBJECT_HEIGHT){
            moveAnimation.stop();
            fadeAnimation.stop();
            removeObject();
        }
    }
}

package objects;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import models.GameModel;

public class Clock extends BonusObject {

    private static final int EXTRA_TIME = 10;

    public Clock(Point2D position){
        super(position);
    }

    @Override
    protected void playerCollisionEffect() {
        //Set text value
        TextDisplay newTimeText = GameModel.getInstance().getTimeLeftText();
        int newTime = Integer.parseInt(newTimeText.getTextClass().getText()) + EXTRA_TIME;
        newTimeText.getTextClass().setText(Integer.toString(newTime));
        GameModel.getInstance().setTimeLeftText(newTimeText);

        //Set new bar with
        double oldWidth = GameModel.getInstance().getTimeLeftBar().getWidth();
        double maxWidth = GameModel.getInstance ( ).getSceneWidth ( )*GameModel.getInstance().GAME_SCREEN_SCALE_FACTOR;
        double extraWidth = EXTRA_TIME * maxWidth / GameModel.getGameLength();

        GameModel.getInstance().getTimeLeftBar().setWidth(oldWidth+extraWidth);
    }

    @Override
    protected void removeObject() {
        GameModel.getInstance().getClocks().remove(this);
        GameModel.getInstance().getRoot().getChildren().remove(this);
    }

    @Override
    protected void drawObject() {
        Rectangle rectangle = new Rectangle(0,0,OBJECT_HEIGHT * 0.8,OBJECT_HEIGHT * 0.8);
        Image img = new Image("clock.png");
        ImagePattern imgPattern = new ImagePattern(img,0,0,1,1,true);
        rectangle.setFill(imgPattern);
        rectangle.setStroke(Color.BLACK);
        rectangle.setStrokeWidth(1);

        this.getChildren().add(rectangle);
    }

    @Override
    protected void addObject() {
        GameModel.getInstance().getClocks().add(this);
        GameModel.getInstance().getRoot().getChildren().add(this);
    }
}

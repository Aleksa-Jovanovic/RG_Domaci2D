package objects;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import models.GameModel;

public class Shield extends BonusObject {

    public Shield(Point2D position){
        super(position);
    }

    @Override
    protected void playerCollisionEffect(){
        Player.setShield();
    }

    @Override
    protected void removeObject(){
        GameModel.getInstance().getShields().remove(this);
        GameModel.getInstance().getRoot().getChildren().remove(this);
    }

    @Override
    protected  void drawObject(){
        Rectangle rectangle = new Rectangle(0,0,OBJECT_HEIGHT * 0.8,OBJECT_HEIGHT * 0.8);
        Image img = new Image("shield.png");
        ImagePattern imgPattern = new ImagePattern(img,0,0,1,1,true);
        rectangle.setFill(imgPattern);
        rectangle.setStroke(Color.BLACK);
        rectangle.setStrokeWidth(1);

        this.getChildren().add(rectangle);
    }

    @Override
    protected void addObject(){
        GameModel.getInstance().getShields().add(this);
        GameModel.getInstance().getRoot().getChildren().add(this);
    }

}

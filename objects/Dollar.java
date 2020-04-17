package objects;

import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import models.GameModel;

public class Dollar extends BonusObject {

    public Dollar(Point2D position){
        super(position);
    }

    @Override
    protected void playerCollisionEffect(){
        GameModel.getInstance().doubleTheScore();
    }

    @Override
    protected void removeObject(){
        GameModel.getInstance().getDollars().remove(this);
        GameModel.getInstance().getRoot().getChildren().remove(this);
    }

    @Override
    protected  void drawObject(){
        Group dollarBox = new Group();
        Rectangle rectangle = new Rectangle(0,0,OBJECT_WIDTH,OBJECT_HEIGHT);
        rectangle.setFill(Color.DARKGREEN);
        rectangle.setStroke(Color.BLACK);
        rectangle.setStrokeWidth(1);
        Rectangle dollarLogo = new Rectangle(OBJECT_WIDTH/4,OBJECT_HEIGHT/7,OBJECT_WIDTH/2,OBJECT_HEIGHT/3);

        Image img = new Image("dolar.png");
        ImagePattern imgPattern = new ImagePattern(img,0,0,1,1,true);
        dollarLogo.setFill(imgPattern);

        dollarBox.getChildren().addAll(rectangle,dollarLogo);
        this.getChildren().add(dollarBox);
    }

    @Override
    protected void addObject(){
        GameModel.getInstance().getDollars().add(this);
        GameModel.getInstance().getRoot().getChildren().add(this);
    }

}

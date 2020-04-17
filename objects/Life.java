package objects;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import models.GameModel;

public class Life extends GameObject {

    //Life icnos
    private static final double LIFE_ICON_OFFSET = GameModel.getInstance().GAME_SCREEN_HORIZONTAL_OFFSTE/5;
    private static final double LIFE_ICON_START_POSITION = GameModel.getInstance().GAME_SCREEN_HORIZONTAL_OFFSTE*2/3;
    private static int currentLifeNumber = 0;
    private static double lastPosition;
    private static double positionDiff = 0;


    public Life(){

        drawIcon();
        currentLifeNumber++;

        GameModel.getInstance().getLifeIcons().add(this);
        GameModel.getInstance().getRoot().getChildren().add(this);
    }

    public static void removeLife(){
        GameModel.getInstance().getRoot().getChildren().remove(GameModel.getInstance().getLifeIcons().get(currentLifeNumber - 1));
        GameModel.getInstance().getLifeIcons().remove(currentLifeNumber - 1);
        currentLifeNumber--;
        lastPosition-=positionDiff;
    }

    public static void drawStarterLifes(){
        new Life();
        new Life();
        new Life();
        new Life();
        new Life();
    }

    private void drawIcon(){

        double PLAYER_WIDTH = Player.PLAYER_WIDTH * 0.8;
        double PLAYER_HEIGHT = Player.PLAYER_HEIGHT * 0.8;

        Rectangle topHat = new Rectangle(PLAYER_WIDTH/4,0,PLAYER_WIDTH/2,PLAYER_HEIGHT/4);
        Rectangle bottomHat = new Rectangle(PLAYER_WIDTH*0.05,topHat.getHeight(),PLAYER_WIDTH*0.9,topHat.getHeight()/4);

        double hatHeight = topHat.getHeight() + bottomHat.getHeight();
        Circle head = new Circle(PLAYER_WIDTH/2,hatHeight+PLAYER_WIDTH*0.3,PLAYER_WIDTH*0.4, Color.SPRINGGREEN);

        //Eyes
        Polygon leftEye = new Polygon();
        leftEye.getPoints().addAll(head.getCenterX() - head.getRadius(), head.getCenterY() - head.getRadius()/2,
                head.getCenterX(), head.getCenterY() - head.getRadius()/2,
                head.getCenterX() - head.getRadius() / 2, head.getCenterY());
        Polygon rightEye = new Polygon();
        rightEye.getPoints().addAll(head.getCenterX() + head.getRadius(), head.getCenterY() - head.getRadius()/2,
                head.getCenterX(), head.getCenterY() - head.getRadius()/2,
                head.getCenterX() + head.getRadius() / 2, head.getCenterY());

        //Mouth
        Arc mouth = new Arc(PLAYER_WIDTH/2,head.getCenterY()+head.getRadius()*0.3,head.getRadius()*0.6,head.getRadius()*0.3,0,-180);
        mouth.setFill(Color.MEDIUMVIOLETRED);
        mouth.setType(ArcType.CHORD);

        Group icon = new Group();
        icon.getChildren ( ).addAll ( head , topHat, bottomHat, leftEye, rightEye, mouth);


        //Changing icon position!
        icon.setTranslateY(GameModel.getInstance().GAME_SCREEN_VERTICAL_OFFSTE/2);
        positionDiff = LIFE_ICON_OFFSET + bottomHat.getWidth();
        if(GameModel.getInstance().getLifeIcons().isEmpty()){ //Nije bilo zivota
            lastPosition = LIFE_ICON_START_POSITION;
            icon.setTranslateX(LIFE_ICON_START_POSITION);
        }else{
            lastPosition += positionDiff;
            icon.setTranslateX(lastPosition);
        }

        this.getChildren().add(icon);
    }

}

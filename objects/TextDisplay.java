package objects;


import javafx.animation.Animation;
import javafx.geometry.Point2D;
import javafx.scene.text.*;
import models.GameModel;

public class TextDisplay extends GameObject {

    private Text text;
    private Animation myAnimation;
    private boolean showing = false;

    public TextDisplay(Point2D position, String textValue){
        super(position);
        this.text = new Text(position.getX(),position.getY(),textValue);
        this.getChildren().addAll(this.text);
    }



    public void setTextValue(String textValue){
        this.text.setText(textValue);
    }

    public Text getTextClass(){
        return text;
    }

    public void setAnimation(Animation animation){
        myAnimation=animation;
    }

    /*public Animation getMyAnimation(){
        return myAnimation;
    }*/

    public void startAnimation(){
        if(!showing){
            GameModel.getInstance().getRoot().getChildren().add(this);
            showing=true;
        }
        myAnimation.playFromStart();
    }

}

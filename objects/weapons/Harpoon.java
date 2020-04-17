package objects.weapons;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import models.GameModel;
import objects.Player;

import java.util.concurrent.CopyOnWriteArrayList;

public class Harpoon extends Weapon {

    public static final float  HARPOON_SPEED    = -13;
    public static final double HARPOON_WIDTH = Player.PLAYER_WIDTH * 0.9;
    private static final double ROPE_LENGTH = GameModel.getInstance().GAME_SCREEN_SCALE_FACTOR * GameModel.getInstance().getSceneHeight() - Player.PLAYER_HEIGHT - Math.sqrt(3)/2 * HARPOON_WIDTH;
    private static final double NUMBER_OF_PARTS = 20;


    private CopyOnWriteArrayList<Shape> parts = new CopyOnWriteArrayList<>();
    private int numberOfShowingRopeParts = 0;
    private double ropePartLength = ROPE_LENGTH / NUMBER_OF_PARTS;
    private double startingPositionY;
    private double lastPositionY;


    public Harpoon ( Point2D position ) {
        super ( position );

        drawHarpoon();

        super.speedY = HARPOON_SPEED;
        super.speedX = 0;

        lastPositionY = startingPositionY = position.getY();

        //Setting harpoon behind the player
        GameModel.getInstance().getRoot().getChildren().remove(GameModel.getInstance().getPlayer());
        GameModel.getInstance().getRoot().getChildren().add(GameModel.getInstance().getPlayer());
    }

    private void drawHarpoon(){

        //Harpoon point
        Polygon harpoon = new Polygon();
        double height = Math.sqrt(3)/2 * HARPOON_WIDTH;
        harpoon.getPoints().addAll((double)0, (double)0,
                -HARPOON_WIDTH/2, height,
                HARPOON_WIDTH/2, height);
        harpoon.setFill ( Color.BLUE );
        parts.add(0, harpoon); //harpoon is on index 0;
        this.getChildren ( ).addAll ( harpoon );

        //Rope
        for(int i=0; i<NUMBER_OF_PARTS; i++){
            double ropePartheight = height + i*ropePartLength;
            CubicCurve ropePart = new CubicCurve(0, ropePartheight,HARPOON_WIDTH ,ropePartheight + ropePartLength * 0.25, -HARPOON_WIDTH, ropePartheight + ropePartLength * 0.75, 0, ropePartheight + ropePartLength);
            ropePart.setFill(null);
            ropePart.setStroke(Color.RED);
            ropePart.setStrokeWidth(1);
            parts.add(i+1, ropePart);
            //this.getChildren().add(ropePart);
        }

        numberOfShowingRopeParts = 0;

    }

    @Override
    public void updatePosition() {
        super.updatePosition();
        if(numberOfShowingRopeParts == NUMBER_OF_PARTS)
            return;

        double height = Math.sqrt(3)/2 * HARPOON_WIDTH;
        if( this.getPosition().getY() + height + numberOfShowingRopeParts * ropePartLength <= startingPositionY){
            this.getChildren().add(parts.get(++numberOfShowingRopeParts));
        }
    }
}

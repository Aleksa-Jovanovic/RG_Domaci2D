package objects;


import javafx.scene.paint.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Translate;
import models.GameModel;

public class Background extends GameObject {

	private Rectangle background;

	public Background ( ) {
		background = new Rectangle (
				GameModel.getInstance ( ).getSceneWidth ( )*GameModel.getInstance().GAME_SCREEN_SCALE_FACTOR,
				GameModel.getInstance ( ).getSceneHeight ( )*GameModel.getInstance().GAME_SCREEN_SCALE_FACTOR
		);
		this.getTransforms().addAll(new Translate(GameModel.getInstance().GAME_SCREEN_HORIZONTAL_OFFSTE, GameModel.getInstance ( ).GAME_SCREEN_VERTICAL_OFFSTE));

		Stop stops[] = {new Stop(0,Color.YELLOW), new Stop(1,Color.BLACK)};
		LinearGradient backgroundColor = new LinearGradient(0,0,0,1,true, CycleMethod.NO_CYCLE, stops);
		background.setFill ( backgroundColor );
		
		this.getChildren ( ).addAll ( background );
	}

	public Background(double x, double y,  double width, double height, ImagePattern pattern){
		background = new Rectangle(width,height);
		this.getTransforms().addAll(new Translate(x,y));
		background.setFill(pattern);
		this.getChildren().addAll(background);
	}

}

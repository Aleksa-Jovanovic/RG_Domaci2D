package objects.weapons;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import models.GameModel;

public class Bullet extends Weapon {
	public static final float  BULLET_SPEED    = -13;
	public static final double BULLET_DIAMETER = GameModel.getInstance ( ).getSceneWidth ( ) * 0.004;

	
	private void drawBullet(){
		Circle bullet = new Circle ( BULLET_DIAMETER );
		
		bullet.setFill ( Color.BLUE );
		
		this.getChildren ( ).addAll ( bullet );
	}
	
	public Bullet ( Point2D position ) {
		super ( position );

		drawBullet();

		super.speedY = BULLET_SPEED;
		super.speedX = 0;
	}

}

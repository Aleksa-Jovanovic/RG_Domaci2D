package objects.weapons;

import javafx.geometry.Point2D;
import models.GameModel;
import objects.MovingGameObject;

public abstract class Weapon extends MovingGameObject {
	@Override
	protected void handleCollisions ( ) {
		// if the weapon get's out of the window, remove it from the scene
		if ( position.getY ( ) <= GameModel.getInstance().GAME_SCREEN_VERTICAL_OFFSTE ) {
			GameModel.getInstance ( ).setWeapon ( null );
			GameModel.getInstance ( ).getRoot ( ).getChildren ( ).remove ( this );
		}
	}
	
	public Weapon ( Point2D position ) {
		super ( position );
		GameModel.getInstance ( ).setWeapon ( this );
		GameModel.getInstance().getRoot().getChildren ( ).add ( this );
	}
	
	public Weapon ( ) { }
	
	@Override
	public void updatePosition ( ) {
		position = new Point2D ( position.getX ( ) + speedX, position.getY ( ) + speedY );
		//setTranslateX(getTranslateX() + speedX);
		setTranslateY ( getTranslateY ( ) + speedY );
		
		handleCollisions ( );
	}
}

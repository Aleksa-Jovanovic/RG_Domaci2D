package objects;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import models.GameModel;

import java.util.Random;

public class Ball extends MovingGameObject {
	
	//private GameModel model = GameModel.getInstance();
	private float ballSpeedX = 8;
	private float ballSpeedY = ballSpeedX * 1.0f;
	private Circle ball;
	private double ballDiameter;
	private boolean isReal;
	private long timeCreated = System.currentTimeMillis();
	private boolean justCreated = true;

	private static final double BALL_DIAMETER = ( float ) ( GameModel.getInstance ( ).getScreenSize ( ).width * 0.025  * GameModel.getInstance().GAME_SCREEN_SCALE_FACTOR);
	private static final double DR = 0.7;
	private static final double DVy = 1.2;
	private static final int NUMBER_OF_SPLITTING = 3;
	private static final double MIN_BALL_DIAMETER = BALL_DIAMETER * Math.pow(DR,NUMBER_OF_SPLITTING) * GameModel.getInstance().GAME_SCREEN_SCALE_FACTOR;

	/*public Ball(){
		super.speedX = ballSpeedX;
		super.speedY = ballSpeedY;
		ballDiameter = BALL_DIAMETER;

		ball = new Circle(BALL_DIAMETER);
		ball.setFill ( Color.RED );
		this.getChildren ( ).addAll ( ball );
	}*/
	
	public Ball ( Point2D position ) {
		super ( position );

		super.speedX = ballSpeedX;
		super.speedY = ballSpeedY;
		ballDiameter = BALL_DIAMETER;

		Random rnd = new Random();
		Color newBallColor = new Color(rnd.nextFloat(),rnd.nextFloat(),rnd.nextFloat(),1);

		ball = new Circle(BALL_DIAMETER);
		ball.setFill ( newBallColor );
		isReal=true;
		this.getChildren ( ).addAll ( ball );
	}

	private Ball ( Point2D position, Color color, double diameter, boolean status ) {
		super ( position );

		super.speedX = ballSpeedX;
		super.speedY = ballSpeedY;
		ballDiameter = diameter;

		ball = new Circle(diameter);
		ball.setFill (color);
		isReal=status;
		this.getChildren ( ).addAll ( ball );
	}


	//Set and Get
	public long getTimeCreated(){
		return timeCreated;
	}

	public void setJustCreated(boolean value){
		justCreated=value;
	}


	//Update Position and Handle methods
	@Override
	public void updatePosition ( ) {
		handleCollisions ( );
		position = new Point2D ( position.getX ( ) + speedX, position.getY ( ) + speedY );
		setTranslateX ( getTranslateX ( ) + speedX );
		setTranslateY ( getTranslateY ( ) + speedY );
		
		if ( speedY < 0 ) {
			speedY += 0.17;
		} else {
			speedY = ballSpeedY;
		}
	}
	
	@Override
	protected void handleCollisions ( ) {
		handleBorderCollisions ( );
		handlePlayerCollisions ( );
		handleBulletCollisions ( );
	}
	
	private void handleBulletCollisions ( ) {
		if ( GameModel.getInstance ( ).getWeapon ( ) == null ) {
			return;
		}
		if ( this.getBoundsInParent ( ) .intersects ( GameModel.getInstance ( ).getWeapon ( ).getBoundsInParent ( ) ) ) {
			//Removing old ball from root-a (so it doesnt show) and from GameModel!
			GameModel.getInstance ( ).getRoot ( ).getChildren ( ).remove ( this );
			GameModel.getInstance().getBalls().remove(this);

			//Also removing weapon from root and GameMoedl!
			GameModel.getInstance().getRoot().getChildren().remove(GameModel.getInstance().getWeapon());
			GameModel.getInstance().setWeapon(null);

			//Creating new balls if this is not the smallest------------------------------------------------------------
			if(!(ball.getRadius() <= MIN_BALL_DIAMETER)){
				Random rnd = new Random();
				Color newBallColor = new Color(rnd.nextFloat(),rnd.nextFloat(),rnd.nextFloat(),1);
				while(newBallColor == this.ball.getFill()){
					newBallColor = new Color(rnd.nextFloat(),rnd.nextFloat(),rnd.nextFloat(),1);
				}
				//For fake ball
				Color transparentNewColor = new Color(newBallColor.getRed(),newBallColor.getGreen(),newBallColor.getBlue(),0.5);
				Random isRealRandom = new Random();

				//Small headStart
				Point2D newPosition = new Point2D(position.getX(), position.getY()-ball.getRadius()*1.8);

				//Making of balls
				Ball ball1;
				Ball ball2;
				if(isRealRandom.nextDouble() < 0.25){ //Fake
					ball1 = new Ball(newPosition,transparentNewColor,this.ballDiameter*DR,false);
				}else{ //Real
					ball1 = new Ball(newPosition,newBallColor,this.ballDiameter*DR,true);
				}
				if(isRealRandom.nextDouble() < 0.25){ //Fake
					ball2 = new Ball(newPosition,transparentNewColor,this.ballDiameter*DR,false);
				}else{ //Real
					ball2 = new Ball(newPosition,newBallColor,this.ballDiameter*DR,true);
				}


				//Setting ball speed
				ball1.speedX=this.speedX;
				ball2.speedX=this.speedX*(-1);
				ball1.ballSpeedY=this.ballSpeedY*(float)DVy;
				ball2.ballSpeedY=this.ballSpeedY*(float)DVy;
				ball1.ballSpeedX=this.ballSpeedX*(float)DVy;
				ball1.ballSpeedX=this.ballSpeedX*(float)DVy;

				//Adding to root and GameModel
				GameModel.getInstance().getBalls().add(ball1);
				GameModel.getInstance().getBalls().add(ball2);
				GameModel.getInstance().getRoot().getChildren().addAll(ball1,ball2);
			}

			//Update score----------------------------------------------------------------------------------------------
			boolean specialPoints = false;
			int totalPoints = 0;

			//Special effects can addUp
			if(GameModel.getInstance().getComboStatus()){
				totalPoints+=GameModel.COMBO_BALL_DESTROYED;
				specialPoints=true;
			}
			if(ball.getRadius() <= MIN_BALL_DIAMETER){
				totalPoints+=GameModel.SMALLEST_BALL_DESTROYED;
				specialPoints=true;
			}
			if(position.getY ( ) - ballDiameter <= 0){
				totalPoints+=GameModel.HIGH_BALL_DESTROYED;
				specialPoints=true;
			}

			//If no special points were added /Else add special points
			if(!specialPoints) {
				GameModel.getInstance().updateScore(GameModel.BALL_DESTROYED);
			}else{
				GameModel.getInstance().updateScore(totalPoints);
			}

			//Make Bonus Object-----------------------------------------------------------------------------------------
			Random bonusRnd = new Random();
			double nextDouble = bonusRnd.nextDouble();
			if(nextDouble <= 0.1666){
				new Dollar(this.getPosition());
			}else if(nextDouble > 0.1666 && nextDouble <= 0.333){
				new Shield(this.getPosition());
			}else if(nextDouble > 0.333 && nextDouble <= 0.5){
				new Clock(this.getPosition());
			}
		}
	}
	
	private void handlePlayerCollisions ( ) {
		if ( this.getBoundsInParent ( ).intersects ( GameModel.getInstance ( ).getPlayer ( ).getBoundsInParent ( ) )) {

			//Check if player is protected or not
			if(Player.isProtected == 0){
				//Check if ball is real
				if(isReal){
					if(GameModel.getInstance().getLifeIcons().isEmpty()){
						GameModel.getInstance ( ).setGameLost ( true );
					}else{
						Life.removeLife();
						Player.setProtected(3,1);
					}

				}else{
					//Removing old ball from root-a (so it doesnt show) and from GameModel!
					GameModel.getInstance ( ).getRoot ( ).getChildren ( ).remove ( this );
					GameModel.getInstance().getBalls().remove(this);
				}
			}else if(Player.isProtected == 2){
				Player.removeShield(false);
			}
		}
	}
	
	private void handleBorderCollisions ( ) {
		if ( position.getX ( ) - ballDiameter < GameModel.getInstance().GAME_SCREEN_HORIZONTAL_OFFSTE || position.getX ( ) > GameModel.getInstance ( ).getSceneWidth ( ) - ballDiameter - GameModel.getInstance().GAME_SCREEN_HORIZONTAL_OFFSTE) {
			speedX = -speedX;
		}

		//If ball hits the top when it is created -> it is destroyed
		if(position.getY ( ) - ballDiameter - GameModel.getInstance().GAME_SCREEN_VERTICAL_OFFSTE <= 0 && justCreated){
			GameModel.getInstance().getRoot().getChildren().remove(this);
			GameModel.getInstance().getBalls().remove(this);
			//Show "Combo!"
			GameModel.getInstance().startComboTextAnimation();
		}

		if (position.getY ( ) - ballDiameter - GameModel.getInstance().GAME_SCREEN_VERTICAL_OFFSTE <= 0 || position.getY ( ) >= GameModel.getInstance ( ).getSceneHeight ( ) - ballDiameter - GameModel.getInstance().GAME_SCREEN_VERTICAL_OFFSTE) {
			speedY = -speedY;
			if (position.getY ( ) - ballDiameter - GameModel.getInstance().GAME_SCREEN_VERTICAL_OFFSTE <= 0){
				position = new Point2D(position.getX(), GameModel.getInstance().GAME_SCREEN_VERTICAL_OFFSTE + ballDiameter);
				this.setTranslateY(position.getY());
			}
		}
	}
}

package objects;

import javafx.animation.FadeTransition;
import javafx.animation.Timeline;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.util.Duration;
import models.GameModel;
import playerStates.MovingLeftState;
import playerStates.MovingRightState;
import playerStates.StandingState;
import playerStates.State;

import java.sql.Time;

public class Player extends MovingGameObject {
	//private GameModel model = GameModel.getInstance();
	private             State state         = new StandingState ( this );
	public static final float PLAYER_WIDTH  = ( float ) ( GameModel.getInstance ( ).getScreenSize ( ).width * 0.01 );
	public static final float PLAYER_HEIGHT = 2 * PLAYER_WIDTH;
	private static final float PLAYER_SPEED  = 10;

	private static Player player;

	private static FadeTransition fadeTransition = null;
	private static FadeTransition halfFadeTrasition = null;
	private static FadeTransition fadeTransitionReset = null;

	public static int shieldedFor = 0;
	public static int protectedFor = 0;
	public static int isProtected = 0;

	
	public Player ( Point2D position ) {
		super ( position );

		// this is necessary in order for this class to respond to key events
		this.setFocusTraversable ( true );

		speedX = PLAYER_SPEED;
		speedY = 0;

		addEventFilters();
		drawPlayer();

		player=this;
	}

	public static void setProtected(int sec, int protectedValue){
		isProtected=protectedValue > 2 ? 2 : protectedValue;
		protectedFor=sec;

		//Setting transparency animation for blink
		fadeTransition = new FadeTransition(Duration.millis(500),player);
		fadeTransition.setAutoReverse(true);
		fadeTransition.setCycleCount(Timeline.INDEFINITE);
		fadeTransition.setFromValue(0);
		fadeTransition.setToValue(1);
		fadeTransition.playFromStart();
	}

	public static void setNotProtected(){
		protectedFor=0;
		isProtected=0;
		fadeTransition.stop();
		fadeTransition = null;
		fadeTransitionReset = new FadeTransition(Duration.millis(1),player);
		fadeTransitionReset.setToValue(1);
		fadeTransitionReset.playFromStart();
	}

	public static void setShield(){
		shieldedFor=10;
		isProtected=2;
		if(fadeTransition != null)
			setNotProtected();
		halfFadeTrasition = new FadeTransition(Duration.millis(500),player);
		halfFadeTrasition.setToValue(0.5);
		halfFadeTrasition.playFromStart();
	}

	public static void removeShield(boolean timeOut){
		if(!timeOut){
			setProtected(5, 1);
		}else{
			setNotProtected();
		}

	}

	private void drawPlayer(){

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

		//Body
		Path body = new Path();
		MoveTo startPoint = new MoveTo(head.getCenterX(), head.getCenterY());
		LineTo drawLine1 = new LineTo(bottomHat.getX(),PLAYER_HEIGHT);
		CubicCurveTo drawLine2 = new CubicCurveTo(head.getCenterX()*0.7,PLAYER_HEIGHT*0.8,PLAYER_WIDTH*0.8,PLAYER_HEIGHT*0.95,PLAYER_WIDTH,PLAYER_HEIGHT*0.9);

		body.getElements().addAll(startPoint,drawLine1,drawLine2, new ClosePath());
		body.setFill(Color.RED);
		body.setStroke(Color.BLACK);
		body.setStrokeWidth(1);

		this.getChildren ( ).addAll ( body, head , topHat, bottomHat, leftEye, rightEye, mouth);
	}

	private void addEventFilters(){

		this.addEventFilter ( KeyEvent.KEY_PRESSED, event -> {
			switch ( event.getCode ( ) ) {
				case RIGHT:
					state = new MovingRightState ( GameModel.getInstance ( ).getPlayer ( ) );
					break;
				case LEFT:
					state = new MovingLeftState ( GameModel.getInstance ( ).getPlayer ( ) );
					break;
			}
		} );

		this.addEventFilter (
				KeyEvent.KEY_RELEASED, event -> {
					if ( event.getCode ( ) == KeyCode.LEFT || event.getCode ( ) == KeyCode.RIGHT ) {
						state = new StandingState ( GameModel.getInstance ( ).getPlayer ( ) );
					}
				}
		);
	}

	@Override
	protected void handleCollisions ( ) {
		if ( position.getX ( ) < GameModel.getInstance().GAME_SCREEN_HORIZONTAL_OFFSTE || position.getX ( ) > GameModel.getInstance ( ).getSceneWidth ( ) - PLAYER_WIDTH - GameModel.getInstance().GAME_SCREEN_HORIZONTAL_OFFSTE) {
			state = new StandingState ( this );
			if ( position.getX ( ) < GameModel.getInstance().GAME_SCREEN_HORIZONTAL_OFFSTE ) {
				setPosition ( new Point2D ( GameModel.getInstance().GAME_SCREEN_HORIZONTAL_OFFSTE, getPosition ( ).getY ( ) ) );
				setTranslateX ( GameModel.getInstance().GAME_SCREEN_HORIZONTAL_OFFSTE );
			}
			
			if ( position.getX ( ) > GameModel.getInstance ( ).getSceneWidth ( ) - PLAYER_WIDTH - GameModel.getInstance().GAME_SCREEN_HORIZONTAL_OFFSTE) {
				setPosition ( new Point2D ( GameModel.getInstance ( ).getSceneWidth ( ) - PLAYER_WIDTH - GameModel.getInstance().GAME_SCREEN_HORIZONTAL_OFFSTE, getPosition ( ).getY ( ) ) );
				setTranslateX ( GameModel.getInstance ( ).getSceneWidth ( ) - PLAYER_WIDTH - GameModel.getInstance().GAME_SCREEN_HORIZONTAL_OFFSTE);
			}
		}
	}
	
	@Override
	public void updatePosition ( ) {
		state.update ( );
		handleCollisions ( );
	}
}

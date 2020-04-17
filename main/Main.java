package main;

import javafx.animation.AnimationTimer;
import javafx.animation.FillTransition;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;
import models.GameModel;
import objects.*;
import objects.weapons.Bullet;
import objects.weapons.Harpoon;
import objects.weapons.Weapon;

import static objects.Player.PLAYER_HEIGHT;
import static objects.Player.PLAYER_WIDTH;

public class Main extends Application {
	
	private AnimationTimer timer;
	private AnimationTimer gameTime;
	private long oldTime = System.currentTimeMillis();
	private static final Integer GAME_LENGTH = 120;
	
	@Override
	public void start ( Stage primaryStage ) {
		Group root = new Group ( );
		primaryStage.setTitle ( "Bubble Split" );
		Scene scene = new Scene ( root, GameModel.getInstance ( ).getSceneWidth ( ), GameModel.getInstance ( ).getSceneHeight ( ) );
		primaryStage.setScene ( scene );
		
		// Disable resizing and maximize button
		primaryStage.setResizable ( false );
		primaryStage.sizeToScene ( );
		
		Ball ball = new Ball ( new Point2D ( 300, 200 ) );
		GameModel.getInstance ( ).getBalls ( ).add ( ball );
		
		Player player = new Player ( new Point2D ( GameModel.getInstance().GAME_SCREEN_HORIZONTAL_OFFSTE*2, GameModel.getInstance ( ).getSceneHeight ( ) - PLAYER_HEIGHT - GameModel.getInstance().GAME_SCREEN_VERTICAL_OFFSTE) );
		GameModel.getInstance ( ).setPlayer ( player );

		//Set game time
		GameModel.setGameLength(GAME_LENGTH);

		//Combo
		TextDisplay comboText = new TextDisplay(new Point2D(GameModel.getInstance().getSceneWidth()/9,25),"Combo!");
		comboText.getTextClass().setFont(new Font(40));
		comboText.getTextClass().setTextAlignment(TextAlignment.CENTER);
		FillTransition comboTextAnimation = new FillTransition(Duration.seconds(2), new Color(1,0,0,1),new Color(1,0,0,0));
		comboText.setAnimation(comboTextAnimation);
		comboTextAnimation.setShape(comboText.getTextClass());
		comboTextAnimation.setOnFinished(event -> GameModel.getInstance().setComboFinished());
		GameModel.getInstance().setComboText(comboText);

		//TimeLeft
		TextDisplay timeLeft = new TextDisplay(new Point2D(GameModel.getInstance().getSceneWidth() / 4.2,GameModel.getInstance().getSceneHeight()/2 - GameModel.getInstance().GAME_SCREEN_VERTICAL_OFFSTE/4), GAME_LENGTH.toString());
		timeLeft.getTextClass().setFont(new Font(30));
		timeLeft.getTextClass().setTextAlignment(TextAlignment.LEFT);
		timeLeft.getTextClass().fillProperty().set(Color.BLUE);
		Rectangle timeLeftRectangle = new Rectangle(0, 0,
				GameModel.getInstance ( ).getSceneWidth ( )*GameModel.getInstance().GAME_SCREEN_SCALE_FACTOR,
				GameModel.getInstance ( ).GAME_SCREEN_VERTICAL_OFFSTE/2
		);
		timeLeftRectangle.setTranslateX(GameModel.getInstance().GAME_SCREEN_HORIZONTAL_OFFSTE);
		timeLeftRectangle.setTranslateY(GameModel.getInstance().getSceneHeight() - GameModel.getInstance().GAME_SCREEN_VERTICAL_OFFSTE/1.2);
		timeLeftRectangle.setFill(Color.RED);
		double timeLeftRectangleChange = timeLeftRectangle.getWidth()/(double)GAME_LENGTH;
		GameModel.getInstance().setTimeLeftText(timeLeft);
		GameModel.getInstance().setTimeLeftBar(timeLeftRectangle);

		//Score
		TextDisplay score = new TextDisplay(new Point2D(GameModel.getInstance().getSceneWidth()/2 - GameModel.getInstance().getSceneWidth()/10, 20), "Score: 0");
		score.getTextClass().setFont(new Font(40));
		score.getTextClass().fillProperty().set(Color.RED);
		GameModel.getInstance().setScoreText(score);

		//Won-Lost
		TextDisplay gameStatus = new TextDisplay(new Point2D(GameModel.getInstance().getSceneWidth()/4,GameModel.getInstance().getSceneHeight()/5),"");
		gameStatus.getTextClass().setFont(new Font(60));

		//BrickBackground
		Image img = new Image("brickWall.jpg");
		ImagePattern brickWallPattern = new ImagePattern(img,0,0,1,1,true);
		Background brickWall = new Background(0,0,GameModel.getInstance().getSceneWidth(), GameModel.getInstance().getSceneHeight(),brickWallPattern);

		root.getChildren ( ).addAll (brickWall, new Background ( ), ball, player, timeLeftRectangle, timeLeft, score);
		GameModel.getInstance ( ).setRoot ( root );
		Life.drawStarterLifes();
		
		scene.setOnKeyPressed ( event -> {
			if ( event.getCode ( ) == KeyCode.SPACE ) {
				root.getChildren ( ).remove ( GameModel.getInstance ( ).getWeapon ( ) );
				Weapon harpoon = new Harpoon ( player.getPosition ( ) .add ( 0.5 * PLAYER_WIDTH, 0 ) );
			}

			if(event.getCode() == KeyCode.P){
				timer.stop();
			}
		} );

		primaryStage.show ( );
		
		timer = new AnimationTimer ( ) {
			@Override
			public void handle ( long l ) {

				//timeLeftRectangle gets shorter
				timeLeftRectangle.setWidth(timeLeftRectangle.getWidth() - timeLeftRectangleChange/60);

				//GameStatus
				if(GameModel.getInstance().getBalls().isEmpty()){
					GameModel.getInstance().setGameWon(true);
				}
				if ( GameModel.getInstance ( ).isGameLost ( ) || GameModel.getInstance ( ).isGameWon ( ) ) {
					if(GameModel.getInstance().isGameLost()){
						gameStatus.setTextValue("YOU LOST!");
						gameStatus.getTextClass().fillProperty().set(Color.RED);
						gameStatus.setTranslateX(gameStatus.getPosition().getX()-160);
						root.getChildren().add(gameStatus);
					}
					if (GameModel.getInstance().isGameWon()) {
						gameStatus.setTextValue("YOU WON!");
						gameStatus.getTextClass().fillProperty().set(Color.GREEN);
						gameStatus.setTranslateX(gameStatus.getPosition().getX()-140);
						root.getChildren().add(gameStatus);
					}
					timer.stop ( );
				}

				//Position updating
				for ( Ball ball : GameModel.getInstance ( ).getBalls ( ) ) {
					if(System.currentTimeMillis() - ball.getTimeCreated() > 1000)
						ball.setJustCreated(false);
					ball.updatePosition ( );
				}

				for(Dollar dollar : GameModel.getInstance().getDollars()){
					dollar.updatePosition();
				}

				for(Shield shield : GameModel.getInstance().getShields()){
					shield.updatePosition();
				}

				for (Clock clock : GameModel.getInstance().getClocks()){
					clock.updatePosition();
				}

				GameModel.getInstance ( ).getPlayer ( ).updatePosition ( );
				if ( GameModel.getInstance ( ).getWeapon ( ) != null ) {
					GameModel.getInstance ( ).getWeapon ( ).updatePosition ( );
				}
				
			}
		};
		timer.start ( );

		gameTime = new AnimationTimer(){
			@Override
			public void handle(long l){
				if(GameModel.getInstance().isGameLost() || GameModel.getInstance().isGameWon()){
					gameTime.stop();
					return;
				}

				//One sec past
				if(System.currentTimeMillis() - oldTime >= 1000){
					oldTime = System.currentTimeMillis();

					//Protection check
					if(Player.isProtected > 0){
						if(Player.isProtected == 1){
							Player.protectedFor--;
							if(Player.protectedFor == 0)
								Player.setNotProtected();
						}else{
							Player.shieldedFor--;
							if(Player.shieldedFor == 0)
								Player.removeShield(true);
						}

					}

					//Write to upper left corner
					int last = Integer.parseInt(timeLeft.getTextClass().getText());
					last--;
					timeLeft.setTextValue(Integer.toString(last));
					if(last==0) {
						GameModel.getInstance().setGameLost(true);
						gameTime.stop();
					}
				}
			}
		};
		gameTime.start();

	}
	
	public static void main ( String[] args ) {
		launch ( args );
	}
}

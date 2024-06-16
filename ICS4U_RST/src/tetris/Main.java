package tetris;

import java.util.ArrayList;

import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * @author jake pommainville and rohan daves
 * 
 * date: 6/7/24
 */
public class Main extends Application{

	private GridPane grdTetris;
	private GridPane grdHold;
	private GridPane grdNext;
	
	private StackPane stkAllScreens;  // A stackpane which hold all the screens on the application
	
	private VBox vbxPauseScreen;   // HBox holding all the elements of the pause screen
	private VBox vbxGameOverScreen;  // VBox holding all elements of game over screen
	
	private Grid grid;
	private int shapeSpeed;
	private boolean running;
	private boolean fistStart;
	
	private MediaPlayer music;
	
	private Label lblscore;
	private Label lbllevel;
	private Label lbllines;
	private Label lblFinalScore;
	private Label lblHighscore;
	
	private int highscore;
	
	private SequentialTransition animation;
	PauseTransition pauseTransition;
	
	final private static int SIZE = 40;
	final private static int SPACE = -1;
	final private static int HUGE_FONT = 70;  // Font sizes used for labels within the pause and game over screens
	final private static int MEDIUM_FONT = 25;
	
	Stage stage;
	
	@Override
	public void stop() {
		grid.save();
	}
	
	@Override
	public void start(Stage stage) throws Exception {
		highscore = 0;
		stkAllScreens = new StackPane();   // This stackpane acts as the root of the application
				
		this.stage = stage;
		
		try {
			music = new MediaPlayer(new Media(Main.class.getClassLoader().getResource("music/Tetris.mp3").toURI().toString()));  // uses MediaPlayer to play music
			music.volumeProperty().set(0.05);  // Turns the volume down
			music.setAutoPlay(true);  // Autoplays the music (when completed, it will restart)
			music.setCycleCount(MediaPlayer.INDEFINITE);  // Restarts the music indefinitely
			//music.play();
			
			music.setOnEndOfMedia(new Runnable() {   // Learned from  https://stackoverflow.com/questions/43190594/javafx-mediaplayer-loop
				@Override
		        public void run() {   // Ensures that the music keeps repeating
		        	music.seek(Duration.ZERO);
		        	music.play();
		        }
		    });
		} catch (Exception e) {   // If something goes wrong
			e.printStackTrace();  // Prints the stacktrace to help with understanding the issue and troubleshooting
		}
		
		
		// creating the gridpane to serve as the grid for the tetris blocks
		grdTetris = new GridPane();   // Instantiating the gridPane which will hold the tetris game
		// sets the spacing
		grdTetris.setVgap(SPACE);   // Setting the spacing between rows and columns
		grdTetris.setHgap(SPACE);
		grdTetris.setPadding(new Insets(SPACE));  // Setting padding for the gridpane
		
		// creating the constraints for each cell
		for (int i = 0; i < Grid.WIDTH; i++) {
			grdTetris.getColumnConstraints().add(new ColumnConstraints(SIZE));
			grdTetris.getRowConstraints().add(new RowConstraints(SIZE));
		}
		
		Label lblHold = new Label("Held Shape:");
		lblHold.setFont(Font.font(STYLESHEET_MODENA, FontWeight.BOLD, 25));
		
		// creating the gridpane to serve as the grid for the tetris blocks
		grdHold = new GridPane();
		// sets the spacing
		grdHold.setVgap(0);
		grdHold.setHgap(0);
		grdHold.setPadding(new Insets(0));
		
		// creating the constraints for each cell
		for (int i = 0; i < Grid.WIDTH-6; i++) {
			grdHold.getColumnConstraints().add(new ColumnConstraints(SIZE));
			grdHold.getRowConstraints().add(new RowConstraints(SIZE));
		}
		
		VBox vbxHold = new VBox(lblHold, grdHold);  // Initializing the "hold" vbox
		vbxHold.setPadding(new Insets(5));  // Setting padding for the vbox
		vbxHold.setSpacing(10);   // Setting spacing for the vbox
		
		Rectangle rectPause = new Rectangle(150,150);  // Makes a rectangle
		rectPause.setFill(Color.LIGHTGRAY);  // Painting the rectangle light gray
		rectPause.setStroke(Color.BLACK);   // Painting the outline of the rectangle black
	
		Image imgSmallPause = new Image(getClass().getResource("/img/miniPause.png").toString());
		ImageView smallPauseLogo = new ImageView(imgSmallPause);  // Small pause logo
		
		StackPane pauseButton = new StackPane();  // Stackpane which will countain all elements of the pause button
		pauseButton.getChildren().addAll(rectPause, smallPauseLogo);  // Stacks the pause logo on top of the rectanlge
		
		pauseButton.setOnMouseClicked(e -> {   // If the stackpane containing pause logo is clicked
			if (running) {    // If the game is running
				animation.pause();  // Pause animation
				music.pause();   // Pause music
				running = false;  // Make it so the game is not running
				showPauseScreen();  // Show the pause screen
			} else {  // If the game is not running
				animation.play();   // Play the animation
				music.play();  // Play the music
				running = true;   // Run the game again
			}
		});
		
		VBox vbxRightSide = new VBox(vbxHold, pauseButton);  // VBox for area to the right of the tetris grid
		vbxRightSide.setPadding(new Insets(5));  // Set the padding and spacing of the grid
		vbxRightSide.setSpacing(425);
		
		Label lblNext = new Label("Next Shape:");  // Label to indicate the upcoming shape
		lblNext.setFont(Font.font(STYLESHEET_MODENA, FontWeight.BOLD, 25));  // Setting the font, font weight, and font size of the label
		
		// creating the gridpane to serve as the grid for the tetris blocks
		grdNext = new GridPane();   // Gridpane which still store the next shape
		// sets the spacing
		grdNext.setVgap(0);  // Setting the gaps between columns, rows and padding to 0
		grdNext.setHgap(0);
		grdNext.setPadding(new Insets(0));
		
		// creating the constraints for each cell
		for (int i = 0; i < Grid.WIDTH-6; i++) {
			grdNext.getColumnConstraints().add(new ColumnConstraints(SIZE));
			if (i < 2) {
				grdNext.getRowConstraints().add(new RowConstraints(SIZE));
			}
		}
		
		VBox vbxNext = new VBox(lblNext, grdNext);  // Creating a gridpane for the next shape
		vbxNext.setPadding(new Insets(5));  // Setting some padding and spacing for this gridpane
		vbxNext.setSpacing(10);
				
		lblscore = new Label("Score: 0");   // Default label for the player's score
		lblscore.setFont(Font.font(STYLESHEET_MODENA, FontWeight.BOLD, 25));  // Setting the font, font weight, and font size of the label
		
		lbllevel = new Label("Level: 0");  // Default label and value for the player's level
		lbllevel.setFont(Font.font(STYLESHEET_MODENA, FontWeight.BOLD, 25)); // Setting the font, font weight, and font size of the label

		lbllines = new Label("Cleared Lines: 0");  // Default label and value for the number of lines cleared by the user
		lbllines.setFont(Font.font(STYLESHEET_MODENA, FontWeight.BOLD, 25)); // Setting the font, font weight, and font size of the label
		
		lblHighscore = new Label("Highscore: 0");  // Defauly label and value for the user's highscore (unless a highscore has been loaded with save file)
		lblHighscore.setFont(Font.font(STYLESHEET_MODENA, FontWeight.BOLD, 25));
		
		VBox vbxLabels = new VBox(lblscore, lbllevel, lbllines, lblHighscore);  // Places all labels into a vbox
		vbxLabels.setPadding(new Insets(5));  // Setting spacing and padding for elements within the vbox
		vbxLabels.setSpacing(10);  // Setting the font, font weight, and font size of the label
		
		Label lblControlTitle = new Label("Controls:");  // Label for the controls
		lblControlTitle.setFont(Font.font(STYLESHEET_MODENA, FontWeight.BOLD, 25));  // Setting the font, font weight, and font size of the label
		
		Label lblControls = new Label(			 // Labels for the different controls needed to play the game
				"A -> left"
				+ "\nD -> Right"
				+ "\nS -> Soft Drop"
				+ "\nSPACE -> Hard Drop"
				+ "\nQ -> Rotate Left"
				+ "\nE -> Rotate Right"
				+ "\nG -> Save"
				+ "\nH -> Load"
				+ "\nCRTL -> Pause");
		lblControls.setFont(Font.font(STYLESHEET_MODENA, FontWeight.BOLD, 25));  // Setting the font, font weight, and font size of the label
		
		VBox vbxControls = new VBox(lblControlTitle, lblControls);  // VBox holding the label and text for the game controls
		vbxControls.setPadding(new Insets(5));  // Setting padding and spacing for elements within vbox
		vbxControls.setSpacing(10);
		
		VBox vbxLeftSide = new VBox(vbxNext, vbxLabels, vbxControls);  // VBox holding all visuals on left side of tetris grid
		vbxLeftSide.setPadding(new Insets(5));  // Setting padding and spacing for elements within vbox
		vbxLeftSide.setSpacing(50);
		
		// creating an hbox to serve as the root
		HBox gameRoot = new HBox();  // An HBox to hold the game screen
		// setting the spacing
		gameRoot.setPadding(new Insets(5));
		gameRoot.setSpacing(300);
		// adding the tetris grid to the root
		gameRoot.getChildren().addAll(vbxLeftSide, grdTetris, vbxRightSide);
		
		
		//   Pause Screen
		
		
		Label lblPaused = new Label("Paused");   // Title of pause screen
		lblPaused.setFont(Font.font("Impact", HUGE_FONT));  // Setting the font, and font size of the label
		lblPaused.setStyle("-fx-text-fill: #FF0000");   // Color of text
		
		Image imgPause = new Image(getClass().getResource("/img/pauseSymbol.png").toString());
		ImageView pauseLogo = new ImageView(imgPause);  // Image of pause symbol
		
		Label lblResume = new Label("Press Control to Resume");   // Title of pause screen
		lblResume.setFont(Font.font("Impact", MEDIUM_FONT));   // Setting the font, and font size of the label
		lblResume.setStyle("-fx-text-fill: #FF0000");  // Color of text
		
		
		vbxPauseScreen = new VBox(10);  // Instantiating new Vbox with spacing
		
		vbxPauseScreen.getChildren().addAll(lblPaused, pauseLogo, lblResume);  // Adding the pause label, logo, and resume instructions 
		vbxPauseScreen.setAlignment(Pos.CENTER);  // Centering all elements in the vbox
		vbxPauseScreen.setStyle("-fx-background-color: rgb(211,211,211,0.85)");  //  Changes the background color of the pause screen
		
	
		//  Game over screen
		
		Label lblGameOver = new Label ("Game Over");   // Label of the game over screen
		lblGameOver.setFont(Font.font("Impact", HUGE_FONT));  // Font and font size of label
		lblGameOver.setStyle("-fx-text-fill: #FF0000");  // Label color
		
		lblFinalScore = new Label();  // Final score label (after player loses)
		lblFinalScore.setFont(Font.font("Impact", MEDIUM_FONT));  // Font and font size
		lblFinalScore.setStyle("-fx-text-fill: #00ff00");  // Font color
		
		Label lblRestart = new Label("Press  n  to Start New Game");   // Instructions to start a new game
		lblRestart.setFont(Font.font("Impact", MEDIUM_FONT));   //  Font and font size
		lblRestart.setStyle("-fx-text-fill: #FF0000");  // Colors the text
		
		vbxGameOverScreen = new VBox(30);  // Instantiating new VBox for game over
		vbxGameOverScreen.getChildren().addAll(lblGameOver, lblFinalScore, lblRestart);  // ADding the game over elements to the vbox
		vbxGameOverScreen.setAlignment(Pos.CENTER);  // Centering elements within the VBox
		vbxGameOverScreen.setStyle("-fx-background-color: #000000");  // Coloring the background
		
		
		// Stack pane holding all possible elements
		
		stkAllScreens.getChildren().addAll(gameRoot); // Adding the game root (tetris game screen) to the stackPane (the root)
		
		
		// creating a new scene with the root as the root node
		Scene scene = new Scene(stkAllScreens);  // Creating a new scene with the root
		
		running = true;  // Game is running
		
		scene.setOnKeyPressed(e -> {   // Detects if a key is pressed on the scene
			if (running) {   // If the game is running
				if (e.getCode().equals(KeyCode.S)) {   
					grid.moveDown();
					updateGridColor();
				} else if (e.getCode().equals(KeyCode.A)) {
					grid.moveLeft();
					updateGridColor();
				} else if (e.getCode().equals(KeyCode.D)) {
					grid.moveRight();
					updateGridColor();
				} else if (e.getCode().equals(KeyCode.Q)) {
					grid.rotateLeft();
					updateGridColor();
				} else if (e.getCode().equals(KeyCode.E)) {
					grid.rotateRight();
					updateGridColor();
				} else if (e.getCode().equals(KeyCode.C)) {
					grid.hold();
					updateGridColor();
				} else if (e.getCode().equals(KeyCode.SPACE)) {
					grid.drop();
					updateGridColor();
				} else if (e.getCode().equals(KeyCode.CONTROL)) {
					animation.pause();
					music.pause();
					running = false;
					showPauseScreen();
				} else if (e.getCode().equals(KeyCode.G)) {
					grid.save();
					updateGridColor();
				} else if (e.getCode().equals(KeyCode.H)) {
					grid.load();
					updateGridColor();
				}
			} else {  // If the game is not running
				if (grid.getGameOver() == false && e.getCode().equals(KeyCode.CONTROL)) {  // If the game is not over and control is clicked
					hidePauseScreen();  // Hide the pause screen, unpause the game
					animation.play(); // Resume the animation
					running = true;  // Make the game run
					music.play();   // Play the music again
				} else if (grid.getGameOver() == false && e.getCode().equals(KeyCode.G)) {  // If the key pressed is G when the game is paused
					grid.save();  // Save the grid
					updateGridColor();
				} else if (grid.getGameOver() == false && e.getCode().equals(KeyCode.H)) {  // If the key pressed is H when the game is paused
					grid.load();  // Load the save
					updateGridColor();
				} else if (grid.getGameOver() == true && e.getCode().equals(KeyCode.N)) {  // If the game is over and N is pressed
		            startNewGame();  // Start a new game
		            music.play();  // Play music
		            stkAllScreens.getChildren().remove(vbxGameOverScreen);  // Remove the gameOverScreen
		            running = true;  // Run the game
		        }
			}
		});
		
		fistStart = true;
		
		startNewGame();  // Initial start of the game
	
		
		// setting the scene to the scene
		stage.setScene(scene);
		// changing the title
		stage.setTitle("Tetris");
		// setting icon image
		stage.getIcons().add(new Image(getClass().getResource("/img/Tetris_logo.png").toString()));
		// showing the scene
		stage.show();
	}
	
	/**
	 * starts a new game
	 */
	private void startNewGame() {			
		grid = new Grid(highscore);
		
		if (fistStart) {
			grid.load();
		}
		
		shapeSpeed = grid.getSpeed();
		
		updateGridColor();
		updateBlocks();
	}
	
	
	/** 
	 * Shows the pause screen when the user clicks the pause button
	 */
	private void showPauseScreen() {
		stkAllScreens.getChildren().add(vbxPauseScreen);
	}
	
	/**
	 * Removes the pause screen from the stackpane
	 */
	private void hidePauseScreen() {
		stkAllScreens.getChildren().remove(vbxPauseScreen);
	}
	
	/**
	 * moves the blocks down after a specified time
	 */
	private void updateBlocks() {
		shapeSpeed = grid.getSpeed();
				
		if (!fistStart) {
			animation.stop();
		} else {
			fistStart = false;
		}
		
		animation = new SequentialTransition();
		PauseTransition pauseTransition = new PauseTransition(Duration.millis(shapeSpeed));
		pauseTransition.setOnFinished(e -> {  // Only moves blocks if game is still going
			if (!grid.getGameOver()) {
				grid.moveDown();
				updateGridColor();
			} else {
				showGameOverScreen();
				animation.stop();
				music.pause();
			}
		});
				
		animation.getChildren().add(pauseTransition);
		animation.setCycleCount(Timeline.INDEFINITE);
		animation.play();
	}

	/**
	 * updates each cell on the grid and changes it's color based on the block
	 */
	private void updateGridColor() {
		if (!grid.getGameOver()) {
			grdTetris.getChildren().clear();
			
			if (shapeSpeed != grid.getSpeed()) {
				animation.stop();
				updateBlocks();
			}
			
			ArrayList<Block> blocks = grid.getBlocks();
			
			for (int i = 0; i < Grid.HEIGHT; i++) {
				for (int j = 0; j < Grid.WIDTH; j++) {
					Block currentBlock = new Block(j, i);
					Group group = new Group();
					Rectangle square = new Rectangle(SIZE, SIZE);
					square.setStroke(Color.DIMGRAY);
					
					if (blocks.contains(currentBlock)) {
						group = getGroup(getColor(blocks.get(blocks.indexOf(currentBlock)).getType()));
					} else {
						square.setFill(Color.BLACK);
						group.getChildren().add(square);
					}
										
					grdTetris.add(group, j, i);
				}
			}
			
			updateInfoGrid(true, grdHold);
			updateInfoGrid(false, grdNext);
			updateLabels();
		} else {
			highscore = grid.getHighscore();
			showGameOverScreen();
			animation.stop();
			music.pause();
		}
	}
	
	/*
	 * Displays the game over screen
	 */
	private void showGameOverScreen() {
		updateLabels();
		
		running = false;
		
		stkAllScreens.getChildren().add(vbxGameOverScreen);
	}
	
	/**
	 * updates the games labels
	 */
	private void updateLabels() {
		lblscore.setText("Score: "+grid.getScore());
		lbllevel.setText("level: "+grid.getLevel());
		lbllines.setText("Cleared Lines: "+grid.getLines());
		lblFinalScore.setText("Final Score: " + grid.getScore());
		lblHighscore.setText("Highscore: " + grid.getHighscore());
	}

	/**
	 * A method to return the correct color for the correct shape type
	 * 
	 * @param type
	 * an {@code int} for the type of the shape
	 * 
	 * @return
	 * the {@code Color} of the shape
	 */
	private Color getColor(int type) {
		switch (type) {
		case 1:
			return Color.DEEPSKYBLUE;
		case 2:
			return Color.ORANGE;
		case 3:
			return Color.BLUE;
		case 4:
			return Color.GOLD;
		case 5:
			return Color.LIMEGREEN;
		case 6:
			return Color.DARKORCHID;
		case 7:
			return Color.RED;
		}
		
		return null;
	}
	
	/**
	 * returns a group containing a square with shading
	 * 
	 * @param color
	 * a {@code Color} for the square in the group
	 * 
	 * @return
	 * an {@code Group} containing a square with shading
	 */
	private Group getGroup(Color color) {
		double thickness = (double) SIZE / 7.0;
		
		Group group = new Group();
		
		Polygon light = new Polygon();
		light.setOpacity(0.3);
		light.setFill(Color.WHITE);
		light.getPoints().addAll(new Double[] {
				0.0,0.0,
				(double)SIZE,0.0,
				(double)SIZE-thickness, thickness, 
				thickness, thickness, 
				thickness,(double)SIZE - thickness, 
				0.0, (double)SIZE
		});
		
		Polygon dark = new Polygon();
		dark.setOpacity(0.25);
		dark.setFill(Color.BLACK);
		dark.getPoints().addAll(new Double[] {
				(double)SIZE,(double)SIZE,
				0.0,(double)SIZE,
				thickness,(double)SIZE-thickness,
				(double)SIZE-thickness,(double)SIZE-thickness,
				(double)SIZE-thickness,thickness,
				(double)SIZE,0.0
		});
		
		Rectangle square = new Rectangle(SIZE, SIZE);
		square.setStroke(Color.BLACK);
		square.setFill(color);
		
		group.getChildren().addAll(square,light, dark);
		
		return group;
	}
	
	/**
	 * a method to update the next shape grid or the held shape grid
	 * 
	 * @param typeOfGrid
	 * a {@code boolean} for the type of grid being updated (true for hold and false for next)
	 * 
	 * @param gridPane
	 * the {@code GridPane} being updated
	 */
	private void updateInfoGrid(boolean typeOfGrid, GridPane gridPane) {
		int typeOfShape = 1;

		Shape shape = null;
		Color color;

		
		gridPane.getChildren().clear();
		
		if (typeOfGrid == true) {
			if (grid.getHold() != null) {
				shape = grid.getHold();
				typeOfShape = grid.getHold().getType();
			}
		} else {
			if (grid.getNext() != null) {
				shape = grid.getNext();
				typeOfShape = grid.getNext().getType();
			}
		}
		
		if (shape != null) {			
			// gets the color of the block
			color = getColor(typeOfShape);
						
			if (typeOfShape != 4 && typeOfShape != 5 && typeOfShape != 2 && typeOfShape != 6) {
				Group group = getGroup(color);
				gridPane.add(group, 0, 0);
			}
			
			if (typeOfShape != 2 && typeOfShape != 3) {
				Group group = getGroup(color);
				gridPane.add(group, 1, 0);
			}
			
			if (typeOfShape != 7 && typeOfShape != 3 && typeOfShape != 6) {
				Group group = getGroup(color);
				gridPane.add(group, 2, 0);
			}
			
			if (typeOfShape == 1) {
				Group group = getGroup(color);
				gridPane.add(group, 3, 0);
			} else {
				Group group = getGroup(color);
				gridPane.add(group, 1, 1);
			}
			
			if (typeOfShape != 5 && typeOfShape != 1) {
				Group group = getGroup(color);
				gridPane.add(group, 2, 1);
			}
			
			if (typeOfShape != 1 && typeOfShape != 7 && typeOfShape != 4) {
				Group group = getGroup(color);
				gridPane.add(group, 0, 1);
			}
		}
		
	}

	public static void main(String args[]) {
		launch(args);
	}
	

}

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
	final private static int HUGE_FONT = 70;
	final private static int MEDIUM_FONT = 25;
	
	Stage stage;
	
	@Override
	public void stop() {
		grid.save();
	}
	
	@Override
	public void start(Stage stage) throws Exception {
		highscore = 0;
		stkAllScreens = new StackPane();
				
		this.stage = stage;
		// TODO get music to loop
		try {
			music = new MediaPlayer(new Media(Main.class.getClassLoader().getResource("music/Tetris.mp3").toURI().toString()));
			music.volumeProperty().set(0.05);
			music.setAutoPlay(true);
			music.setCycleCount(MediaPlayer.INDEFINITE);
			//music.play();
			
			music.setOnEndOfMedia(new Runnable() {   // Learned from  https://stackoverflow.com/questions/43190594/javafx-mediaplayer-loop
				@Override
		        public void run() {
		        	music.seek(Duration.ZERO);
		        	music.play();
		        }
		    });
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		// creating the gridpane to serve as the grid for the tetris blocks
		grdTetris = new GridPane();
		// sets the spacing
		grdTetris.setVgap(SPACE);
		grdTetris.setHgap(SPACE);
		grdTetris.setPadding(new Insets(SPACE));
		
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
		
		VBox vbxHold = new VBox(lblHold, grdHold);
		vbxHold.setPadding(new Insets(5));
		vbxHold.setSpacing(10);
		
		Rectangle rectPause = new Rectangle(150,150);  // Makes a rectangle
		rectPause.setFill(Color.LIGHTGRAY);
		rectPause.setStroke(Color.BLACK);
	
		Image imgSmallPause = new Image(getClass().getResource("/img/miniPause.png").toString());
		ImageView smallPauseLogo = new ImageView(imgSmallPause);  // Small pause logo
		
		StackPane pauseButton = new StackPane();
		pauseButton.getChildren().addAll(rectPause, smallPauseLogo);  // Stacks the pause logo on top of the rectanlge
		
		pauseButton.setOnMouseClicked(e -> {   // IF the stackpane containing pause logo is clicked
			if (running) {
				animation.pause();
				music.pause();
				running = false;
				showPauseScreen();
			} else {
				animation.play();
				music.play();
				running = true;
			}
		});
		
		VBox vbxRightSide = new VBox(vbxHold, pauseButton);
		vbxRightSide.setPadding(new Insets(5));
		vbxRightSide.setSpacing(425);
		
		Label lblNext = new Label("Next Shape:");
		lblNext.setFont(Font.font(STYLESHEET_MODENA, FontWeight.BOLD, 25));
		
		// creating the gridpane to serve as the grid for the tetris blocks
		grdNext = new GridPane();
		// sets the spacing
		grdNext.setVgap(0);
		grdNext.setHgap(0);
		grdNext.setPadding(new Insets(0));
		
		// creating the constraints for each cell
		for (int i = 0; i < Grid.WIDTH-6; i++) {
			grdNext.getColumnConstraints().add(new ColumnConstraints(SIZE));
			if (i < 2) {
				grdNext.getRowConstraints().add(new RowConstraints(SIZE));
			}
		}
		
		VBox vbxNext = new VBox(lblNext, grdNext);
		vbxNext.setPadding(new Insets(5));
		vbxNext.setSpacing(10);
				
		lblscore = new Label("Score: 0");
		lblscore.setFont(Font.font(STYLESHEET_MODENA, FontWeight.BOLD, 25));
		
		lbllevel = new Label("Level: 0");
		lbllevel.setFont(Font.font(STYLESHEET_MODENA, FontWeight.BOLD, 25));

		lbllines = new Label("Cleared Lines: 0");
		lbllines.setFont(Font.font(STYLESHEET_MODENA, FontWeight.BOLD, 25));
		
		lblHighscore = new Label("Highscore: 0");
		lblHighscore.setFont(Font.font(STYLESHEET_MODENA, FontWeight.BOLD, 25));
		
		VBox vbxLabels = new VBox(lblscore, lbllevel, lbllines, lblHighscore);
		vbxLabels.setPadding(new Insets(5));
		vbxLabels.setSpacing(10);
		
		Label lblControlTitle = new Label("Controls:");
		lblControlTitle.setFont(Font.font(STYLESHEET_MODENA, FontWeight.BOLD, 25));
		
		Label lblControls = new Label(			
				"A -> left"
				+ "\nD -> Right"
				+ "\nS -> Soft Drop"
				+ "\nSPACE -> Hard Drop"
				+ "\nQ -> Rotate Left"
				+ "\nE -> Rotate Right"
				+ "\nC -> Hold"
				+ "\nG -> Save"
				+ "\nH -> Load"
				+ "\nCRTL -> Pause");
		lblControls.setFont(Font.font(STYLESHEET_MODENA, FontWeight.BOLD, 25));
		
		VBox vbxControls = new VBox(lblControlTitle, lblControls);
		vbxControls.setPadding(new Insets(5));
		vbxControls.setSpacing(10);
		
		VBox hbxLeftSide = new VBox(vbxNext, vbxLabels, vbxControls);
		hbxLeftSide.setPadding(new Insets(5));
		hbxLeftSide.setSpacing(35);
		
		// creating an hbox to serve as the root
		HBox root = new HBox();
		// setting the spacing
		root.setPadding(new Insets(5));
		root.setSpacing(300);
		// adding the tetris grid to the root
		root.getChildren().addAll(hbxLeftSide, grdTetris, vbxRightSide);
		
		
		//   Pause Screen
		
		
		Label lblPaused = new Label("Paused");   // Title of pause screen
		lblPaused.setFont(Font.font("Impact", HUGE_FONT));
		lblPaused.setStyle("-fx-text-fill: #FF0000");
		
		Image imgPause = new Image(getClass().getResource("/img/pauseSymbol.png").toString());
		ImageView pauseLogo = new ImageView(imgPause);  // Image of pause symbol
		
		Label lblResume = new Label("Press Control to Resume");   // Title of pause screen
		lblResume.setFont(Font.font("Impact", MEDIUM_FONT));
		lblResume.setStyle("-fx-text-fill: #FF0000");
		
		
		vbxPauseScreen = new VBox(10);
		
		vbxPauseScreen.getChildren().addAll(lblPaused, pauseLogo, lblResume);
		vbxPauseScreen.setAlignment(Pos.CENTER);  // Centering all elements in the vbox
		vbxPauseScreen.setStyle("-fx-background-color: rgb(211,211,211,0.85)");
		
	
		//  Game over screen
		
		Label lblGameOver = new Label ("Game Over");
		lblGameOver.setFont(Font.font("Impact", HUGE_FONT));
		lblGameOver.setStyle("-fx-text-fill: #FF0000");
		
		lblFinalScore = new Label();
		lblFinalScore.setFont(Font.font("Impact", MEDIUM_FONT));
		lblFinalScore.setStyle("-fx-text-fill: #00ff00");
		
		Label lblRestart = new Label("Press  n  to Start New Game");   // Title of pause screen
		lblRestart.setFont(Font.font("Impact", MEDIUM_FONT));
		lblRestart.setStyle("-fx-text-fill: #FF0000");
		
		vbxGameOverScreen = new VBox(30);
		vbxGameOverScreen.getChildren().addAll(lblGameOver, lblFinalScore, lblRestart);
		vbxGameOverScreen.setAlignment(Pos.CENTER);
		vbxGameOverScreen.setStyle("-fx-background-color: #000000");
		
		
		// Stack pane holding all possible elements
		
		stkAllScreens.getChildren().addAll(root);
		
		
		// creating a new scene with the root as the root node
		Scene scene = new Scene(stkAllScreens);
		
		running = true;
		
		scene.setOnKeyPressed(e -> {
			if (running) {
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
			} else {
				if (grid.getGameOver() == false && e.getCode().equals(KeyCode.CONTROL)) {
					hidePauseScreen();
					animation.play();
					running = true;
					music.play();
				} else if (grid.getGameOver() == false && e.getCode().equals(KeyCode.G)) {
					grid.save();
					updateGridColor();
				} else if (grid.getGameOver() == false && e.getCode().equals(KeyCode.H)) {
					grid.load();
					updateGridColor();
				} else if (grid.getGameOver() == true && e.getCode().equals(KeyCode.N)) {
		            startNewGame();
		            music.play();
		            stkAllScreens.getChildren().remove(vbxGameOverScreen);
		            running = true;
		        }
			}
		});
		
		fistStart = true;
		
		startNewGame();
	
		
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
		// creates a new grid with the highscore
		grid = new Grid(highscore);
		
		// checks if this is the first start
		if (fistStart) {
			// loads from the file
			grid.load();
		}
		
		// gets the speed of the game
		shapeSpeed = grid.getSpeed();
		
		// updates the cells on the grid
		updateGridColor();
		// creates the sequential transition
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
		// gets the speed of the game
		shapeSpeed = grid.getSpeed();
				
		// checks if this is the first launch
		if (!fistStart) {
			// stops the transition if not
			animation.stop();
		} else {
			// sets firstStart to false
			fistStart = false;
		}
		
		// creates a new sequential transition
		animation = new SequentialTransition();
		// creates a new pause transition with the time specified by the game
		PauseTransition pauseTransition = new PauseTransition(Duration.millis(shapeSpeed));
		pauseTransition.setOnFinished(e -> {  // Only moves blocks if game is still going
			// checks if the game is not over
			if (!grid.getGameOver()) {
				// moves the blocks down
				grid.moveDown();
				// updates the cells in the grid
				updateGridColor();
			}
		});
				
		// adds the pause transition to the sequential transition
		animation.getChildren().add(pauseTransition);
		// sets the cycle count to inefinete
		animation.setCycleCount(Timeline.INDEFINITE);
		// plays the transition
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
		// checks each type
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
		
		// returns null else
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
		// a variable for the width of the shading
		double thickness = (double) SIZE / 7.0;
		
		// creating a group to store everything in
		Group group = new Group();
		
		// creating a polygon for the light shading
		Polygon light = new Polygon();
		// setting the opacity
		light.setOpacity(0.3);
		// setting the color
		light.setFill(Color.WHITE);
		// creating the points
		light.getPoints().addAll(new Double[] {
				0.0,0.0,
				(double)SIZE,0.0,
				(double)SIZE-thickness, thickness, 
				thickness, thickness, 
				thickness,(double)SIZE - thickness, 
				0.0, (double)SIZE
		});
		
		// creating a polygon for the dark shading
		Polygon dark = new Polygon();
		// setting the opacity
		dark.setOpacity(0.25);
		// setting the color
		dark.setFill(Color.BLACK);
		// creating the points
		dark.getPoints().addAll(new Double[] {
				(double)SIZE,(double)SIZE,
				0.0,(double)SIZE,
				thickness,(double)SIZE-thickness,
				(double)SIZE-thickness,(double)SIZE-thickness,
				(double)SIZE-thickness,thickness,
				(double)SIZE,0.0
		});
		
		// creating a rectangle the size of a cell
		Rectangle square = new Rectangle(SIZE, SIZE);
		// setting the outline to black
		square.setStroke(Color.BLACK);
		// setting the color of the rectangle to the input color
		square.setFill(color);
		
		// adding all the objects to the group
		group.getChildren().addAll(square,light, dark);
		
		// returning the group
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
		// intializing variables
		int typeOfShape = 1;
		Shape shape = null;
		
		Color color;

		// clearing the edited grid
		gridPane.getChildren().clear();
		
		// checking which grid is being edited
		if (typeOfGrid == true) {
			if (grid.getHold() != null) {
				// getting the shape data
				shape = grid.getHold();
				// getting the type data
				typeOfShape = grid.getHold().getType();
			}
		} else {
			if (grid.getNext() != null) {
				// getting the shape data
				shape = grid.getNext();
				// getting the type data
				typeOfShape = grid.getNext().getType();
			}
		}
		
		// checks if there is a shape
		if (shape != null) {			
			// gets the color for the specified shape
			color = getColor(typeOfShape);
						
			// checks if the shape isn't a square block, an s block, an l block, or a t block
			if (typeOfShape != 4 && typeOfShape != 5 && typeOfShape != 2 && typeOfShape != 6) {
				// gets a group containing a rectangle with the color
				Group group = getGroup(color);
				// adds that group to the gridpane
				gridPane.add(group, 0, 0);
			}
			
			// checks if the shape isn't an l block or a j block
			if (typeOfShape != 2 && typeOfShape != 3) {
				// gets a group containing a rectangle with the color
				Group group = getGroup(color);
				// adds that group to the gridpane
				gridPane.add(group, 1, 0);
			}
			
			// checks if the shape isn't a z block, a j block, or a t block
			if (typeOfShape != 7 && typeOfShape != 3 && typeOfShape != 6) {
				// gets a group containing a rectangle with the color
				Group group = getGroup(color);
				// adds that group to the gridpane
				gridPane.add(group, 2, 0);
			}
			
			// checks if the shape is a line block
			if (typeOfShape == 1) {
				// gets a group containing a rectangle with the color
				Group group = getGroup(color);
				// adds that group to the gridpane
				gridPane.add(group, 3, 0);
			} else {
				// gets a group containing a rectangle with the color
				Group group = getGroup(color);
				// adds that group to the gridpane
				gridPane.add(group, 1, 1);
			}
			
			// checks if the shape isn't an s block or a line block
			if (typeOfShape != 5 && typeOfShape != 1) {
				// gets a group containing a rectangle with the color
				Group group = getGroup(color);
				// adds that group to the gridpane
				gridPane.add(group, 2, 1);
			}
			
			// checks if the shape isn't a line block, a z block, or a square block
			if (typeOfShape != 1 && typeOfShape != 7 && typeOfShape != 4) {
				// gets a group containing a rectangle with the color
				Group group = getGroup(color);
				// adds that group to the gridpane
				gridPane.add(group, 0, 1);
			}
		}
		
	}

	public static void main(String args[]) {
		launch(args);
	}
	

}

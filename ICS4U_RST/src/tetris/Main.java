package tetris;

import java.io.File;
import java.util.ArrayList;

import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
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
import simpleIO.Console;

/**
 * @author jake pommainville and rohan daves
 * 
 * date: 6/7/24
 */
public class Main extends Application{

	private GridPane grdTetris;
	private GridPane grdHold;
	private GridPane grdNext;
	
	private Grid grid;
	private int shapeSpeed;
	private boolean running;
	private boolean animationFirstTime;
	
	private MediaPlayer music;
	
	private Label score;
	private Label level;
	private Label lines;
	
	private SequentialTransition animation;
	PauseTransition pauseTransition;
	
	final private static int SIZE = 40;
	final private static int SPACE = -1;
	
	Stage stage;
	
	@Override
	public void start(Stage stage) throws Exception {
		this.stage = stage;
		// TODO get music to loop
		music = new MediaPlayer(new Media(new File("res/music/Tetris.mp3").toURI().toString()));
		music.volumeProperty().set(0.05);
		music.play();
		
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
		grdHold.setVgap(SPACE);
		grdHold.setHgap(SPACE);
		grdHold.setPadding(new Insets(SPACE));
		
		// creating the constraints for each cell
		for (int i = 0; i < Grid.WIDTH-6; i++) {
			grdHold.getColumnConstraints().add(new ColumnConstraints(SIZE));
			grdHold.getRowConstraints().add(new RowConstraints(SIZE));
		}
		
		VBox vbxHold = new VBox(lblHold, grdHold);
		vbxHold.setPadding(new Insets(5));
		vbxHold.setSpacing(10);
		
		Rectangle rectPause = new Rectangle(150,150);
		rectPause.setOnMouseClicked(e -> {
			if (running) {
				animation.pause();
				music.pause();
				running = false;
			} else {
				animation.play();
				music.play();
				running = true;
			}
		});
		rectPause.setFill(Color.LIGHTGRAY);
		rectPause.setStroke(Color.BLACK);
		
		VBox vbxRightSide = new VBox(vbxHold, rectPause);
		vbxRightSide.setPadding(new Insets(5));
		vbxRightSide.setSpacing(425);
		
		Label lblNext = new Label("Next Shape:");
		lblNext.setFont(Font.font(STYLESHEET_MODENA, FontWeight.BOLD, 25));
		
		// creating the gridpane to serve as the grid for the tetris blocks
		grdNext = new GridPane();
		// sets the spacing
		grdNext.setVgap(SPACE);
		grdNext.setHgap(SPACE);
		grdNext.setPadding(new Insets(SPACE));
		
		// creating the constraints for each cell
		for (int i = 0; i < Grid.WIDTH-6; i++) {
			grdNext.getColumnConstraints().add(new ColumnConstraints(SIZE));
			grdNext.getRowConstraints().add(new RowConstraints(SIZE));
		}
		
		VBox vbxNext = new VBox(lblNext, grdNext);
		vbxNext.setPadding(new Insets(5));
		vbxNext.setSpacing(10);
				
		score = new Label("Score: 0");
		score.setFont(Font.font(STYLESHEET_MODENA, FontWeight.BOLD, 25));
		
		level = new Label("Level: 0");
		level.setFont(Font.font(STYLESHEET_MODENA, FontWeight.BOLD, 25));

		lines = new Label("Cleared Lines: 0");
		lines.setFont(Font.font(STYLESHEET_MODENA, FontWeight.BOLD, 25));
		
		VBox hbxLabels = new VBox(score, level, lines);
		hbxLabels.setPadding(new Insets(5));
		hbxLabels.setSpacing(10);
		
		VBox hbxLeftSide = new VBox(vbxNext, hbxLabels);
		hbxLeftSide.setPadding(new Insets(5));
		hbxLeftSide.setSpacing(10);
		
		// creating an hbox to serve as the root
		HBox root = new HBox();
		// setting the spacing
		root.setPadding(new Insets(5));
		root.setSpacing(300);
		// adding the tetris grid to the root
		root.getChildren().addAll(hbxLeftSide, grdTetris, vbxRightSide);
		
		// creating a new scene with the root as the root node
		Scene scene = new Scene(root);
		
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
					if (running) {
						animation.pause();
						running = false;
					}
				} else if (e.getCode().equals(KeyCode.G)) {
					grid.save();
					updateGridColor();
				} else if (e.getCode().equals(KeyCode.H)) {
					grid.load();
					updateGridColor();
				}
			} else {
				if (e.getCode().equals(KeyCode.CONTROL)) {
					animation.play();
					running = true;
				} else if (e.getCode().equals(KeyCode.G)) {
					grid.save();
					updateGridColor();
				} else if (e.getCode().equals(KeyCode.H)) {
					grid.load();
					updateGridColor();
				}
			}
		});
		
		animationFirstTime = true;
		
		startNewGame();
		
		// setting the scene to the scene
		stage.setScene(scene);
		// changing the title
		stage.setTitle("Tetris");
		// setting icon image
		stage.getIcons().add(new Image("file:res/img/Tetris_logo.png"));
		// showing the scene
		stage.show();
	}
	
	/**
	 * starts a new game
	 */
	private void startNewGame() {
		grid = new Grid();
		shapeSpeed = grid.getSpeed();
		
		updateGridColor();
		updateBlocks();
	}
	
	/**
	 * moves the blocks down after a specified time
	 */
	private void updateBlocks() {
		shapeSpeed = grid.getSpeed();
		
		Console.print(shapeSpeed);
		
		if (!animationFirstTime) {
			animation.stop();
		} else {
			animationFirstTime = false;
		}
		
		animation = new SequentialTransition();
		pauseTransition = new PauseTransition(Duration.millis(shapeSpeed));
		pauseTransition.setOnFinished(e -> {
			grid.moveDown();
			updateGridColor();
		});
		
		Console.print(pauseTransition.getDuration());
		
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
			// TODO add method for lose screen
			startNewGame();
		}
	}
	
	/**
	 * updates the games labels
	 */
	private void updateLabels() {
		score.setText("Score: "+grid.getScore());
		level.setText("level: "+grid.getLevel());
		lines.setText("Cleared Lines: "+grid.getLines());
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

package tetris;

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
import javafx.scene.paint.Color;
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
	
	private Grid grid;
	private int shapeSpeed;
	
	boolean running;
	
	private Label score;
	private Label level;
	private Label lines;
	
	private SequentialTransition shapeTransition;
	
	final private static int SIZE = 40;
	final private static int SPACE = -1;
	
	@Override
	public void start(Stage stage) throws Exception {
		
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
				shapeTransition.pause();
				running = false;
			} else {
				shapeTransition.play();
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
					shapeTransition.pause();
					running = false;
				} else {
					shapeTransition.play();
					running = true;
				}
			}
		});
		
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
		
		shapeTransition = new SequentialTransition();
		PauseTransition pauseTransition = new PauseTransition(Duration.millis(shapeSpeed));
		pauseTransition.setOnFinished(e -> {
			grid.moveDown();
			updateGridColor();
		});
		
		shapeTransition.getChildren().add(pauseTransition);
		shapeTransition.setCycleCount(Timeline.INDEFINITE);
		shapeTransition.play();
	}

	/**
	 * updates each cell on the grid and changes it's color based on the block
	 */
	private void updateGridColor() {
		if (!grid.getGameOver()) {
			grdTetris.getChildren().clear();
			
			if (shapeSpeed != grid.getSpeed()) {
				shapeTransition.stop();
				updateBlocks();
			}
			
			ArrayList<Block> blocks = grid.getBlocks();
			
			for (int i = 0; i < Grid.HEIGHT; i++) {
				for (int j = 0; j < Grid.WIDTH; j++) {
					Block currentBlock = new Block(j, i);
					Group group = new Group();
					Rectangle square = new Rectangle(SIZE, SIZE);
					square.setStroke(Color.BLACK);
					
					if (blocks.contains(currentBlock)) {
						square.setFill(getColor(blocks.get(blocks.indexOf(currentBlock)).getType()));
					} else {
						square.setFill(Color.LIGHTGRAY);
					}
					
					group.getChildren().add(square);
					
					grdTetris.add(group, j, i);
				}
			}
			
			updateInfoGrid(true, grdHold);
			updateInfoGrid(false, grdNext);
			updateLabels();
		} else {
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
			return Color.YELLOW;
		case 5:
			return Color.GREEN;
		case 6:
			return Color.PURPLE;
		case 7:
			return Color.RED;
		}
		
		return null;
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
				Rectangle square = new Rectangle(SIZE, SIZE);
				square.setStroke(Color.BLACK);
				square.setFill(color);;
				gridPane.add(square, 0, 0);
			}
			
			if (typeOfShape != 2 && typeOfShape != 3) {
				Rectangle square = new Rectangle(SIZE, SIZE);
				square.setStroke(Color.BLACK);
				square.setFill(color);
				gridPane.add(square, 1, 0);
			}
			
			if (typeOfShape != 7 && typeOfShape != 3 && typeOfShape != 6) {
				Rectangle square = new Rectangle(SIZE, SIZE);
				square.setStroke(Color.BLACK);
				square.setFill(color);
				gridPane.add(square, 2, 0);
			}
			
			if (typeOfShape == 1) {
				Rectangle square = new Rectangle(SIZE, SIZE);
				square.setStroke(Color.BLACK);
				square.setFill(color);
				gridPane.add(square, 3, 0);
			} else {
				Rectangle square = new Rectangle(SIZE, SIZE);
				square.setStroke(Color.BLACK);
				square.setFill(color);
				gridPane.add(square, 1, 1);
			}
			
			if (typeOfShape != 5 && typeOfShape != 1) {
				Rectangle square = new Rectangle(SIZE, SIZE);
				square.setStroke(Color.BLACK);
				square.setFill(color);
				gridPane.add(square, 2, 1);
			}
			
			if (typeOfShape != 1 && typeOfShape != 7 && typeOfShape != 4) {
				Rectangle square = new Rectangle(SIZE, SIZE);
				square.setStroke(Color.BLACK);
				square.setFill(color);
				gridPane.add(square, 0, 1);
			}
		}
		
	}

	public static void main(String args[]) {
		launch(args);
	}

}

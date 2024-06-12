package tetris;

import java.util.ArrayList;

import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * @author jake pommainville and rohan dave
 * 
 * date: 6/7/24
 */
public class Main extends Application{

	private GridPane grdTetris;
	
	private Grid grid;
	private int shapeSpeed;
	
	SequentialTransition shapeTransition;

	private boolean running;
	
	final private static int SIZE = 30;
	final private static int SPACE = 3;
	
	@Override
	public void start(Stage stage) throws Exception {
		
		// creating the gridpane to serve as the grid for the tetris blocks
		grdTetris = new GridPane();
		// sets the spacing
		grdTetris.setVgap(SPACE);
		grdTetris.setHgap(SPACE);
		grdTetris.setPadding(new Insets(SPACE));
		grdTetris.setGridLinesVisible(true);
		
		// creating the constraints for each cell
		for (int i = 0; i < Grid.WIDTH; i++) {
			grdTetris.getColumnConstraints().add(new ColumnConstraints(SIZE));
			grdTetris.getRowConstraints().add(new RowConstraints(SIZE));
		}
		
		// creating an hbox to serve as the root
		HBox root = new HBox();
		// setting the spacing
		root.setPadding(new Insets(5));
		root.setSpacing(SIZE-5);
		// adding the tetris grid to the root
		root.getChildren().add(grdTetris);
		
		// creating a new scene with the root as the root node
		Scene scene = new Scene(root);
		
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
			}
		});
		
		startNewGame();
		
		// setting the scene to the scene
		stage.setScene(scene);
		// changing the title
		stage.setTitle("Tetris");
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
						switch (blocks.get(blocks.indexOf(currentBlock)).getType()) {
							case 1:
								square.setFill(Color.DEEPSKYBLUE);
								break;
							case 2:
								square.setFill(Color.ORANGE);
								break;
							case 3:
								square.setFill(Color.BLUE);
								break;
							case 4:
								square.setFill(Color.YELLOW);
								break;
							case 5:
								square.setFill(Color.RED);
								break;
							case 6:
								square.setFill(Color.PURPLE);
								break;
							case 7:
								square.setFill(Color.FORESTGREEN);
								break;
						}
					} else {
						square.setFill(Color.LIGHTGRAY);
					}
					
					group.getChildren().add(square);
					
					grdTetris.add(group, j, i);
				}
			}
		} else {
			startNewGame();
		}
	}

	public static void main(String args[]) {
		launch(args);
	}

}

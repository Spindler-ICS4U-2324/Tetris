package tetris;

import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
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
	
	final private static int SIZE = 30;
	final private static int SPACE = 3;
	
	@Override
	public void start(Stage stage) throws Exception {
		
		// creating the gridpane to serve as the grid for the tetris blocks
		grdTetris = new GridPane();
		// sets the spacing
		grdTetris.setVgap(SIZE);
		grdTetris.setHgap(SIZE);
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
		
		startNewGame();
		
		// setting the scene to the scene
		stage.setScene(scene);
		// changing the title
		stage.setTitle("Tetris");
		// showing the scene
		stage.show();
	}
	
	private void startNewGame() {
		grid = new Grid();
		shapeSpeed = grid.getSpeed();
		
		updateGridColor();
		updateBlocks();
		
	}

	private void updateBlocks() {
		grdTetris.getChildren().clear();
		
		SequentialTransition shapeTransition = new SequentialTransition();
		PauseTransition pauseTransition = new PauseTransition(Duration.millis(shapeSpeed));
		pauseTransition.setOnFinished(e -> {
			grid.moveDown();
			updateGridColor();
		});
		
	}

	private void updateGridColor() {
		// TODO Auto-generated method stub
		
	}

	public static void main(String args[]) {
		launch(args);
	}

}

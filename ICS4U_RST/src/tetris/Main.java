package tetris;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;

/**
 * @author jake pommainville and rohan dave
 * 
 * date: 6/7/24
 */
public class Main extends Application{

	final private static int SIZE = 30;
	final private static int SPACE = 3;
	
	@Override
	public void start(Stage stage) throws Exception {
		
		// creating the gridpane to serve as the grid for the tetris blocks
		GridPane grdTetris = new GridPane();
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
		
		// setting the scene to the scene
		stage.setScene(scene);
		// changing the title
		stage.setTitle("Tetris");
		// showing the scene
		stage.show();
	}
	
	public static void main(String args[]) {
		launch(args);
	}

}

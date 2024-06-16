package tetris;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.function.Predicate;

/**
 * @author Jake Pommainville and Rohan Daves
 * 
 * this file handles all the game logic and stores all the information for it
 * 
 * Date: today
 */
public class Grid {
	/**
	 * a {@code boolean} for the status of the game
	 */
	private boolean gameOver;
	/**
	 * a {@code boolean} for weather a shape has already been held
	 */
	private boolean alreadyHeld;
	
	/**
	 * an {@code ArrayList} for all the blocks in the tetris grid
	 */
	private ArrayList<Block> blocks;
	/**
	 * an {@code ArrayList} for the next shapes
	 */
	private ArrayList<Shape> nextShapes;
	
	/**
	 * an {@code int} for the amount of lines cleared
	 */
	private int numClearedLines;
	/**
	 * an {@code int} for the speed the game updates in ms
	 */
	private int speed;
	/**
	 * an {@code int} for the score achieved
	 */
	private int score;
	/**
	 * an {@code int} for the current level
	 */
	private int level;
	
	/**
	 * an {@code int} for the highscore of the game
	 */
	private int highscore;
	
	/**
	 * a {@code Shape} for the current shape used in the game
	 */
	private Shape currentShape;
	/**
	 * a {@code Shape} for the shape being held
	 */
	private Shape holdShape;
	
	/**
	 * an {@code int} for the size of the grid
	 */
	public final static int WIDTH = 10, HEIGHT = 20;

	/**
	 * the constructor for the Grid class
	 */
	Grid(int highscore) {
		// sets everything to the default values
		blocks = new ArrayList<Block>();
		nextShapes = new ArrayList<Shape>();

		gameOver = false;
		
		numClearedLines = 0;
		level = 0;
		score = 0;
		speed = 633;
		
		holdShape = null;
		this.highscore = highscore;
		
		updateNextShapes();
		createNewShape();
	}
	
	/**
	 * Creates a new shape and saves the current shape into the grid
	 */
	private void createNewShape() {
		// sets alreadyHeld to false
		alreadyHeld = false;
		
		// checks if there is a current shape to save
		if (currentShape != null) 
			// saves its blocks to the blocks array to store on the grid
			blocks.addAll(currentShape.getBlocks());
		
		// sets the current shape to the next shape in the next shapes arraylist
		currentShape = nextShapes.get(0);
		
		// removes the next shape in the next shapes arraylist
		nextShapes.remove(0);
		
		// checks if there are any shapes left in the next shapes array list
		if (nextShapes.size() == 0) {
			// repopulates the nextshapes arraylist
			updateNextShapes();
		}
	}
	
	/**
	 * generates new shapes into an array in a random order with no duplicates
	 */
	private void updateNextShapes() {
		// creates var for the random number
		int randomNumber;
		
		// creates an array forr random numbers
		int[] randomNumbers = new int[7];
		
		// checks each pos in the random numbers array
		for (int i = 0; i < 7; i++) {
			// creates a boolean duplicate
			boolean duplicate;
			do { 
				// sets the duplicate to false
				duplicate = false;
				// creates a random number
				randomNumber = (int) (Math.random()*7)+1;
				// checks each random number in the random number array
				for (int j = 0; j < 7; j++) {
					// checks if there are duplicate numbers in the random numbers array
					if (randomNumbers[j] == randomNumber) {
						// sets duplicate to true
						duplicate = true;
					}
				}
			} while (duplicate == true);
			// sets the random number created to the pos in the random numbers array
			randomNumbers[i] = randomNumber;
		}
					
		// creates a new shape in the nextShapes arraylist with the value for each shape in the random numbers array
		for (int i = 0; i < randomNumbers.length; i++) {
			nextShapes.add(new Shape(randomNumbers[i]));
		}
	}

	/**
	 * a method to get every block in the tetris grid
	 * 
	 * @return
	 * an {@code ArrayList} with every block on the tetris grid
	 */
	public ArrayList<Block> getBlocks() {
		// creates a new array list
		ArrayList<Block> blocks = new ArrayList<Block>();
		
		// saves the blocks array to it
		blocks.addAll(this.blocks);
		// adds the current shapes blocks too
		blocks.addAll(currentShape.getBlocks());
		
		return blocks;
	}
	
	/**
	 * holds a shape if it wasn't previously swapped
	 */
	public void hold() {
		// checks if a shape was already swapped
		if (!alreadyHeld) {
			// checks if there is held shape
			if (holdShape == null) {
				// sets the held shape to the current shape
				holdShape = currentShape;
				// sets the current shape to null
				currentShape = null;
				// creates a new shape for the current shape
				createNewShape();
			} else {
				// saves the type of shape held
				int type = holdShape.getType();
				// sets the held shape to the current shape
				holdShape = currentShape;
				// sets the current shape to the held type
				currentShape = new Shape(type);
				// set the alreadyHeld var to true as the shape has been swapped
				alreadyHeld = true;
			}
		}
	}
	
	/**
	 * a method to drop the current shape to the lowest possible space
	 */
	public void drop() {
		// runs a loop for the height of the board
		for (int i = 0; i < HEIGHT; i++) {
			// checks if a block is touching the bottom or a block and stops the loop
			if (checkBlocksBelow() || checkBottom()) {
				break;
			}
			// otherwise moves the block down
			moveDown();
		}
	}
	
	/**
	 * checks each row of the grid to determine if a line is formed and adjusts the score, deletes the line, 
	 * and moves every block above the line down
	 */
	private void checkLines() {
		// creates an arraylist which will contain the y value of any full lines
		ArrayList<Integer> fullLines = new ArrayList<Integer>();
		
		// runs a loop for each y value of the grid
		for (int i = 0; i < HEIGHT; i++) {
			// creats a fullline booloean to determine if the line at the current y pos is full
			boolean fullLine = true;
			
			// checks each x value of the grid to determine if there is a block
			for (int j = 0; j < WIDTH; j++) {
				if (!blocks.contains(new Block(j,i))) {
					// sets full line to false if there is no block
					fullLine = false;
				}
			}
			
			// checks if full line stays true and adds it to the fulllines arrayList
			if (fullLine) {
				fullLines.add(i);
			}
		}
		
		// checks if there are any full lines
		if (fullLines.size() != 0 ) {
			// sets the number of cleared lines to the number of full lines
			numClearedLines += fullLines.size();
			// calculates the new score based off the number of full lines
			calculateScore(fullLines.size());
			
			// checks each y value in the full lines arraylist
			for (int i : fullLines) {
				
				// creates a predicate to check if a block has the same y value as the y value in the full lines array list
				Predicate<Block> checkY = e -> e.getY() == i;
				// removes a block if the predicate is true
				blocks.removeIf(checkY);
				
				// checks each block
				for (int j = 0; j < blocks.size(); j++) {
					// moves them down by 1 space if they were above the cleared line
					if (blocks.get(j).getY() < i) {
						blocks.get(j).modY();
					}
				}
			}
		}
		
		// updates the level
		level = numClearedLines / 10;
				
		// updates the game speed
		updateSpeed();
		// updates the highcsore
		saveHighscore();
	}

	/**
	 * updates the speed of the game based on the current level
	 */
	private void updateSpeed() {
		// speed values for each level loosely based off feel and wiki
		if (level == 0) {
			speed = 633;
		} else if (level == 1) {
			speed = 466;
		} else if (level == 2) {
			speed = 383;
		} else if (level == 3) {
			speed = 300;
		} else if (level == 4) {
			speed = 183;
		} else if (level == 5) {
			speed = 150;
		} else if (level == 6) {
			speed = 100;
		} else if (level == 7) {
			speed = 85;
		} else if (level == 8) {
			speed = 75;
		} else if (level == 9) {
			speed = 65;
		} else if (9 < level && level < 19) {
			speed = 55;
		} else if (18 < level && level < 29) {
			speed =  33;
		} else {
			speed = 16;
		}
	}

	/**
	 * updates the score according to the number of lines cleared
	 * 
	 * @param numClearedLines
	 * an {@code int} for the number of lines cleared
	 */
	private void calculateScore(int numClearedLines) {
		// creates a var for the base score
		int baseScore = 40;
		
		// checks for a certain number of lines and increases the score value
		if (numClearedLines == 2) {
			baseScore = 100;
		} else if (numClearedLines == 3) {
			baseScore = 300;
		} else if (numClearedLines >= 4) {
			baseScore = 1000;
		}
		
		// multiplies the score by the level to increase score for higher difficulty
		score += baseScore * (level + 1);
	}
	
	/**
	 * a method to get the highscore
	 * 
	 * @return
	 * an {@code int} for the highest score achieved on the save file
	 */
	public int getHighscore() {
		return highscore;
	}

	/**
	 * a method to get the current speed of the game
	 * 
	 * @return
	 * an {@code int} with the speed of the game in ms
	 */
	public int getSpeed() {
		return speed;
	}
	
	/**
	 * @return
	 * returns a {@code boolean} for the status of the game
	 */
	public boolean getGameOver() {
		return gameOver;
	}

	/**
	 * moves the current shape down
	 */
	public void moveDown() {
		// checks if the shape is not touching the bottom and there are no blocks below it
		if (!checkBottom() && !checkBlocksBelow()) {
			// moves the shape down 1 grid space
			currentShape.moveDown();
		} else {
			// checks if the shape is touching the top
			if (checkTop()) {
				// saves the highscore
				saveHighscore();
				// sets the gameover to true
				gameOver = true;
			} else {
				// creates a new shape
				createNewShape();
				// checks for lines
				checkLines();
			}
		}
	}

	/**
	 * moves the current shape to the left
	 */
	public void moveLeft() {
		if (!checkLeft() && !checkBlocksLeft()) {
			currentShape.moveLeft();
		}
	}
	
	/**
	 * moves the current shape to the right
	 */
	public void moveRight() {
		if (!checkRight() && !checkBlocksRight()) {
			currentShape.moveRight();
		}
	}
	
	/**
	 * rotates the shape left
	 */
	public void rotateLeft() {
		currentShape.rotateShape(this.blocks, 1);
	}
	
	/**
	 * rotates the shape right
	 */
	public void rotateRight() {
		currentShape.rotateShape(this.blocks, 0);
	}
	
	/**
	 * checks if the shape is touching the bottom
	 * 
	 * @return
	 * a {@code Boolean} where true is if the shape is touching the bottom and false is if it is not
	 */
	private boolean checkBottom() {
		// checks each block in the current shape
		for (Block i : currentShape.getBlocks()) {
			// checks if the block is touching the bottom of the grid
			if (i.getY() == HEIGHT - 1) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * checks if the shape is touching the left side of the grid
	 * 
	 * @return
	 * a {@code Boolean} where true is if the shape is touching the left side of the grid and false is if it is not
	 */
	private boolean checkLeft() {
		// checks each block in the current shape
		for (Block i : currentShape.getBlocks()) {
			// checks if the block is touching left side of the grid
			if (i.getX() == 0) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * checks if the shape is touching the right side of the grid
	 * 
	 * @return
	 * a {@code Boolean} where true is if the shape is touching the right side of the grid and false is if it is not
	 */
	private boolean checkRight() {
		// checks each block in the current shape
		for (Block i : currentShape.getBlocks()) {
			// checks if the block is touching right side of the grid
			if (i.getX() == WIDTH - 1) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * checks if there are blocks below the shape and if it's touching the top
	 * 
	 * @return
	 * a {@code boolean} if true
	 */
	private boolean checkTop() {
		// checks each block in the current shape
		for (Block i : currentShape.getBlocks()) {
			// checks if it is touching the top of the grid
			if (i.getY() == 0) {
				return true;
			} 
		}
		
		return false;
	}
	
	private void saveHighscore() {
		// checks if the score is higher than the highscore
		if (score > highscore) {
			highscore = score;
		}
	}

	/**
	 * checks if there are blocks below the current {@code Shape}
	 * 
	 * @return
	 * a {@code boolean} true if there are blocks below and false if there are not
	 */
	private boolean checkBlocksBelow() {
		// checks each block in the current shape
		for (Block i : currentShape.getBlocks()) {
			// checks if there is a block below
			if (blocks.contains(new Block(i.getX(), i.getY()+1))) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * checks if there are blocks to the left of the current {@code Shape}
	 * 
	 * @return
	 * a {@code boolean} true if there are blocks to the left and false if there are not
	 */
	private boolean checkBlocksLeft() {
		// checks each block in the current shape
		for (Block i : currentShape.getBlocks()) {
			// checks if there is a block to the left
			if (blocks.contains(new Block(i.getX()-1, i.getY()))) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * checks if there are blocks to the right of the current {@code Shape}
	 * 
	 * @return
	 * a {@code boolean} true if there are blocks to the right and false if there are not
	 */
	private boolean checkBlocksRight() {
		// checks each block in the current shape
		for (Block i : currentShape.getBlocks()) {
			// checks if there is a block to the right
			if (blocks.contains(new Block(i.getX()+1, i.getY()))) {
				return true;
			}
		}
		return false;
	}

	/**
	 * get's the held {@code Shape}
	 * 
	 * @return
	 * the {@code Shape} that is currently held
	 */
	public Shape getHold() {
		return holdShape;
	}

	/**
	 * get's the next {@code Shape}
	 * 
	 * @return
	 * the next {@code Shape}
	 */
	public Shape getNext() {
		// checks if there are any next shapes
		if (nextShapes.size() == 0) {
			return null;
		} else {
			return nextShapes.get(0);
		}
	}

	/**
	 * get's the current score
	 * 
	 * @return
	 * an {@code int} for the score
	 */
	public int getScore() {
		return score;
	}

	/**
	 * get's the current level
	 * 
	 * @return
	 * an {@code int} for the level
	 */
	public int getLevel() {
		return level;
	}
	
	/**
	 * get's the number of cleared lines
	 * 
	 * @return
	 * an {@code int} for the number of cleared lines
	 */
	public int getLines() {
		return numClearedLines;
	}

	/**
	 * saves all game data to a file
	 */
	public void save() {
		// creates a fileWriter
		FileWriter fileWrite;
		// creates a printWriter
		PrintWriter filePrinter;
		
		// creates a new file for the save data in the save folder
		try {
			File file = new File("C:\\Tetris\\save\\Tetris.txt");
		    file.getParentFile().mkdirs();
		    file.createNewFile();
		} catch (Exception e) {
		    e.printStackTrace();
		}
		
		try {
			// gets the file at the specified dir
			File dir = new File("C:/Tetris/save/Tetris.txt");
			fileWrite = new FileWriter(dir);
			// creates a file printerWriter using the fileWrite
			filePrinter = new PrintWriter(fileWrite);
			
			// writes the score
			filePrinter.println(score);
			// writes the level
			filePrinter.println(level);
			// writes the nubmer of cleared lines
			filePrinter.println(numClearedLines);
			// writes the speed
			filePrinter.println(speed);

			// writes the current shapes type
			filePrinter.println(currentShape.getType());
			
			// checks if there is a held shape
			if (holdShape != null) {
				// prints the held shapes type
				filePrinter.println(holdShape.getType());
			} else {
				// prints 0 if not
				filePrinter.println(0);
			}
			
			// prints the amount of blocks
			filePrinter.println(blocks.size());
			
			// runs a loop for each block in the array printing all its info
			for (int i = 0; i < blocks.size(); i++) {
				filePrinter.println(blocks.get(i).getX());
				filePrinter.println(blocks.get(i).getY());
				filePrinter.println(blocks.get(i).getType());
			}
			
			// prints the amount of next shapes
			filePrinter.println(nextShapes.size());
			
			// runs a loop for each shape in the array
			for (int i = 0; i < nextShapes.size(); i++) {
				// prints the shapes type
				filePrinter.println(nextShapes.get(i).getType());
			}
			
			// prints the highscore
			filePrinter.println(highscore);
			
			// closes the file
			fileWrite.close();		
		} catch(Exception e) {
			// prints the error message
			e.printStackTrace();
		}
	}
	
	/**
	 * loads game data from a file
	 */
	public void load() {
		// clears the array and sets gameover to false
		blocks.clear();
		nextShapes.clear();
		
		gameOver = false;
		
		// attempts to load data from a file
		try {
			// create fileReader to read the data from the Tetris.txt file
			FileReader passengerFile = new FileReader("C:/Tetris/save/Tetris.txt");
			// creates a buggered reader using the fileReader
			BufferedReader passengerStream = new BufferedReader(passengerFile);
	
			// loads the score first
	    	score = Integer.parseInt(passengerStream.readLine());
	    	// loads the level next
	    	level = Integer.parseInt(passengerStream.readLine());
	    	// loads the number of cleared lines next
	    	numClearedLines = Integer.parseInt(passengerStream.readLine());
	    	// loads the speed next
	    	speed = Integer.parseInt(passengerStream.readLine());
	    	
	    	// creates a new shape for the current shape with the type based on this current line
	    	currentShape = new Shape(Integer.parseInt(passengerStream.readLine()));
	    	
	    	// gets the type of the held shape
	    	int holdShapeType = Integer.parseInt(passengerStream.readLine());
	    	
	    	// checks if its a valid type
	    	if (holdShapeType != 0) {
	    		// creates a new hold shape with that type
	    		holdShape = new Shape(holdShapeType);
	    	} else {
	    		// sets to null if not
	    		holdShape = null;
	    	}
	    	
	    	//reads the size of the blocks array
	    	int size = Integer.parseInt(passengerStream.readLine());
	
	    	// populates the grid with the blocks by filling up the blocks array with saved data
	    	for (int i = 0; i < size; i++) {
	    		// creates a new block with the specifid x, y, and type values and puts that in the blocks array
	    		int x = Integer.parseInt(passengerStream.readLine());
	    		int y = Integer.parseInt(passengerStream.readLine());
	    		int type = Integer.parseInt(passengerStream.readLine());
	    		blocks.add(new Block(x,y,type));
	    	}
	    	
	    	// gets the size of the next shapes array
	    	size = Integer.parseInt(passengerStream.readLine());
	    	
	    	// runs a loop to get all of the next shapes
	    	for (int i = 0; i < size; i++) {
	    		// creates a new shape with the specified type
	    		nextShapes.add(new Shape(Integer.parseInt(passengerStream.readLine())));
	    	}
	    	
	    	// reads the highscore from the file
	    	highscore = Integer.parseInt(passengerStream.readLine());
	    	
	    	// closes the file
	    	passengerFile.close();
		} catch (Exception e) {
			// loads a new blank game if nothing can be loaded from the file
			loadNewBlankGame();
		}
	}

	private void loadNewBlankGame() {
		// creates an new instance
		blocks = new ArrayList<Block>();
		// creates an new instance
		nextShapes = new ArrayList<Shape>();

		// sets gameover to false
		gameOver = false;
		
		// resets all values to default
		numClearedLines = 0;
		level = 0;
		score = 0;
		speed = 633;
		
		// sets the shapes to null
		holdShape = null;
		currentShape = null;
		
		// sets the highscore to 0
		this.highscore = 0;
		
		// creates a new shape
		updateNextShapes();
		createNewShape();
	}
}

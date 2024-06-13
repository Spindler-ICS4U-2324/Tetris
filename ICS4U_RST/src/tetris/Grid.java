package tetris;

import java.util.ArrayList;
import java.util.function.Predicate;

/**
 * @author Jake Pommainville and Rohan Daves
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
	Grid() {
		blocks = new ArrayList<Block>();
		nextShapes = new ArrayList<Shape>();

		gameOver = false;
		
		numClearedLines = 0;
		level = 0;
		score = 0;
		speed = 800;
		
		holdShape = null;
		
		updateNextShapes();
		createNewShape();
	}
	
	/**
	 * Creates a new shape and saves the current shape into the grid
	 */
	private void createNewShape() {
		alreadyHeld = false;
		
		if (currentShape != null) 
			blocks.addAll(currentShape.getBlocks());
		
		currentShape = nextShapes.get(0);
		
		nextShapes.remove(0);
		
		if (nextShapes.size() == 0) {
			updateNextShapes();
		}
	}
	
	/**
	 * generates new shapes into an array in a random order with no duplicates
	 */
	private void updateNextShapes() {
		int randomNumber;
		
		int[] randomNumbers = new int[7];
		
		for (int i = 0; i < 7; i++) {
			boolean check;
			 do { 
				check = true;
				randomNumber = (int) (Math.random()*7)+1;
				for (int j = 0; j < 7; j++) {
					if (randomNumbers[j] == randomNumber) {
						check = false;
					}
				}
			} while (check == false);
			randomNumbers[i] = randomNumber;
		}
					
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
		ArrayList<Block> blocks = new ArrayList<Block>();
		
		blocks.addAll(this.blocks);
		blocks.addAll(currentShape.getBlocks());
		
		return blocks;
	}
	
	/**
	 * holds a shape if it wasn't previously swapped
	 */
	public void hold() {
		if (!alreadyHeld) {
			if (holdShape == null) {
				holdShape = currentShape;
				currentShape = null;
				createNewShape();
			} else {
				int type = holdShape.getType();
				holdShape = currentShape;
				currentShape = new Shape(type);
				alreadyHeld = true;
			}
		}
	}
	
	/**
	 * a method to drop the current shape to the lowest possible space
	 */
	public void drop() {
		for (int i = 0; i < HEIGHT; i++) {
			if (checkBlocksBelow() || checkBottom()) {
				break;
			}
			moveDown();
		}
	}
	
	/**
	 * checks each row of the grid to determine if a line is formed and adjusts the score, deletes the line, 
	 * and moves every block above the line down
	 */
	private void checkLines() {
		ArrayList<Integer> fullLines = new ArrayList<Integer>();
		
		for (int i = 0; i < HEIGHT; i++) {
			boolean fullLine = true;
			
			for (int j = 0; j < WIDTH; j++) {
				if (!blocks.contains(new Block(j,i))) {
					fullLine = false;
				}
			}
			
			if (fullLine) {
				fullLines.add(i);
			}
		}
		
		if (fullLines.size() != 0 ) {
			numClearedLines += fullLines.size();
			calculateScore(fullLines.size());
			
			for (int i : fullLines) {
				
				Predicate<Block> checkY = e -> e.getY() == i;
				blocks.removeIf(checkY);
				
				for (int j = 0; j < blocks.size(); j++) {
					if (blocks.get(j).getY() < i) {
						blocks.get(j).modY();
					}
				}
			}
		}
		
		level = numClearedLines / 10;
		
		updateSpeed();
	}

	/**
	 * updates the speed of the game based on the current level
	 */
	private void updateSpeed() {
		double time = 48.0;
		
		if (-1 < level && level < 9) {
			speed = (int) (((time - (level * 5.0)) / 60.0) * 1000.0);
		} else if (level == 9) {
			speed = 100;
		} else if (9 < level && level < 19) {
			speed = 66;
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
		int lineMultiplier = 40;
		
		if (numClearedLines == 2) {
			lineMultiplier = 100;
		} else if (numClearedLines == 3) {
			lineMultiplier = 300;
		} else if (numClearedLines >= 4) {
			lineMultiplier = 1000;
		}
		
		score += lineMultiplier * (level + 1);
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
		if (!checkBottom() && !checkBlocksBelow()) {
			currentShape.moveDown();
		} else {
			if (checkTop()) {
				gameOver = true;
			}
			createNewShape();
			checkLines();
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
		currentShape.rotateCounterClockwise(WIDTH, this.blocks);
	}
	
	/**
	 * rotates the shape right
	 */
	public void rotateRight() {
		currentShape.rotateClockwise(WIDTH, this.blocks);
	}
	
	/**
	 * checks if the shape is touching the bottom
	 * 
	 * @return
	 * a {@code Boolean} where true is if the shape is touching the bottom and false is if it is not
	 */
	private boolean checkBottom() {
		for (Block i : currentShape.getBlocks()) {
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
		for (Block i : currentShape.getBlocks()) {
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
		for (Block i : currentShape.getBlocks()) {
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
		for (Block i : currentShape.getBlocks()) {
			if (i.getY() == 0) {
				return true;
			} 
		}
		
		return false;
	}
	
	/**
	 * checks if there are blocks below the current {@code Shape}
	 * 
	 * @return
	 * a {@code boolean} true if there are blocks below and false if there are not
	 */
	private boolean checkBlocksBelow() {
		for (Block i : currentShape.getBlocks()) {
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
		for (Block i : currentShape.getBlocks()) {
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
		for (Block i : currentShape.getBlocks()) {
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
}

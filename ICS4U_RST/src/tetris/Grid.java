package tetris;

import java.util.ArrayList;
import java.util.function.Predicate;

public class Grid {
	/**
	 * a {@code boolean} for the status of the game
	 */
	private boolean gameOver;
	
	/**
	 * an {@code ArrayList} for all the blocks in the tetris grid
	 */
	private ArrayList<Block> blocks;
	
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
	 * a variable for the current shape used in the game
	 */
	private Shape currentShape;
	/**
	 * a variable for the next shape used in the game
	 */
	private Shape nextShape;
	
	/**
	 * an {@code int} for the size of the grid
	 */
	public final static int WIDTH = 10, HEIGHT = 20;

	/**
	 * the constructor for the Grid class
	 */
	Grid() {
		this.blocks = new ArrayList<Block>();
		this.gameOver = false;
		this.numClearedLines = 0;
		this.level = 0;
		this.score = 0;
		this.speed = 800;
		
		int randomNumber;
		
		randomNumber = (int) (Math.random()*7)+1;
		
		nextShape = new Shape(randomNumber);

		randomNumber = (int) (Math.random()*7)+1;
		
		currentShape = new Shape(randomNumber);
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
		
	}

	/**
	 * Creates a new shape and saves the current shape into the grid
	 */
	private void createNewShape() {
		int randomNumber = (int) (Math.random()*7)+1;
		
		if (currentShape != null) 
			blocks.addAll(currentShape.getBlocks());
		
		currentShape = nextShape;
		
		nextShape = new Shape(randomNumber);
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
	 * moves the current shape down
	 */
	public void moveDown() {
		if (!checkBottom() && !checkBlocksBelow()) {
			currentShape.moveDown();
		} else {
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
	 * checks if there are blocks below the current shape
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
	 * checks if there are blocks to the left of the current shape
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
	 * checks if there are blocks to the right of the current shape
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
}

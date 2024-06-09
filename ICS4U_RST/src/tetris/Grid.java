package tetris;

import java.util.ArrayList;

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
		
		randomNumber = (int) Math.random()*7+1;
		
		nextShape = new Shape();

		randomNumber = (int) Math.random()*7+1;
		
		currentShape = new Shape();
	}


	private void createNewShape() {
		int randomNumber = (int) Math.random()*7+1;
		
		currentShape = nextShape;
		
		nextShape = new Shape();
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

	public void moveDown() {
		
	}
}

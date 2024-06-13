package tetris;

import java.util.ArrayList;

/**
 * 
 * @author: Rohan Daves and Jake Pommainville
 * @date: June 9, 2024
 * @course: ICS4U
 * @program: Shape.java
 * @purpose: This class handles the shapes of the blocks. Several blocks come together to make different distinct shapes.
 * {@code 1} represents a line block, {@code 2} represents an L-block, {@code 3} represents a J-block,
 * {@code 4} represents a square block, {@code 5} represents an S-block, 
 * {@code 6} represents a T-block, {@code 7} represents a Z-block.
 *
 */
public class Shape {
	// Variable Definition
	private int type;  // Stores the type of shape
	private ArrayList<Block> blocks;  // Stores all the blocks which form a shape

	/**
	 * Constructor method for the shape class
	 * @param shapeType
	 * 		An <code>int</code> containing the type of shape being created
	 */
	public Shape(int shapeType) {
		type = shapeType;
		blocks = new ArrayList<Block>();

		createShape();
	}

	/**
	 * A method which creates a shape based on the type desired. Shapes include a line block, L-block, J-block, square block, 
	 * S-block, T-block, and Z-block
	 */
	public void createShape() {
		switch (type) {
		case 1:    // Line block
			this.blocks.add(new Block(3, 0, type));
			this.blocks.add(new Block(4, 0, type));
			this.blocks.add(new Block(5, 0, type));
			this.blocks.add(new Block(6, 0, type));

			break;

		case 2:  // L-block
			this.blocks.add(new Block(3, 1, type));
			this.blocks.add(new Block(4, 1, type));
			this.blocks.add(new Block(5, 1, type));
			this.blocks.add(new Block(5, 0, type));

			break;

		case 3:  // J-block
			this.blocks.add(new Block(3, 1, type));
			this.blocks.add(new Block(4, 1, type));
			this.blocks.add(new Block(5, 1, type));
			this.blocks.add(new Block(3, 0, type));

			break;

		case 4:  // Square block
			this.blocks.add(new Block(4, 0, type));
			this.blocks.add(new Block(5, 0, type));
			this.blocks.add(new Block(4, 1, type));
			this.blocks.add(new Block(5, 1, type));

			break;

		case 5:  // S-block
			this.blocks.add(new Block(3, 1, type));
			this.blocks.add(new Block(4, 1, type));
			this.blocks.add(new Block(4, 0, type));
			this.blocks.add(new Block(5, 0, type));

			break;

		case 6:  // T-block
			this.blocks.add(new Block(3, 0, type));
			this.blocks.add(new Block(4, 0, type));
			this.blocks.add(new Block(5, 0, type));
			this.blocks.add(new Block(4, 1, type));

			break;

		case 7:  // Z-block
			this.blocks.add(new Block(3, 0, type));
			this.blocks.add(new Block(4, 0, type));
			this.blocks.add(new Block(4, 1, type));
			this.blocks.add(new Block(5, 1, type));

			break;
		}
	}

	/**
	 * Moves the shape down by moving individual blocks
	 */
	public void moveDown() {
		for (Block currentBlock : blocks) {  // Checks every block and moves them all down one square
			currentBlock.modY();
		}
	}

	/**
	 * Accessor method which returns the arrayList which stores the blocks in the shape
	 * @return An <code>ArrayList</code> containing all the blocks in the shape
	 */
	public ArrayList<Block> getBlocks() {
		return blocks;
	}

	/**
	 * Moves the shape left
	 */
	public void moveLeft() {
		for (Block currentBlock : blocks) {  // Checks every block and moves them all left one square
			currentBlock.modX(-1);
		}
	}

	/**
	 * Moves the shape right
	 */
	public void moveRight() {
		for (Block currentBlock : blocks) {  // Checks every block and moves them all right one square
			currentBlock.modX(1);
		}
	}
	
	/**
	 * An accessor method which returns the type of shape
	 * @return
	 * 		An {@code int} which represents the type of shape 
	 */
	public int getType() {
		return type;
	}

	/**
	 * A method which rotates a shape 90 degrees counter clockwise
	 * @param gridWidth
	 * 		An {@code int} representing how many squares (spaces in grid) wide the tetris grid is
	 * @param gridBlocks
	 * 		An {code ArrayList} that stores the information of all the blocks on the tetris grid
	 */
	public void rotateCounterClockwise(int gridWidth, ArrayList<Block> gridBlocks) {
		
		// Variable Definition
			int centralX = blocks.get(1).getX();  // Gets the x coordinate for the block which the shape will be rotated
												  // about
			int centralY = blocks.get(1).getY();  // Gets the y coordinate for the block which the shape will be rotated
												  // about
			int relativeX;  // Relative x and y when viewing the central x and y as the origin
			int relativeY;
			int translatedX;  // Translated coordinates (90 degree shift) of the block compared to the relative origin
			int translatedY;
			boolean isLeftBreached = false;  // Boolean variables which track if the shape exceeds the left or right boundary
			boolean isRightBreached = false;  // after being rotated.
			boolean isBottomBreached = false;   // Detects if a block has exceeded the bottom border of the grid
		
		if (blocks.get(0).getType() != 4) {   // Only rotates the shape if it isn't a square block, which doesn't need rotation
			
			for (Block currentBlock : blocks) {
				relativeX = currentBlock.getX() - centralX;  // Makes the central coordinates the origin
				relativeY = currentBlock.getY() - centralY;

				translatedX = relativeY;   // Uses (y,-x) to rotate the block 90 degrees counter clockwise
				translatedY = -relativeX;

				currentBlock.setX(translatedX + centralX);  // Adds the coordinates of the relative origin,
				currentBlock.setY(translatedY + centralY);  // resulting in the coordinates of the rotated block on the
															  // grid

				if (currentBlock.getX() < 0) {  // If a coordinate is less than 0, it's out of bounds to the left
					isLeftBreached = true;

				} else if (currentBlock.getX() > (gridWidth - 1)) {  // If a coordinate is greater than gridWidth-1,
					isRightBreached = true;       // it's out of bounds to the right.

				}
				
				if (currentBlock.getY() > (Grid.HEIGHT - 1)) {  // If the bottom of the grid is breached
					isBottomBreached = true;
				}
			}

			// TODO - Keep moving the shape right or left until the whole shape is in the bounds of the tetris grid

			moveShapeInBounds(isRightBreached, isLeftBreached, gridWidth, isBottomBreached);
			
			
			
		}
		
		

	}
	
	/** 
	 * A method which changes the x-positions of the shape to make sure they stay within the grid's boundaries
	 * @param isRightBreached
	 * 		A {@code boolean} which represents if the shape has exceeded the right boundary of the board
	 * @param isLeftBreached
	 * 		A {@code boolean} which represents if the shape has exceeded the left boundary of the board
	 * @param gridWidth
	 * 		An {@code int} which represents the width of the board - how many spaces are in the tetris grid
	 * @param isBottmBreached
	 * 		A {@code boolean} which represents if the bottom of the grid has been breached
	 */
	private void moveShapeInBounds(boolean isRightBreached, boolean isLeftBreached, int gridWidth, boolean isBottomBreached) {
		// Variable Definition
		int mostOutOfBoundsLeft = 0;  // An int storing the coordinate that is most out of bounds of the grid to the left
		int mostOutOfBoundsRight = gridWidth - 1;  // An int storing the coordinate that is most out of bounds of the grid to the right
		int mostOutOfBoundsDown = Grid.HEIGHT - 1;
		
		// Processing
		if (isLeftBreached) {  // If out of bounds to the left
			
			for (Block currentBlock : blocks) {   // Cycling through all blocks
				if (currentBlock.getX() < 0 && currentBlock.getX() < mostOutOfBoundsLeft) {  // If the block is to the left and the most to the left
					mostOutOfBoundsLeft = currentBlock.getX();   // Assign x-coordinate to mostOutOfBoundsLeft
				}
			}
			
			for (Block currentBlock: blocks) {  // Cycling through all blocks
				currentBlock.setX(currentBlock.getX() - mostOutOfBoundsLeft);   // Moves all of them into bounds of grid
			}
			
		} else if (isRightBreached) {   // If out of bounds to the right
			
			for (Block currentBlock : blocks) {
				if (currentBlock.getX() > (gridWidth - 1) && currentBlock.getX() > mostOutOfBoundsRight) {  // If the block is to the left and the most to the left
					mostOutOfBoundsRight = currentBlock.getX();   // Assign x-coordinate to mostOutOfBoundsRight
				}
			}
			
			for (Block currentBlock: blocks) {  // Cycling through all blocks
				currentBlock.setX(currentBlock.getX() - (mostOutOfBoundsRight - (gridWidth-1)));   // Moves all of them into bounds of grid
			}
			
		}
		
		if (isBottomBreached) {  // If the bottom is breached
			
			for (Block currentBlock : blocks) {  // Cycle through every block
				if (currentBlock.getY() > (Grid.HEIGHT - 1) && currentBlock.getY() > mostOutOfBoundsDown) {  // If the block is below and most below
					mostOutOfBoundsDown = currentBlock.getY();  // Assign its y-coordinate to the most below coordinate
				}
			}
			
			for (Block currentBlock: blocks) {  // Cycling through all blocks
				currentBlock.setY(currentBlock.getY() - (mostOutOfBoundsDown - (Grid.HEIGHT - 1)));   // Moves all of them into bounds of grid
			}
			
		}
		
	}

	/**
	 * A method which rotates a shape 90 degrees clockwise
	 * @param gridWidth
	 * 		An {@code int} representing how many squares (spaces in grid) wide the tetris grid is
	 * @param gridBlocks
	 * 		An {code ArrayList} that stores the information of all the blocks on the tetris grid
	 */
	public void rotateClockwise(int gridWidth, ArrayList<Block> gridBlocks) {
		// Variable Definition
		int centralX = blocks.get(1).getX();  // Gets the x coordinate for the block which the shape will be rotated about
		int centralY = blocks.get(1).getY();  // Gets the y coordinate for the block which the shape will be rotated about		  
		int relativeX;  // Relative x and y when viewing the central x and y as the origin
		int relativeY;
		int translatedX;  // Translated coordinates (90 degree shift) of the block compared to the relative origin
		int translatedY;
		boolean isLeftBreached = false;  // Boolean variables which track if the shape exceeds the left or right boundary
		boolean isRightBreached = false;  // after being rotated.
		boolean isBottomBreached = false;   // Detects if a block has exceeded the bottom border of the grid

		if (blocks.get(0).getType() != 4) {   // Only rotates the shape if it isn't a square block, which doesn't need rotation
			
			for (Block currentBlock : blocks) {
				relativeX = currentBlock.getX() - centralX;  // Makes the central coordinates the origin
				relativeY = currentBlock.getY() - centralY;

				translatedX = -relativeY;   // Uses (y,-x) to rotate the block 90 degrees clockwise
				translatedY = relativeX;

				currentBlock.setX(translatedX + centralX);  // Adds the coordinates of the relative origin,
				currentBlock.setY(translatedY + centralY);  // resulting in the coordinates of the rotated block on the grid

				if (currentBlock.getX() < 0) {  // If a coordinate is less than 0, it's out of bounds to the left
					isLeftBreached = true;

				} else if (currentBlock.getX() > (gridWidth - 1)) {  // If a coordinate is greater than gridWidth-1,
					isRightBreached = true;       // it's out of bounds to the right.

				}
				
				if (currentBlock.getY() > (Grid.HEIGHT - 1)) { // If the bottom of the grid is breached
					isBottomBreached = true;
				}

			}
			
			moveShapeInBounds(isRightBreached, isLeftBreached, gridWidth, isBottomBreached);  // Moves the shape in bounds of the grid
			
		}
		
	}

}
 
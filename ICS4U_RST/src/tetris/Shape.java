package tetris;

import java.util.ArrayList;

/**
 * 
 * @author: Rohan Daves and Jake Pommainville
 * @date: June 9, 2024
 * @course: ICS4U
 * @program: Shape.java
 * @purpose: This class handles the shapes of the blocks. Several blocks come together to make different distinct shapes
 *
 */
public class Shape {
	// Variable Definition
	private int type;  // Stores the type of shape
	private int rotation;  // Stores the rotation of the shape
	private ArrayList<Block> blocks;  // Stores all the blocks which form a shape

	/**
	 * Constructor method for the shape class
	 * @param shapeType
	 * 		An <code>int</code> containing the type of shape being created
	 */
	public Shape(int shapeType) {
		type = shapeType;
		rotation = 0;
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
	 * A method which rotates a shape 90 degrees counter clockwise
	 * @param gridWidth
	 * 		An {@code int} representing how many squares (spaces in grid) wide the tetris grid is
	 */
	public void rotateCounterClockwise(int gridWidth) {
		// Variable Definition
		int centralX = blocks.get(1).getX();  // Gets the x coordinate for the block which the shape will be rotated
											  // about
		int centralY = blocks.get(1).getY();  // Gets the y coordinate for the block which the shape will be rotated
											  // about
		int relativeX;  // Relative x and y when viewing the central x and y as the origin
		int relativeY;
		int translatedX;  // Translated coordinates (90 degree shift) of the block compared to the relative origin
		int translatedY;
		boolean isLeftBreached;  // Boolean variables which track if the shape exceeds the left or right boundary after
		boolean isRightBreached;  // being rotated.

		for (Block currentBlock : blocks) {
			relativeX = currentBlock.getX() - centralX;  // Makes the central coordinates the origin
			relativeY = currentBlock.getY() - centralY;

			translatedX = relativeY;   // Uses (y,-x) to rotate the block 90 degrees counter clockwise
			translatedY = -relativeX;

			currentBlock.setX(translatedX + centralX);  // Adds the coordinates of the relative origin,
			currentBlock.setY(translatedY + centralY);  // resulting in the coordinates of the rotated block on the grid

			if (currentBlock.getX() < 0) {  // If a coordinate is less than 0, it's out of bounds to the left
				isLeftBreached = true;

			} else if (currentBlock.getX() > (gridWidth - 1)) {  // If a coordinate is greater than gridWidth-1, it's
				isRightBreached = true;       // out of bounds to the right.

			}
		}

		//TODO - Keep moving the shape right or left until the whole shape is in the bounds of the tetris grid
		
		for (Block currentBlock : blocks) {
			
		}

	}

	/**
	 * A method which rotates a shape 90 degrees clockwise
	 * @param gridWidth
	 * 		An {@code int} representing how many squares (spaces in grid) wide the tetris grid is
	 */
	public void rotateClockwise(int gridWidth) {
		// Variable Definition
		int centralX = blocks.get(1).getX();  // Gets the x coordinate for the block which the shape will be rotated
											  // about
		int centralY = blocks.get(1).getY();  // Gets the y coordinate for the block which the shape will be rotated
											  // about
		int relativeX;  // Relative x and y when viewing the central x and y as the origin
		int relativeY;
		int translatedX;  // Translated coordinates (90 degree shift) of the block compared to the relative origin
		int translatedY;

		for (Block currentBlock : blocks) {
			relativeX = currentBlock.getX() - centralX;  // Makes the central coordinates the origin
			relativeY = currentBlock.getY() - centralY;

			translatedX = -relativeY;   // Uses (y,-x) to rotate the block 90 degrees clockwise
			translatedY = relativeX;

			currentBlock.setX(translatedX + centralX);  // Adds the coordinates of the relative origin,
			currentBlock.setY(translatedY + centralY);  // resulting in the coordinates of the rotated block on the grid

		}

	}

}
 
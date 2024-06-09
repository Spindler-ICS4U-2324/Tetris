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
	}

	/**
	 * A method which creates a shape based on the type desired. Shapes include a line block, L-block, J-block, square block, 
	 * S-block, T-block, and Z-block
	 */
	public void createShape() {
		switch (type) {
		case 1:    // Line block
			this.blocks.add(new Block(4, 0, type));
			this.blocks.add(new Block(5, 0, type));
			this.blocks.add(new Block(6, 0, type));
			this.blocks.add(new Block(7, 0, type));

			break;

		case 2:  // L-block
			this.blocks.add(new Block(4, 1, type));
			this.blocks.add(new Block(5, 1, type));
			this.blocks.add(new Block(6, 1, type));
			this.blocks.add(new Block(6, 0, type));

			break;

		case 3:  // J-block
			this.blocks.add(new Block(4, 1, type));
			this.blocks.add(new Block(5, 1, type));
			this.blocks.add(new Block(6, 1, type));
			this.blocks.add(new Block(4, 0, type));

			break;

		case 4:  // Square block
			this.blocks.add(new Block(5, 0, type));
			this.blocks.add(new Block(6, 0, type));
			this.blocks.add(new Block(5, 1, type));
			this.blocks.add(new Block(6, 1, type));

			break;

		case 5:  // S-block
			this.blocks.add(new Block(4, 1, type));
			this.blocks.add(new Block(5, 1, type));
			this.blocks.add(new Block(5, 0, type));
			this.blocks.add(new Block(6, 0, type));

			break;

		case 6:  // T-block
			this.blocks.add(new Block(4, 0, type));
			this.blocks.add(new Block(5, 0, type));
			this.blocks.add(new Block(6, 0, type));
			this.blocks.add(new Block(5, 1, type));

			break;

		case 7:  // Z-block
			this.blocks.add(new Block(4, 0, type));
			this.blocks.add(new Block(5, 0, type));
			this.blocks.add(new Block(5, 1, type));
			this.blocks.add(new Block(6, 1, type));

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

}

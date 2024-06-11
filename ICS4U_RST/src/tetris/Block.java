package tetris;

/**
 * 
 * @author: Rohan Daves and Jake Pommainville
 * @date: June 7, 2024
 * @course: ICS4U
 * @program: Block.java
 * @purpose: A class to handle colours and coordinates of individual blocks (squares) on the gridpane
 *
 */
public class Block {
	// Variable Definition
	private int x;  // x-coordinate of block
	private int y;  // y-coordinate of block
	private int type;  // Colour of the block

	/**
	 * The default constructor method to create a block
	 * @param xCoordinate
	 * 		An <code>int</code> containing the x-coordinate of the block
	 * @param yCoordinate
	 * 		An <code>int</code> containing the y-coordinate of the block
	 */
	public Block(int xCoordinate, int yCoordinate) {
		x = xCoordinate;
		y = yCoordinate;  // Assigns the private variables the variables passed through the constructor method
	}

	/**
	 * An overloaded constructor method to create a block
	 * @param xCoordinate
	 * 		An <code>int</code> containing the x-coordinate of the block
	 * @param yCoordinate
	 * 		An <code>int</code> containing the y-coordinate of the block
	 * @param shapeType
	 * 		An <code>int</code> containing the type of block. An <code>int</code> containing the type of block. 
	 * <code>1</code> represents a line block, <code>2</code> represents an L-block, <code>3</code> represents a J-block,
	 * <code>4</code> represents a square block, <code>5</code> represents an S-block, 
	 * <code>6</code> represents a T-block, <code>7</code> represents a Z-block.
	 */
	public Block(int xCoordinate, int yCoordinate, int shapeType) {
		x = xCoordinate;
		y = yCoordinate;  // Assigns the private variables the variables passed through the constructor method
		type = shapeType;  // Assigns shapeType to type, representing the type of block
	}

	/**
	 * A mutator method which allows the x coordinate of the block to be changed
	 * @param xCoordinate
	 * 		An <code>int</code> which contains the x coordinate of the block will be changed to 
	 */
	public void setX(int xCoordinate) {
		x = xCoordinate;
	}
	
	/**
	 * A mutator method which allows the x coordinate of the block to be changed
	 * @param xCoordinate
	 * 		An <code>int</code> which contains the x coordinate of the block will be changed to 
	 */
	public void setY(int yCoordinate) {
		y = yCoordinate;
	}
	
	/**
	 * A mutator method which allows the y-coordinate of the block to be changed
	 * @param shift
	 * 		An <code>int</code> containing the amount by which the user wants to shift the block in the y-axis
	 */
	public void modY(int shift) {
		y += shift;  // Changes the block's y-coordinate
	}
	
	/**
	 * A mutator method which allows the y-coordinate of the block to be increased by 1 (move down)
	 */
	public void modY() {
		y += 1;  // Changes the block's y-coordinate
	}

	/**
	 * A mutator method which allows the x-coordinate of the block to be changed
	 * @param shift
	 * 		An <code>int</code> containing the amount by which the user wants to shift the block in the x-axis
	 */
	public void modX(int shift) {
		x += shift;  // Changes the block's x-coordinate
	}
	
	

	/**
	 * Accessor method which allows users to get the y-coordinate of the block
	 * @return An <code>int</code> containing the y-coordinate of the block
	 */
	public int getY() {
		return y;  // Returns the y-coordinate of the block
	}

	/**
	 * Accessor method which allows users to get the x-coordinate of the block
	 * @return An <code>int</code> containing the x-coordinate of the block
	 */
	public int getX() {
		return x;  // Returns the x-coordinate of the block
	}

	/**
	 * Accessor method which allows users to get the type of the block
	 * @return An <code>int</code> containing the type of the block. An <code>int</code> containing the type of block. <code>1</code> represents a line block, 
	 * <code>2</code> represents an L-block, <code>3</code> represents a J-block, <code>4</code> represents a square block,
	 * <code>5</code> represents an S-block, <code>6</code> represents a T-block, <code>7</code> represents a Z-block.
	 */
	public int getType() {
		return type;  // Returns the type of the block
	}

	/** 
	 * Mutator method which sets types of blocks
	 * @param type
	 * 		An <code>int</code> containing the type of block. <code>1</code> represents a line block, 
	 * <code>2</code> represents an L-block, <code>3</code> represents a J-block, <code>4</code> represents a square block,
	 * <code>5</code> represents an S-block, <code>6</code> represents a T-block, <code>7</code> represents a Z-block.
	 */
	public void setType(int type) {
		this.type = type;

	}

	/**
	 * Overrided equals method which compares blocks
	 * @param obj
	 * 		An <code>Object</code> which will be compared to the block
	 * @return A <code>boolean</code> which represents if the object is equal to the one being compared
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Block) {   // If the object is a block
			if (((Block) obj).x == x && ((Block) obj).y == y) {  // If the x and y coordinates are the same
				return true;  // Return true
			}
		}

		return false;  // If none of previous conditions are met, return false
	}

} 

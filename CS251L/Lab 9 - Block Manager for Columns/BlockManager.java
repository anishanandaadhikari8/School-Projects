/**
 * Anish Adhikari UNM- aaadhikari999@gmail.com
 * CS 251 - Lab 9 Columns Game Model
 * 
 * Model class for the columns game.
 * 
 * Includes many methods for manipulating a board with Blocks.
 * Removing, searching for matches, dropping blocks, etc.
 * 
 * All of this will be displayed in a GUI to represent physical
 * blocks that will be moved around.
 * 
 * BlockManager.java
 */
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class BlockManager {

    int gameRows;
    int gameCols;
    int blockTypes;
    char[][] board;
    Random rand;
    
    /**
     * Constructor to make a whole block manager
     * @param randomSeed Seed for the random number generator. Eventually the "real" random number will 
     * contain a seed like a time-stamp
     * @param rows How many rows large you want the board to be.
     * @param cols How many columns large you want the board to be.
     * @param blockTypes How many block types you'd like to use for your game.
     */
    public BlockManager(int randomSeed, int rows, int cols, int blockTypes) {
        this.gameRows = rows;
        this.gameCols = cols;
        this.blockTypes = blockTypes;
        
        // Initializing the board
        board = new char[rows][cols];
        for(int i = 0; i < rows; ++i) {
            for(int j = 0; j < cols; ++j) {
                board[i][j] = '.';
            }
        }
        
        rand = new Random(randomSeed);
    }
    
    /**
     * Makes a new random block of the allowable blocktypes
     * @return a new block
     */
    public Block makeBlock() {
        return new Block(rand, blockTypes);
    }
    
    /**
     * Drops a new block onto the board.
     * @param dropBlock This is the given block to be dropped on the board
     * @param column This is the given column to drop the piece into
     * @return The bottom coordinate of the block that was placed
     */
    public Coordinate dropBlock(Block dropBlock, int column) {
        
        // Finding the first row that is available, 0 is a valid row
        int rowToOccupy = -1;
        for(int i = gameRows - 1; i >= 0; --i) {
            if (board[i][column] == '.') {
                rowToOccupy = i;
                break;
            }
        }
        
        // Placing the elements of the block onto the board, 
        // TODO: if rowToOccupy is 0 or 1, it's gameOver in the GUI, 
        // this functionality will get added in part 2
        if(rowToOccupy >= 0) {
            board[rowToOccupy][column] = dropBlock.getElements()[2];
        }
        if(rowToOccupy >= 1) {
            board[rowToOccupy-1][column] = dropBlock.getElements()[1];
        }
        if(rowToOccupy >= 2) {
            board[rowToOccupy-2][column] = dropBlock.getElements()[0];  
        }
        
        // Returning the coordinates of the bottom element of the block that was dropped
        return new Coordinate(rowToOccupy, column);
    }
    
    /**
     * Checks the 3 elements of the piece that was just dropped, calls the 
     * heavy lifting search method to look in all 4x2 directions.
     * @param firstElement The bottom coordinate of the block we just dropped
     * @return A set of elements that "won" and need to be removed
     */
    public Set<Coordinate> checkBoardAfterDrop(Coordinate firstElement) {
        
        // This set will contain any coordinates that need to be removed 
        Set<Coordinate> winningElements = new HashSet<Coordinate>();
        
        // Checking 3 elements or until the top of the board
        int iterations = (firstElement.getRow() < 3) ? firstElement.getRow() : 3;
        for(int i = 0; i < iterations; ++i) {
            search(firstElement, -1, -1, winningElements);
            search(firstElement, -1, 0, winningElements);
            search(firstElement, -1, 1, winningElements);
            search(firstElement, 0, -1, winningElements);
            
            // Changing the row to check to go up the puzzle
            firstElement.setRow(firstElement.getRow() - 1);
        }
        
        return winningElements;
        
    }
    
    /**
     * The "heavy-lifting" method that searches the board for matches of 3, 4 or 5.
     * First, it finds the bounds of the given coordinate to find the max iterations
     * to go in each direction.
     * It uses those bounds as limits to iterate backwards and forwards and add coordinates to a 
     * temporary set until it hits a non-matching element.
     * Lastly, this method adds the temporary set to the master set of winners if it has 3 or more elements.
     * 
     * @param element A given coordinate to search around.
     * @param rowOffset The row offset to search for.
     * @param colOffset The column offset to search for.
     * @param winners Set of "winning elements" that need to be removed
     */
    public void search(Coordinate element, int rowOffset, int colOffset, Set<Coordinate> winners) {
        
        int workingRow = element.getRow();
        int workingCol = element.getCol();
        int backwardIters, forwardIters;
        
        // Getting the maximum bounds of each direction so we don't go outside the bounds of the board
        int upperBound = workingRow;
        int lowerBound = gameRows - workingRow - 1;
        int leftBound = workingCol;
        int rightBound = gameCols - workingCol - 1;

        // Figuring out how many iterations to go forwards and backwards in the for loops later on
        if(rowOffset == -1 && colOffset == -1) {
            backwardIters = (leftBound < upperBound) ? leftBound : upperBound;
            forwardIters = (rightBound < lowerBound) ? rightBound : lowerBound;
        }
        else if(rowOffset == 0 && colOffset == -1) {
            backwardIters = leftBound;
            forwardIters = rightBound;
        }
        else if(rowOffset == -1 && colOffset == 0) {
            backwardIters = upperBound;
            forwardIters = lowerBound;
        }
        else{
            backwardIters = (rightBound < upperBound) ? rightBound : upperBound;
            forwardIters = (leftBound < lowerBound) ? leftBound: lowerBound;
        }
        
        // Making a temporary list of coordinates that match our base element, also adding the base element
        Set<Coordinate> tempCoords = new HashSet<Coordinate>();
        tempCoords.add(new Coordinate(workingRow, workingCol));
        
        // Iterating backwards and finding our our matches
        for(int i = 1; i <= backwardIters; ++i) {
            if(board[workingRow][workingCol] == board[workingRow + rowOffset*i][workingCol + colOffset*i]) {
                tempCoords.add(new Coordinate(workingRow + rowOffset*i, workingCol + colOffset*i));
            }
            else break;
        }
        
        // Iterating fowards and finding our matches
        for(int i = 1; i <= forwardIters; ++i) {
            if(board[workingRow][workingCol] == board[workingRow - rowOffset*i][workingCol - colOffset*i]) {
                tempCoords.add(new Coordinate(workingRow - rowOffset*i, workingCol - colOffset*i));
            }
            else break;
        }
        
        // If we have 3 total elements in the temporary set, then add it to the master list of "winners"
        if(tempCoords.size() >= 3) {
            winners.addAll(tempCoords);
        }

    }
    
    /**
     * Using the given set and removing all of the elements at each coordinate and
     * then fill those spots with blanks. Additionally, this method will drop 
     * "orphaned" blocks down.
     * @param winners Set of "winning elements" to be removed
     */
    public void removeBlocks(Set<Coordinate> winners) {
        
        Set<Integer> columnsAffected = new HashSet<Integer>();
        
        // Setting each coordinate to remove to a blank character
        for (Coordinate c : winners) {
            columnsAffected.add(c.getCol());
            board[c.getRow()][c.getCol()] = '.';
        }
          
        // Shifting the characters above down into the empty slots for each column that was affected
        for (Integer c : columnsAffected) {
            int howManyBlank = 0;
            int initialBlankElement = 0;
            int elementsToMove = 0;
            
            // Finding the first blank element in the column, from bottom to top
            for(int i = gameRows - 1; i >= 0; --i) {
                if(board[i][c] == '.') {
                    initialBlankElement = i;
                    break;
                }
            }
            
            // Finding how many blank elements are above this one blank element
            for(int i = initialBlankElement; i >= 0; --i) {
                if(board[i][c] == '.') {
                    howManyBlank++;
                }
                else break;
                
            }
            
            // Finding out how many non-blank elements are above the first blank element
            for(int i = initialBlankElement - howManyBlank; i >= 0; --i) {
                if(board[i][c] != '.') {
                    elementsToMove++;
                }
                else break;

            }         
            
            // Actually dropping the pieces down into the empty slots
            if(howManyBlank + elementsToMove != gameRows) {
                for(int i = 0; i < elementsToMove; ++i) {
                    board[initialBlankElement - i][c] = board[initialBlankElement - i - howManyBlank][c];
                    board[initialBlankElement - i - howManyBlank][c] = '.';
                }
            }
            
        }
      
    }
    
    /**
     * This is a "brother" to checkBoardAfterDrop, but instead of searching for the 3
     * elements of a given block, it searches the entire board that isn't blank.
     * This will get called continuously until no winning elements are left.
     * @return A new set of winning elements if any
     */
    public Set<Coordinate> globalSearch() {
        
        Set<Coordinate> winningElements = new HashSet<Coordinate>();
        
        // Filling a set with all the possible elements on the board that aren't blank
        Set<Coordinate> allElements = new HashSet<Coordinate>();
        for(int i = 0; i < gameRows; ++i) {
            for(int j = 0; j < gameCols; ++j) {
                if(board[i][j] != '.') {
                    allElements.add(new Coordinate(i, j)); 
                }
            }
        }
        
        for(Coordinate c : allElements) {
            search(c, -1, -1, winningElements);
            search(c, -1, 0, winningElements);
            search(c, -1, 1, winningElements);
            search(c, 0, -1, winningElements);
        }
        
        return winningElements;   
    }
    
    /**
     * Prints the current board to stdout
     */
    @Override
    public String toString() {
        String outputStr = "";
        for(int i = 0; i < gameRows; ++i) {
            for(int j = 0; j < gameCols; ++j) {
                outputStr += board[i][j];
            }
            outputStr += "\n";
        }
        
        return outputStr;
    }
    
}

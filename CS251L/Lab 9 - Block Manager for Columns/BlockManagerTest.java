/**
 *Anish Adhikari UNM- aaadhikari999@gmail.com
 * CS 251 - Lab 9 Columns Game Model
 * 
 * Test class for testing BlockManager's.
 * 
 * BlockManagerTest.java
 */
import java.util.HashSet;
import java.util.Set;

public class BlockManagerTest {

    public static void main(String[] args) {
        
        // Board with 6 rows, 5 cols, 3 block types
        BlockManager bm1 = new BlockManager(42, 6, 5, 3);
        System.out.println("create empty block manager with 6 rows, 5 cols, 3 block types");
        
        // Printing initial game board
        System.out.println(bm1.toString());
        
        // Dropping a block in column 0
        System.out.println("drop pieces in even columns to start");
        
        dropTestPiece(bm1, 0, 0);
        dropTestPiece(bm1, 2, 0);
        dropTestPiece(bm1, 1, 0);
        dropTestPiece(bm1, 1, 0);
        dropTestPiece(bm1, 2, 0);
        dropTestPiece(bm1, 2, 0);
        dropTestPiece(bm1, 2, 0);
        dropTestPiece(bm1, 1, 0);
        dropTestPiece(bm1, 0, 0);
        dropTestPiece(bm1, 3, 0);
        
        // New board with 10 rows, 8 cols, 5 block types
        BlockManager bm2 = new BlockManager(42, 10, 8, 5);
        System.out.println("create empty block manager with 10 rows, 8 cols, 5 block types");
        System.out.println(bm2.toString());
        System.out.println("drop pieces in even columns to start");
        
        dropTestPiece(bm2, 0, 0);
        dropTestPiece(bm2, 2, 0);
        dropTestPiece(bm2, 4, 0);
        dropTestPiece(bm2, 1, 0);
        dropTestPiece(bm2, 3, 0);     
        
        // Looking for matches of 5
        BlockManager bm3 = new BlockManager(42, 10, 8, 3);
        System.out.println("Testing functionality of finding a match of 5");
        System.out.println(bm3.toString());
        dropTestPiece(bm3, 0, 0);
        dropTestPiece(bm3, 1, 0);
        dropTestPiece(bm3, 3, 0);
        dropTestPiece(bm3, 4, 1);
        dropTestPiece(bm3, 5, 0);

        // Test of 5
        dropTestPiece(bm3, 2, 0);
               
    }
    
    /**
     * Primary test method that will actually drop a piece. Includes a loop that will
     * continue to search for global matches after a successful match.
     * @param bm Block Manager object to populate and call methods off from.
     * @param column Column you want to drop a piece onto the board.
     * @param numRotates How many times you want to rotate the Block you're dropping.
     */
    public static void dropTestPiece(BlockManager bm, int column, int numRotates) {
        Set<Coordinate> winningElements = new HashSet<Coordinate>();
        
        Block dropBlock = bm.makeBlock();
        for(int i = 0; i < numRotates; ++i) {
            dropBlock.rotate();
        }
        
        System.out.println("dropping piece");
        System.out.println(dropBlock.toString());
        System.out.println("in column " + column);
        Coordinate firstElement = bm.dropBlock(dropBlock, column);
        System.out.println(bm.toString());

        winningElements = bm.checkBoardAfterDrop(firstElement);
        
        while(winningElements.size() != 0) {
            System.out.println(winningElements.size() + " blocks will be removed\n");
            bm.removeBlocks(winningElements);
            winningElements = bm.globalSearch();
            System.out.println(bm.toString());
        }
       
        System.out.println("drop complete\n");
        
    }
}

/**
 * Anish Adhikari UNM- aaadhikari999@gmail.com
 * CS 251 - Lab 9 Columns Game Model
 * 
 * Block object will contain 3 elements among a variable amount of block types.
 * Rotate method allows the user to rotate the block to place the block in a 
 * different configuration.
 * 
 * Block.java
 */
import java.util.Random;

public class Block {
    
    char[] elements = new char[3];
    char[] availableChars;
    
    public Block(Random rand, int numOfElements) {
        availableChars = new char[numOfElements];
        
        // Adding elements to our character array to be used in the blocks
        // Starting at 97 for 'a' and going up from there.
        for(int i = 0; i < numOfElements; ++i) {
            availableChars[i] = (char)(97 + i);
        }
        
        int uniqueCharsIndex = availableChars.length - 1;
        
        elements[0] = availableChars[rand.nextInt(uniqueCharsIndex)];
        elements[1] = availableChars[rand.nextInt(uniqueCharsIndex)];
        elements[2] = availableChars[rand.nextInt(uniqueCharsIndex)];
        
    }
    
    /**
     * Rotate method, will wrap an element from the bottom of the block to the top, 
     * top to middle and middle to botom.
     */
    public void rotate() {
        char tmp = elements[2];
        
        elements[2] = elements[1];
        elements[1] = elements[0];
        elements[0] = tmp;      
    }
    
    /**
     * Gives the whole list of elements or chars inside the block
     * @return Character array containing elements
     */
    public char[] getElements() {
        return elements;
    }
    
    /**
     * String representation of the character array of elements in the Block.
     */
    @Override
    public String toString() {
        return(elements[0] + "\n" + elements[1] + "\n" + elements[2]);
    }
    
}

import javafx.scene.canvas.GraphicsContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class BoardSetup
{
    private Random rand;
    private ArrayList<BogglePiece> board;
    private GraphicsContext gcCanvas;

    /**
     * BoardSetup()
     * This is BoardSetup's constructor.
     * @param board Board we will setup with BogglePiece's
     * @param gcCanvas GraphicsContext we will paint our pieces onto
     */
    public BoardSetup(ArrayList<BogglePiece> board, GraphicsContext gcCanvas)
    {
        this.board = board;
        this.gcCanvas = gcCanvas;
        rand = new Random();
    }

    /**
     * generateNewBoard()
     * Forward facing method of this class.
     * This generates a board based upon parameters that are given by the user.
     * It handles drawing all of the initial pieces as well as making the calls for making a random
     * dice board or a real dice board.
     * @param sizeOfSide Pixel size of each piece by side. They are squares so this is width and height
     * @param gridSize Size of board by side. This should be 4, 5, or 6 depending upon what game type given
     * @param realDice Boolean that lets us know whether to generate random dice or real Boggle dice
     */
    public void generateNewBoard(int sizeOfSide, int gridSize, boolean realDice)
    {
        int fixedPadding = sizeOfSide / 4;

        ArrayList<Object> chars = new ArrayList<>();

        // Guarantees a valid board so we don't have to re-draw (expensive) multiple times
        if(realDice == false) { generateValidRandomBoard(chars, gridSize); }
        else
        {
            BoggleDieSet dieSet = new BoggleDieSet(gridSize);
            dieSet.rollTheDice(chars);
        }

        for(int i = 0; i < gridSize*gridSize; ++i)
        {
            BogglePiece bp = new BogglePiece(fixedPadding + (i%gridSize) * (sizeOfSide + fixedPadding),
                    fixedPadding + (i/gridSize)*(sizeOfSide + fixedPadding), sizeOfSide, sizeOfSide, chars.get(i));
            board.add(bp);

            // Each piece knows how to draw itself
            bp.draw(gcCanvas);
        }

    }

    /**
     * generateValidRandomBoard()
     * If we are using random dice this is the powerhouse method that selects all of the pieces, makes sure there
     * is no more than 4 of them and then also randomly places U's behind or in front of Q's.
     *
     * When selecting a letter, if that letter is in the list 4 times already, then the recursive helper
     * method will be called to guarantee a valid letter.
     *
     * When placing a U next to a Q there is checks to make sure that still no more than 4 Us are placed in total.
     * Additionally, there is more checks to make sure the Us aren't placed in odd places like on a 5x5 board if a
     * Q is at element 4 (top right), then placing a U at element 5 doesn't touch the original Q.
     * @param chars Structure that holds the letters to be placed on the boggle board. In this case, they will be
     *              chars and not strings.
     * @param gridSize Size of board by side. This should be 4, 5, or 6 depending upon what game type given.
     */
    private void generateValidRandomBoard(ArrayList<Object> chars, int gridSize)
    {
        ArrayList<Integer> qLocations = new ArrayList<>();

        HashMap<Character, Integer> occurrenceOfChars = new HashMap<>();

        ArrayList<Character> badChars = new ArrayList<>();
        Character charToAdd;

        int count = 0;

        // This loop places pieces in the chars array and prevents more than 4 of any given tile being selected
        for(int i = 0; i < gridSize*gridSize; ++i)
        {
            charToAdd = getRandomLetter();

            count = occurrenceOfChars.containsKey(charToAdd) ? occurrenceOfChars.get(charToAdd) : 0;
            if(count >= 4)
            {
                badChars.add(charToAdd);
                charToAdd = validRandomBoardHelper(badChars);
            }

            chars.add(charToAdd);
            occurrenceOfChars.put(charToAdd, count + 1);
            if(charToAdd == 'Q') { qLocations.add(i); }
        }

        // Possibly placing U's next to Q's
        for(int i = 0; i < qLocations.size(); ++i)
        {
            int indexOfQ = qLocations.get(i);
            count = occurrenceOfChars.containsKey('U') ? occurrenceOfChars.get('U') : 0;

            // Generates a random number between -1, 0 and 1. If -1, place u to left of Q, if 1, place u to right of Q,
            // if 0 don't place u next to Q at all at all
            int randomQPlacement = rand.nextInt(3) - 1;

            // Attempting to place a 'U' on the left side of the Q
            // Makes a check to make sure it doesn't end up on the row above on the opposite side or off the board
            // Additionally, makes a check to make sure we don't have more than 4 Us
            if(randomQPlacement == -1 && indexOfQ % gridSize != 0 && count < 4)
            {
                chars.add(indexOfQ - 1, 'U');
                chars.remove(indexOfQ); // When we add an element at Q - 1, Q becomes Q + 1, so we delete the char at Q
                occurrenceOfChars.put('U', count + 1);
            }
            else if(randomQPlacement == 1 && indexOfQ + 1 % gridSize != 0 && count < 4)
            {
                chars.add(indexOfQ + 1, 'U');
                chars.remove(indexOfQ - 1); // Delete the element behind the q
                occurrenceOfChars.put('U', count + 1);
            }

        }

    }

    /**
     * validRandomBoardHelper()
     * This is a recursive method that guarantees a valid character based upon what characters are already
     * on the board 4 times.
     * @param badChars List of characters that are on the list 4 times. We don't want to generate more of these.
     * @return A valid char to be placed in the chars list and then onto the board.
     */
    private char validRandomBoardHelper(ArrayList<Character> badChars)
    {
        char charToAdd = getRandomLetter();
        for(int i = 0; i < badChars.size(); ++i)
        {
            if(badChars.get(i) == charToAdd)
            {
                validRandomBoardHelper(badChars);
            }
        }
        return charToAdd;
    }

    /**
     * getRandomLetter()
     * Gets a random uppercase character from A to Z (ASCII 65-90)
     * @return A random char
     */
    private char getRandomLetter() { return (char)(rand.nextInt(26) + 65); }

}

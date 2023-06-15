import java.util.ArrayList;
import java.util.Random;

public class BoggleDie
{
    private ArrayList<String> die;

    /**
     * BoggleDie()
     * This is BoggleDie's constructor.
     * @param dieString The string representing a given die with 6 die-faces.
     */
    public BoggleDie(String dieString)
    {
        die = new ArrayList<>();
        addDieFaces(dieString);
    }

    /**
     * addDieFaces()
     * Takes the string that is given from the dieSet and adds each 1 or 2-character die face to an
     * element in the die
     * @param dieString The die string that was gathered from real Boggle pieces to parse.
     */
    private void addDieFaces(String dieString)
    {
        // Adder is needed as we will potentially have more than 6 characters in the string we are checking.
        // Imagine the die's with only faces with 2 letters, that string will be 12 characters long
        int adder = 0;
        StringBuilder possibleTwoChar = new StringBuilder();

        // Adding the 1 or 2-letter die faces to the die list
        for(int i = 0; i < 6; ++i)
        {
            if(i + adder + 1 != dieString.length() && Character.isLowerCase(dieString.charAt(i + adder + 1)) == true)
            {
                possibleTwoChar.append(dieString.charAt(i + adder));
                possibleTwoChar.append(dieString.charAt(i + adder + 1));
                die.add(possibleTwoChar.toString());
                adder++;
                possibleTwoChar = new StringBuilder();
            }
            else
            {
                die.add(String.valueOf(dieString.charAt(adder + i)));
            }
        }
    }

    /**
     * rollDie()
     * Picks a random "character" (1 or 2-character string based on whether it's a normal char like 'E' or 'Qu')
     * @return String representing a chosen die face that will ultimately be displayed on the board.
     */
    public String rollDie()
    {
        Random rand = new Random();
        return die.get(rand.nextInt(6)); // 0 - 5 positions
    }

}

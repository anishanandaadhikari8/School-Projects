import java.util.ArrayList;
import java.util.Collections;

public class BoggleDieSet
{
    private ArrayList<BoggleDie> boggleDieSet;

    /**
     * This is BoggleDieSet's constructor
     * @param gridSize This is the size of the grid we're going to create, 4x4, 5x5, 6x6
     */
    public BoggleDieSet(int gridSize)
    {
        boggleDieSet = new ArrayList<>();
        if(gridSize == 4) { makeBoggleSet(); }
        else if(gridSize == 5) { makeBigBoggleSet(); }
        else { makeSuperBigBoggleSet(); }
    }

    /**
     * makeBoggleSet()
     * Method that fills the dieset with a 4x4 Boggle letter distribution. I found the Boggle letter distribution at
     * https://boardgamegeek.com/geeklist/182883/item/3595361#item3595361
     * I was also able to implement a design that allowed for the 2-character die faces to be used.
     */
    private void makeBoggleSet()
    {
        boggleDieSet.add(new BoggleDie("AACIOT"));
        boggleDieSet.add(new BoggleDie("ABILTY"));
        boggleDieSet.add(new BoggleDie("ABJMOQu"));
        boggleDieSet.add(new BoggleDie("ACDEMP"));
        boggleDieSet.add(new BoggleDie("ACELRS"));
        boggleDieSet.add(new BoggleDie("ADENVZ"));
        boggleDieSet.add(new BoggleDie("AHMORS"));
        boggleDieSet.add(new BoggleDie("BFIORX"));
        boggleDieSet.add(new BoggleDie("DENOSW"));
        boggleDieSet.add(new BoggleDie("DKNOTU"));
        boggleDieSet.add(new BoggleDie("EEFHIY"));
        boggleDieSet.add(new BoggleDie("EGINTV"));
        boggleDieSet.add(new BoggleDie("EGKLUY"));
        boggleDieSet.add(new BoggleDie("EHINPS"));
        boggleDieSet.add(new BoggleDie("ELPSTU"));
        boggleDieSet.add(new BoggleDie("GILRUW"));
    }

    /**
     * makeBigBoggleSet()
     * Method that fills the dieset with a 5x5 Big Boggle letter distribution. Letter distribution found at
     * https://boardgamegeek.com/thread/300883/letter-distribution
     * Also uses 2-character die faces
     */
    private void makeBigBoggleSet()
    {
        boggleDieSet.add(new BoggleDie("AAAFRS"));
        boggleDieSet.add(new BoggleDie("AAEEEE"));
        boggleDieSet.add(new BoggleDie("AAFIRS"));
        boggleDieSet.add(new BoggleDie("ADENNN"));
        boggleDieSet.add(new BoggleDie("AEEEEM"));
        boggleDieSet.add(new BoggleDie("AEEGMU"));
        boggleDieSet.add(new BoggleDie("AEGMNN"));
        boggleDieSet.add(new BoggleDie("AFIRSY"));
        boggleDieSet.add(new BoggleDie("BJKQXZ"));
        boggleDieSet.add(new BoggleDie("CCNSTW"));
        boggleDieSet.add(new BoggleDie("CEIILT"));
        boggleDieSet.add(new BoggleDie("CEIPST"));
        boggleDieSet.add(new BoggleDie("DDLNOR"));
        boggleDieSet.add(new BoggleDie("DHHLOR"));
        boggleDieSet.add(new BoggleDie("DHHNOT"));
        boggleDieSet.add(new BoggleDie("DHLNOR"));
        boggleDieSet.add(new BoggleDie("EIIITT"));
        boggleDieSet.add(new BoggleDie("CEILPT"));
        boggleDieSet.add(new BoggleDie("EMOTTT"));
        boggleDieSet.add(new BoggleDie("ENSSSU"));
        boggleDieSet.add(new BoggleDie("QuInThErHeAn"));
        boggleDieSet.add(new BoggleDie("GORRVW"));
        boggleDieSet.add(new BoggleDie("HIPRRY"));
        boggleDieSet.add(new BoggleDie("NOOTUW"));
        boggleDieSet.add(new BoggleDie("OOOTTU"));
    }

    /**
     * makeSuperBigBoggleSet()
     * Method that fills the dieset with a 6x6 Super Big Boggle letter distribution. Letter distribution found at
     * https://www.boardgamegeek.com/geeklist/182883/item/3813433#item3813433
     * This does not account for the "blocker pieces" that are in the 6x6 like "#", I replaced those with vowels.
     */
    private void makeSuperBigBoggleSet()
    {
        boggleDieSet.add(new BoggleDie("AAAFRS"));
        boggleDieSet.add(new BoggleDie("AAEEEE"));
        boggleDieSet.add(new BoggleDie("AAEEOO"));
        boggleDieSet.add(new BoggleDie("AAFIRS"));
        boggleDieSet.add(new BoggleDie("ABDEIO"));
        boggleDieSet.add(new BoggleDie("ADENNN"));
        boggleDieSet.add(new BoggleDie("AEEEEM"));
        boggleDieSet.add(new BoggleDie("AEEGMU"));
        boggleDieSet.add(new BoggleDie("AEGMNN"));
        boggleDieSet.add(new BoggleDie("AEILMN"));
        boggleDieSet.add(new BoggleDie("AEINOU"));
        boggleDieSet.add(new BoggleDie("AFIRSY"));
        boggleDieSet.add(new BoggleDie("AnErHeInQuTh"));
        boggleDieSet.add(new BoggleDie("BBJKXZ"));
        boggleDieSet.add(new BoggleDie("CCENST"));
        boggleDieSet.add(new BoggleDie("CDDLNN"));
        boggleDieSet.add(new BoggleDie("CEIITT"));
        boggleDieSet.add(new BoggleDie("CEIPST"));
        boggleDieSet.add(new BoggleDie("CFGNUY"));
        boggleDieSet.add(new BoggleDie("DDHNOT"));
        boggleDieSet.add(new BoggleDie("DHHLOR"));
        boggleDieSet.add(new BoggleDie("DHHNOW"));
        boggleDieSet.add(new BoggleDie("DHLNOR"));
        boggleDieSet.add(new BoggleDie("EHILRS"));
        boggleDieSet.add(new BoggleDie("EIILST"));
        boggleDieSet.add(new BoggleDie("EILPST"));
        boggleDieSet.add(new BoggleDie("EIOAUA"));
        boggleDieSet.add(new BoggleDie("EMTTTO"));
        boggleDieSet.add(new BoggleDie("ENSSSU"));
        boggleDieSet.add(new BoggleDie("GORRVW"));
        boggleDieSet.add(new BoggleDie("HIRSTV"));
        boggleDieSet.add(new BoggleDie("HOPRST"));
        boggleDieSet.add(new BoggleDie("IPRSYY"));
        boggleDieSet.add(new BoggleDie("JKQuWXZ"));
        boggleDieSet.add(new BoggleDie("NOOTUW"));
        boggleDieSet.add(new BoggleDie("OOOTTU"));
    }

    /**
     * rollTheDice()
     * "Rolls" all the dice in the dieSet and puts them into the chars list for placement on the board
     * @param chars Structure that holds the letters to be placed on the boggle board.
     */
    public void rollTheDice(ArrayList<Object> chars)
    {
        Collections.shuffle(boggleDieSet);
        for(int i = 0; i < boggleDieSet.size(); ++i)
        {
            chars.add(boggleDieSet.get(i).rollDie());
        }
    }

}

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;
import java.io.*;
import java.util.*;

public class GameManager
{
    private GraphicsContext gcCanvas;
    private ArrayList<BogglePiece> board;
    private LinkedHashSet<String> words;
    private StringBuilder buildingWord;
    private HashMap<Integer, String> badWords;
    private HashMap<Integer, String> goodWords;
    private BufferedReader reader;
    private BoardSetup boardSetup;
    private int currentScore;
    private BogglePiece lastPiecePlayed;
    private boolean gameOver;

    /**
     * GameManager()
     * Constructor for GameManager that starts the logic of the game.
     * @param gcCanvas GraphicsContext to paint onto
     * @param gridSize How big we want our grid. 4 for Boggle, 5 for BB, 6 for SBB
     * @param realDice Whether or not we want to "roll" Boggle dice or get random pieces
     */
    public GameManager(GraphicsContext gcCanvas, int gridSize, boolean realDice, BoggleTimer gameTimer)
    {
        this.gcCanvas = gcCanvas;
        gameOver = false;
        board = new ArrayList<>();
        words = new LinkedHashSet<>();
        buildingWord = new StringBuilder();
        badWords = new HashMap<>();
        goodWords = new HashMap<>();
        boardSetup = new BoardSetup(board, gcCanvas);

        // Lambda syntax for setting a new event handler that calls a method
        gameTimer.setGameOverEvent(e -> displayEndScreen());

        openFile();

        // Setting up the board and starting the game
        boardSetup.generateNewBoard(80, gridSize, realDice);
    }

    /**
     * openFile()
     * Opens the dictionary file for use.
     */
    private void openFile()
    {
        try
        {
            InputStream inputFile = getClass().getResourceAsStream("dictionary.txt");
            reader = new BufferedReader(new InputStreamReader(inputFile, "UTF-8"));
            String line;
            while((line = reader.readLine()) != null) { words.add(line); }
        }
        catch (IOException e) {}
    }

    /**
     * checkWord()
     * This is a powerhouse public facing method to be called when we want to check if a word is in the dictionary.
     *
     * If we find the word in the dictionary it will be added to the good list, if not found then the bad list.
     * If it's already in one of these lists then no list additions happen.
     * Either way, after checking a word we must call the methods to un-highlight the board pieces
     *
     * @param lblWordValid Label that outputs whether we found the word or not.
     * @param lblGoodWords List of chosen words in dictionary.
     * @param lblBadWords List of chosen words not in dictionary.
     * @param lblScore Score label that gets passed to the label update method
     */
    public void checkWord(Label lblWordValid, Label lblGoodWords, Label lblBadWords, Label lblScore)
    {
        String builtWord = buildingWord.toString();

        // Checks if the word isn't already in the list of good/bad words and isn't empty (eliminates duplicates)
        if(goodWords.containsValue(builtWord) == false && badWords.containsValue(builtWord) == false &&
                builtWord.isEmpty() == false && gameOver == false)
        {
            // This will make sure if even a valid word is placed, but under 3 characters, it's still bad
            if(words.contains(builtWord.toLowerCase()) && builtWord.length() > 2)
            {
                lblWordValid.setText(buildingWord.toString() + " found!");
                goodWords.put(goodWords.size(), builtWord);
                playSound("Word_Success.wav");
            }
            else
            {
                lblWordValid.setText(buildingWord.toString() + " not found!");
                badWords.put(badWords.size(), builtWord);
                playSound("Word_Failure.wav");
            }
        }

        updateLabels(lblGoodWords, lblBadWords, lblScore);
        resetBoardAfterCheck();
    }

    /**
     * updateLabels()
     * This method updates all the labels for valid words, invalid words, and the score
     * @param lblGoodWords List of chosen words in dictionary.
     * @param lblBadWords List of chosen words not in dictionary.
     * @param lblScore Score label that gets updated based upon how many points we add up from each word.
     */
    private void updateLabels(Label lblGoodWords, Label lblBadWords, Label lblScore)
    {
        String goodWordText = "";
        String badWordText = "";
        currentScore = 0;
        for(int i = 0; i < goodWords.size(); ++i) { goodWordText += goodWords.get(i) + " - " + calculateScore(goodWords.get(i)) + "\n"; }
        for(int i = 0; i < badWords.size(); ++i) { badWordText += badWords.get(i) + " - 0" + "\n"; }
        lblGoodWords.setText(goodWordText);
        lblBadWords.setText(badWordText);
        lblScore.setText("Score: " + currentScore);
    }

    /**
     * calculateScore()
     * Calculates the score for a given word and adds it to the accumulating score.
     * @param builtWord Valid word that we are getting the score for.
     * @return An int that represent the score of the word.
     */
    private int calculateScore(String builtWord)
    {
        int wordScore = 0;
        if(builtWord.length() > 2) { wordScore = builtWord.length() - 2; }
        currentScore += wordScore;
        return wordScore;
    }

    /**
     * resetBoardAfterCheck()
     * Resets the game state so that the player can try to select another word.
     * The pieces get un-highlighted and a few variables get reset.
     */
    private void resetBoardAfterCheck()
    {
        buildingWord = new StringBuilder();

        // Resetting the board pieces back to "normal", i.e. not highlighted
        for(int i = 0; i < board.size(); ++i)
        {
            if(board.get(i).getIsHighlighted() == true)
            {
                board.get(i).setIsHighlighted(false, gcCanvas);
            }
        }
        lastPiecePlayed = null;
    }

    /**
     * attemptSelectPiece()
     * This method parses a users' click and attempt to select a piece on the board.
     * If the coordinates lay within a piece, it will detect it and we can add the piece's letter to our current
     * building word. There is also logic to check to make sure the piece you're trying to select is valid (a neighbor).
     * @param x x-coord that was clicked on in the canvas
     * @param y y-coord that was clicked on in the canvas
     * @param lblCurrentWord Label that holds the current word we are building.
     */
    public void attemptSelectPiece(int x, int y, Label lblCurrentWord)
    {
        for(int i = 0; i < board.size(); ++i)
        {
            // If the click matches a piece and it isn't already highlighted...
            if(board.get(i).isInBounds(x, y) == true &&
                    board.get(i).getIsHighlighted() == false && gameOver == false)
            {
                // Now that we've selected the piece the player selected, we have to find out if it's a neighbor
                BogglePiece selectedPiece = board.get(i);
                boolean validPlay = false;
                if(lastPiecePlayed == null) { validPlay = true; }
                else if(lastPiecePlayed.isNeighbor(selectedPiece) == true) { validPlay = true; }

                if(validPlay == true)
                {
                    selectedPiece.setIsHighlighted(true, gcCanvas);
                    buildingWord.append(selectedPiece.getLetter());
                    lblCurrentWord.setText("Word: " + buildingWord.toString());
                    lastPiecePlayed = selectedPiece;
                    break;
                }
            }
        }
    }

    /**
     * playSound()
     * Plays a given sound.
     * @param filePath Path of the sound you're trying to play
     */
    private void playSound(String filePath)
    {
        try{
            InputStream is = getClass().getResourceAsStream(filePath);
            AudioStream audioStream = new AudioStream(is);
            AudioPlayer.player.start(audioStream);
        }
        catch(IOException e) { System.out.println(e.getMessage()); }
    }

    /**
     * displayEndScreen()
     * This will get called by the onFinished attribute of the timer to show the user some stats about the game
     * and close the game after closing the alert.
     */
    private void displayEndScreen()
    {
        gameOver = true;
        playSound("Times_Up.wav");
        Alert gameAlert = new Alert(Alert.AlertType.INFORMATION);
        gameAlert.setTitle("Time\'s up!");
        gameAlert.setHeaderText(null);
        String contentText = "The game is over. The time has expired.\nYou made " + goodWords.size() + " word(s) and scored " +
                currentScore + " point(s).";
        gameAlert.setContentText(contentText);

        // There is an interesting java bug that does not allow an alert's showAndWait inside an animation timer.
        // BoggleTimer uses a timeline (an animation timer) to tick and eventually finish the game.
        // Bug details are found here: https://bugs.openjdk.java.net/browse/JDK-8095631

        // This workaround allows the alert to call system exit after closing the alert box.
        gameAlert.showingProperty().addListener((observable,oldValue,newValue)->{
            if(!newValue){ System.exit(0); }
        });
        gameAlert.show();
    }

}

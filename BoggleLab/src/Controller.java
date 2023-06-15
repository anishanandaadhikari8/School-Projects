import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import java.util.*;

public class Controller
{

    @FXML
    private Label lblWordValid, lblCurrentWord, lblGoodWords, lblBadWords, lblScore, lblTime;

    @FXML
    private Canvas canvasBoard;

    private BoggleTimer gameTimer;

    private GameManager gm;

    private GraphicsContext gcCanvas;

    /**
     * Controller()
     * This is Controller's constructor
     */
    public Controller() { }

    /**
     * initialize()
     * This method sets up the UI
     */
    @FXML
    private void initialize()
    {
        gcCanvas = canvasBoard.getGraphicsContext2D();
        int gridSize = askGameType();
        boolean realDice = askLetterType();
        gameTimer = new BoggleTimer(3*60);
        gm = new GameManager(gcCanvas, gridSize, realDice, gameTimer);
        gameTimer.startTime(lblTime);
    }

    /**
     * askGameType()
     * Opens a dialog asking what version of Boggle the player would like to play.
     * @return An int representing the size of the grid. 4 for Boggle, 5 for Big Boggle, 6 for Super Big Boggle
     */
    private int askGameType()
    {
        ArrayList<String> gameTypes = new ArrayList<>();
        gameTypes.add("Boggle (4x4)");
        gameTypes.add("Big Boggle (5x5)");
        gameTypes.add("Super Big Boggle (6x6)");

        ChoiceDialog dialog = new ChoiceDialog(gameTypes.get(0), gameTypes);
        dialog.setTitle("Boggle!");
        dialog.setHeaderText("Pick your game type.");
        Optional<String> result = dialog.showAndWait();

        if(result.isPresent())
        {
            if(result.get() == gameTypes.get(2)) { return 6; }
            else if(result.get() == gameTypes.get(1)) { return 5; }
        }

        return 4;
    }

    /**
     * askLetterType()
     * Opens a dialog asking if the user wants to play with "random dice" or "real Boggle dice"
     * @return A boolean, true if we're using real dice, false if using random dice.
     */
    private boolean askLetterType()
    {
        ArrayList<String> letterTypes = new ArrayList<>();
        letterTypes.add("Spec letters (totally random)");
        letterTypes.add("Real world letters (pre-configured dice)");

        ChoiceDialog dialog = new ChoiceDialog(letterTypes.get(0), letterTypes);
        dialog.setTitle("Boggle!");
        dialog.setHeaderText("Pick your letter type.");
        Optional<String> result = dialog.showAndWait();

        if(result.isPresent())
        {
            if(result.get() == letterTypes.get(1)) { return true; }
        }
        return false;
    }

    /**
     * canvasClicked()
     * When a user clicks on the canvas, this method will parse whether it was a left click or a right click.
     * If it's a left click then we attempt to select the piece (if it's a valid selection) and continue building
     * a word.
     *
     * If it's a right click then we check the word that was built and see if it's valid. From there we update
     * the UI showing the good or bad word the user checked.
     * @param me This is the event that holds the information about where the user clicked.
     */
    @FXML
    private void canvasClicked(MouseEvent me)
    {
        // LEFT CLICK
        if(me.getButton().name() == "PRIMARY")
        {
            gm.attemptSelectPiece((int)me.getX(), (int)me.getY(), lblCurrentWord);
        }
        // RIGHT CLICK
        else if(me.getButton().name() == "SECONDARY")
        {
            gm.checkWord(lblWordValid, lblGoodWords, lblBadWords, lblScore);
        }
    }

    /**
     * rulesClicked()
     * This will open an information alert telling the user the rules of the game.
     */
    @FXML
    private void rulesClicked()
    {
        String rulesString =
                "Rules of Boggle:\nTry to form as many words as possible in the alotted time.\n" +
                        "Letters are valid if:\n-At least 3-letters long\n" +
                        "-Are visible on the tray via adjacent letters without using the same piece\n" +
                        "-Are not capitalized or foreign words." +
                        "-Are listed in the standard dictionary\n\n" +
                        "Score is calculated as such: word length - 2, so \"mouse\" is 3 points\n" +
                        "There is no penalty for short words or words not in dictionary.";
        openMenuItem("Rules", rulesString);
    }

    /**
     * aboutClicked()
     * This will open an information alert giving the user some general information about the program.
     */
    @FXML
    private void aboutClicked()
    {
        String aboutString =
                "Game of Boggle - CS 351 Lab 2\nAnthony Galczak\nWGalczak@gmail.com - agalczak@unm.edu";
        openMenuItem("About", aboutString);
    }

    /**
     * controlsClicked()
     * This will open an information alert giving the user information about the game controls.
     */
    @FXML
    private void controlsClicked()
    {
        String controlsString =
                "Left click to select a game piece.\nKeep in mind you must select an adjacent piece" +
                        " horizontally, vertically, or diagonally.\nRight-click to try to check the" +
                        " current built word.";
        openMenuItem("Controls", controlsString);
    }

    /**
     * openMenuItem()
     * Generalized method for opening an alert for a given menu item being clicked.
     * @param title The title bar to be displayed on the alert.
     * @param info The text to be displayed inside of the alert.
     */
    private void openMenuItem(String title, String info)
    {
        gameTimer.pauseTimer();
        Alert generalAlert = new Alert(Alert.AlertType.INFORMATION);
        generalAlert.setTitle(title);
        generalAlert.setHeaderText(null);
        generalAlert.setContentText(info);
        generalAlert.showAndWait();
        gameTimer.restartTimer();
    }

}

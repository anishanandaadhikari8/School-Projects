import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.util.Duration;
import java.util.concurrent.TimeUnit;

public class BoggleTimer
{

    private Timeline timeline;
    private int timeInSeconds;
    private Label lblTime;
    private EventHandler gameOverEvent;

    /**
     * BoggleTimer()
     * This is BoggleTimer's constructor.
     * @param timeInSeconds How many seconds we want the timer for
     */
    public BoggleTimer(int timeInSeconds)
    {
        this.timeInSeconds = timeInSeconds;
        timeline = new Timeline();
        initializeGameTimer();
    }

    /**
     * startTime()
     * Starts the timer to start playing the game.
     * @param lblTime Label that holds the game time that we want to update.
     */
    public void startTime(Label lblTime)
    {
        timeline.playFromStart();
        this.lblTime = lblTime;
        updateLabel();
    }

    /**
     * restartTimer()
     * Plays the clock in case user somehow paused the timer.
     */
    public void restartTimer()
    {
        timeline.play();
    }

    /**
     * pauseTimer()
     * Pauses the timer in case user somehow pauses the timer (Menus?)
     */
    public void pauseTimer()
    {
        timeline.pause();
    }

    /**
     * setGameOverEvent()
     * This method allows us to set a game over event from anywhere in the program.
     * @param gameOverEvent The event handler we're using when the timer expires.
     */
    public void setGameOverEvent(EventHandler gameOverEvent)
    {
        this.gameOverEvent = gameOverEvent;
        timeline.setOnFinished(gameOverEvent);
    }

    /**
     * initializeGameTimer()
     * Sets up the timeline and handlers for each second of the gameplay and the game end signal
     */
    private void initializeGameTimer() {
        timeline.setCycleCount(timeInSeconds);

        // The first value is how often the keyframe ticks, second one is the event handler
        // that executes on the tick
        KeyFrame gameTick = new KeyFrame(Duration.seconds(1), e->decrementTime());
        timeline.getKeyFrames().add(gameTick);
    }

    /**
     * decrementTime()
     * Method that is called every second to reduce the play clock time
     */
    private void decrementTime()
    {
        timeInSeconds--;
        updateLabel();
    }

    /**
     * updateLabel()
     * Updates the time label
     */
    private void updateLabel()
    {
        String timeString = String.format("%01d:%02d", TimeUnit.SECONDS.toMinutes(timeInSeconds), TimeUnit.SECONDS.toSeconds(timeInSeconds % 60));
        lblTime.setText("Time: " + timeString);
    }

}

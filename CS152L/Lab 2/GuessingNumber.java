/**
 * @version 2016-08-31
 * @author Anish Adhikari
 */
 * 

import java.util.Scanner;

public class NumberGuesser {
    // Instantiating class for referencing methods
    public static NumberGuesser ng = new NumberGuesser();

    public static void main(String[] args) {
        // Creating scanner object for use on command line
        Scanner userInput = new Scanner(System.in); 
        System.out.println("What is your name?");
        String playerName = userInput.nextLine();

        // Asking user if they'd like to try again
        boolean retry = true;
        while (retry){
            System.out.println(playerName + ", pick a number 1 to 10 please.");
            Integer playerGuess = Integer.parseInt(userInput.nextLine()); // Dangerous conversion
            
            System.out.println(ng.verifyGuess(playerGuess, playerName));
            System.out.println("Would you like to play again? (y/n)");
            
            String playerResponse = userInput.nextLine();
                   
            if (!playerResponse.equals("y")){
                retry = false;
            }
        }
        System.out.println("Thanks for playing my guessing game!");

    }
    
    // Encapsulated isGuessRight
    private boolean isGuessRight;
    public boolean isGuessRight() {
        return isGuessRight;
    }
    public void setGuessRight(boolean isGuessRight) {
        this.isGuessRight = isGuessRight;
    }

    public String verifyGuess(Integer playerGuess, String playerName){
        String gameOutput;
        Integer randomNumber = (int)((Math.random() * 10) + 1);
        System.out.println("You guessed " + playerGuess);
        System.out.println("I was thinking of " + randomNumber);
        if (randomNumber.equals(playerGuess)){
            gameOutput = "Congratulations, " + playerName + ". You guessed my number!";
            setGuessRight(true);
        }
        else{
            gameOutput = "Sorry, " + playerName + ". Your guess wasn't correct.";
            setGuessRight(false);
        }
        
        return gameOutput;
    }
    
}


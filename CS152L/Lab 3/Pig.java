/**
 * @version 2016-09-15
 * @author Anish Adhikari
 */
 * 

import java.util.Scanner;


public class Pig {
    
    // Variables for use in this class
    static public Player p1;
    static public Player p2;
    static public int pointsNeeded = 100;
    static boolean p1sTurn;
    static int accumulatedScore;
    static boolean goAgain;
    static Scanner scanIn;
    
    // Shortened method for print  "Ohhhhhh python *blush*"
    static public void print(String arg){
        System.out.println(arg);
    }
    
    // Method for when switching turns from one player to another
    static public void switchTurns(){
        p1sTurn = !p1sTurn;
        accumulatedScore = 0;
        print("Score:\n" + p1.getName() + ": " + p1.getScore() + "\n"
                + p2.getName() + ": "  + p2.getScore() + "\n");
        goAgain = false;
    }
    
    // Sleep method for effect
    static public void sleep(){
        try{
            Thread.sleep(1000); 
        }
        catch (InterruptedException e){
            print("How'd you do that?\n" + e.getMessage());
        }
    }
    
    // Method for if it is a player vs. player gametype
    static public void playerLoop(){
        // Game loop
        while (p1.getScore() < pointsNeeded && p2.getScore() < pointsNeeded){
            
            if(p1sTurn) print(p1.getName() + ", it is your turn.");
            else print(p2.getName() + ", it is your turn.");
            sleep();
                       
            // Turn loop
            goAgain = true;
            while(goAgain){
                playerTurn();
            }
        }
    }
    
    // Method for taking a player turn, used in playerLoop and botLoop
    public static void playerTurn(){
        int dice = (int)(Math.random() * 6 + 1);
        if (dice == 1){
            print("You rolled a " + dice + "! You lose your "
                    + "accumulated points and your turn!");
            switchTurns();
        }
        else{
            accumulatedScore += dice;
            print("You rolled a " + dice + ".");
            print("You have " + accumulatedScore + " points!");
            print("Keep going? (y/n)");
            String answer = scanIn.nextLine().toLowerCase();
            
            if (answer.equals("n") == true || 
                    answer.equals("no") == true){
                if (p1sTurn){
                    p1.setScore(p1.getScore() + accumulatedScore);
                }
                else {
                    p2.setScore(p2.getScore() + accumulatedScore);
                }
                switchTurns();
            }
        }
    }
    
    // Method for if it is a player vs. AI game
    static public void botLoop(boolean cheating){
        String[] randomInsults = new String[]{"Haha, you can't even beat a " +
                "computer.", "What kind of programmer are you.", 
                "lol, getpwnt scrub", "uw0tm8", "Just give up now"};
        
        int randomName = (int)(Math.random() * 4);
        
        // If the player asks for a cheater then give them one!
        if (cheating != true){
            int randomStyle = (int)(Math.random() * 3);
            p2 = new Player(randomName, true, randomStyle);
        }
        else{
            p2 = new Player(randomName, true, 1);
        }
        p1 = new Player();
  
        print("You have selected Human vs. AI mode! Good Luck!");
        print("Enter your name.");
        p1.setName(scanIn.nextLine());
        print("Your opponent's name: " + p2.getName() + "\nYour opponent's " +
                "playstle: " + p2.getStyle());
        
        // Game loop
        while(p1.getScore() < pointsNeeded && p2.getScore() < pointsNeeded){
            
            // Player loop
            if(p1sTurn){
                print(p1.getName() + ", it is your turn.");
                sleep();
            }
            while(p1sTurn){
                playerTurn();
            }
            
            // If player's score is above 100 then this while loop will not
            // end as the bot loop is below it, therefore I need a break if
            // the player score equals or goes above 100
            if (p1.getScore() >= 100) break;

            // Bot loop
            if(!p1sTurn){
                print("It is " + p2.getName() + "'s turn!");
                sleep();
            while(!p1sTurn){
                sleep();
                
                // Generating a roll given the information given via the args
                int botRoll = p2.generateBotRoll(p1.getScore(), 
                        accumulatedScore, p2);
                
                // Same general behavior as player, just different outputs
                if (botRoll == 1){
                    print(p2.getName() + " rolled a 1! Lucky break!");
                    switchTurns();
                }
                else if (botRoll > 1 && botRoll < 11){
                    accumulatedScore += botRoll;
                    print(p2.getName() + " rolled a " + botRoll + "!");
                    print(p2.getName() + " has " + accumulatedScore + 
                            " accumulated points.");    
                }
                else if (botRoll == 0){
                    print(p2.getName() + " wants to save their points.");
                    p2.setScore(p2.getScore() + accumulatedScore);
                    switchTurns();
                }
                
                // Cheater message to player
                if (botRoll > 6 && botRoll < 11){
                    
                    print(randomInsults[(int)(Math.random() * 4) + 1]);
                    sleep();
                    sleep();
                }
    
            } 
            }
        }
    }

    static public void main(String[] args) {
        boolean wholeGameAgain = true;
        
        while(wholeGameAgain){
            // Initializing values
            p1 = new Player("Player1", false);
            p2 = new Player("Player2", false);
            p1sTurn = true;
            accumulatedScore = 0;
            scanIn = new Scanner(System.in);
            
            // Asking player what kind of game they'd like to play
            print("Welcome to the Pig game!\nFirst to " + pointsNeeded + 
                    " wins!\nWhat gametype would you like to play?");
            print("1.) \"Quick\" Game\n2.) Normal Game\n3.) Human vs. AI" +
                    "\n4.) Human vs. AI Cheater");
            Integer gametype = 0;
            try{
                gametype = Integer.parseInt(scanIn.nextLine());
            }
            catch(ArithmeticException e){
                print("This isn't a valid number!");
                print(e.getMessage());
                System.exit(1);
            }
            catch(NumberFormatException e){
                print("This isn't a valid number!");
                print(e.getMessage());
                System.exit(1);
            }
            
            // Selecting the gametype via constructors
            if (gametype == 1){
                print("Good luck!");
                playerLoop();
            }
            else if (gametype == 2){
                // Populating names for the game about to begin
                print("Player 1, enter your name.");
                p1.setName(scanIn.nextLine());
                print("Player 2, enter your name.");
                p2.setName(scanIn.nextLine());
                print("Good luck, " + p1.getName() + " & " + p2.getName() + 
                        "!");
                playerLoop();
            }
            else if (gametype == 3){
                botLoop(false);
            }
            else if (gametype == 4){
                botLoop(true);
            }
            else{
                print("That isn't a valid entry. Defaulting to Quick game.");
                playerLoop();
            }
            
            // Declaring winner
            if (p2.isAI() == false){
                if(p1.getScore() > p2.getScore()){
                    print("Congratulations, " + p1.getName() + "! You won!");
                }
                else{
                    print("Congratulations, " + p2.getName() + "! You won!");
                }
            }
            else{
                if(p1.getScore() > p2.getScore()){
                    print("Congratulations, " + p1.getName() + "! You won!");
                }
                else{
                    print("Sorry, " + p1.getName() + ". You lost.");
                }
                if(p2.getStyle() == "Cheating") print("Schmuck.");
            }
            
            print("Would you like to play another game again?(y/n)");
            String another = scanIn.nextLine().toLowerCase();
            if(another.equals("y") || another.equals("yes")){
                wholeGameAgain = true;
            }
            else{
                wholeGameAgain = false;
            }
        }
        
        print("Thanks for playing my Pig game!");
        
    }

}

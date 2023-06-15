
public class Player {
    private int score;
    private String name;
    private boolean isAI;
    private int style;
    public static String[] botNames = new String[]{"SuperMegaDeath 9000", 
            "Hyperion 5", "Bob", "Cynthia"};
    public static String[] botStyles = new String[]{"Normal", "Cheating", 
            "Aggressive"};
    
    // Constructors
    public Player(){
        
    }
    public Player(String name){
        this.setName(name);
    }
    public Player(String name, boolean isAI){
        this.setName(name);
        this.setAI(isAI);
    }
    public Player(int name, boolean isAI, int style){
        this.setName(botNames[name]);
        this.setAI(isAI);
        this.setStyle(style);
    }
    
    // Generating an AI roll based on what the player's current total score is
    // as well as what the bot's accumulated score is thusfar
    public int generateBotRoll(int playerScore, int accumulatedScore, Player bot){
        int roll = 0;
        int trueScore = bot.getScore() + accumulatedScore;
        
        // Normal style
        if(bot.style == 0){
            if (accumulatedScore < 10 && trueScore < 100){
                roll = (int)(Math.random() * 6) + 1;
            }
            else{
                roll = 0;
            }
        }
        
        // Cheating style
        else if(bot.style == 1){
            if(accumulatedScore < 50 && trueScore < 100){
                int chanceToLose = (int)(Math.random() * 6) + 1;
                if (chanceToLose == 1 || chanceToLose == 2){
                    roll = (int)(Math.random() * 6) + 1;
                }
                else{
                    roll = (int)(Math.random() * 5) + 6;
                }
            }
            else{
                roll = 0;
            }
            
        }
        
        // Aggressive style
        else{
            if (accumulatedScore < 25 && trueScore < 100){
                roll = (int)(Math.random() * 6) + 1;
            }
            else{
                roll = 0;
            }
        }
        
        return roll;
    }
    
    // Mutators / Accessors
    public int getScore() {
        return score;
    }
    public void setScore(int score) {
        this.score = score;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public boolean isAI() {
        return isAI;
    }
    public void setAI(boolean isAI) {
        this.isAI = isAI;
    }
    public String getStyle() {
        return botStyles[style];
    }
    public void setStyle(int style) {
        this.style = style;
    }
     
    
}

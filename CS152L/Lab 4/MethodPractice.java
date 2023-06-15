
 /**
 * @version 2016-09-25 
 * @author Anish Adhikari
 */
 
 
 import java.util.HashMap;
public class MethodPractice {

    /**
     * Returns the difference of its arguments.
     * @param x First argument
     * @param y Second argument
     * @return Difference of x and y
     */
    public static int diffTwo( int x, int y ) {
      return x - y;
    }

    /**
     * Is argument even?
     * @param x Value to check.
     * @return True if x is an even number, false otherwise.
     */
    public static boolean isEven( int x ) {
      if ((x % 2) == 0){
        return true;
      }
      else{
        return false;
      }
    }

    /**
     * Does the given string contain the letter M (either upper or
     * lower case)?
     * @param x String to check
     * @return True if x contains M, false otherwise.
     */
    public static boolean containsM( String x ) {
      if((x.toLowerCase().indexOf('m')) == -1){
        return false;
      }
      else{
        return true;
      }
      
      
    }

    /**
     * Where is the location of the letter M (upper or lower case) in
     * the given string?
     * @param x String to check
     * @return 0 based location of first occurrence of M in x,
     *         -1 if M is not present.
     */
    public static int indexOfM( String x ) {
      return x.toLowerCase().indexOf('m');
    }


    /**
     * This method returns a response based on the string input:
     *     Apple => Orange
     *     Hello => Goodbye!
     *     meat => potatoes
     *     Turing => Machine
     *     Yay! => \o/
     * Any other input should be responded to with:
     *     I don't know what to say to that. 
     * @param input The input string
     * @return Corresponding output string.
     */
    public static String respond( String input ) {
      HashMap<String, String> map = new HashMap<String, String>();
      map.put("Apple", "Orange");
      map.put("Hello", "Goodbye!");
      map.put("meat", "potatoes");
      map.put("Turing", "Machine");
      map.put("Yay!", "\\o/");
      
      if(map.get(input) != null){
        return map.get(input);
      }
      else{
        return "I don't know what to say to that.";
      }
      
    }

    /**
     * Average up to five positive numbers. Any non-positive values are
     * not included in the average. (Note: zero is not positive.)
     * @param a First value
     * @param b Second value
     * @param c Third value
     * @param d Fourth value
     * @param e Fifth value
     * @return Average of the positive input values. If none are positive, returns -1.
     */
    public static double averagePositives( int a, int b, int c, int d, int e ) {
      int[] nums = {a, b, c, d, e};
      double sum = 0, avg = 0;
      int howManyNums = 0;
      
      for(int i = 0; i < nums.length; ++i){
        if(nums[i] > 0){
          sum += nums[i];
          howManyNums++;
        }
      }
      
      if (sum == 0){
        return -1;
      }
      else{
        avg = sum / howManyNums;
        return avg;
      }

    }





    // WRITE A METHOD FROM SCRATCH
    //
    // Write a method called squareOddHalveEven that returns the
    // square of odd numbers and returns even numbers divided by two.
    //
    // The method should take a single int argument and return an int
    //
    // Don't forget to use the public static modifiers
    public static int squareOddHalveEven (int num) {
      if ((num % 2) == 0){
        return num / 2;
      }
      else{
        return num*num;
      }
    }


    


    // WRITE A METHOD FROM SCRATCH
    //
    // Write a method called calculatePayment that takes two
    // arguments, an int meal which is the cost of a meal, and a
    // double tip which represents the tip percentage one would add to
    // the bill.   
    // The method must return a double which equals how much should be
    // paid.
    //
    // int meal must be greater than 0
    // double tip must be 0 or greater and .5 or less (no tips over 50%)
    // If either number is invalid, return -1;
    //
    // Don't forget to use the public static modifiers
    public static double calculatePayment (int mealCost, double tipPercent){
      if (mealCost <= 0 || tipPercent > .5 || tipPercent < 0){
        return -1;
      }
      else{
        return (Double.valueOf(mealCost) * tipPercent) + mealCost;
      }
    }



    // This code tests your program's completeness.
    public static void main(String[] args) {
        int completeness = 0;

        if( diffTwo(2,3) == -1 ) { completeness++; }
        if( diffTwo(4,-5) == 9 ) { completeness++; }
        
        if( !isEven(-3) ) { completeness++; }    
        if( isEven(2) ) { completeness++; }    
        if( isEven(0) ) { completeness++; }    

        if( containsM( "man" ) ) { completeness++; }    
        if( !containsM( "dog" ) ) { completeness++; }    
        if( containsM( "EXCLAIMS!" ) ) { completeness++; } 

        if( indexOfM( "man" ) == 0 ) { completeness++; } 
        if( indexOfM( "EXCLAIMS!" ) == 6 ) { completeness++; } 
        if( indexOfM( "dog" ) == -1 ) { completeness++; } 
        if( indexOfM( "klmmMmmM" ) == 2 ) { completeness++; } 
        if( indexOfM( "klMMmMMm" ) == 2 ) { completeness++; }

        if( respond( "Apple" ).equals( "Orange" ) ) { completeness++; } 
        if( respond( "Hello" ).equals( "Goodbye!" ) ) { completeness++; } 
        if( respond( "meat" ).equals( "potatoes" ) ) { completeness++; } 
        if( respond( "Turing" ).equals( "Machine" ) ) { completeness++; } 
        if( respond( "Yay!" ).equals( "\\o/" ) ) { completeness++; } 
        if( respond( "xxx" ).equals( "I don't know what to say to that." ) ) { completeness++; } 

        if( averagePositives( 12,13,12,13,12 ) == 12.4 ) { completeness++; } 
        if( averagePositives( 0,0,0,0,0 ) == -1 ) { completeness++; } 
        if( averagePositives( 0,0,15,0,-2 ) == 15 ) { completeness++; } 
        if( averagePositives( 100,-3,4021,-2,13 ) == 1378 ) { completeness++; } 


        // UNCOMMENT AFTER IMPLEMENTING squareOddHalveEvenMethod
        if( squareOddHalveEven( 4 ) == 2 ) { completeness++; } 
        if( squareOddHalveEven( 0 ) == 0 ) { completeness++; } 
        if( squareOddHalveEven( 3 ) == 9 ) { completeness++; } 


        // UNCOMMENT AFTER IMPLEMENTING calculatePayment
        if( calculatePayment( 0, .3 ) == -1 ) { completeness++; } 
        if( calculatePayment( 10, .2 ) == 12.0 ) { completeness++; } 
        if( calculatePayment( 100, .6 ) == -1 ) { completeness++; } 
        if( calculatePayment( 120, .32 ) == 158.4 ) { completeness++; } 
    
        System.out.println( "Your program's completeness is currently: " + completeness + "/30" );
    }

}

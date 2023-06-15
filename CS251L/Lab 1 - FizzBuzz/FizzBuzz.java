// Anish Adhikari UNM- aaadhikari999@gmail.com
// CS 251 - Lab 1 FizzBuzz
// This class allows a user to specify custom arguments to a FizzBuzz program.
// Format is as follows:
// FizzBuzz *ITERATIONS* *WHEN_TO_FIZZ* *WHEN_TO_BUZZ*

public class FizzBuzz {
	
	public static void main(String[] args) {
		// args[0] is number of iterations
		// args[1] is when we say "Fizz"
		// args[2] is when we say "Buzz"
		int iterations = Integer.parseInt(args[0]);
		int fizzModulo = Integer.parseInt(args[1]);
		int buzzModulo = Integer.parseInt(args[2]);
		String outputStr;
		
		for(Integer i = 1; i <= iterations; ++i) {
			// FizzBuzz with only two logical if's
			// If this were python I could do slicing MAGIC
			outputStr = "";
			if((i % fizzModulo) == 0) outputStr += "Fizz";
			if((i % buzzModulo) == 0) outputStr += "Buzz";
			
			// If we didn't add any strings to the output string, 
			// then we must assign the number we're on
			if(outputStr.equals("")) outputStr = i.toString();
			
			System.out.println(outputStr);
		}
		
	}

}

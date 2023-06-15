/**
 * @version 2016-08-22
 * @author Anthony Galczak
 * Lab 1
 */

public class AsciiArt {
	
	/**
	 * Prints my name in ASCII Art to the console.
	 * @param args Command-line arguments are ignored.
	 */
	
	// Can't be over 60x24, each character must be 3x3, 9 characters
	public static void main(String[] args){
		printAnthony();
		printA();
		printN();
		printT();
		printH();
		printO();
		printN();
		printY();
	}
	
	public static void printAnthony(){
		System.out.println(" _ _           _ _ _ _          _ _ _ ");
		System.out.println("|   |  |\\   |    |     |     | |     | |\\   | \\   /");
		System.out.println("| _ |  | \\  |    |     |_ _ _| |     | | \\  |  \\ /");
		System.out.println("|   |  |  \\ |    |     |     | |     | |  \\ |   |");
		System.out.println("|   |  |   \\|    |     |     | |_ _ _| |   \\|   |");
	}
	
	public static void printA(){
		System.out.println(" _ _  ");
		System.out.println("|   | ");
		System.out.println("| _ | ");
		System.out.println("|   | ");
		System.out.println("|   | ");	
	}
	public static void printN(){
		System.out.println("|\\   | ");
		System.out.println("| \\  | ");
		System.out.println("|  \\ | ");
		System.out.println("|   \\| ");
	}
	public static void printT(){
		System.out.println("_ _ _ _ ");
		System.out.println("   |    ");
		System.out.println("   |    ");
		System.out.println("   |    ");
		System.out.println("   |    ");
	}
	public static void printH(){
		System.out.println("|     | ");
		System.out.println("|_ _ _| ");
		System.out.println("|     | ");
		System.out.println("|     | ");
	}
	public static void printO(){
		System.out.println("_ _ _   ");
		System.out.println("|     | ");
		System.out.println("|     | ");
		System.out.println("|     | ");
		System.out.println("|_ _ _| ");
	}
	public static void printY(){
		System.out.println("\\   / ");
		System.out.println(" \\ /  ");
		System.out.println("  |    ");
		System.out.println("  |    ");
	}
	
}

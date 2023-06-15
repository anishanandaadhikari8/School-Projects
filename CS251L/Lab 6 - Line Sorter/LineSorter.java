// Anish Adhikari UNM- aaadhikari999@gmail.com
// CS 251 - Lab 6 Line Sorter
// Sorts a file's lines by line length
// Will output shortest to longest and then by alphabetization

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

// LineSorter class implements Comparator so that we can use compare off the class
public class LineSorter implements Comparator<String> {
    
    public static void main(String[] args) throws Exception { 
        // Throwing an exception if the wrong amount of arguments are given
        if(args.length != 2) {
            throw new Exception("Call this method with an input and output file");
        }
        
        Path inputFile = Paths.get(args[0]);
        Path outputFile = Paths.get(args[1]);
        ArrayList<String> lineList = new ArrayList<String>();
        
        try {
            InputStream in = Files.newInputStream(inputFile);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            PrintWriter writer = new PrintWriter(outputFile.toString());
            String line = null;
            
            while((line = reader.readLine()) != null) {
                lineList.add(line);
            }
            
            // Sorting the collection of strings with my custom comparator
            Collections.sort(lineList, new LineSorter());
            
            for(int i = 0; i < lineList.size(); ++i) {
                writer.write(lineList.get(i) + "\n");
            }                     
            writer.close();   
        }
        catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
           
    }
    
    /**
     * Compare method that is inherited via the Comparator<String> implementation
     * Compares two strings and returns first by length, if length is the same then
     * returns based on alphabetization
     */
    public int compare(String s1, String s2) {
        
        // If the strings are the same length, then use the standard comparison (alphabetical)
        if(s1.length() != s2.length()) {
            return s1.length() - s2.length();
        }
        else {
            return s1.compareTo(s2);
        }
    }
    
}

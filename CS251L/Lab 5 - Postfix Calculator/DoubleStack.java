// Anish Adhikari UNM- aaadhikari999@gmail.com
// CS 251 - Lab 5 - Postfix Calculator
// Class that implements the Stack class
// Useful methods for a postfix calculator

import java.util.LinkedList;

public class DoubleStack implements Stack<Double> {
    
    LinkedList<Double> theList = new LinkedList<Double>();
    
    /**
     * Checks if the list is empty and returns a boolean.
     */
    public boolean isEmpty() {
        return theList.isEmpty();
    }

    /** 
     * Pushes a double value onto the top of the stack.
     */
    public void push(Double val) {
        theList.push(val);
    }

    /**
     * Pops a double value off the top of the stack and returns it.
     */
    public Double pop() {
        return theList.pop();
    }

    /**
     * Peeks at the top of te stack and returns a double.
     */
    public Double peek() {
        return theList.peek();
    }

}

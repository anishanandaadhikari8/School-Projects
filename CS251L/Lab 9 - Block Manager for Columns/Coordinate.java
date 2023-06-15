/**
 * Anish Adhikari UNM- aaadhikari999@gmail.com
 * CS 251 - Lab 9 Columns Game Model
 * 
 * Coordinate class to hold a row and column.
 * Has capability for objects to be compared against eachother.
 * 
 * Coordinate.java
 */
public class Coordinate {
    private int col;
    private int row;
    
    public Coordinate(int row, int col) {
        this.row = row;
        this.col = col;
    }
    
    public int getRow() {
        return row;
    }
    
    public int getCol() {
        return col;
    }
    
    public void setRow(int row) {
        this.row = row;
    }
    
    public void setCol(int col) {
        this.col = col;
    }
    
    /**
     * Need this equals method for hashSet to remove duplicate elements.
     */
    @Override
    public boolean equals(Object obj) {
        return obj instanceof Coordinate && ((Coordinate)obj).getCol() == this.col &&
                ((Coordinate)obj).getRow() == this.row;
    }
    
    /**
     * Need this hashCode method for hashSet to remove duplicate elements.
     */
    @Override
    public int hashCode() {
        return this.col*1000 + this.row;
    }
    
    /**
     * Outputs a given coordinate in string representation.
     */
    @Override
    public String toString() {
        return "Row: " + row + " Col: " + col;
    }

}

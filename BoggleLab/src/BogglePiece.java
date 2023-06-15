import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class BogglePiece
{
    private int x, y, width, height;
    private char letter;
    private boolean isHighlighted;
    private String letterAsString;
    private boolean isChar;
    DropShadow dropShadow;

    public BogglePiece(int x, int y, int width, int height, Object genericLetter)
    {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        isHighlighted = false;

        dropShadow = new DropShadow();
        dropShadow.setRadius(5);
        dropShadow.setOffsetX(3);
        dropShadow.setOffsetY(3);
        dropShadow.setColor(Color.color(0.4, 0.5, 0.5));

        // Using some generic type magic to fill the piece with a 1 or 2 character value
        if(genericLetter.getClass() == String.class)
        {
            letterAsString = String.valueOf(genericLetter);
            isChar = false;
        }
        else
        {
            letter = (char)genericLetter;
            isChar = true;
        }
    }

    /**
     * draw()
     * Every BogglePiece knows ho to draw itself based upon the coordinates that it is given.
     * This method handles highlighting of the pieces as well as setting either a char or String into
     * the piece.
     * @param gcCanvas This is the graphics object we are drawing onto.
     */
    public void draw(GraphicsContext gcCanvas)
    {
        if(isHighlighted == false) { gcCanvas.setFill(Color.LIGHTBLUE); }
        else { gcCanvas.setFill(Color.DARKBLUE); }

        gcCanvas.setEffect(dropShadow);
        gcCanvas.fillRoundRect(x, y, width, height, width/5, height/5);
        gcCanvas.strokeRoundRect(x, y, width, height, width/5, height/5);
        gcCanvas.setFill(Color.WHITE);
        gcCanvas.setFont(new Font("Arial", width/2));
        if(isChar == true) { gcCanvas.fillText(String.valueOf(letter), x + width/2, y + height/2 + height/8); }
        else { gcCanvas.fillText(letterAsString, x + width/5, y + height/2 + height/8); }

    }

    /**
     * isInBounds()
     * Checks to see if a given click location was in bounds for this piece.
     * This is a fundamental method for selecting pieces and finding it's neighbors.
     * @param xClicked What x-coord was clicked.
     * @param yClicked What y-coord was clicked.
     * @return If the coords are within the bounds of the piece, return true. Otherwise, return false.
     */
    public boolean isInBounds(int xClicked, int yClicked)
    {
        if(xClicked >= x && xClicked <= x + width && yClicked >= y && yClicked <= y + height) { return true; }
        else{ return false; }
    }

    /**
     * getLetter()
     * Returns the piece's letter. It may be a char or string.
     * @return An object that is a char or string representing a die-face.
     */
    public Object getLetter()
    {
        if(isChar == true) { return letter; }
        else { return letterAsString; }
    }

    /**
     * getIsHighlighted()
     * @return A boolean representing whether or not this piece is highlighted. True if highlighted. False if not.
     */
    public boolean getIsHighlighted() { return isHighlighted; }

    public void setIsHighlighted(boolean isHighlighted, GraphicsContext gcCanvas)
    {
        this.isHighlighted = isHighlighted;
        draw(gcCanvas);
    }

    /**
     * isNeighbor()
     * Finds out whether or not a given piece is a neighbor to this piece. This is very useful for the Boggle rule
     * of play having to continue through adjacent pieces.
     *
     * Because the piece is drawn starting from the upper-right, when we want to go left or up we only need to go
     * - width/2 or - height/2 respectively. When looking for pieces to the right or down we need to look height down to get
     * off the current piece, then height/2 to get to the "center" of the next piece.
     *
     * Additionally, padding won't matter here because this mechanism will place roughly in the middle of the piece
     * Padding would only matter if the padding is greater than the size of the piece (not allowable)
     *
     * @param possibleNeighbor This is the piece we are comparing against as a possible neighbor.
     * @return A boolean that says whether or not this given piece is a neighbor. True if it's a neighbor.
     */
    public boolean isNeighbor(BogglePiece possibleNeighbor)
    {

        int topY = y - height/2;
        int botY = y + height + height/2;
        int leftX = x - width/2;
        int rightX = x + width + width/2;

        // Padding doesn't matter as width*2 and height*2 will put me inside edges of neighboring pieces
        if(possibleNeighbor.isInBounds(leftX, topY) == true) { return true; }
        else if(possibleNeighbor.isInBounds(x, topY) == true) { return true; }
        else if(possibleNeighbor.isInBounds(rightX, topY) == true) { return true; }
        else if(possibleNeighbor.isInBounds(leftX, y) == true) { return true; }
        else if(possibleNeighbor.isInBounds(rightX, y) == true) { return true; }
        else if(possibleNeighbor.isInBounds(leftX, botY) == true) { return true; }
        else if(possibleNeighbor.isInBounds(x, botY) == true) { return true; }
        else if(possibleNeighbor.isInBounds(rightX, botY) == true) { return true; }
        else { return false; }
    }

}

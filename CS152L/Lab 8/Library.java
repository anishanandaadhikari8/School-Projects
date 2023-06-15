/**
 * 
 * CS152L-001 - Lab 8- Library
 * Creating Book and Author objects and utilizing them in a library
 * Student name: Anish Adhikari
 
 */




public class Library { 

    /** Books in the library. */
    private Book[] books;
    
    /** Number of copies for each book. */
    private int[] copies;

    /** Number of copies currently checked out for each book. */
    private int[] checkedOut;

    /** Number of unique books in the library. */
    private int numBooks;

    /** Construct a new empty Library. */
    public Library() {
        // We'll assume we never have more than 400 books.
        int librarySize = 400;
        books = new Book[librarySize];
        copies = new int[librarySize];
        checkedOut = new int[librarySize];
        numBooks = 0;
    }

    /**
     * Get the number of total copies of all books that exist in the
     * library.
     * @return Total number of copies in the library.
     */
    public int totalCopies() {
    	int intCopies = 0;
    	for(int i = 0; i < copies.length; ++i)
    	{
    		intCopies += copies[i];
    	}
        return intCopies;
    }

    /**
     * Get the number of copies currently checked out.
     * @return Total number of copies checked out.
     */
    public int totalCheckedOut() {
    	int intCheckedOut = 0;
    	for(int i = 0; i < checkedOut.length; ++i)
    	{
    		intCheckedOut += checkedOut[i];
    	}
        return intCheckedOut;
    }

    /**
     * Get a String representing the status of the library.
     * @return Status string.
     */
    public String statusString() {
    	String output = "";
    	output += "Total unique books: " + numBooks + "\n";
    	output += "Total number of copies: " + totalCopies() + "\n";
    	output += "Total checked out: " + totalCheckedOut();
    	
        return output;
    }

    /**
     * Add all the books in the array to the library. Adds one copy of
     * each book.
     * @param newBooks Books to add.
     */
    public void addBooks( Book[] newBooks ) {
    	boolean foundTheBook;
    	for(int i = 0; i < newBooks.length; ++i)
    	{
    		foundTheBook = false;
    		for(int j = 0; j < numBooks; ++j)
    		{
    			if(newBooks[i].equals(books[j]))
    			{
    				copies[j]++;
    				foundTheBook = true;
    			}
    		}
    		
    		if(!foundTheBook)
    		{
    			books[numBooks] = newBooks[i];
    			copies[numBooks]++;
    			numBooks++;
    		}
    	}
    	
    }

    /**
     * Add a single book the library.
     *
     * If the book is already present, adds another copy.
     * If the book is new, add it after the existing books.
     * @param b Book to add.
     */
    public void addBook( Book b ) {
    	boolean addedACopy = false;
    	for(int i = 0; i < numBooks; ++i)
    	{
    		if(b.equals(books[i]))
    		{
    			copies[i]++;
    			addedACopy = true;
    			break;
    		}

    	}
    	
    	// If the book wasn't found then add it to the end of the list
    	if(!addedACopy)
    	{
    		books[numBooks] = b;
    		copies[numBooks]++;
    		numBooks++;
    	}
    }  
  
    /**
     * Checks out a book from the library if possible.
     * @param b Book to check out.
     * @return String denoting success or failure.
     */
    public String checkOutBook ( Book b ) {
        boolean foundTheBook = false;
        String outputString = "";
    	
    	for(int i = 0; i < numBooks; ++i)
        {
        	if(b.equals(books[i]))
        	{
        		foundTheBook = true;
        		if(copies[i] > checkedOut[i])
        		{
        			outputString = "Checked out!";
        			checkedOut[i]++;
        		}
        		else
        		{
        			outputString = "All out of copies.";
        		}
        	}
        }
    	
    	if(!foundTheBook)
    	{
    		outputString = "Book not found.";
    	}
    	
    	return outputString;
    }

    /**
     * Checks in a book to the library if possible.
     * @param b Book to check in.
     * @return String denoting success or failure.
     */
    public String checkInBook ( Book b ) {
        boolean foundTheBook = false;
        String outputString = "";
    	
    	for(int i = 0; i < numBooks; ++i)
        {
        	if(b.equals(books[i]))
        	{
        		foundTheBook = true;
        		if(checkedOut[i] > 0)
        		{
        			checkedOut[i]--;
        			outputString = "Checked in!";
        		}
        		else
        		{
        			outputString = "All of our copies are already checked in.";
        		}
        	}
        }
    	
    	if(!foundTheBook)
    	{
    		outputString = "Book not found.";
    	}
    	
    	return outputString;
    }
  
    /**
     * Get string representation of entire library collection and status.
     * @return String representation of library.
     */
    public String toString() {
        String outputString = "";
        
        for(int i = 0; i < numBooks; ++i)
        {
    		outputString += i + ". " + books[i].toString() + 
        			" : " + (copies[i] - checkedOut[i]) + "/" + copies[i] + "\n";
        	
        }
        
        outputString += "\n" + statusString();
        
        return outputString;
    }
  
    /**
     * Get number of unique books that exist for a given author.
     * @param a The author to check.
     * @return Number of books by the author.
     */
    public int numBooksByAuthor( Author a ) { 
    	int totalAuthorMatches = 0;
    	for(int i = 0; i < numBooks; ++i)
    	{
    		if(books[i].getAuthor().isSame(a))
    		{
    			totalAuthorMatches++;
    		}
    	}

        return totalAuthorMatches;
    }

    /**
     * Returns a String that lists the unique books which exist for a
     * given author, in standard book format, with a newline after
     * each.  If no books are found for the author, returns string
     * that says so.
     * 
     * @param a The author in question.
     * @return String listing books by the author.
     */
    public String listBooksByAuthor( Author a ) { 
    	String outputString = "";
    	
    	for(int i = 0; i < numBooks; ++i)
    	{
    		if(books[i].getAuthor().isSame(a))
    		{
    			outputString += books[i].toString() + "\n";
    		}
    	}
        if(outputString == "")
        {
        	outputString = "No books by " + a.toString() + ".";
        }
        
        return outputString;
    }  
  
    /**
     * Returns string that lists the unique books with contain the
     * given string within their titles, without regard for case, with
     * a newline after each.  If no books are found containing the
     * string, returns string that says so.
     * @param s The string to look for in the titles.
     * @return String listing books that contain given string in titles.
     */
    public String listBooksByTitle( String s ) {
    	String outputString = "";
        for(int i = 0; i < numBooks; ++i)
        {
        	if(books[i].getTitle().toLowerCase().contains(s))
        	{
        		outputString += books[i].toString() + "\n";
        	}
        }
        if(outputString == "")
        {
        	outputString = "No books with \"" + s + "\" in the title.";
        }
        
        return outputString;
    }

    /**
     * Deletes book entirely from the library.
     * @param b Book to remove.
     * @return String denoting success or failure.
     */
    public String deleteBook( Book b ) {
    	boolean foundTheBook = false;
    	int arrayFound = 0;
    	for(int i = 0; i < numBooks; ++i)
    	{
    		if(books[i].equals(b))
    		{
    			foundTheBook = true;
    			arrayFound = i;
    		}
    	}
    	
    	if(foundTheBook)
    	{
    		numBooks--;
    		// Note: if I could use ArrayUtils these become one liners
    		
    		Book[] tmpBooks = new Book[400];
    		int[] tmpCopies = new int[400];
    		int[] tmpCheckedOut = new int[400];
    		
    		// Deleting the found item in the books array and shifting all elements down 1
        	for(int i = arrayFound; i < numBooks; ++i)
        	{
        		books[i] = books[i+1];
        		copies[i] = copies[i+1];
        		checkedOut[i] = checkedOut[i+1];
        	}
        	
        	// Transferring all of the elements of books into the new temp array except the last one
        	for(int i = 0; i < numBooks; ++i)
        	{
        		tmpBooks[i] = books[i];
        		tmpCopies[i] = copies[i];
        		tmpCheckedOut[i] = checkedOut[i];
        	}
        	
        	// Assigning the pointer of books to the pointer of the new correct array;
        	books = tmpBooks;
        	copies = tmpCopies;
        	checkedOut = tmpCheckedOut;
        	
        	return "Book removed.";
    	}

        return "Book not found.";
    }  
  
}

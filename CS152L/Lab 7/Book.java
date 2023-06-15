/**
 * CS 152 Lab 5 - Hangman
 * CS152L-001 - Lab 7 - Books
 * Creating Book and Author classes
 * Student name: Anish Adhikari
 */


public class Book {
	
	private String title;
	private Author author;
	private int pubYear;
	private String ISBN;
	
	// Setting default values and further constructors link to this one
	public Book()
	{
		title = AuthorBookConstants.UNKNOWN_TITLE;
		author = AuthorBookConstants.UNKNOWN_AUTHOR;
		pubYear = AuthorBookConstants.UNKNOWN_YEAR;
		ISBN = AuthorBookConstants.UNKNOWN_ISBN;
	}
	
	public Book(String title)
	{
		this();
		this.title = title;
	}
	
	public Book(String title, Author author)
	{
		this();
		this.title = title;
		this.author = author;
	}
	
	
	public void setTitle(String title)
	{
		// Title should not be blank
		if(title != "")
		{
			this.title = title;
		}
		else
		{
			System.out.println("Error - Title is blank.");
		}
	}
	
	public String getTitle()
	{
		return title;
	}
	
	public void setAuthor(Author author)
	{
		this.author = author;
	}
	
	public Author getAuthor()
	{
		return this.author;
	}
	
	public void setPubYear(int year)
	{
		// Can't publish in the future
		if(year < 2016)
		{
			this.pubYear = year;
		}
		else
		{
			System.out.println("Error - This year is in the future");
		}
	}
		
	
	public int getPubYear()
	{
		return pubYear;
	}
	
	public void setISBN(String isbn)
	{
		// ISBNs are standard length of 13 digits
		if(isbn.length() == 13)
		{
			this.ISBN = isbn;
		}
		else
		{
			System.out.println("Error - ISBN is wrong length.");
		}
	}
	
	public String getISBN()
	{
		return this.ISBN;
	}
	
	public boolean sameAuthor(Book other)
	{
		return (other.author.isSame(this.author));
	}
	
	public boolean equals(Book other)
	{
		return other.getISBN().equals(this.getISBN());
	}
	
	public String toString()
	{
		// Validating which type of string to output
		if(this.author != AuthorBookConstants.UNKNOWN_AUTHOR &&
				this.getPubYear() != AuthorBookConstants.UNKNOWN_YEAR &&
				this.title != AuthorBookConstants.UNKNOWN_TITLE)
		{
			return title + " (" + this.getPubYear() + "). " + this.author.toString() + ".";
		}
		else if(this.author != AuthorBookConstants.UNKNOWN_AUTHOR 
				&& this.title != AuthorBookConstants.UNKNOWN_TITLE)
		{
			return title + ". " + this.author.toString() + ".";
		}
		else
		{
			return title + ".";
		}
		
	}
}

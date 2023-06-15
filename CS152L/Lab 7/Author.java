/**
 * CS 152 Lab 5 - Hangman
 * CS152L-001 - Lab 7 - Books
 * Creating Book and Author classes
 * Student name: Anish Adhikari
 */


public class Author {
	
	private String lastName;
	private String firstName;
	private int birth;
	private int death;
	
	public Author(String lastName, String firstName)
	{
		this.lastName = lastName;
		this.firstName = firstName;
		birth = AuthorBookConstants.UNKNOWN_YEAR;
		death = AuthorBookConstants.UNKNOWN_YEAR;
	}
	
	public int getBirth()
	{
		return birth;
	}
	
	public int getDeath()
	{
		return death;
	}
	
	public void setYears(int birth)
	{
		// Births cannot be set in pre-AD years
		if(birth > 0)
		{
			this.birth = birth;
		}
		else
		{
			System.out.println("Error - Birth set to before AD.");
		}
	}
	
	public void setYears(int birth, int death)
	{
		// Making sure birth isn't greater than death, handles exception
		if(birth < death)
		{
			this.birth = birth;
			this.death = death;
		}
		else
		{
			System.out.print("Error - Birth cannot be after death.");
		}
		
	}
	
	public boolean isSame(Author other)
	{
		// First element of the array will be last name, second will be first name or initial
		String[] thisAuthorArray = this.toString().split(",");
		String[] otherAuthorArray = other.toString().split(",");
		
		// If the first and last names are exactly the same then return true
		if(other.toString().equals(this.toString()))
		{
			return true;
		}
		// Finding out if there is a first initial similarity
		else
		{
			// If the last names are equal, do some logic for first name initial
			if(thisAuthorArray[0].equals(otherAuthorArray[0]))
			{
				// If one of the first names is an initial and 
				// the first char is equal to the other first char
				if(thisAuthorArray[1].length() == 2 || otherAuthorArray[1].length() == 2 &&
						(thisAuthorArray[1].charAt(1) == otherAuthorArray[1].charAt(1)))
				{
					return true;
				}
			}
		}
		
		return false;
	}
	
	public String toString()
	{
		return this.lastName + ", " + this.firstName;
	}
	
	public String infoString()
	{
		String output = "";
		output += this.lastName + ", " + this.firstName;
		
		if(this.birth != AuthorBookConstants.UNKNOWN_YEAR 
				&& this.death != AuthorBookConstants.UNKNOWN_YEAR)
		{
			output += " (" + this.birth + "-" + this.death + ")";
		}
		else if(this.birth != AuthorBookConstants.UNKNOWN_YEAR)
		{
			output += " (b. " + this.birth + ")";
		}
		
		return output;
	}
	
}

/**
 * 
 * CS152L-001 - Lab 9 - Card and Deck classes
 * These classes will allow us to play a swing GUI solitaire game
 * Student name: Anish Adhikari
 
 */

public class Card {
	
	private Rank rank;
	private Suit suit;
	private boolean faceUp;
	private boolean isBlack;

	public Card(Rank rank, Suit suit)
	{
		this.rank = rank;
		this.suit = suit;
		if(suit.name() == "CLUBS" || suit.name() == "SPADES")
		{
			isBlack = true;
		}
		else
		{
			isBlack = false;
		}
		
		faceUp = true;
	}
	
	public Rank getRank()
	{
		return rank;
	}
	
	public Suit getSuit()
	{
		return suit;
	}
	
	public boolean isFaceUp()
	{
		return faceUp;
	}
	
	public boolean isBlack()
	{
		return isBlack;
	}
	
	public void setFaceUp(boolean faceUp)
	{
		this.faceUp = faceUp;
	}
	
	
}

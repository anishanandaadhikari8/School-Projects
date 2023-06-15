

/**
 * 
 * CS152L-001 - Lab 9 - Card and Deck classes
 * These classes will allow us to play a swing GUI solitaire game
 * Student name: Anish Adhikari
 
 */
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Deck {

	public Card[] cards;
	private int numCards;
	
	public Deck()
	{
		cards = new Card[52];
	}
	
	public void add(Card card)
	{
		if(this.size() < 52)
		{
			cards[this.size()] = card;
			numCards++;
		}
		
	}
	
	public void fill()
	{
		for(int i = 0; i < 13; ++i)
		{
			for(int j = 0; j < 4; ++j)
			{
				cards[i*4 + j] = new Card(Rank.values()[i], Suit.values()[j]);
				numCards++;
			}
		}
	}
	
	public Card get(int n)
	{
		return cards[n];
	}
	
	public void move(Deck other)
	{
		if(other.size() < 52)
		{
			other.cards[other.size()] = this.getTop();
			cards[cards.length - 1] = null;
			
			other.setSize(other.size() + 1);
			numCards--;
		}

	}
	
	public void move(Deck other, int n)
	{
		for(int i = 0; i < n; ++i)
		{
			other.cards[other.size() + i] = cards[numCards - (n - i)];
		}
		other.setSize(other.size() + n);
		numCards -= n;
	}
	
	public int size()
	{
		return numCards;
	}
	
	public void setSize(int num)
	{
		numCards = num;
	}
	
	public Card getTop()
	{
		if(numCards == 0)
		{
			return null;
		}
		else
		{
			return cards[numCards - 1];
		}
		
	}
	
	public void shuffle()
	{
		for(int i = 0; i < numCards; ++i)
		{
			// Implementing an ArrayList to take advantage of the Collections.shuffle method
			// There is a manual way to do this, however there are weird fringe cases where
			// a random index could be the same as your iterator and .1% non-shuffles
			List<Card> alCards = new ArrayList<Card>(Arrays.asList(cards));
			Collections.shuffle(alCards);
			cards = alCards.toArray(new Card[alCards.size()]);
		
      // Source for original algorithm
      // http://math.hws.edu/javanotes/source/chapter5/Deck.java
			// Traditional code without more advanced data types, just in case
			// int randIndex = (int)(Math.random() * numCards);
			// Card tmpCard = cards[i];
			// cards[i] = cards[randIndex];
			// cards[randIndex] = tmpCard;
		}
	}
}

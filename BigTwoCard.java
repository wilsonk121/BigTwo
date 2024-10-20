import java.util.Arrays;
import java.util.ArrayList;

public class BigTwoCard extends Card
{
	//constructors
	public BigTwoCard(int suit, int rank)
	{
		super(suit,rank);	
	}
	
	public int compareTo(Card card)
	{
		if (this.rank == card.getRank() && this.suit == card.getSuit())
		{
			return 0;
		}
		
		ArrayList<Integer> BigTwoRankAnchor =new ArrayList<Integer>(Arrays.asList(2,3,4,5,6,7,8,9,10,11,12,0,1));

		int mycard_order = BigTwoRankAnchor.indexOf(this.rank);
		int yourcard_order = BigTwoRankAnchor.indexOf(card.getRank());
		
		if (mycard_order > yourcard_order)
		{
			return 1; //mycard is larger
		}
		if (mycard_order < yourcard_order)
		{
			return -1; //mycard is smaller
		}
		
		if(this.suit > card.getSuit()) //both card has the same rank
		{
			return 1;
		}
		else
		{
			return -1;
		}	
	}
}

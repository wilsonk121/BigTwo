
public abstract class SameRankHands extends Hand
{
	public SameRankHands(CardGamePlayer player, CardList cards)
	{
		super(player, cards);
	}
	
	private int numOfCardInHand;
	
	public void setNumOfCardInHand(int n)
	{
		numOfCardInHand = n;
	}
	
	public boolean isValid()
	{
		if(this.size() != numOfCardInHand)
			return false;
		for(int i = 0; i < (this.size()-1); i++)
		{
			if(this.getCard(i).getRank() != this.getCard(i+1).getRank())
			{
				return false;
			}
		}
		return true;
	}
}

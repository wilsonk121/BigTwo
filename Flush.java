public class Flush extends Hand
{
	public Flush(CardGamePlayer player, CardList cards)
	{
		super(player, cards);
	}
	
	public boolean isValid()
	{
		if(this.size() != 5)
			return false;
		for(int i = 0; i < (this.size() - 1); i++)
		{
			if(this.getCard(i).getSuit() != this.getCard(i+1).getSuit())
				return false;
		}
		return true;
	}
	
	public String getType()
	{
		return "Flush";
	}
}

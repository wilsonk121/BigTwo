public class Quad extends Hand
{
	public Quad(CardGamePlayer player, CardList cards)
	{
		super(player, cards);
	}
	
	public boolean isValid()
	{
		if(this.size() != 5)
			return false;
		if(this.getCard(0).getRank() == this.getCard(1).getRank() && this.getCard(1).getRank() == this.getCard(2).getRank() && this.getCard(2).getRank() == this.getCard(3).getRank())
			return true;
		if(this.getCard(1).getRank() == this.getCard(2).getRank() && this.getCard(2).getRank() == this.getCard(3).getRank() && this.getCard(3).getRank() == this.getCard(4).getRank())
			return true;
		return false;
	}
	
	public String getType()
	{
		return "Quad";
	}
	
	public Card getTopCard() 
	{
		if(this.getCard(0).getRank() == this.getCard(1).getRank() && this.getCard(1).getRank() == this.getCard(2).getRank() && this.getCard(2).getRank() == this.getCard(3).getRank())
			return this.getCard(3);
		else
			return this.getCard(4);
	}
}



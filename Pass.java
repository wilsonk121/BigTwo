
public class Pass extends Hand
{
	public Pass(CardGamePlayer player, CardList cards)
	{
		super(player, cards);
	}
	public boolean isValid()
	{
		if(this.size() == 0)
			return true;
		return false;
	}
	public String getType()
	{
		return "Pass";
	}
}


public class Pair extends SameRankHands{
	public Pair(CardGamePlayer player, CardList cards)
	{
		super(player, cards);
		this.setNumOfCardInHand(2);
	}
	public String getType()
	{
		return "Pair";
	}
}

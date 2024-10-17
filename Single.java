public class Single extends SameRankHands{
	public Single(CardGamePlayer player, CardList cards)
	{
		super(player, cards);
		this.setNumOfCardInHand(1);
	}
	public String getType()
	{
		return "Single";
	}
}


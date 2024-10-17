
public class Triple extends SameRankHands{
	public Triple(CardGamePlayer player, CardList cards)
	{
		super(player, cards);
		this.setNumOfCardInHand(3);
	}
	public String getType()
	{
		return "Triple";
	}
}

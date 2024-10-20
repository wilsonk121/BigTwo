import java.util.ArrayList;
import java.util.Arrays;

public abstract class Hand extends CardList {
	// private member variables
	private CardGamePlayer player;

	// public constructors
	public Hand(CardGamePlayer player, CardList cards) {
		this.player = player;
		for (int i = 0; i < cards.size(); i++) // copy all Cards from the CardList cards to this hand
		{
			this.addCard(cards.getCard(i));
		}
		this.sort();
	}

	// public methods
	public CardGamePlayer getPlayer() {
		return this.player;
	}

	public Card getTopCard() {
		return this.getCard(this.size() - 1);
	}

	boolean beats(Hand hand) {
		if (this.getType() == "Pass") {
			return true;
		}

		ArrayList<String> orderOfHand = new ArrayList<String>(
				Arrays.asList("Straight", "Flush", "FullHouse", "Quad", "StraightFlush"));
		int myHandOrder = orderOfHand.indexOf(this.getType());
		int yourHandOrder = orderOfHand.indexOf(hand.getType());

		if (myHandOrder > yourHandOrder) {
			return true;
		}
		if (myHandOrder < yourHandOrder) {
			return false;
		}
		
		if((this.getType() == "Flush" && hand.getType() == "Flush" || this.getType() == "StraightFlush" && hand.getType() == "StraightFlush") && this.getTopCard().getSuit() != hand.getTopCard().getSuit())
		{
			if(this.getTopCard().getSuit() > hand.getTopCard().getSuit())
				return true;
			else
				return false;
		}
			
		else if (this.getTopCard().compareTo(hand.getTopCard()) == 1) {
			return true;
		} else {
			return false;
		}
	}

	// abstract methods
	public abstract boolean isValid();
	public abstract String getType();
}

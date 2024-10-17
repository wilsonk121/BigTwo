import java.util.ArrayList;
import java.util.Arrays;

public class Straight extends Hand {
	public Straight(CardGamePlayer player, CardList cards) {
		super(player, cards);
	}

	public boolean isValid() {
		if (this.size() != 5)
			return false;
		ArrayList<Integer> BigTwoRankAnchor = new ArrayList<Integer>(
				Arrays.asList(2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 0, 1));
		for (int i = 0; i < (this.size() - 1); i++) {
			if (BigTwoRankAnchor.indexOf(this.getCard(i).getRank()) != (BigTwoRankAnchor.indexOf(this.getCard(i + 1).getRank()) - 1)) 
			{
				System.out.println(BigTwoRankAnchor.indexOf(this.getCard(i).getRank()));
				System.out.println(BigTwoRankAnchor.indexOf(this.getCard(i + 1).getRank()));
				return false;
			}
		}
		return true;
	}

	public String getType() {
		return "Straight";
	}
}

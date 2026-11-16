package postclass09.exercise4;

import e3.chapter3.Card;
import e3.chapter3.Rank;
import e3.chapter3.Suit;

public final class Exercise4Demo {
	private Exercise4Demo() { }

	public static void main(String[] pArgs) {
		Game game = new Game();
		Card two = new Card(Rank.TWO, Suit.CLUBS);
		Card king = new Card(Rank.KING, Suit.HEARTS);

		System.out.println(game.compare(two, king)); // 0: no order selected yet
		game.setComparator(Card.createByRankComparator());
		System.out.println(game.compare(two, king)); // negative: TWO comes before KING
	}
}

package postclass09.exercise4;

import java.util.Comparator;
import java.util.Objects;

import e3.chapter3.Card;

/** A game whose card ordering can be selected after initialization. */
public class Game {
	private Comparator<Card> aComparator = new UndeterminedCardComparator();

	/** Selects the comparison order once the game rules are known. */
	public void setComparator(Comparator<Card> pComparator) {
		aComparator = Objects.requireNonNull(pComparator);
	}

	public int compare(Card pCard1, Card pCard2) {
		return aComparator.compare(pCard1, pCard2);
	}

	/** Null Object: until a rule is selected, cards compare as equivalent. */
	private static final class UndeterminedCardComparator implements Comparator<Card> {
		@Override
		public int compare(Card pCard1, Card pCard2) {
			return 0;
		}
	}
}

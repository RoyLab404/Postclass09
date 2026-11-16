package postclass09.exercise6;

import chapter6.Concert;
import chapter6.Movie;
import chapter6.Show;

public final class Exercise6Demo {
	private Exercise6Demo() { }

	public static void main(String[] pArgs) {
		Show twoMovies = new CompositeShow(
				new Movie("The Matrix", 1999, 136),
				new Movie("Arrival", 2016, 116));
		CompositeShow program = new CompositeShow(
				new Concert("Live in Hanoi", "Example Band", 90), twoMovies);

		for (Show show : program) {
			System.out.println(show.description());
			if (show instanceof CompositeShow nested) {
				for (Show child : nested) {
					System.out.println("  - " + child.description());
				}
			}
		}
	}
}

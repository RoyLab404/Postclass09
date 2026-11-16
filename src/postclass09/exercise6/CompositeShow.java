package postclass09.exercise6;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import chapter6.Show;

/** A show made of two to five shows; its iterator exposes the direct components. */
public final class CompositeShow implements Show, Iterable<Show> {
	private final List<Show> aShows;

	public CompositeShow(Show... pShows) {
		Objects.requireNonNull(pShows);
		if (pShows.length < 2 || pShows.length > 5) {
			throw new IllegalArgumentException("A composite show must contain 2 to 5 shows");
		}
		aShows = List.of(pShows);
	}

	@Override
	public String description() {
		return aShows.stream().map(Show::description).collect(Collectors.joining("; "));
	}

	@Override
	public int runningTime() {
		return aShows.stream().mapToInt(Show::runningTime).sum();
	}

	@Override
	public Show copy() {
		return new CompositeShow(aShows.stream().map(Show::copy).toArray(Show[]::new));
	}

	@Override
	public Iterator<Show> iterator() {
		return aShows.iterator();
	}
}

package fuzzy.matching.domain;

import java.util.List;
import java.util.function.BiFunction;

/**
 * Interface implemented by Document, Element and Token to enable matching and
 * scoring these objects.
 */
@SuppressWarnings("rawtypes")
public interface Matchable {

  public long getChildCount(Matchable other);

  public BiFunction<Match, List<Score>, Score> getScoringFunction();

  public double getWeight();

  public long getUnmatchedChildCount(Matchable other);
}

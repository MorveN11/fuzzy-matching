package fuzzy.matching.component;

import fuzzy.matching.domain.Element;
import fuzzy.matching.domain.ElementClassification;
import fuzzy.matching.domain.MatchType;
import fuzzy.matching.domain.Token;
import fuzzy.matching.exception.MatchException;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Repository to store the Tokens.
 */
@SuppressWarnings("rawtypes")
public class TokenRepo {

  private Map<ElementClassification, Repo> repoMap;

  public TokenRepo() {
    this.repoMap = new ConcurrentHashMap<>();
  }

  /**
   * Puts the Token in the Repo.
   *
   * @param token Token to be put in the Repo
   */
  public void put(Token token) {

    ElementClassification elementClassification = token.getElement().getElementClassification();
    Repo repo = repoMap.get(elementClassification);

    if (repo == null) {
      repo = new Repo(token.getElement().getMatchType());
      repoMap.put(elementClassification, repo);
    }
    repo.put(token, token.getElement());
  }

  /**
   * Gets the Set of Elements for the given Token.
   *
   * @param token Token to be matched
   * @return Set of Elements
   */
  public Set<Element> get(Token token) {
    Repo repo = repoMap.get(token.getElement().getElementClassification());
    if (repo != null) {
      return repo.get(token);
    }
    return null;
  }

  private class Repo {

    MatchType matchType;

    Map<Object, Set<Element>> tokenElementSet;

    TreeSet<Object> tokenBinaryTree;

    private final Double agePctOf = 10D;
    private final Double datePctOf = 15777e7D; // 5 years of range

    Repo(MatchType matchType) {
      this.matchType = matchType;
      if (MatchType.NEAREST_NEIGHBORS.equals(matchType)) {
        tokenBinaryTree = new TreeSet<>();
        tokenElementSet = new ConcurrentHashMap<>();
      }
      if (MatchType.EQUALITY.equals(matchType)) {
        tokenElementSet = new ConcurrentHashMap<>();
      }
    }

    void put(Token token, Element element) {
      if (MatchType.NEAREST_NEIGHBORS.equals(matchType)) {
        tokenBinaryTree.add(token.getValue());
        Set<Element> elements = tokenElementSet.getOrDefault(token.getValue(), new HashSet<>());
        elements.add(element);
        tokenElementSet.put(token.getValue(), elements);
      }

      if (MatchType.EQUALITY.equals(matchType)) {
        Set<Element> elements = tokenElementSet.getOrDefault(token.getValue(), new HashSet<>());
        elements.add(element);
        tokenElementSet.put(token.getValue(), elements);
      }
    }

    Set<Element> get(Token token) {
      switch (matchType) {
        case EQUALITY:
          return tokenElementSet.get(token.getValue());
        case NEAREST_NEIGHBORS:
          TokenRange tokenRange;
          switch (token.getElement().getElementClassification().getElementType()) {
            case AGE:
              tokenRange = new TokenRange(
                  token, token.getElement().getNeighborhoodRange(), agePctOf);
              break;
            case DATE:
              tokenRange = new TokenRange(
                  token, token.getElement().getNeighborhoodRange(), datePctOf);
              break;
            default:
              tokenRange = new TokenRange(token, token.getElement().getNeighborhoodRange());
          }
          return tokenBinaryTree.subSet(tokenRange.lower, true, tokenRange.higher, true)
              .stream()
              .flatMap(val -> tokenElementSet.get(val).stream()).collect(Collectors.toSet());
        default:
          break;
      }
      return null;
    }
  }

  private class TokenRange {

    private final Object lower;
    private final Object higher;

    TokenRange(Token token, double pct, Double pctOf) {
      Object value = token.getValue();
      if (value instanceof Double) {
        this.lower = getLower((Double) value, pct, pctOf).doubleValue();
        this.higher = getHigher((Double) value, pct, pctOf).doubleValue();
      } else if (value instanceof Integer) {
        this.lower = getLower((Integer) value, pct, pctOf).intValue();
        this.higher = getHigher((Integer) value, pct, pctOf).intValue();
      } else if (value instanceof Long) {
        this.lower = getLower((Long) value, pct, pctOf).longValue();
        this.higher = getHigher((Long) value, pct, pctOf).longValue();
      } else if (value instanceof Float) {
        this.lower = getLower((Float) value, pct, pctOf).floatValue();
        this.higher = getHigher((Float) value, pct, pctOf).floatValue();
      } else if (value instanceof Date) {
        this.lower = new Date(getLower(((Date) value).getTime(), pct, pctOf).longValue());
        this.higher = new Date(getHigher(((Date) value).getTime(), pct, pctOf).longValue());
      } else {
        throw new MatchException("Data Type not supported");
      }
    }

    TokenRange(Token token, double pct) {
      this(token, pct, null);
    }

    private Number getLower(Number number, double pct, Double pctOf) {
      Double dpctOf = pctOf != null ? pctOf : number.doubleValue();
      Double pctVal = Math.abs(dpctOf * (1.0 - pct));
      return number.doubleValue() - pctVal;
    }

    private Number getHigher(Number number, double pct, Double pctOf) {
      Double dpctOf = pctOf != null ? pctOf : number.doubleValue();
      Double pctVal = Math.abs(dpctOf * (1.0 - pct));
      return number.doubleValue() + pctVal;
    }

  }

}

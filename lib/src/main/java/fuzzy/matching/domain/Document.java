package fuzzy.matching.domain;

import fuzzy.matching.function.ScoringFunction;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * <p>
 * The primary object for matching. The required attribute is a unique key and
 * elements
 * </p>
 * Configurable attributes
 * <ul>
 * <li>elements - A set of Element object to match against</li>
 * <li>threshold - Value above which documents are considered a match, default
 * 0.5</li>
 * </ul>
 */
@SuppressWarnings("rawtypes")
public class Document implements Matchable {
  private Document(String key, Set<Element> elements, double threshold) {
    this.key = key;
    this.elements = elements;
    this.threshold = threshold;
  }

  private String key;
  private Set<Element> elements;
  private Set<Element> preProcessedElement;
  private double threshold;
  private Boolean source;

  public String getKey() {
    return key;
  }

  public Set<Element> getElements() {
    return elements;
  }

  /**
   * <p>
   * Returns the pre-processed Element set.
   * </p>
   */
  public Set<Element> getPreProcessedElement() {
    if (this.preProcessedElement == null) {
      this.preProcessedElement = getDistinctNonEmptyElements().collect(Collectors.toSet());
    }
    return preProcessedElement;
  }

  public double getThreshold() {
    return threshold;
  }

  public Stream<Element> getDistinctElements() {
    return this.elements.stream()
        .filter(distinctByKey(Element::getPreprocessedValueWithType));
  }

  /**
   * <p>
   * Returns a stream of distinct non-empty elements.
   * </p>
   */
  public Stream<Element> getDistinctNonEmptyElements() {
    return getDistinctElements()
        .filter(m -> {
          if (m.getPreProcessedValue() instanceof String) {
            return !StringUtils.isEmpty(m.getPreProcessedValue().toString());
          } else {
            return m.getPreProcessedValue() != null;
          }
        });
  }

  private static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
    Set<Object> seen = ConcurrentHashMap.newKeySet();
    return t -> seen.add(keyExtractor.apply(t));
  }

  @Override
  public long getChildCount(Matchable other) {
    if (other instanceof Document) {
      Document o = (Document) other;
      List<ElementClassification> childrenType = this.getPreProcessedElement().stream()
          .map(Element::getElementClassification).collect(Collectors.toList());
      List<ElementClassification> otherChildrenType = o.getPreProcessedElement().stream()
          .map(Element::getElementClassification).collect(Collectors.toList());
      return CollectionUtils.union(childrenType, otherChildrenType).size();
    }
    return 0;

  }

  @Override
  public long getUnmatchedChildCount(Matchable other) {
    if (other instanceof Document) {
      Document o = (Document) other;
      List<ElementClassification> childrenType = this.getPreProcessedElement().stream()
          .map(Element::getElementClassification).collect(Collectors.toList());
      List<ElementClassification> otherChildrenType = o.getPreProcessedElement().stream()
          .map(Element::getElementClassification).collect(Collectors.toList());
      return CollectionUtils.disjunction(childrenType, otherChildrenType).size();
    }
    return 0;
  }

  @Override
  public BiFunction<Match, List<Score>, Score> getScoringFunction() {
    return ScoringFunction.getExponentialWeightedAverageScore();
  }

  @Override
  public double getWeight() {
    return 1.0;
  }

  public Boolean isSource() {
    return source;
  }

  public void setSource(Boolean source) {
    this.source = source;
  }

  /**
   * <p>
   * Builder class for Document.
   * </p>
   */
  public static class Builder {
    private String key;
    private Set<Element> elements;
    private double threshold = 0.5;

    public Builder(String key) {
      this.key = key;
    }

    public Builder setThreshold(double threshold) {
      this.threshold = threshold;
      return this;
    }

    /**
     * <p>
     * Add an Element to the Document.
     * </p>
     *
     * @param element Element to add
     * @return Builder
     */
    public Builder addElement(Element element) {
      if (this.elements == null || this.elements.isEmpty()) {
        this.elements = new HashSet<>();
      }
      this.elements.add(element);
      return this;
    }

    /**
     * <p>
     * Add a list of Element to the Document.
     * </p>
     *
     * @return Builder
     */
    public Document createDocument() {
      Document doc = new Document(key, elements, threshold);
      doc.elements.stream().forEach(element -> element.setDocument(doc));
      return doc;
    }
  }

  @Override
  public String toString() {
    return "{" + getOrderedElements(elements) + "}";
  }

  /**
   * <p>
   * Returns a list of elements ordered by ElementType.
   * </p>
   */
  public List<Element> getOrderedElements(Set<Element> elements) {
    return elements.stream().sorted(
        Comparator.comparing(ele -> ele.getElementClassification().getElementType()))
        .collect(Collectors.toList());
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Document document = (Document) o;

    return key.equals(document.key);

  }

  @Override
  public int hashCode() {
    return key.hashCode();
  }
}

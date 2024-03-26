package fuzzy.matching.component;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import fuzzy.matching.domain.Document;
import fuzzy.matching.domain.Element;
import fuzzy.matching.domain.ElementType;
import fuzzy.matching.domain.MatchType;
import fuzzy.matching.domain.Token;
import fuzzy.matching.exception.MatchException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

/**
 * Test class for TokenRepo.
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class TokenRepoTest {

  private final AtomicInteger ai = new AtomicInteger(0);

  @Test
  public void shouldGetForNameWithEquality() {
    List<Object> names = Arrays.asList("Amy Doe", "Brian Doe", "Jane Amy", "Michael Wane");

    List<Element> elements = getElements(names, ElementType.NAME, null);

    TokenRepo tokenRepo = new TokenRepo();

    elements.forEach(element -> {
      List<Token> tokenStream = element.getTokens();
      tokenStream.forEach(token -> tokenRepo.put(token));
    });

    Element<String> testElement1 = new Element.Builder<String>()
        .setType(ElementType.NAME).setValue("Amy")
        .createElement();
    Token token1 = testElement1.getTokens().get(0);
    Set<Element> matchingElements1 = tokenRepo.get(token1);
    assertTrue(matchingElements1.contains(elements.get(0)));
    assertTrue(matchingElements1.contains(elements.get(2)));

    Element<String> testElement2 = new Element.Builder<String>()
        .setType(ElementType.NAME).setValue("Doe")
        .createElement();
    Token token2 = testElement2.getTokens().get(0);
    Set<Element> matchingElements2 = tokenRepo.get(token2);
    assertTrue(matchingElements2.contains(elements.get(0)));
    assertTrue(matchingElements2.contains(elements.get(1)));
  }

  @Test
  public void shouldGetForNumberWithNearestNeighbor() {
    List<Object> numbers = Arrays.asList(100, 200, 1, 25, 700, 99, 210, 500);

    List<Element> elements = getElements(numbers, ElementType.NUMBER, null);

    TokenRepo tokenRepo = new TokenRepo();

    elements.forEach(element -> {
      List<Token> tokenStream = element.getTokens();
      tokenStream.forEach(token -> tokenRepo.put(token));
    });

    Element<Number> testElement1 = new Element.Builder()
        .setType(ElementType.NUMBER).setValue(101).createElement();
    Token token1 = testElement1.getTokens().get(0);
    Set<Element> matchingElements1 = tokenRepo.get(token1);
    assertTrue(matchingElements1.contains(elements.get(0)));
    assertTrue(matchingElements1.contains(elements.get(5)));

    Element<Number> testElement2 = new Element.Builder()
        .setType(ElementType.NUMBER).setValue(205).createElement();
    Token token2 = testElement2.getTokens().get(0);
    Set<Element> matchingElements2 = tokenRepo.get(token2);
    assertTrue(matchingElements2.contains(elements.get(1)));
    assertTrue(matchingElements2.contains(elements.get(6)));
  }

  @Test
  public void shouldGetForNumberForNegativeWithNearestNeighbor() {
    List<Object> numbers = Arrays.asList(-100, -200, -1, -25, -700, -99, -210, -500);

    List<Element> elements = getElements(numbers, ElementType.NUMBER, null);

    TokenRepo tokenRepo = new TokenRepo();

    elements.forEach(element -> {
      List<Token> tokenStream = element.getTokens();
      tokenStream.forEach(token -> tokenRepo.put(token));
    });

    Element<Number> testElement1 = new Element.Builder()
        .setType(ElementType.NUMBER).setValue(-101).createElement();
    Token token1 = testElement1.getTokens().get(0);
    Set<Element> matchingElements1 = tokenRepo.get(token1);
    assertTrue(matchingElements1.contains(elements.get(0)));
    assertTrue(matchingElements1.contains(elements.get(5)));

    Element<Number> testElement2 = new Element.Builder()
        .setType(ElementType.NUMBER).setValue(-205).createElement();
    Token token2 = testElement2.getTokens().get(0);
    Set<Element> matchingElements2 = tokenRepo.get(token2);
    assertTrue(matchingElements2.contains(elements.get(1)));
    assertTrue(matchingElements2.contains(elements.get(6)));
  }

  @Test
  public void shouldGetForNumberWithEquality() {
    List<Object> numbers = Arrays.asList(100, 200, 1, 25, 700, 99, 210, 500);

    List<Element> elements = getElements(numbers, ElementType.NUMBER, MatchType.EQUALITY);

    TokenRepo tokenRepo = new TokenRepo();

    elements.forEach(element -> {
      List<Token> tokenStream = element.getTokens();
      tokenStream.forEach(token -> tokenRepo.put(token));
    });

    Element<Number> testElement1 = new Element.Builder()
        .setType(ElementType.NUMBER).setValue(100).createElement();
    Token token1 = testElement1.getTokens().get(0);
    Set<Element> matchingElements1 = tokenRepo.get(token1);
    assertTrue(matchingElements1.contains(elements.get(0)));

    Element<Number> testElement2 = new Element.Builder()
        .setType(ElementType.NUMBER).setValue(200).createElement();
    Token token2 = testElement2.getTokens().get(0);
    Set<Element> matchingElements2 = tokenRepo.get(token2);
    assertTrue(matchingElements2.contains(elements.get(1)));
  }

  @Test
  public void shouldGetForNotSupportedWithNearestNeighbor() {
    List<Object> numbers = Arrays.asList("100", "200", "1", "25", "700", "99", "210", "500");

    List<Element> elements = getElements(numbers, ElementType.TEXT, MatchType.NEAREST_NEIGHBORS);

    TokenRepo tokenRepo = new TokenRepo();

    elements.forEach(element -> {
      List<Token> tokenStream = element.getTokens();
      tokenStream.forEach(token -> tokenRepo.put(token));
    });

    Element<String> testElement1 = new Element.Builder()
        .setType(ElementType.TEXT).setValue("101").createElement();
    Token token1 = testElement1.getTokens().get(0);

    assertThrows(MatchException.class, () -> tokenRepo.get(token1));
  }

  @Test
  public void shouldGetForDateWithNearestNeighbor() {
    List<Object> numbers = Arrays.asList(
        getDate("01/01/2020 00:00:00 GMT"), getDate("12/01/2020 00:00:00 GMT"),
        getDate("02/01/2020 00:00:00 GMT"), getDate("01/01/1970 01:59:00 GMT"));

    List<Element> elements = getElements(numbers, ElementType.DATE, null);

    TokenRepo tokenRepo = new TokenRepo();

    elements.forEach(element -> {
      List<Token> tokenStream = element.getTokens();
      tokenStream.forEach(token -> tokenRepo.put(token));
    });

    Element<Date> testElement1 = new Element.Builder().setType(ElementType.DATE)
        .setValue(getDate("01/01/1970 02:00:00 GMT")).createElement();
    Token token1 = testElement1.getTokens().get(0);
    Set<Element> matchingElements1 = tokenRepo.get(token1);
    assertTrue(matchingElements1.contains(elements.get(3)));
  }

  private Date getDate(String val) {
    DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss z");
    try {
      return df.parse(val);
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return null;
  }

  @Test
  public void shouldGetMultipleMatchedWithNearestNeighbour() {
    List<Object> numbers = Arrays.asList(100, 100);

    List<Element> elements = getElements(numbers, ElementType.NUMBER, null);

    TokenRepo tokenRepo = new TokenRepo();

    elements.forEach(element -> {
      List<Token> tokenStream = element.getTokens();
      tokenStream.forEach(token -> tokenRepo.put(token));
    });

    Element<Number> testElement1 = new Element.Builder()
        .setType(ElementType.NUMBER).setValue(100).createElement();
    Token token1 = testElement1.getTokens().get(0);
    Set<Element> matchingElements1 = tokenRepo.get(token1);
    assertTrue(matchingElements1.contains(elements.get(0)));
    assertTrue(matchingElements1.contains(elements.get(1)));
  }

  private List<Element> getElements(
      List<Object> values, ElementType elementType, MatchType matchType) {
    return values.stream()
        .map(value -> getElement(value, elementType, matchType)).collect(Collectors.toList());
  }

  private Element getElement(Object value, ElementType elementType, MatchType matchType) {
    Document.Builder documentBuilder = new Document.Builder(ai.incrementAndGet() + "");
    Element.Builder elementBuilder = new Element.Builder().setType(elementType).setValue(value);
    if (matchType != null) {
      elementBuilder.setMatchType(matchType);
    }
    Element element = elementBuilder.createElement();
    documentBuilder.addElement(element);
    element.setDocument(documentBuilder.createDocument());
    return element;
  }
}

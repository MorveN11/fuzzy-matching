package fuzzy.matching.types;

import static org.junit.jupiter.api.Assertions.assertEquals;

import fuzzy.matching.domain.Element;
import fuzzy.matching.domain.ElementType;
import fuzzy.matching.domain.Token;
import fuzzy.matching.function.TokenizerFunction;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

/**
 * Test class for PhoneTokenizer.
 */
@SuppressWarnings("unchecked")
public class PhoneTokenizerTest {
  /**
   * This test case tests the behavior of the decaGramTokenizer method with an
   * empty number
   * of the PhoneTokenizer.
   */
  @Test
  public void testDecaGramTokenizer_EmptyNumber() {
    Element<String> elem = new Element.Builder<String>()
        .setType(ElementType.PHONE).setValue("").createElement();
    Function<Element<String>, Stream<Token<String>>> tokenizerFunction = TokenizerFunction
        .decaGramTokenizer();
    Stream<Token<String>> tokenStream = tokenizerFunction.apply(elem);
    assertEquals("[{}]", tokenStream.collect(Collectors.toList()).toString());
  }

  /**
   * This test case tests the behavior of the decaGramTokenizer method with a
   * single number
   * of the PhoneTokenizer.
   */
  @Test
  public void testDecaGramTokenizer_SingleNumber() {
    Element<String> elem = new Element.Builder<String>().setType(ElementType.PHONE)
        .setValue("1").createElement();
    Function<Element<String>, Stream<Token<String>>> tokenizerFunction = TokenizerFunction
        .decaGramTokenizer();
    Stream<Token<String>> tokenStream = tokenizerFunction.apply(elem);
    List<Token<String>> tokens = tokenStream.collect(Collectors.toList());
    assertEquals("[1]", tokens.stream().map(Token::getValue)
        .collect(Collectors.toList()).toString());
  }

  /**
   * This test case tests the behavior of the decaGramTokenizer method with a
   * multiple digit number
   * of the PhoneTokenizer.
   */
  @Test
  public void testDecaGramTokenizer_MultipleNumberDigits() {
    Element<String> elem = new Element.Builder<String>().setType(ElementType.PHONE)
        .setValue("12345")
        .createElement();
    Function<Element<String>, Stream<Token<String>>> tokenizerFunction = TokenizerFunction
        .decaGramTokenizer();
    Stream<Token<String>> tokenStream = tokenizerFunction.apply(elem);
    List<Token<String>> tokens = tokenStream.collect(Collectors.toList());
    assertEquals("[12345]", tokens.stream().map(Token::getValue)
        .collect(Collectors.toList()).toString());
  }

  /**
   * This test case tests the behavior of the decaGramTokenizer method with a long
   * number
   * of the PhoneTokenizer.
   */
  @Test
  public void testDecaGramTokenizer_MultipleLongNumber() {
    Element<String> elem = new Element.Builder<String>().setType(ElementType.PHONE)
        .setValue("11223434567899")
        .createElement();
    Function<Element<String>, Stream<Token<String>>> tokenizerFunction = TokenizerFunction
        .decaGramTokenizer();
    Stream<Token<String>> tokenStream = tokenizerFunction.apply(elem);
    List<Token<String>> tokens = tokenStream.collect(Collectors.toList());
    assertEquals("[1122343456, 1223434567, 2234345678, 2343456789, 3434567899]",
        tokens.stream().map(Token::getValue).collect(Collectors.toList()).toString());
  }

  @Test
  public void testDecaGramTokenizer_MultipleLongNumber2() {
    Element<String> elem = new Element.Builder<String>().setType(ElementType.PHONE)
        .setValue("112234345690000000000").createElement();
    Function<Element<String>, Stream<Token<String>>> tokenizerFunction = TokenizerFunction
        .decaGramTokenizer();
    Stream<Token<String>> tokenStream = tokenizerFunction.apply(elem);
    List<Token<String>> tokens = tokenStream.collect(Collectors.toList());
    assertEquals(
        "[1122343456, 1223434569, 2234345690, 2343456900, 3434569000, "
            + "4345690000, 3456900000, 4569000000, 5690000000, 6900000000, 9000000000, 0000000000]",
        tokens.stream().map(Token::getValue).collect(Collectors.toList()).toString());
  }

  /**
   * This test case tests the behavior of the decaGramTokenizer method with an
   * alphabet element
   * of the PhoneTokenizer.
   */
  @Test
  public void testDecaGramTokenizer_AlphabetElement() {
    Element<String> elem = new Element.Builder<String>().setType(ElementType.PHONE)
        .setValue("HELLO  WORLD")
        .createElement();
    Function<Element<String>, Stream<Token<String>>> tokenizerFunction = TokenizerFunction
        .decaGramTokenizer();
    Stream<Token<String>> tokenStream = tokenizerFunction.apply(elem);
    List<Token<String>> tokens = tokenStream.collect(Collectors.toList());
    assertEquals("[]", tokens.stream().map(Token::getValue)
        .collect(Collectors.toList()).toString());
  }

  /**
   * This test case tests the behavior of the decaGramTokenizer method with an
   * alphanumeric element
   * of the PhoneTokenizer.
   */
  @Test
  public void testDecaGramTokenizer_AlphaNumericElement() {
    Element<String> elem = new Element.Builder<String>()
        .setType(ElementType.PHONE)
        .setValue("HELLO 123456789012 WORLD").createElement();
    Function<Element<String>, Stream<Token<String>>> tokenizerFunction = TokenizerFunction
        .decaGramTokenizer();
    Stream<Token<String>> tokenStream = tokenizerFunction.apply(elem);
    List<Token<String>> tokens = tokenStream.collect(Collectors.toList());
    assertEquals("[1234567890, 2345678901, 3456789012]",
        tokens.stream().map(Token::getValue).collect(Collectors.toList()).toString());
  }
}

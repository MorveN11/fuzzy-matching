package fuzzy.matching.function;

import static fuzzy.matching.domain.ElementType.ADDRESS;
import static fuzzy.matching.domain.ElementType.EMAIL;
import static fuzzy.matching.domain.ElementType.NAME;
import static fuzzy.matching.domain.ElementType.NUMBER;
import static fuzzy.matching.domain.ElementType.PATH;
import static fuzzy.matching.domain.ElementType.PHONE;
import static fuzzy.matching.domain.ElementType.TEXT;
import static fuzzy.matching.function.TokenizerFunction.triGramTokenizer;
import static fuzzy.matching.function.TokenizerFunction.valueTokenizer;
import static fuzzy.matching.function.TokenizerFunction.wordSoundexEncodeTokenizer;
import static fuzzy.matching.function.TokenizerFunction.wordTokenizer;
import static org.junit.jupiter.api.Assertions.assertEquals;

import fuzzy.matching.domain.Element;
import fuzzy.matching.domain.Token;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

/**
 * Test class for TokenizerFunction.
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class TokenizerFunctionTest {
  @Test
  public void itShouldGetNnGramTokenizer_Success() {
    String value = "james_parker";
    Element elem = new Element.Builder().setType(EMAIL).setValue(value).createElement();
    assertEquals((value.length() - 2) < 0 ? 1 : (value.length() - 2),
        triGramTokenizer().apply(elem).count());
  }

  @Test
  public void itShouldReturnNnGramTokenForSmallStrings_Success() {
    String value = "jp";
    Element elem = new Element.Builder().setType(EMAIL).setValue(value).createElement();
    assertEquals(1, triGramTokenizer().apply(elem).count());
  }

  @Test
  public void itShouldGetWordTokenizerForAddress_Success() {
    String value = "123 new Street, minneapolis mn";
    Element elem = new Element.Builder().setType(ADDRESS).setValue(value).createElement();
    assertEquals(5, wordTokenizer().apply(elem).count());
  }

  @Test
  public void itShouldGetWordTokenizerForName_Success() {
    String value = "James G. Parker";
    Element elem = new Element.Builder().setType(NAME).setValue(value).createElement();
    assertEquals(3, wordTokenizer().apply(elem).count());
  }

  @Test
  public void itShouldGetValueTokenizer_Success() {
    String value = "1234567890";
    Element elem = new Element.Builder().setType(PHONE).setValue(value).createElement();
    assertEquals(1, valueTokenizer().apply(elem).count());
    assertEquals("11234567890", valueTokenizer().apply(elem).findFirst().get().getValue());
  }

  @Test
  public void itShouldGetValueTokenizerForNumber_Success() {
    String value = "123.34";
    Element elem = new Element.Builder().setType(NUMBER).setValue(value).createElement();
    assertEquals(1, valueTokenizer().apply(elem).count());
    assertEquals("123.34", valueTokenizer().apply(elem).findFirst().get().getValue());
  }

  @Test
  public void itShouldTestExtraSpaceAndSpecialCharacters_Success() {
    String value = "12/3,    new     Street, minneapolis-   mn";
    Element elem = new Element.Builder().setType(ADDRESS).setValue(value).createElement();
    assertEquals(5, wordTokenizer().apply(elem).count());
  }

  @Test
  public void itShouldGetNnGramTokenizerLongString() {
    String value = "thisStringIsUsedForTestingAReallyHumungusExtremly"
        + "LongAndLargeStringForEnsuringThePerformanceOfTheLuceneTokenizerFunction";
    Element elem = new Element.Builder().setType(EMAIL).setValue(value).createElement();
    assertEquals((value.length() - 2) < 0 ? 1 : (value.length() - 2),
        triGramTokenizer().apply(elem).count());

    String value2 = "thisStringIsUsedForTestingAReallyHumungusExtremlyLongAndLargeString";
    Element elem2 = new Element.Builder().setType(EMAIL).setValue(value2).createElement();
    assertEquals((value2.length() - 2) < 0 ? 1 : (value2.length() - 2),
        triGramTokenizer().apply(elem2).count());
  }

  @Test
  public void itShouldGetWordSoundexEncodeTokenizerForAddress() {
    String value = "123 new Street 23rd Ave";
    Element elem = new Element.Builder().setType(ADDRESS).setValue(value).createElement();
    Stream<Token> resultStream = wordSoundexEncodeTokenizer().apply(elem);
    List<Token> results = resultStream.collect(Collectors.toList());
    assertEquals(5, results.size());
    assertEquals("123", results.get(0).getValue());
    assertEquals("N000", results.get(1).getValue());
    assertEquals("S363", results.get(2).getValue());
    assertEquals("23rd", results.get(3).getValue());
  }

  @Test
  public void itShouldGetWordSoundexEncodeTokenizerForName() {
    String value1 = "Stephen Wilkson";
    Element elem1 = new Element.Builder().setType(NAME).setValue(value1).createElement();
    Stream<Token> tokenStream1 = wordSoundexEncodeTokenizer().apply(elem1);
    List<Token> results1 = tokenStream1.collect(Collectors.toList());
    assertEquals("S315", results1.get(0).getValue());
    assertEquals("W425", results1.get(1).getValue());

    String value2 = "Steven Wilson";
    Element elem2 = new Element.Builder().setType(NAME).setValue(value2).createElement();
    Stream<Token> tokenStream2 = wordSoundexEncodeTokenizer().apply(elem2);
    List<Token> results2 = tokenStream2.collect(Collectors.toList());
    assertEquals("S315", results2.get(0).getValue());
    assertEquals("W425", results2.get(1).getValue());
  }

  @Test
  public void itShouldCustomTokenizeText() {
    String value = "123a234a345";

    Element defaultTokenElement = new Element.Builder().setType(TEXT)
        .setValue(value)
        .createElement();
    Stream<Token> defaultStream = (Stream<Token>) defaultTokenElement.getTokenizerFunction()
        .apply(defaultTokenElement);
    List<Token> defaultResults = defaultStream.collect(Collectors.toList());
    assertEquals(1, defaultResults.size());
    assertEquals(value, defaultResults.get(0).getValue());

    // Split the value with delimiter as character 'a'
    Function<Element<String>, Stream<Token>> customTokenFunc = (element) -> Arrays
        .stream(element.getPreProcessedValue().split("a"))
        .map(token -> new Token(token, element));

    Element customTokenElement = new Element.Builder().setType(TEXT)
        .setTokenizerFunction(customTokenFunc)
        .setValue(value)
        .createElement();
    Stream<Token> customStream = (Stream<Token>) customTokenElement
        .getTokenizerFunction()
        .apply(customTokenElement);
    List<Token> customResults = customStream.collect(Collectors.toList());
    assertEquals(3, customResults.size());
    assertEquals("123", customResults.get(0).getValue());
  }

  @Test
  public void itShouldGetPathTokenizer_Success() {
    String value = "path/to/file.txt";
    Element elem = new Element.Builder().setType(PATH).setValue(value).createElement();
    Stream<Token> resultStream = TokenizerFunction.pathTokenizer().apply(elem);
    List<Token> results = resultStream.collect(Collectors.toList());
    System.out.println(results);
    assertEquals(3, results.size());
    assertEquals("path", results.get(0).getValue());
    assertEquals("to", results.get(1).getValue());
    assertEquals("file", results.get(2).getValue());
  }

  @Test
  public void itShouldGetPathTokenizerForSingleWord_Success() {
    String value = "path";
    Element elem = new Element.Builder().setType(PATH).setValue(value).createElement();
    Stream<Token> resultStream = TokenizerFunction.pathTokenizer().apply(elem);
    List<Token> results = resultStream.collect(Collectors.toList());
    assertEquals(1, results.size());
    assertEquals("path", results.get(0).getValue());
  }

  @Test
  public void itShouldGetPathTokenizerForPathWithSpaces_Success() {
    String value = "path/ with / spaces/ file.txt";
    Element elem = new Element.Builder().setType(PATH).setValue(value).createElement();
    Stream<Token> resultStream = TokenizerFunction.pathTokenizer().apply(elem);
    List<Token> results = resultStream.collect(Collectors.toList());
    assertEquals(4, results.size());
    assertEquals("path", results.get(0).getValue());
    assertEquals("with", results.get(1).getValue());
    assertEquals("spaces", results.get(2).getValue());
    assertEquals("file", results.get(3).getValue());
  }
}

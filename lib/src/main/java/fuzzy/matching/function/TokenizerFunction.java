package fuzzy.matching.function;

import fuzzy.matching.domain.Element;
import fuzzy.matching.domain.Token;
import fuzzy.matching.exception.MatchException;
import fuzzy.matching.util.Utils;
import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Stream;
import org.apache.commons.codec.language.Soundex;

/**
 * A functional interface to Tokenize Elements.
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class TokenizerFunction {

  private static final Soundex soundex = new Soundex();

  public static Function<Element, Stream<Token>> valueTokenizer() {
    return (element -> Stream.of(new Token(element.getPreProcessedValue(), element)));
  }

  public static Function<Element<String>, Stream<Token<String>>> pathTokenizer() {
    return (element) -> Arrays.stream(element.getPreProcessedValue().split("/"))
        .map(token -> new Token<String>(token, element));
  }

  public static Function<Element<String>, Stream<Token<String>>> wordTokenizer() {
    return (element) -> Arrays.stream(element.getPreProcessedValue().split("\\s+"))
        .map(token -> new Token<String>(token, element));
  }

  /**
   * Tokenizes the element value into soundex codes.
   *
   * @return Function to tokenize the element value into soundex codes
   */
  public static Function<Element<String>, Stream<Token<String>>> wordSoundexEncodeTokenizer() {
    return (element) -> Arrays.stream(element.getPreProcessedValue().toString().split("\\s+"))
        .map(val -> {
          String code = val;
          if (!Utils.isNumeric(val)) {

            code = soundex.encode(val);
            if (code.equals("")) {
              code = val;
            }
          }
          // System.out.println(val +"->" + code);
          return code;
        }).map(token -> new Token<String>(token, element));
  }

  public static Function<Element<String>, Stream<Token<String>>> triGramTokenizer() {
    return (element) -> getNnGramTokens(3, element);
  }

  public static Function<Element<String>, Stream<Token<String>>> decaGramTokenizer() {
    return (element) -> getNnGramTokens(10, element);
  }

  public static Function<Element<String>, Stream<Token<String>>> idTokenizer() {
    return (element) -> getNnGramTokens(4, element);
  }

  /**
   * Tokenizes the element value into n-grams.
   *
   * @return Function to tokenize the element value into n-grams
   */
  public static Function<Element<String>, Stream<Token<String>>> priceTokenizer() {
    return (element) -> {
      String price = element.getValue();
      int numericCount = price.replaceAll("[^0-9]", "").length();
      return getNnGramTokens(numericCount, element);
    };
  }

  /**
   * Tokenizes the element value into n-grams.
   *
   * @param size    n-gram size
   * @param element element to tokenize
   * @return Stream of tokens
   */
  public static Stream<Token<String>> getNnGramTokens(int size, Element element) {
    Object elementValue = element.getPreProcessedValue();
    String elementValueStr;
    if (elementValue instanceof String) {
      elementValueStr = (String) elementValue;
    } else {
      throw new MatchException("Unsupported data type");
    }
    return Utils.getNnGrams(elementValueStr, size).map(str -> new Token<String>(str, element));

  }

  public static Function<Element<String>, Stream<Token<String>>> chainTokenizers(
      Function<Element<String>, Stream<Token<String>>>... tokenizers) {
    return element -> Arrays.stream(tokenizers).flatMap(fun -> fun.apply(element));
  }
}

package fuzzy.matching.util;

import fuzzy.matching.exception.MatchException;
import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.lucene.analysis.ngram.NGramTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

/**
 * Utility class for common operations.
 */
public class Utils {

  /**
   * utility method to get n-grams of a string.
   *
   * @param value A String of element value to be tokenized
   * @param size  An integer of the size of n-grams
   * @return Stream of n-grams
   */
  public static Stream<String> getNnGrams(String value, int size) {
    Stream.Builder<String> stringStream = Stream.builder();
    if (value.length() <= size) {
      stringStream.add(value);
    } else {
      NGramTokenizer nnGramTokenizer = new NGramTokenizer(size, size);
      CharTermAttribute charTermAttribute = nnGramTokenizer.addAttribute(CharTermAttribute.class);
      nnGramTokenizer.setReader(new StringReader(value));
      try {
        nnGramTokenizer.reset();
        while (nnGramTokenizer.incrementToken()) {
          stringStream.add(charTermAttribute.toString());
        }
        nnGramTokenizer.end();
        nnGramTokenizer.close();
      } catch (IOException io) {
        throw new MatchException("Failure in creating tokens : ", io);
      }
    }
    return stringStream.build();
  }

  /**
   * utility method to apply dictionary for normalizing strings.
   *
   * @param str  A String of element value to be normalized
   * @param dict A dictionary map containing the mapping of string to normalize
   * @return the normalized string
   */
  public static String getNormalizedString(String str, Map<String, String> dict) {
    return Arrays.stream(str.split("\\s+"))
        .map(d -> dict.containsKey(d.toLowerCase()) ? dict.get(d.toLowerCase())
            : d)
        .collect(Collectors.joining(" "));
  }

  public static boolean isNumeric(String str) {
    return str.matches(".*\\d.*");
  }
}

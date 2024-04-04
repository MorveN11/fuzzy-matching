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

  /**
   * utility method to calculate the edit distance between two strings.
   *
   * @param s1 A String of the first string
   * @param s2 A String of the second string
   * @return An integer of the edit distance between the two strings
   */
  public static int editDistance(String s1, String s2) {
    int m = s1.length();
    int n = s2.length();

    int[][] dp = new int[m + 1][n + 1];

    for (int i = 0; i <= m; i++) {
      dp[i][0] = i;
    }

    for (int j = 0; j <= n; j++) {
      dp[0][j] = j;
    }

    for (int i = 1; i <= m; i++) {
      for (int j = 1; j <= n; j++) {
        if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
          dp[i][j] = dp[i - 1][j - 1];
        } else {
          dp[i][j] = 1 + Math.min(dp[i - 1][j], Math.min(dp[i][j - 1], dp[i - 1][j - 1]));
        }
      }
    }

    return dp[m][n];
  }
}

package fuzzy.matching.domain;

import static fuzzy.matching.domain.MatchType.EQUALITY;
import static fuzzy.matching.domain.MatchType.NEAREST_NEIGHBORS;
import static fuzzy.matching.function.PreProcessFunction.*;
import static fuzzy.matching.function.TokenizerFunction.decaGramTokenizer;
import static fuzzy.matching.function.TokenizerFunction.pathTokenizer;
import static fuzzy.matching.function.TokenizerFunction.triGramTokenizer;
import static fuzzy.matching.function.TokenizerFunction.valueTokenizer;
import static fuzzy.matching.function.TokenizerFunction.wordSoundexEncodeTokenizer;
import static fuzzy.matching.function.TokenizerFunction.wordTokenizer;
import static fuzzy.matching.function.TokenizerFunction.idTokenizer;

import java.util.function.Function;

/**
 * Enum to define different types of Element.
 * This is used only to categorize the data, and apply functions at different
 * stages of match.
 * The functions, can be overridden from Element class using the appropriate
 * setters at the time of creation.
 */
@SuppressWarnings("rawtypes")
public enum ElementType {
  NAME,
  TEXT,
  ADDRESS,
  EMAIL,
  PHONE,
  NUMBER,
  DATE,
  AGE,
  PATH,
  ID;

  protected Function getPreProcessFunction() {
    switch (this) {
        case ID:
            return idPreProcessing();
      case PATH:
        return pathPreprocessing();
      case NAME:
        return namePreprocessing();
      case TEXT:
        return removeSpecialChars();
      case ADDRESS:
        return addressPreprocessing();
      case EMAIL:
        return removeDomain();
      case PHONE:
        return usPhoneNormalization();
      case NUMBER:
      case AGE:
        return numberPreprocessing();
      default:
        return none();
    }
  }

  protected Function getTokenizerFunction() {
    switch (this) {
        case ID:
            return idTokenizer();
      case PATH:
        return pathTokenizer();
      case NAME:
        return wordSoundexEncodeTokenizer();
      case TEXT:
        return wordTokenizer();
      case ADDRESS:
        return wordSoundexEncodeTokenizer();
      case EMAIL:
        return triGramTokenizer();
      case PHONE:
        return decaGramTokenizer();
      default:
        return valueTokenizer();
    }
  }

  protected MatchType getMatchType() {
    switch (this) {
      case NUMBER:
      case DATE:
      case AGE:
        return NEAREST_NEIGHBORS;
      default:
        return EQUALITY;
    }
  }
}

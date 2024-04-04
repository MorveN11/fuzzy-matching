package fuzzy.matching.domain;

import static fuzzy.matching.domain.MatchType.EQUALITY;
import static fuzzy.matching.domain.MatchType.EQUALITY_DISTANCE;
import static fuzzy.matching.domain.MatchType.NEAREST_NEIGHBORS;
import static fuzzy.matching.function.PreProcessFunction.idTypePreProcessing;
import static fuzzy.matching.function.PreProcessFunction.addressPreprocessing;
import static fuzzy.matching.function.PreProcessFunction.namePreprocessing;
import static fuzzy.matching.function.PreProcessFunction.none;
import static fuzzy.matching.function.PreProcessFunction.numberPreprocessing;
import static fuzzy.matching.function.PreProcessFunction.pathPreprocessing;
import static fuzzy.matching.function.PreProcessFunction.removeDomain;
import static fuzzy.matching.function.PreProcessFunction.removeSpecialChars;
import static fuzzy.matching.function.PreProcessFunction.usPhoneNormalization;
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
  ID,

  ;

  protected Function getPreProcessFunction() {
    switch (this) {
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
      case ID:
        return idTypePreProcessing();
      default:
        return none();
    }
  }

  protected Function getTokenizerFunction() {
    switch (this) {
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
      case ID:
        return idTokenizer();
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
      case ID:
        return EQUALITY_DISTANCE;
      default:
        return EQUALITY;
    }
  }
}

package fuzzy.matching.function;

import static fuzzy.matching.domain.ElementType.ADDRESS;
import static fuzzy.matching.domain.ElementType.NAME;
import static fuzzy.matching.domain.ElementType.TEXT;
import static org.junit.jupiter.api.Assertions.assertEquals;

import fuzzy.matching.domain.Element;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import org.junit.jupiter.api.Test;

/**
 * Test class for PreProcessFunction.
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class PreProcessFunctionTest {
  @Test
  public void itShouldRemoveSuffixFromName_Success() {
    String value = "James Parker JR.";
    Element<String> element = new Element.Builder<String>().setType(
        NAME).setValue(value).createElement();
    assertEquals("james parker", element.getPreProcessedValue());
  }

  @Test
  public void itShouldPreprocessAddress() {
    String value = "123 XYZ Ltd st, TX";
    Element element = new Element.Builder<String>()
        .setType(ADDRESS).setValue(value).createElement();
    assertEquals("123 xyz ltd street texas", element.getPreProcessedValue());
  }

  @Test
  public void itShouldCustomPreprocessAddress() {
    String value = "123_XYZ_Ltd_st, TX";
    Function<String, String> customPreProcessing = (str -> str.replaceAll("_", " "));
    customPreProcessing = customPreProcessing.andThen(PreProcessFunction.addressPreprocessing());

    Element element = new Element.Builder().setType(ADDRESS)
        .setPreProcessingFunction(customPreProcessing)
        .setValue(value)
        .createElement();

    assertEquals("123 xyz ltd street texas", element.getPreProcessedValue());
  }

  @Test
  public void itShouldGetNullString_Success() {
    String value = "   ";
    Element element = new Element.Builder().setType(NAME).setValue(value).createElement();
    assertEquals("", element.getPreProcessedValue());
  }

  @Test
  public void itShouldRemoveTrailingNumbersFromName_Success() {
    String value = "Nova LLC-1";
    Element element = new Element.Builder().setType(NAME).setValue(value).createElement();
    assertEquals("nova", element.getPreProcessedValue());
  }

  @Test
  public void itShouldTestComposingFunction() {
    String value = "James Parker jr.";
    Element element = new Element.Builder().setType(TEXT).setValue(value).createElement();
    assertEquals("james parker jr", element.getPreProcessedValue());

    Element element1 = new Element.Builder().setType(TEXT).setValue(value)
        .setPreProcessingFunction(PreProcessFunction.removeSpecialChars()
            .compose(str -> str.toString().replace("jr.", "")))
        .createElement();
    assertEquals("james parker", element1.getPreProcessedValue());
  }

  @Test
  public void itShouldNormalizeAddress_Success() {
    String value = "123 some-street ave PLano, TX";
    Element element = new Element.Builder().setType(ADDRESS).setValue(value).createElement();
    assertEquals("123 somestreet avenue plano texas", element.getPreProcessedValue());
  }

  @Test
  public void itShouldRemoveSpecialCharPhone_Success() {
    String value = "+1-(123)-456-4345";
    String result = (String) PreProcessFunction.removeSpecialChars().apply(value);
    assertEquals("11234564345", result);
  }

  @Test
  public void itShouldApplyNumberPreprocessing_Success() {
    String value = "$ value -34.76";
    String result = (String) PreProcessFunction.numberPreprocessing().apply(value);
    assertEquals("-34.76", result);
  }

  @Test
  public void itShouldApplyNumberPreprocessing_Failure() {
    String value = "$ value thirty four";
    String result = (String) PreProcessFunction.numberPreprocessing().apply(value);
    assertEquals(value, result);
  }

  @Test
  public void itShouldApplyNonePreprocessing_Success() throws ParseException {
    DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
    Date input = df.parse("04/01/2020");
    Date result = (Date) PreProcessFunction.none().apply(input);
    assertEquals(input, result);
  }

  @Test
  public void itShouldPreprocessPath() {
    String value = "/$home/$user/#files/file.txt";
    String result = (String) PreProcessFunction.pathPreprocessing().apply(value);
    assertEquals("/home/user/files/file", result);
  }

  @Test
  public void stringListPreProcessingShouldJoinListWithSpaces() {
    List<String> value = Arrays.asList("Hello", "World");
    String result = PreProcessFunction.stringListPreProcessing().apply(value);
    assertEquals("Hello World", result);
  }
}

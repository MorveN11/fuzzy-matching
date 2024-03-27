package fuzzy.matching.component;

import static fuzzy.matching.domain.ElementType.ADDRESS;
import static fuzzy.matching.domain.ElementType.AGE;
import static fuzzy.matching.domain.ElementType.DATE;
import static fuzzy.matching.domain.ElementType.EMAIL;
import static fuzzy.matching.domain.ElementType.NAME;
import static fuzzy.matching.domain.ElementType.NUMBER;
import static fuzzy.matching.domain.ElementType.PHONE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import fuzzy.matching.domain.Document;
import fuzzy.matching.domain.Element;
import fuzzy.matching.domain.ElementType;
import fuzzy.matching.domain.Match;
import fuzzy.matching.function.PreProcessFunction;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * MatchServiceTest.
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class MatchServiceTest {
  private static final Logger LOGGER = LoggerFactory.getLogger(MatchServiceTest.class);

  private MatchService matchService = new MatchService();

  /**
   * Write output to a CSV file.
   *
   * @return The List of documents
   * @throws FileNotFoundException if file not found
   */
  public List<Document> getTestDocuments() throws FileNotFoundException {
    return StreamSupport.stream(getCsvReader("test-data.csv").spliterator(), false).map(csv -> {
      return new Document.Builder(csv[0])
          .addElement(new Element.Builder().setType(NAME).setValue(csv[1]).createElement())
          .addElement(new Element.Builder().setType(ADDRESS).setValue(csv[2]).createElement())
          .addElement(new Element.Builder().setType(PHONE).setValue(csv[3]).setWeight(2)
              .setThreshold(0.5).createElement())
          .addElement(new Element.Builder().setType(EMAIL).setValue(csv[4]).createElement())
          .setThreshold(0.5)
          .createDocument();
    }).collect(Collectors.toList());
  }

  private List<Document> getTestDocuments(
      List<Object> values, ElementType elementType, Double neighborhoodRange) {
    AtomicInteger ai = new AtomicInteger(0);
    return values.stream().map(num -> {
      Element.Builder elementBuilder = new Element.Builder().setType(elementType).setValue(num);
      if (neighborhoodRange != null) {
        elementBuilder.setNeighborhoodRange(neighborhoodRange);
      }
      return new Document.Builder(Integer.toString(ai.incrementAndGet()))
          .addElement(elementBuilder.createElement())
          .createDocument();
    }).collect(Collectors.toList());
  }

  @Test
  public void itShouldApplyMatchForDemo() throws IOException {
    Map<String, List<Match<Document>>> result = matchService.applyMatchByDocId(getDemoDocuments());
    result.entrySet().forEach(entry -> {
      entry.getValue().forEach(match -> {
        System.out.println(
            "Data: " + match.getData() + " Matched With: " + match.getMatchedWith() + " Score: "
                + match.getScore().getResult());
      });
    });
    assertEquals(5, result.size());
  }

  @Test
  public void itShouldApplyMatchByDocId() {
    String[][] input = {
        { "1", "Steven Wilson", "45th Avenue 5th st." },
        { "2", "John Doe", "546 freeman ave" },
        { "3", "Stephen Wilkson", "45th Ave 5th Street" }
    };
    List<Document> documentList = Arrays.asList(input).stream().map(contact -> {
      return new Document.Builder(contact[0])
          .addElement(new Element.Builder<String>()
              .setValue(contact[1]).setType(NAME).createElement())
          .addElement(new Element.Builder<String>()
              .setValue(contact[2]).setType(ADDRESS).createElement())
          .createDocument();
    }).collect(Collectors.toList());

    Map<String, List<Match<Document>>> result = matchService.applyMatchByDocId(documentList);
    result.entrySet().forEach(entry -> {
      entry.getValue().forEach(match -> {
        System.out.println(
            "Data: " + match.getData() + " Matched With: " + match.getMatchedWith() + " Score: "
                + match.getScore().getResult());
      });
    });
    assertEquals(2, result.size());
  }

  @Test
  public void itShouldApplyMatchByGroups() {
    String[][] input = {
        { "1", "Steven Wilson", "45th Avenue 5th st." },
        { "2", "John Doe", "546 freeman ave" },
        { "3", "Stephen Wilkson", "45th Ave 5th Street" }
    };
    List<Document> documentList = Arrays.asList(input).stream().map(contact -> {
      return new Document.Builder(contact[0])
          .addElement(new Element.Builder<String>()
              .setValue(contact[1]).setType(NAME).createElement())
          .addElement(new Element.Builder<String>()
              .setValue(contact[2]).setType(ADDRESS).createElement())
          .createDocument();
    }).collect(Collectors.toList());

    Set<Set<Match<Document>>> result = matchService.applyMatchByGroups(documentList);
    result.forEach(matches -> {
      matches.forEach(match -> {
        System.out.println(
            "Data: " + match.getData() + " Matched With: " + match.getMatchedWith() + " Score: "
                + match.getScore().getResult());
      });
    });
    assertEquals(1, result.size());
  }

  @Test
  public void itShouldApplyMatchByDocIdForSingleDoc() throws IOException {
    Document doc = new Document.Builder("TestMatch")
        .addElement(new Element.Builder().setType(NAME)
            .setValue("john doe").createElement())
        .addElement(new Element.Builder().setType(ADDRESS)
            .setValue("546 freeman ave dallas tx 75024").createElement())
        .addElement(new Element.Builder().setType(PHONE)
            .setValue("2122232235").createElement())
        .addElement(new Element.Builder().setType(EMAIL)
            .setValue("john@doe.com").createElement())
        .createDocument();
    Map<String, List<Match<Document>>> result = matchService
        .applyMatchByDocId(doc, getTestDocuments());
    writeOutput(result);
    assertEquals(1, result.size());
  }

  @Test
  public void itShouldApplyMatchByDocIdForAnList() throws IOException {
    Map<String, List<Match<Document>>> result = matchService.applyMatchByDocId(getTestDocuments());
    writeOutput(result);
    assertEquals(6, result.size());
  }

  @Test
  public void itShouldApplyMatchByGroupsForAnList() throws IOException {
    Set<Set<Match<Document>>> result = matchService.applyMatchByGroups(getTestDocuments());
    writeOutput(result);
    assertEquals(2, result.size());
  }

  /**
   * Write output to a CSV file.
   */
  public static Stream<Element> getOrderedElements(Set<Element> elements) {
    List<Element> l = elements.stream()
        .sorted(Comparator.comparing(ele -> ele.getElementClassification().getElementType()))
        .collect(Collectors.toList());
    return l.stream();
  }

  @Test
  public void itShouldApplyMatchForMultiplePhoneNumber() {
    List<Document> inputData = new ArrayList<>();
    inputData.add(new Document.Builder("1")
        .addElement(new Element.Builder().setType(NAME)
            .setValue("Kapa Limited").createElement())
        .addElement(new Element.Builder().setType(ADDRESS)
            .setValue("texas").createElement())
        .addElement(new Element.Builder().setType(PHONE)
            .setValue("8204354957 xyz").createElement())
        .addElement(new Element.Builder().setType(PHONE)
            .setValue("").createElement())
        .addElement(new Element.Builder().setType(PHONE)
            .setValue("(848) 398-3868").createElement())
        .addElement(new Element.Builder().setType(EMAIL)
            .setValue("kirit@kapalimited.com").createElement())
        .createDocument());
    inputData.add(new Document.Builder("2")
        .addElement(new Element.Builder().setType(NAME)
            .setValue("Tram Kapa Ltd LLC").createElement())
        .addElement(new Element.Builder().setType(ADDRESS)
            .setValue("texas").createElement())
        .addElement(new Element.Builder().setType(PHONE)
            .setValue("(848) 398-3868").createElement())
        .addElement(new Element.Builder().setType(PHONE)
            .setValue("(820) 435-4957").createElement())
        .addElement(new Element.Builder().setType(PHONE)
            .setValue("").createElement())
        .addElement(new Element.Builder().setType(EMAIL)
            .setValue("kirit@nekoproductions.com").createElement())
        .createDocument());
    Map<Document, List<Match<Document>>> result = matchService.applyMatch(inputData);
    assertEquals(2, result.size());
    assertThat(result.entrySet().stream()
        .map(entry -> entry.getKey().getKey()).collect(Collectors.toList()),
        CoreMatchers.hasItems("1", "2"));
  }

  @Test
  public void itShouldApplyMatchForMultipleEmptyPhoneNumber() {
    List<Document> inputData = new ArrayList<>();
    inputData.add(new Document.Builder("1")
        .addElement(new Element.Builder().setType(NAME).setValue("Kapa Limited").createElement())
        .addElement(new Element.Builder().setType(ADDRESS).setValue("texas").createElement())
        .addElement(new Element.Builder().setType(PHONE)
            .setValue("8204354957 xyz").setWeight(2).setThreshold(0.5)
            .createElement())
        .addElement(new Element.Builder().setType(PHONE)
            .setValue("").setWeight(2).setThreshold(0.5).createElement())
        .addElement(new Element.Builder().setType(PHONE)
            .setValue("(848) 398-3868").setWeight(2).setThreshold(0.5)
            .createElement())
        .addElement(
            new Element.Builder().setType(EMAIL)
                .setValue("kirit@kapalimited.com").setThreshold(0.5).createElement())
        .createDocument());
    inputData.add(new Document.Builder("2")
        .addElement(new Element.Builder().setType(NAME)
            .setValue("Tram Kapa Ltd LLC").createElement())
        .addElement(new Element.Builder().setType(ADDRESS)
            .setValue("texas").createElement())
        .addElement(new Element.Builder().setType(PHONE)
            .setValue("").setWeight(2).setThreshold(0.5).createElement())
        .addElement(new Element.Builder().setType(PHONE)
            .setValue("").setWeight(2).setThreshold(0.5).createElement())
        .addElement(new Element.Builder().setType(PHONE)
            .setValue("").setWeight(2).setThreshold(0.5).createElement())
        .addElement(new Element.Builder().setType(EMAIL)
            .setValue("kirit@nekoproductions.com").setThreshold(0.5)
            .createElement())
        .createDocument());
    Map<Document, List<Match<Document>>> result = matchService.applyMatch(inputData);
    assertEquals(2, result.size());
    assertThat(result.entrySet().stream()
        .map(entry -> entry.getKey().getKey()).collect(Collectors.toList()),
        CoreMatchers.hasItems("1", "2"));
  }

  @Test
  public void itShouldApplyMatchForDuplicateTokensWithNoMatch() {
    List<Document> inputData = new ArrayList<>();
    inputData.add(new Document.Builder("1")
        .addElement(new Element.Builder().setType(NAME).setValue("lucky DAVID ABC").createElement())
        .addElement(new Element.Builder().setType(ADDRESS)
            .setValue("123 W Plano St PLANO TX 33130").createElement())
        .addElement(new Element.Builder().setType(PHONE)
            .setWeight(2).setValue("").setThreshold(0.5).createElement())
        .addElement(new Element.Builder().setType(PHONE)
            .setWeight(2).setValue("").setThreshold(0.5).createElement())
        .addElement(new Element.Builder().setType(PHONE)
            .setWeight(2).setValue("").setThreshold(0.5).createElement())
        .addElement(new Element.Builder().setType(EMAIL)
            .setValue("").setThreshold(0.5).createElement())
        .createDocument());
    inputData.add(new Document.Builder("2")
        .addElement(new Element.Builder().setType(NAME).setValue("Ramirez Yara").createElement())
        .addElement(
            new Element.Builder().setType(ADDRESS)
                .setValue("123 W Plano St 2111 Plano TX 33130").createElement())
        .addElement(
            new Element.Builder().setType(PHONE).setWeight(2)
                .setValue("1231231234").setThreshold(0.5).createElement())
        .addElement(new Element.Builder().setType(PHONE).setWeight(2)
            .setValue("").setThreshold(0.5).createElement())
        .addElement(new Element.Builder().setType(PHONE).setWeight(2)
            .setValue("").setThreshold(0.5).createElement())
        .addElement(
            new Element.Builder().setType(EMAIL)
                .setValue("yara1345@gmail.com").setThreshold(0.5).createElement())
        .createDocument());
    Map<Document, List<Match<Document>>> result = matchService.applyMatch(inputData);
    assertTrue(result.isEmpty());
  }

  @Test
  public void itShouldApplyMatch() {
    List<Document> inputData = new ArrayList<>();
    inputData.add(new Document.Builder("1")
        .addElement(new Element.Builder().setType(NAME).setValue("James Parker").createElement())
        .addElement(new Element.Builder().setType(ADDRESS)
            .setValue("123 new st. Minneapolis MN").createElement())
        .addElement(new Element.Builder().setType(PHONE)
            .setWeight(2).setValue("(123) 234 2345").setThreshold(0.5)
            .createElement())
        .addElement(
            new Element.Builder().setType(EMAIL)
                .setValue("jparker@gmail.com").setThreshold(0.5).createElement())
        .createDocument());
    inputData.add(new Document.Builder("2")
        .addElement(new Element.Builder().setType(NAME).setValue("James").createElement())
        .addElement(new Element.Builder().setType(ADDRESS)
            .setValue("123 new Street, minneapolis mn").createElement())
        .addElement(new Element.Builder().setType(PHONE).setWeight(2)
            .setValue("123-234-2345").setThreshold(0.5)
            .createElement())
        .addElement(
            new Element.Builder().setType(EMAIL)
                .setValue("james_parker@domain.com").setThreshold(0.5).createElement())
        .createDocument());

    Map<Document, List<Match<Document>>> result = matchService.applyMatch(inputData);
    assertEquals(2, result.size());
    assertThat(result.entrySet().stream()
        .map(entry -> entry.getKey().getKey()).collect(Collectors.toList()),
        CoreMatchers.hasItems("1", "2"));
  }

  @Test
  public void itShouldApplyMatchWith3Documents() {
    List<Document> inputData = new ArrayList<>();
    inputData.add(new Document.Builder("1")
        .addElement(new Element.Builder().setType(NAME).setValue("James Parker").createElement())
        .addElement(new Element.Builder().setType(ADDRESS)
            .setValue("123 new st. Minneapolis MN").createElement())
        .addElement(new Element.Builder().setType(PHONE).setWeight(2)
            .setValue("(123) 234 2345").setThreshold(0.5)
            .createElement())
        .addElement(
            new Element.Builder().setType(EMAIL)
                .setValue("jparker@gmail.com").setThreshold(0.5).createElement())
        .createDocument());
    inputData.add(new Document.Builder("2")
        .addElement(new Element.Builder().setType(NAME).setValue("James").createElement())
        .addElement(new Element.Builder().setType(ADDRESS)
            .setValue("123 new Street, minneapolis mn").createElement())
        .addElement(new Element.Builder().setType(PHONE)
            .setWeight(2).setValue("123-234-2345").setThreshold(0.5)
            .createElement())
        .addElement(
            new Element.Builder().setType(EMAIL)
                .setValue("james_parker@domain.com").setThreshold(0.5).createElement())
        .createDocument());
    inputData.add(new Document.Builder("3")
        .addElement(new Element.Builder().setType(NAME).setValue("John D").createElement())
        .addElement(new Element.Builder().setType(ADDRESS)
            .setValue("33 hammons Dr. Texas").createElement())
        .addElement(
            new Element.Builder().setType(PHONE).setWeight(2)
                .setValue("9901238484").setThreshold(0.5).createElement())
        .addElement(
            new Element.Builder().setType(EMAIL)
                .setValue("d_john@domain.com").setThreshold(0.5).createElement())
        .createDocument());
    Map<Document, List<Match<Document>>> result = matchService.applyMatch(inputData);
    int totalMatches = result.values().stream().mapToInt(List::size).sum();
    assertThat(result.entrySet().stream()
        .map(entry -> entry.getKey().getKey()).collect(Collectors.toList()),
        CoreMatchers.hasItems("1", "2"));
    assertEquals(2, totalMatches);
  }

  @Test
  public void itShouldApplyMatchWithFailure() {
    List<Document> inputData = new ArrayList<>();
    inputData.add(new Document.Builder("1")
        .addElement(new Element.Builder().setType(NAME).setValue("James Parker").createElement())
        .addElement(new Element.Builder().setType(ADDRESS)
            .setValue("123 new st. Minneapolis MN").createElement())
        .addElement(new Element.Builder().setType(PHONE)
            .setWeight(2).setValue("(123) 234 2345").setThreshold(0.5)
            .createElement())
        .addElement(
            new Element.Builder().setType(EMAIL)
                .setValue("jparker@gmail.com").setThreshold(0.5).createElement())
        .createDocument());
    inputData.add(new Document.Builder("2")
        .addElement(new Element.Builder().setType(NAME)
            .setValue("Peter Watson").createElement())
        .addElement(new Element.Builder().setType(ADDRESS)
            .setValue("321 john Q Hammons Street, Plano, TX - 75054")
            .createElement())
        .addElement(
            new Element.Builder().setType(PHONE)
                .setWeight(2).setValue("9091238877").setThreshold(0.5).createElement())
        .addElement(
            new Element.Builder().setType(EMAIL)
                .setValue("peter.watson@domain.com").setThreshold(0.5).createElement())
        .createDocument());

    Map<Document, List<Match<Document>>> result = matchService.applyMatch(inputData);
    assertTrue(result.isEmpty());
  }

  @Test
  public void itShouldApplyMatchForMultipleEmptyField() {
    List<Document> inputData = new ArrayList<>();
    inputData.add(new Document.Builder("1")
        .addElement(new Element.Builder().setType(NAME).setValue("James Parker").createElement())
        .addElement(new Element.Builder().setType(ADDRESS)
            .setValue("123 new st. Minneapolis MN").createElement())
        .addElement(new Element.Builder().setType(PHONE)
            .setWeight(2).setValue("").setThreshold(0.5).createElement())
        .addElement(new Element.Builder().setType(EMAIL)
            .setValue("").setThreshold(0.5).createElement())
        .createDocument());
    inputData.add(new Document.Builder("2")
        .addElement(new Element.Builder().setType(NAME)
            .setValue("James").createElement())
        .addElement(new Element.Builder().setType(ADDRESS)
            .setValue("123 new street Minneapolis MN").createElement())
        .addElement(new Element.Builder().setType(PHONE)
            .setWeight(2).setValue("").setThreshold(0.5).createElement())
        .addElement(new Element.Builder().setType(EMAIL)
            .setValue("").setThreshold(0.5).createElement())
        .createDocument());

    Map<Document, List<Match<Document>>> result = matchService.applyMatch(inputData);
    assertThat(result.entrySet().stream()
        .map(entry -> entry.getKey().getKey()).collect(Collectors.toList()),
        CoreMatchers.hasItems("1", "2"));
    assertEquals(2, result.size());
  }

  @Test
  public void itShouldApplyMatchForEmptyInput() {
    List<Document> inputData = new ArrayList<>();
    Map<Document, List<Match<Document>>> result = matchService.applyMatch(inputData);
    assertTrue(result.isEmpty());
  }

  @Test
  public void itShouldApplyMatchForWhiteSpaceWithNoFalsePositive() {
    List<Document> inputData = new ArrayList<>();
    inputData.add(new Document.Builder("1")
        .addElement(new Element.Builder().setType(NAME)
            .setValue("sdwet ert rdfgh, LLC").createElement())
        .addElement(new Element.Builder().setType(ADDRESS)
            .setValue(" ").createElement())
        .addElement(new Element.Builder().setType(PHONE)
            .setWeight(2).setValue("").setThreshold(0.5).createElement())
        .addElement(new Element.Builder().setType(EMAIL)
            .setValue("sdwet@abc.com").setThreshold(0.5).createElement())
        .createDocument());
    inputData.add(new Document.Builder("2")
        .addElement(new Element.Builder().setType(NAME).setValue("sad sdf LLC").createElement())
        .addElement(new Element.Builder().setType(ADDRESS).setValue(" ").createElement())
        .addElement(new Element.Builder().setType(PHONE)
            .setWeight(2).setValue("").setThreshold(0.5).createElement())
        .addElement(
            new Element.Builder().setType(EMAIL)
                .setValue("sad@something.com").setThreshold(0.5).createElement())
        .createDocument());

    Map<Document, List<Match<Document>>> result = matchService.applyMatch(inputData);
    assertTrue(result.isEmpty());
  }

  // It tests whether there is any match between two different type element
  @Test
  public void itShouldApplyMatchElementsWithDifferentType() {
    List<Document> documents = new ArrayList<>();
    documents.add(new Document.Builder("1")
        .addElement(new Element.Builder().setType(NAME).setValue("John d").createElement())
        .addElement(new Element.Builder().setType(ADDRESS)
            .setValue("freeman ave dallas 75024").createElement())
        .addElement(new Element.Builder().setType(PHONE)
            .setWeight(2).setValue("435-221-5432").setThreshold(0.5)
            .createElement())
        .addElement(new Element.Builder().setType(EMAIL)
            .setValue("john_doe@gmail.com").createElement())
        .createDocument());
    documents.add(new Document.Builder("2")
        .addElement(new Element.Builder().setType(NAME)
            .setValue("john doe").createElement())
        .addElement(
            new Element.Builder().setType(ADDRESS)
                .setValue("546 freeman avenue dallas tx 75024").createElement())
        .addElement(new Element.Builder().setType(PHONE)
            .setWeight(2).setValue("435-334-2234").setThreshold(0.5)
            .createElement())
        .addElement(new Element.Builder().setType(EMAIL).setValue("john@doe.com").createElement())
        .createDocument());

    Map<Document, List<Match<Document>>> result = matchService.applyMatch(documents);
    assertTrue(result.isEmpty());
  }

  /**
   * Get test data.
   */
  public List<Document> getDemoDocuments() throws FileNotFoundException {
    AtomicInteger index = new AtomicInteger();
    return StreamSupport.stream(getCsvReader("demo.csv").spliterator(), false).map(csv -> {
      return new Document.Builder(index.incrementAndGet() + "")
          .addElement(new Element.Builder<String>().setType(NAME).setValue(csv[0]).createElement())
          .createDocument();
    }).collect(Collectors.toList());
  }

  /**
   * Get test data.
   */
  public static CSVReader getCsvReader(String fileName) throws FileNotFoundException {
    return new CSVReaderBuilder(
        new FileReader(MatchServiceTest.class.getClassLoader().getResource(fileName).getFile()))
        .withSkipLines(1)
        .build();
  }

  @Test
  public void itShouldApplyMatchWithSourceList() throws FileNotFoundException {
    List<Document> sourceData = new ArrayList<>();
    sourceData.add(new Document.Builder("S1")
        .addElement(new Element.Builder().setType(NAME)
            .setValue("James Parker").createElement())
        .addElement(new Element.Builder().setType(ADDRESS)
            .setValue("123 new st. Minneapolis MN").createElement())
        .addElement(new Element.Builder().setType(PHONE)
            .setWeight(2).setValue("(123) 234 2345").setThreshold(0.5)
            .createElement())
        .addElement(
            new Element.Builder().setType(EMAIL)
                .setValue("jparker@gmail.com").setThreshold(0.5).createElement())
        .createDocument());
    sourceData.add(new Document.Builder("S2")
        .addElement(new Element.Builder().setType(NAME).setValue("James").createElement())
        .addElement(new Element.Builder().setType(ADDRESS)
            .setValue("123 new Street, minneapolis mn").createElement())
        .addElement(new Element.Builder().setType(PHONE)
            .setWeight(2).setValue("123-234-2345").setThreshold(0.5)
            .createElement())
        .addElement(
            new Element.Builder().setType(EMAIL)
                .setValue("james_parker@domain.com").setThreshold(0.5).createElement())
        .createDocument());

    Map<Document, List<Match<Document>>> result = matchService
        .applyMatch(sourceData, getTestDocuments());
    assertThat(result.entrySet().stream()
        .map(entry -> entry.getKey().getKey()).collect(Collectors.toList()),
        CoreMatchers.hasItems("S1", "S2"));
    assertEquals(2, result.size());
  }

  @Test
  public void itShouldApplyMatchWithSourceDocument() throws FileNotFoundException {
    Document doc = new Document.Builder("S1")
        .addElement(new Element.Builder().setType(NAME).setValue("James Parker").createElement())
        .addElement(new Element.Builder().setType(ADDRESS)
            .setValue("123 new st. Minneapolis MN").createElement())
        .addElement(new Element.Builder().setType(PHONE)
            .setWeight(2).setValue("(123) 234 2345").setThreshold(0.5)
            .createElement())
        .addElement(
            new Element.Builder().setType(EMAIL)
                .setValue("jparker@gmail.com").setThreshold(0.5).createElement())
        .createDocument();

    Map<Document, List<Match<Document>>> result = matchService.applyMatch(doc, getTestDocuments());
    assertThat(result.entrySet().stream()
        .map(entry -> entry.getKey().getKey()).collect(Collectors.toList()),
        CoreMatchers.hasItems("S1"));
    assertEquals(1, result.size());
  }

  @Test
  public void itShouldApplyMatchWithScoreNotMoreThanOne() {
    List<Document> inputData = new ArrayList<>();
    Document doc1 = new Document.Builder("1")
        .addElement(new Element.Builder().setType(NAME).setValue("Kapa Limited").createElement())
        .addElement(
            new Element.Builder().setType(ADDRESS)
                .setValue("123 some street, plano, texas - 75070").createElement())
        .addElement(new Element.Builder().setType(PHONE)
            .setValue("123-456-7890").setThreshold(0.5).setWeight(2)
            .createElement())
        .addElement(
            new Element.Builder().setType(PHONE)
                .setValue("1234567890").setThreshold(0.5).setWeight(2).createElement())
        .addElement(new Element.Builder().setType(PHONE)
            .setValue("").setWeight(2).setThreshold(0.5).createElement())
        .addElement(
            new Element.Builder().setType(EMAIL)
                .setValue("kirit@kapalimited.com").setThreshold(0.5).createElement())
        .createDocument();
    Document doc2 = new Document.Builder("2")
        .addElement(new Element.Builder().setType(NAME).setValue("ABC CORP").createElement())
        .addElement(
            new Element.Builder().setType(ADDRESS)
                .setValue("123 some street, plano, texas - 75070").createElement())
        .addElement(new Element.Builder()
            .setType(PHONE).setValue("123-456-7890").setWeight(2).setThreshold(0.5)
            .createElement())
        .addElement(
            new Element.Builder().setType(PHONE)
                .setValue("1234567890").setWeight(2).setThreshold(0.5).createElement())
        .addElement(new Element.Builder()
            .setType(PHONE).setValue("").setWeight(2).setThreshold(0.5).createElement())
        .addElement(new Element.Builder()
            .setType(EMAIL).setValue("kirit@nekoproductions.com").setThreshold(0.5)
            .createElement())
        .createDocument();
    inputData.addAll(Arrays.asList(doc1, doc2));
    Map<Document, List<Match<Document>>> result = matchService.applyMatch(inputData);
    assertEquals(2, result.size());
    assertThat(result.entrySet().stream()
        .map(entry -> entry.getKey().getKey()).collect(Collectors.toList()),
        CoreMatchers.hasItems("1", "2"));
    assertTrue(result.get(doc1).get(0).getResult() <= 1);
  }

  @Test
  public void itShouldApplyMatchWithConfigurablePreProcessingFunctions() throws IOException {

    Map<Document, List<Match<Document>>> result1 = matchService.applyMatch(
        getTestData(PreProcessFunction.removeSpecialChars(),
            PreProcessFunction.removeSpecialChars(), 0.7));
    assertEquals(2, result1.size());

    Map<Document, List<Match<Document>>> result2 = matchService.applyMatch(
        getTestData(PreProcessFunction.namePreprocessing(),
            PreProcessFunction.addressPreprocessing(), 0.7));
    assertEquals(4, result2.size());
  }

  @Test
  public void itShouldOverridePreProcessingDictionary() {
    Map<String, String> newNameDict = new HashMap<String, String>() {
      {
        put("Queen", "");
        put("Third", "");
        put("III", "");
      }
    };

    Function<String, String> newNamePreProcessing = (str) -> {
      return Arrays.stream(str.split("\\s+"))
          .map(d -> newNameDict.containsKey(d) ? newNameDict.get(d)
              : d)
          .collect(Collectors.joining(" "));
    };

    String[][] input = {
        { "1", "Victoria Third" },
        { "2", "Queen Victoria III" },
    };
    List<Document> documentList = Arrays.asList(input).stream().map(contact -> {
      return new Document.Builder(contact[0])
          .addElement(new Element.Builder<String>().setValue(contact[1]).setType(NAME)
              .setPreProcessingFunction(newNamePreProcessing)
              .createElement())
          .createDocument();
    }).collect(Collectors.toList());
    Map<Document, List<Match<Document>>> result = matchService.applyMatch(documentList);

    assertEquals(2, result.size());
  }

  @Test
  public void itShouldApplyMatchForBalancedEmptyElements() throws FileNotFoundException {
    List<Document> inputData = new ArrayList<>();
    inputData.add(new Document.Builder("1")
        .addElement(new Element.Builder().setType(NAME).setValue("James Parker").createElement())
        .addElement(new Element.Builder().setType(ADDRESS).setValue("").createElement())
        .addElement(new Element.Builder().setType(PHONE).setValue("").createElement())
        .addElement(new Element.Builder()
            .setType(EMAIL).setValue("jamesparker@email.com").createElement())
        .createDocument());
    inputData.add(new Document.Builder("2")
        .addElement(new Element.Builder().setType(NAME).setValue("James").createElement())
        .addElement(new Element.Builder().setType(ADDRESS).setValue("").createElement())
        .addElement(new Element.Builder().setType(PHONE).setValue("").createElement())
        .addElement(new Element.Builder()
            .setType(EMAIL).setValue("jamesparker@email.com").createElement())
        .createDocument());

    Map<Document, List<Match<Document>>> result = matchService.applyMatch(inputData);
    assertThat(result.entrySet().stream()
        .map(entry -> entry.getKey().getKey()).collect(Collectors.toList()),
        CoreMatchers.hasItems("1", "2"));
    assertEquals(0.75, result.entrySet().stream()
        .map(entry -> entry.getValue())
        .collect(Collectors.toList()).get(0).get(0).getResult(), 0.01);
  }

  @Test
  public void itShouldApplyMatchForUnBalancedEmptyElements() {
    List<Document> inputData = new ArrayList<>();
    inputData.add(new Document.Builder("1")
        .addElement(new Element.Builder().setType(NAME).setValue("James Parker").createElement())
        .addElement(new Element.Builder()
            .setType(ADDRESS).setValue("123 Some Street").createElement())
        .addElement(new Element.Builder().setType(PHONE).setValue("").createElement())
        .addElement(new Element.Builder()
            .setType(EMAIL).setValue("jamesparker@email.com").createElement())
        .createDocument());
    inputData.add(new Document.Builder("2")
        .addElement(new Element.Builder().setType(NAME).setValue("James").createElement())
        .addElement(new Element.Builder().setType(ADDRESS).setValue("").createElement())
        .addElement(new Element.Builder().setType(PHONE).setValue("123-123-1234").createElement())
        .addElement(new Element.Builder()
            .setType(EMAIL).setValue("jamesparker@email.com").createElement())
        .createDocument());

    Map<Document, List<Match<Document>>> result = matchService.applyMatch(inputData);
    assertThat(result.entrySet().stream()
        .map(entry -> entry.getKey().getKey()).collect(Collectors.toList()),
        CoreMatchers.hasItems("1", "2"));
    assertEquals(0.625, result.entrySet().stream()
        .map(entry -> entry.getValue())
        .collect(Collectors.toList()).get(0).get(0).getResult(), 0.01);
  }

  @Test
  public void itShouldApplyMatchWithVariance() {
    List<Document> inputData = new ArrayList<>();
    inputData.add(new Document.Builder("1")
        .addElement(new Element.Builder()
            .setType(NAME).setVariance("self").setValue("Tom Kelly").createElement())
        .createDocument());
    inputData.add(new Document.Builder("2")
        .addElement(new Element.Builder()
            .setType(NAME).setVariance("self").setValue("tom kelly").createElement())
        .createDocument());
    Map<Document, List<Match<Document>>> result = matchService.applyMatch(inputData);
    assertEquals(2, result.size());
    assertThat(result.entrySet().stream()
        .map(entry -> entry.getKey().getKey()).collect(Collectors.toList()),
        CoreMatchers.hasItems("1", "2"));
  }

  @Test
  public void itShouldApplyMatchWithDifferentVariance() {
    List<Document> inputData = new ArrayList<>();
    inputData.add(new Document.Builder("1")
        .addElement(new Element.Builder()
            .setType(NAME).setVariance("self").setValue("Tom Kelly").createElement())
        .createDocument());
    inputData.add(new Document.Builder("2")
        .addElement(new Element.Builder()
            .setType(NAME).setVariance("spouse").setValue("tom kelly").createElement())
        .createDocument());
    Map<Document, List<Match<Document>>> result = matchService.applyMatch(inputData);
    assertEquals(0, result.size());
  }

  @Test
  public void itShouldApplyMatchWithInteger() {
    List<Object> numbers = Arrays.asList(91, 100, 200, 152, 11, 15, 10, 200);
    List<Document> documentList1 = getTestDocuments(numbers, NUMBER, null);
    Map<Document, List<Match<Document>>> result1 = matchService.applyMatch(documentList1);
    assertEquals(6, result1.size());

    for (Map.Entry<Document, List<Match<Document>>> entry : result1.entrySet()) {
      int doc = (int) entry.getKey().getElements().iterator().next().getValue();
      List<Object> matches = entry.getValue().stream()
          .map(m -> m.getMatchedWith(

          ).getElements().iterator().next().getValue()).collect(Collectors.toList());
      for (Object matchWith : matches) {
        int match = (int) matchWith;
        int small = Math.min(doc, match);
        int big = Math.max(doc, match);
        assertTrue(small >= (int) 0.9 * big && small <= (int) 1.1 * big);
      }
    }

    List<Document> documentList2 = getTestDocuments(numbers, NUMBER, 0.99);
    Map<Document, List<Match<Document>>> result2 = matchService.applyMatch(documentList2);
    assertEquals(2, result2.size());
  }

  @Test
  public void itShouldApplyMatchWithDoubleType() {
    List<Object> numbers = Arrays.asList(23D, 22D, 10D, 5D, 9D, 11D, 10.5, 23.2);

    List<Document> documentList1 = getTestDocuments(numbers, NUMBER, null);
    Map<Document, List<Match<Document>>> result1 = matchService.applyMatch(documentList1);
    assertEquals(6, result1.size());

    List<Document> documentList2 = getTestDocuments(numbers, NUMBER, 0.99);
    Map<Document, List<Match<Document>>> result2 = matchService.applyMatch(documentList2);
    assertEquals(2, result2.size());
  }

  @Test
  public void itShouldApplyMatchWithDate() {
    List<Object> dates = Arrays.asList(
        getDate("01/01/2020"), getDate("12/01/2020"), getDate("02/01/2020"));
    List<Document> documentList = getTestDocuments(dates, DATE, null);
    Map<Document, List<Match<Document>>> result = matchService.applyMatch(documentList);
    assertEquals(2, result.size());
  }

  @Test
  public void itShouldApplyMatchWithDateForHighNeighborhoodRange() {
    List<Object> dates = Arrays.asList(
        getDate("01/01/2020"), getDate("01/02/2020"), getDate("02/01/2019"));
    List<Document> documentList = getTestDocuments(
        dates, DATE, 0.99); // 0.99 neighborhood is about 18 days
    Map<Document, List<Match<Document>>> result = matchService.applyMatch(documentList);
    assertEquals(2, result.size());
  }

  @Test
  public void itShouldApplyMatchWithAge() {
    List<Object> numbers = Arrays
        .asList(1, 2, 9, 10, 11, 45, 49, 50, 52, 55, 90, 95, 100, 107, 115);
    List<Document> documentList1 = getTestDocuments(numbers, AGE, null);
    Map<Document, List<Match<Document>>> result1 = matchService.applyMatch(documentList1);
    documentList1.get(0).getElements().iterator().next().getValue();
    Map<Object, List<Object>> collect = result1.entrySet().stream()
        .collect(Collectors.toMap(e -> e.getKey().getElements().iterator().next().getValue(),
            e -> e.getValue()
                .stream().map(m -> m.getMatchedWith().getElements().iterator().next().getValue())
                .collect(Collectors.toList())));
    assertEquals(7, result1.size());

    for (Map.Entry<Object, List<Object>> entry : collect.entrySet()) {
      int doc = (int) entry.getKey();
      for (Object matchWith : entry.getValue()) {
        int match = (int) matchWith;
        double diff = Math.abs(doc - match);
        assertTrue(diff <= 1);
      }
    }

    List<Document> documentList2 = getTestDocuments(numbers, AGE, 0.7);
    Map<Document, List<Match<Document>>> result2 = matchService.applyMatch(documentList2);
    Map<Object, List<Object>> collect2 = result2.entrySet().stream()
        .collect(Collectors.toMap(e -> e.getKey().getElements().iterator().next().getValue(),
            e -> e.getValue()
                .stream().map(m -> m.getMatchedWith().getElements().iterator().next().getValue())
                .collect(Collectors.toList())));
    assertEquals(9, result2.size());

    for (Map.Entry<Object, List<Object>> entry : collect2.entrySet()) {
      int doc = (int) entry.getKey();
      for (Object matchWith : entry.getValue()) {
        int match = (int) matchWith;
        double diff = Math.abs(doc - match);
        assertTrue(diff <= 3);
      }
    }

  }

  private Date getDate(String val) {
    DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
    try {
      return df.parse(val);
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Write output to a CSV file.
   */
  public static void writeOutput(Map<String, List<Match<Document>>> result) throws IOException {
    CSVWriter writer = new CSVWriter(new FileWriter("src/test/resources/output.csv"));
    writer.writeNext(
        new String[] { "Key", "Matched Key", "Score", "Name", "Address", "Email", "Phone" });

    result.entrySet().stream().sorted(Map.Entry.<String, List<Match<Document>>>comparingByKey())
        .forEach(entry -> {
          String[] keyArrs = Stream.concat(Stream.of(entry.getKey(), entry.getKey(), ""),
              getOrderedElements(entry.getValue().stream()
                  .map(match -> match.getData())
                  .findFirst().get()
                  .getElements()).map(e -> e.getValue()))
              .toArray(String[]::new);
          writer.writeNext(keyArrs);

          entry.getValue().stream().forEach(match -> {
            Document md = match.getMatchedWith();
            String[] matchArrs = Stream.concat(
                Stream.of("", md.getKey(), Double.toString(match.getResult())),
                getOrderedElements(md.getElements()).map(e -> e.getValue())).toArray(String[]::new);
            writer.writeNext(matchArrs);
            LOGGER.info("        " + match);
          });
        });
    writer.close();
  }

  /**
   * Write output to a CSV file.
   *
   * @param result Set of matches
   * @throws IOException if an I/O error occurs
   */
  public static void writeOutput(Set<Set<Match<Document>>> result) throws IOException {
    CSVWriter writer = new CSVWriter(new FileWriter("src/test/resources/output.csv"));
    writer.writeNext(
        new String[] { "Key", "Matched Key", "Score", "Name", "Address", "Email", "Phone" });

    result.forEach(matches -> {
      String[] arr = { "Group" };
      writer.writeNext(arr);

      matches.stream().forEach(match -> {
        Document md = match.getMatchedWith();
        String[] matchArrs = Stream.concat(
            Stream.of("", md.getKey(), Double.toString(match.getResult())),
            getOrderedElements(md.getElements()).map(e -> e.getValue())).toArray(String[]::new);
        writer.writeNext(matchArrs);
      });
    });
    writer.close();
  }

  private List<Document> getTestData(Function namePreProcessing,
      Function addressPreProcessing, double docThreshold) throws FileNotFoundException {
    return StreamSupport.stream(getCsvReader("test-data.csv").spliterator(), false).map(csv -> {
      return new Document.Builder(csv[0])
          .addElement(new Element.Builder().setType(NAME).setValue(csv[1])
              .setPreProcessingFunction(namePreProcessing)
              .createElement())
          .addElement(new Element.Builder().setType(ADDRESS).setValue(csv[2])
              .setPreProcessingFunction(addressPreProcessing)
              .createElement())
          .addElement(new Element.Builder().setType(PHONE).setValue(csv[3]).createElement())
          .addElement(new Element.Builder().setType(EMAIL).setValue(csv[4]).createElement())
          .setThreshold(docThreshold)
          .createDocument();
    }).collect(Collectors.toList());
  }

  @Test
  public void itShouldApplyMatchForMultiplePaths() {
    List<Document> inputData = new ArrayList<>();
    inputData.add(new Document.Builder("1")
        .addElement(new Element.Builder().setType(ElementType.PATH)
            .setValue("/usr/local/bin").createElement())
        .addElement(new Element.Builder().setType(ElementType.PATH)
            .setValue("/usr/bin").createElement())
        .createDocument());
    inputData.add(new Document.Builder("2")
        .addElement(new Element.Builder().setType(ElementType.PATH)
            .setValue("/usr/local/share").createElement())
        .addElement(new Element.Builder().setType(ElementType.PATH)
            .setValue("/usr/bin").createElement())
        .createDocument());
    Map<Document, List<Match<Document>>> result = matchService.applyMatch(inputData);
    assertEquals(2, result.size());
    assertThat(result.entrySet().stream()
        .map(entry -> entry.getKey().getKey()).collect(Collectors.toList()),
        CoreMatchers.hasItems("1", "2"));
  }

  @Test
  public void itShouldApplyMatchForSimilarPaths() {
    List<Document> inputData = new ArrayList<>();
    inputData.add(new Document.Builder("1")
        .addElement(new Element.Builder().setType(ElementType.PATH)
            .setValue("/usr/local/bin").createElement())
        .addElement(new Element.Builder().setType(ElementType.PATH)
            .setValue("/usr/bin").createElement())
        .createDocument());
    inputData.add(new Document.Builder("2")
        .addElement(new Element.Builder().setType(ElementType.PATH)
            .setValue("/usr/local/binn").createElement())
        .addElement(new Element.Builder().setType(ElementType.PATH)
            .setValue("/usr/bin").createElement())
        .createDocument());
    Map<Document, List<Match<Document>>> result = matchService.applyMatch(inputData);
    assertEquals(2, result.size());
    assertThat(result.entrySet().stream()
        .map(entry -> entry.getKey().getKey()).collect(Collectors.toList()),
        CoreMatchers.hasItems("1", "2"));
  }
}

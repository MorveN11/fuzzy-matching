package fuzzy.matching.types;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import fuzzy.matching.component.MatchService;
import fuzzy.matching.domain.Document;
import fuzzy.matching.domain.Element;
import fuzzy.matching.domain.ElementType;
import fuzzy.matching.domain.Match;
import fuzzy.matching.domain.MatchType;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

/**
 * Test class for IDMatchType.
 */
@SuppressWarnings("unchecked")
public class IdMatchTypeTest {
  @Test
  public void testExactMatch() {
    MatchService matchService = new MatchService();
    Document doc1 = new Document.Builder("doc1")
        .addElement(new Element.Builder<String>()
            .setType(ElementType.ID)
            .setValue("1234SDF13212356")
            .setMatchType(MatchType.EQUALITY_DISTANCE)
            .createElement())
        .createDocument();

    Document doc2 = new Document.Builder("doc2")
        .addElement(new Element.Builder<String>()
            .setType(ElementType.ID)
            .setValue("1234SDF13212356")
            .setMatchType(MatchType.EQUALITY_DISTANCE)
            .createElement())
        .createDocument();

    Map<String, List<Match<Document>>> matches = matchService
        .applyMatchByDocId(Arrays.asList(doc1, doc2));

    assertEquals(2, matches.size());
    assertTrue(matches.containsKey("doc1"));
    assertEquals(1, matches.get("doc1").size());
    assertEquals("doc2", matches.get("doc1").getFirst().getMatchedWith().getKey());

    boolean matched = false;
    for (List<Match<Document>> matchList : matches.values()) {
      for (Match<Document> match : matchList) {
        if (match.getData().getKey().equals("doc1")
            && match.getMatchedWith().getKey().equals("doc2")) {
          assertEquals(1.0, match.getScore().getResult(), 0.0);
          matched = true;
          break;
        }
      }
    }
    assertTrue(matched);
  }

  @Test
  public void testMatchWithEditDistance() {
    MatchService matchService = new MatchService();
    Document doc1 = new Document.Builder("doc1")
        .addElement(new Element.Builder<String>()
            .setType(ElementType.ID)
            .setValue("1234SDF13212356")
            .setMatchType(MatchType.EQUALITY_DISTANCE)
            .createElement())
        .createDocument();

    Document doc2 = new Document.Builder("doc2")
        .addElement(new Element.Builder<String>()
            .setType(ElementType.ID)
            .setValue("121234SDF13212356ASD3457")
            .setMatchType(MatchType.EQUALITY_DISTANCE)
            .createElement())
        .createDocument();

    Map<String, List<Match<Document>>> matches = matchService
        .applyMatchByDocId(Arrays.asList(doc1, doc2));

    assertEquals(2, matches.size());
    assertTrue(matches.containsKey("doc1"));
    assertEquals(1, matches.get("doc1").size());
    assertEquals("doc2", matches.get("doc1").getFirst().getMatchedWith().getKey());

    boolean matched = false;
    for (List<Match<Document>> matchList : matches.values()) {
      for (Match<Document> match : matchList) {
        if (match.getData().getKey().equals("doc1")
            && match.getMatchedWith().getKey().equals("doc2")) {
          assertEquals(0.65, match.getScore().getResult(), 0.0);
          matched = true;
          break;
        }
      }
    }
    assertTrue(matched);
  }

  @Test
  public void testNoMatch() {
    MatchService matchService = new MatchService();
    Document doc1 = new Document.Builder("doc1")
        .addElement(new Element.Builder<String>()
            .setType(ElementType.ID)
            .setValue("123456")
            .setMatchType(MatchType.EQUALITY_DISTANCE)
            .createElement())
        .createDocument();

    Document doc2 = new Document.Builder("doc2")
        .addElement(new Element.Builder<String>()
            .setType(ElementType.ID)
            .setValue("abcdef")
            .setMatchType(MatchType.EQUALITY_DISTANCE)
            .createElement())
        .createDocument();

    Map<String, List<Match<Document>>> matches = matchService
        .applyMatchByDocId(Arrays.asList(doc1, doc2));

    assertEquals(0, matches.size());
  }
}

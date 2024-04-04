package fuzzy.matching.newElementTypes;
import fuzzy.matching.component.MatchService;
import fuzzy.matching.domain.*;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PriceMatchTypeTest {
    @Test
    public void testExactMatch() {
        MatchService matchService = new MatchService();
        Document doc1 = new Document.Builder("doc1")
                .addElement(new Element.Builder<String>()
                        .setType(ElementType.ID)
                        .setValue("100 BOL")
                        .setMatchType(MatchType.EQUALITY_DISTANCE)
                        .createElement())
                .createDocument();

        Document doc2 = new Document.Builder("doc2")
                .addElement(new Element.Builder<String>()
                        .setType(ElementType.ID)
                        .setValue("100 BOL")
                        .setMatchType(MatchType.EQUALITY_DISTANCE)
                        .createElement())
                .createDocument();

        Map<String, List<Match<Document>>> matches = matchService.applyMatchByDocId(Arrays.asList(doc1, doc2));

        assertEquals(2, matches.size());
        assertTrue(matches.containsKey("doc1"));
        assertEquals(1, matches.get("doc1").size());
        assertEquals("doc2", matches.get("doc1").getFirst().getMatchedWith().getKey());

        boolean matched = false;
        for (List<Match<Document>> matchList : matches.values()) {
            for (Match<Document> match : matchList) {
                if (match.getData().getKey().equals("doc1") && match.getMatchedWith().getKey().equals("doc2")) {
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
                        .setType(ElementType.PRICE)
                        .setValue("500 USD")
                        .setMatchType(MatchType.EQUALITY_DISTANCE)
                        .createElement())
                .createDocument();

        Document doc2 = new Document.Builder("doc2")
                .addElement(new Element.Builder<String>()
                        .setType(ElementType.PRICE)
                        .setValue("500 JPN")
                        .setMatchType(MatchType.EQUALITY_DISTANCE)
                        .createElement())
                .createDocument();

        Map<String, List<Match<Document>>> matches = matchService.applyMatchByDocId(Arrays.asList(doc1, doc2));

        assertEquals(2, matches.size());
        assertTrue(matches.containsKey("doc1"));
        assertEquals(1, matches.get("doc1").size());
        assertEquals("doc2", matches.get("doc1").getFirst().getMatchedWith().getKey());

        boolean matched = false;
        for (List<Match<Document>> matchList : matches.values()) {
            for (Match<Document> match : matchList) {
                if (match.getData().getKey().equals("doc1") && match.getMatchedWith().getKey().equals("doc2")) {
                    assertEquals(0.6, match.getScore().getResult(), 0.0);
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
                        .setType(ElementType.PRICE)
                        .setValue("890 JPN")
                        .setMatchType(MatchType.EQUALITY_DISTANCE)
                        .createElement())
                .createDocument();

        Document doc2 = new Document.Builder("doc2")
                .addElement(new Element.Builder<String>()
                        .setType(ElementType.PRICE)
                        .setValue("132 BOL")
                        .setMatchType(MatchType.EQUALITY_DISTANCE)
                        .createElement())
                .createDocument();

        Map<String, List<Match<Document>>> matches = matchService.applyMatchByDocId(Arrays.asList(doc1, doc2));

        assertEquals(0, matches.size());
    }
}

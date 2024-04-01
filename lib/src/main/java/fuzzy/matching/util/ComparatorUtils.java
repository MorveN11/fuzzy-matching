package fuzzy.matching.util;

import fuzzy.matching.component.MatchService;
import fuzzy.matching.domain.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ComparatorUtils {
    private static final MatchService matchService = new MatchService();

    public static List<Document> convertToDocumentList(String name, ElementType type) {
        String[][] arr = {{"1", name}};
        return convertToDocumentList(arr, type);
    }

    public static List<Document> convertToDocumentList(String[][] input, ElementType type) {
        List<Document> documentList = new ArrayList<>();
        for (String[] row : input) {
            String id = row[0];
            String value = row[1];
            Document document = new Document.Builder(id)
                    .addElement(new Element.Builder<String>().setValue(value).setType(type).createElement())
                    .createDocument();
            documentList.add(document);
        }
        return documentList;
    }


    public static Double calculateFuzzySimilarity(List<Document> doc1, List<Document> doc2) {
        List<Match<Document>> matches = new ArrayList<>();
        matchService.applyMatchByDocId(doc1, doc2).forEach((key, value) -> matches.addAll(value));

        double totalScore = 0.0;
        for (Match<Document> match : matches) {
            totalScore += match.getScore().getResult();
        }

        return matches.isEmpty() ? 0.0 : totalScore / matches.size();
    }

}

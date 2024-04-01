package fuzzy.matching.controller;

import fuzzy.matching.domain.Document;
import fuzzy.matching.domain.ElementType;
import fuzzy.matching.model.User;
import fuzzy.matching.util.ComparatorUtils;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class UserComparator {
    public Double compareUser(User user1, User user2) {
        double nameSimilarity = calculateNameSimilarity(user1.getName(), user2.getName());
        double idSimilarity = calculateIDSimilarity(user1.getId(), user2.getId());
        double genderSimilarity = calculateGenderSimilarity(user1.getGender(), user2.getGender());
        double ageSimilarity = calculateAgeSimilarity(String.valueOf(user1.getAge()), String.valueOf(user2.getAge()));
        return (nameSimilarity * 0.4) + (idSimilarity * 0.3) + (genderSimilarity * 0.2) + (ageSimilarity * 0.1);
    }

    public Double calculateNameSimilarity(String name1, String name2) {
        List<Document> doc1 = ComparatorUtils.convertToDocumentList(name1, ElementType.NAME);
        List<Document> doc2 = ComparatorUtils.convertToDocumentList(name2, ElementType.NAME);
        return ComparatorUtils.calculateFuzzySimilarity(doc1, doc2);
    }
    public Double calculateIDSimilarity(String id1, String id2) {
        List<Document> doc1 = ComparatorUtils.convertToDocumentList(id1, ElementType.ID);
        List<Document> doc2 = ComparatorUtils.convertToDocumentList(id2, ElementType.ID);
        return ComparatorUtils.calculateFuzzySimilarity(doc1, doc2);
    }

    public Double calculateGenderSimilarity(String gender1, String gender2) {
        List<Document> doc1 = ComparatorUtils.convertToDocumentList(gender1, ElementType.TEXT);
        List<Document> doc2 = ComparatorUtils.convertToDocumentList(gender2, ElementType.TEXT);
        return ComparatorUtils.calculateFuzzySimilarity(doc1, doc2);
    }
    public Double calculateAgeSimilarity(String age1, String age2) {
        List<Document> doc1 = ComparatorUtils.convertToDocumentList(age1, ElementType.TEXT);
        List<Document> doc2 = ComparatorUtils.convertToDocumentList(age2, ElementType.TEXT);
        return ComparatorUtils.calculateFuzzySimilarity(doc1, doc2);
    }
}

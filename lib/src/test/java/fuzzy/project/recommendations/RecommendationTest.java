package fuzzy.project.recommendations;

import static org.junit.jupiter.api.Assertions.assertEquals;

import fuzzy.project.Recommendations;
import fuzzy.project.data.Data;
import fuzzy.project.model.Book;
import fuzzy.project.model.User;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;

class RecommendationTest {

    @Test
    void itShouldTestRecommendation1() {
        Data data = new Data();
        List<User> users = data.getUsers();
        User user = users.get(104);
        List<User> similarUsers = users.stream().filter(u -> !u.getId().equals(user.getId())).toList();
        Recommendations checkSimilar = new Recommendations(user, similarUsers);
        int amountRecommendations = 29;
        Set<Book> recommendations = checkSimilar.getRecommendations();
        assertEquals(amountRecommendations, recommendations.size());
        System.out.println(user);
        System.out.println(recommendations);
    }

    @Test
    void itShouldTestRecommendation2() {
        Data data = new Data();
        List<User> users = data.getUsers();
        User user = users.get(23);
        List<User> similarUsers = users.stream().filter(u -> !u.getId().equals(user.getId())).toList();
        Recommendations checkSimilar = new Recommendations(user, similarUsers);
        int amountRecommendations = 55;
        Set<Book> recommendations = checkSimilar.getRecommendations();
        assertEquals(amountRecommendations, recommendations.size());
        System.out.println(user);
        System.out.println(recommendations);
    }

    @Test
    void itShouldTestRecommendation3() {
        Data data = new Data();
        List<User> users = data.getUsers();
        User user = users.get(268);
        List<User> similarUsers = users.stream().filter(u -> !u.getId().equals(user.getId())).toList();
        Recommendations checkSimilar = new Recommendations(user, similarUsers);
        int amountRecommendations = 82;
        Set<Book> recommendations = checkSimilar.getRecommendations();
        assertEquals(amountRecommendations, recommendations.size());
        System.out.println(user);
        System.out.println(recommendations);
    }

    @Test
    void itShouldTestRecommendation4() {
        Data data = new Data();
        List<User> users = data.getUsers();
        User user = users.get(85);
        List<User> similarUsers = users.stream().filter(u -> !u.getId().equals(user.getId())).toList();
        Recommendations checkSimilar = new Recommendations(user, similarUsers);
        int amountRecommendations = 56;
        Set<Book> recommendations = checkSimilar.getRecommendations();
        assertEquals(amountRecommendations, recommendations.size());
        System.out.println(user);
        System.out.println(recommendations);

    }

    @Test
    void itShouldTestRecommendation5() {
        Data data = new Data();
        List<User> users = data.getUsers();
        User user = users.get(198);
        List<User> similarUsers = users.stream().filter(u -> !u.getId().equals(user.getId())).toList();
        Recommendations checkSimilar = new Recommendations(user, similarUsers);
        int amountRecommendations = 60;
        Set<Book> recommendations = checkSimilar.getRecommendations();
        assertEquals(amountRecommendations, recommendations.size());
        System.out.println(user);
        System.out.println(recommendations);
    }
}

package fuzzy.project.recommendations;

import static org.junit.jupiter.api.Assertions.assertEquals;

import fuzzy.project.Recommendations;
import fuzzy.project.data.Data;
import fuzzy.project.model.User;
import java.util.List;
import org.junit.jupiter.api.Test;

class RecommendationTest {

  @Test
  void itShouldTestRecommendation() {
    Data data = new Data();
    List<User> users = data.getUsers();
    User user = users.get(104);
    assertEquals(
        """
            ID: a97b7d14-b47e-4941-a0f7-c8b5f1503cec
            Name: Elena Baker Ramirez
            Age: 89
            Gender: female
            FavoriteGenres: [Drama, Satire]
            BooksId: [d92c96b4-8650-4744-83de-13c7888c582b, f666eb9a-2264-47f4-9375-1d5f27f93a65, 11a6d8bb-5e8b-4bb2-8239-760e9b07603d, f666eb9a-2264-47f4-9375-1d5f27f93a65, 3d4bfe2b-383a-4b02-9314-2cf2dab3dc84, 84b83354-c00b-4605-8a6e-3927a31d2d05, 6f2ef5d4-3e0d-4c1b-b8a3-fbdc0518b556, 84b83354-c00b-4605-8a6e-3927a31d2d05, 8d8a7688-4fd1-43af-b611-f60dd9fc5f1b]
            """,
        user.toString());
    List<User> similarUsers = users.stream().filter(u -> !u.getId().equals(user.getId())).toList();
    Recommendations checkSimilar = new Recommendations(user, similarUsers);
    int amountRecommendations = 29;
    assertEquals(amountRecommendations, checkSimilar.getRecommendations().size());
    System.out.println(user);
    System.out.println(checkSimilar.getRecommendations());
  }
}

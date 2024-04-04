package fuzzy.project.recommendations;

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

    System.out.println(user);

    List<User> similarUsers = users.stream().filter(u -> !u.getId().equals(user.getId())).toList();

    Recommendations checkSimilar = new Recommendations(user, similarUsers);

    System.out.println(checkSimilar.getRecommendations());
  }
}

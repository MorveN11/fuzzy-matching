package fuzzy.matching.similar;

import fuzzy.project.data.Data;
import fuzzy.project.model.CheckSimilar;
import fuzzy.project.model.User;
import java.util.List;
import org.junit.jupiter.api.Test;

class SimilarTest {

  @Test
  void itShouldTestSimilar() {
    Data data = new Data();

    List<User> users = data.getUsers();

    User user = users.get(206);

    System.out.println(user);

    List<User> similarUsers = users.stream().filter(u -> !u.getId().equals(user.getId())).toList();

    CheckSimilar checkSimilar = new CheckSimilar(user, similarUsers);

    System.out.println(checkSimilar.getUsers());
  }
}

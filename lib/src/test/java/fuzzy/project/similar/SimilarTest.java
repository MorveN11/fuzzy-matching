package fuzzy.project.similar;

import static org.junit.jupiter.api.Assertions.assertEquals;

import fuzzy.project.CheckSimilar;
import fuzzy.project.data.Data;
import fuzzy.project.model.SimilarUser;
import fuzzy.project.model.User;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;

class SimilarTest {

  @Test
  void itShouldTestSimilar1() {
    Data data = new Data();

    List<User> users = data.getUsers();

    User user = users.get(53);

    List<User> similarUsers = users.stream().filter(u -> !u.getId().equals(user.getId())).toList();

    CheckSimilar checkSimilar = new CheckSimilar(user, similarUsers);

    Set<SimilarUser> possibleDuplicateUsers = checkSimilar.getUsers();
    int amountSimilarUsers = 5;
    assertEquals(amountSimilarUsers, possibleDuplicateUsers.size());
    System.out.println(user);
    System.out.println(possibleDuplicateUsers);
  }

  @Test
  void itShouldTestSimilar2() {
    Data data = new Data();

    List<User> users = data.getUsers();

    User user = users.get(104);

    List<User> similarUsers = users.stream().filter(u -> !u.getId().equals(user.getId())).toList();

    CheckSimilar checkSimilar = new CheckSimilar(user, similarUsers);

    Set<SimilarUser> possibleDuplicateUsers = checkSimilar.getUsers();
    int amountSimilarUsers = 5;
    assertEquals(amountSimilarUsers, possibleDuplicateUsers.size());
    System.out.println(user);
    System.out.println(possibleDuplicateUsers);
  }

  @Test
  void itShouldTestSimilar3() {
    Data data = new Data();

    List<User> users = data.getUsers();

    User user = users.get(209);

    List<User> similarUsers = users.stream().filter(u -> !u.getId().equals(user.getId())).toList();

    CheckSimilar checkSimilar = new CheckSimilar(user, similarUsers);

    Set<SimilarUser> possibleDuplicateUsers = checkSimilar.getUsers();
    int amountSimilarUsers = 5;
    assertEquals(amountSimilarUsers, possibleDuplicateUsers.size());
    System.out.println(user);
    System.out.println(possibleDuplicateUsers);
  }

  @Test
  void itShouldTestSimilar4() {
    Data data = new Data();

    List<User> users = data.getUsers();

    User user = users.get(298);

    List<User> similarUsers = users.stream().filter(u -> !u.getId().equals(user.getId())).toList();

    CheckSimilar checkSimilar = new CheckSimilar(user, similarUsers);

    Set<SimilarUser> possibleDuplicateUsers = checkSimilar.getUsers();
    int amountSimilarUsers = 5;
    assertEquals(amountSimilarUsers, possibleDuplicateUsers.size());
    System.out.println(user);
    System.out.println(possibleDuplicateUsers);
  }

  @Test
  void itShouldTestSimilar5() {
    Data data = new Data();

    List<User> users = data.getUsers();

    User user = users.get(134);

    List<User> similarUsers = users.stream().filter(u -> !u.getId().equals(user.getId())).toList();

    CheckSimilar checkSimilar = new CheckSimilar(user, similarUsers);

    Set<SimilarUser> possibleDuplicateUsers = checkSimilar.getUsers();
    int amountSimilarUsers = 5;
    assertEquals(amountSimilarUsers, possibleDuplicateUsers.size());
    System.out.println(user);
    System.out.println(possibleDuplicateUsers);
  }
}

package fuzzy.project.data;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

import fuzzy.project.model.Book;
import fuzzy.project.model.User;
import java.util.List;
import org.junit.jupiter.api.Test;

class DataTest {

  @Test
  void itShouldRetrieveAllJson() {
    Data data = new Data();

    List<User> users = data.getUsers();
    List<Book> books = data.getBooks();
    System.out.println(users.get(0));
    assertNotEquals(0, users.size());
    assertNotEquals(0, books.size());
  }
}

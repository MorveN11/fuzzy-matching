package fuzzy.matching.data;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

import fuzzy.project.data.Data;
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
    assertNotEquals(0, users.size());
    assertNotEquals(0, books.size());
  }
}

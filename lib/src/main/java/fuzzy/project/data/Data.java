package fuzzy.project.data;

import fuzzy.project.model.Book;
import fuzzy.project.model.User;
import fuzzy.project.reader.JsonReader;
import java.util.List;

/**
 * The Data class provides methods to retrieve books and users from JSON files.
 */
public class Data {
  /**
   * Retrieves a list of books from a JSON file.
   *
   * @return The list of books.
   */
  public List<Book> getBooks() {
    JsonReader<Book> jsonReader = new JsonReader<>();
    List<Book> books = jsonReader.parseJsonToList("data/book.json", Book.class);
    return books;
  }

  /**
   * Retrieves a list of users from a JSON file and populates their book lists.
   *
   * @return The list of users.
   */
  public List<User> getUsers() {
    JsonReader<User> jsonReader = new JsonReader<>();
    List<User> users = jsonReader.parseJsonToList("data/users.json", User.class);
    List<Book> books = getBooks();
    for (User user : users) {
      for (String id : user.getBookListIds()) {
        Book book = getBookById(books, id);
        user.addBook(book);
      }
    }
    return users;
  }

  /**
   * Retrieves a book from a list of books based on its ID.
   *
   * @param books The list of books.
   * @param id    The ID of the book to retrieve.
   * @return The book with the specified ID, or null if not found.
   */
  private Book getBookById(List<Book> books, String id) {
    for (Book book : books) {
      if (book.getId().equals(id)) {
        return book;
      }
    }
    return null;
  }
}

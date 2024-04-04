package fuzzy.project.doc;

import fuzzy.matching.domain.Document;
import fuzzy.matching.domain.Element;
import fuzzy.project.model.Book;
import java.util.List;
import java.util.Set;

/**
 * Represents a user document.
 */
@SuppressWarnings("rawtypes")
public class UserDocument extends Document {

  private final List<String> idBooks;
  private final List<Book> books;
  private final String name;
  private final int age;
  private final String gender;

  private UserDocument(String key, Set<Element> elements, double threshold, List<String> idBooks,
      List<Book> books, String name, int age, String gender) {
    super(key, elements, threshold);
    this.idBooks = idBooks;
    this.books = books;
    this.name = name;
    this.age = age;
    this.gender = gender;
  }

  public List<String> getIdBooks() {
    return idBooks;
  }

  public List<Book> getBooks() {
    return books;
  }

  public String getName() {
    return name;
  }

  public int getAge() {
    return age;
  }

  public String getGender() {
    return gender;
  }

  /**
   * Builder for UserDocument.
   */
  public static class Builder extends Document.Builder {

    private List<String> idBooks;
    private List<Book> books;
    private String name;
    private int age;
    private String gender;

    public Builder(String id) {
      super(id);
    }

    public Builder setIdBooks(List<String> idBooks) {
      this.idBooks = idBooks;
      return this;
    }

    public Builder setBooks(List<Book> books) {
      this.books = books;
      return this;
    }

    public Builder setName(String name) {
      this.name = name;
      return this;
    }

    public Builder setAge(int age) {
      this.age = age;
      return this;
    }

    public Builder setGender(String gender) {
      this.gender = gender;
      return this;
    }

    /**
     * <p>
     * Add an Element to the Document.
     * </p>
     */
    public Builder addElement(Element element) {
      return (Builder) super.addElement(element);
    }

    /**
     * <p>
     * Add a list of Element to the Document.
     * </p>
     *
     * @return Builder
     */
    public UserDocument createUserDocument() {
      UserDocument doc = new UserDocument(key, elements, threshold, idBooks,
          books, name, age, gender);
      doc.getElements().stream().forEach(element -> element.setDocument(doc));
      return doc;
    }
  }
}

package fuzzy.project.model;

import static fuzzy.matching.domain.ElementType.AGE;
import static fuzzy.matching.domain.ElementType.GENRE;
import static fuzzy.matching.domain.ElementType.NAME;
import static fuzzy.matching.domain.ElementType.NUMBER;
import static fuzzy.matching.domain.ElementType.TEXT;

import com.google.gson.annotations.SerializedName;
import fuzzy.matching.domain.Document;
import fuzzy.matching.domain.Element;
import fuzzy.project.domain.UserDocument;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a user.
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class User implements Comparable<User> {

  @SerializedName("ID")
  private String id;

  @SerializedName("Name")
  private String name;

  @SerializedName("Age")
  private int age;

  @SerializedName("Gender")
  private String gender;

  private List<Book> bookList;

  @SerializedName("BookList")
  private List<String> idBookList;

  @SerializedName("FavoriteGenres")
  private List<String> favoriteGenres;

  /**
   * Constructs a new User object with the specified id, name, age, gender, and
   * book list.
   *
   * @param id       the user's id
   * @param name     the user's name
   * @param age      the user's age
   * @param gender   the user's gender
   * @param bookList the user's book list
   */
  public User(String id, String name, int age, String gender,
      List<Book> bookList, List<String> favoriteGenres) {
    this.id = id;
    this.name = name;
    this.age = age;
    this.gender = gender;
    this.bookList = bookList;
    idBookList = new ArrayList<>();
    bookList = new ArrayList<>();
    this.favoriteGenres = favoriteGenres;
  }

  /**
   * Returns the user's id.
   *
   * @return the user's id
   */
  public String getId() {
    return id;
  }

  /**
   * Sets the user's id.
   *
   * @param id the user's id
   */
  public void setId(String id) {
    this.id = id;
  }

  /**
   * Returns the user's name.
   *
   * @return the user's name
   */
  public String getName() {
    return name;
  }

  /**
   * Sets the user's name.
   *
   * @param name the user's name
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Returns the user's age.
   *
   * @return the user's age
   */
  public int getAge() {
    return age;
  }

  /**
   * Sets the user's age.
   *
   * @param age the user's age
   */
  public void setAge(int age) {
    this.age = age;
  }

  /**
   * Returns the user's gender.
   *
   * @return the user's gender
   */
  public String getGender() {
    return gender;
  }

  /**
   * Sets the user's gender.
   *
   * @param gender the user's gender
   */
  public void setGender(String gender) {
    this.gender = gender;
  }

  /**
   * Returns the user's book list.
   *
   * @return the user's book list
   */
  public List<Book> getBookList() {
    return bookList;
  }

  /**
   * Sets the user's book list.
   *
   * @param bookList the user's book list
   */
  public void setBookList(List<Book> bookList) {
    this.bookList = bookList;
  }

  /**
   * Returns the user's book list ids.
   *
   * @return the user's book list ids
   */
  public List<String> getBookListIds() {
    return idBookList;
  }

  /**
   * Sets the user's book list ids.
   *
   * @param idBookList the user's book list ids
   */
  public void setBookListIds(List<String> idBookList) {
    this.idBookList = idBookList;
  }

  /**
   * Adds a book to the user's book list.
   *
   * @param book the book to add
   */
  public void addBook(Book book) {
    if (bookList == null) {
      bookList = new ArrayList<>();
    }
    bookList.add(book);
  }

  /**
   * Returns the user's favorite genres.
   *
   * @return the user's favorite genres
   */
  public List<String> getFavoriteGenres() {
    return favoriteGenres;
  }

  /**
   * Sets the user's favorite genres.
   *
   * @param favoriteGenres the user's favorite genres
   */
  public void setFavoriteGenres(List<String> favoriteGenres) {
    this.favoriteGenres = favoriteGenres;
  }

  /**
   * Converts the User to a Document.
   *
   * @return Document
   */
  public Document toUserDocument() {
    return new UserDocument.Builder(this.id)
        .setBooks(this.bookList)
        .setIdBooks(this.idBookList)
        .setName(this.name)
        .setAge(this.age)
        .setGender(gender)
        .setFavoriteGenres(favoriteGenres)
        .addElement(new Element.Builder().setType(NAME).setValue(this.name).createElement())
        .addElement(new Element.Builder().setType(AGE).setValue(this.age).createElement())
        .addElement(new Element.Builder().setType(TEXT).setValue(this.gender).createElement())
        .addElement(new Element.Builder().setType(NUMBER).setValue(this.idBookList.size())
            .createElement())
        .createUserDocument();
  }

  /**
   * Converts the User to a Document for recommendations.
   *
   * @return Document
   */
  public Document toUserRecommendations() {
    return new UserDocument.Builder(this.id)
        .setBooks(this.bookList)
        .setIdBooks(this.idBookList)
        .setName(this.name)
        .setAge(this.age)
        .setGender(gender)
        .setFavoriteGenres(favoriteGenres)
        .addElement(new Element.Builder().setType(GENRE)
            .setValue(this.favoriteGenres).createElement())
        .addElement(new Element.Builder().setType(NUMBER).setValue(this.idBookList.size())
            .createElement())
        .createUserDocument();
  }

  /**
   * Builder class for User.
   */
  public static class Builder {

    public Builder() {
    }

    public User build(UserDocument elements) {
      return elementsToUser(elements);

    }

    /**
     * Converts a Set of Elements to a User.
     *
     * @param document Set of Elements
     * @return User
     */
    public User elementsToUser(UserDocument document) {
      String id = document.getKey();
      String name = document.getName();
      int age = document.getAge();
      String gender = document.getGender();
      List<String> idBookList = document.getIdBooks();
      List<Book> bookList = document.getBooks();
      List<String> favoriteGenres = document.getFavoriteGenres();
      User user = new User(id, name, age, gender, bookList, favoriteGenres);
      user.setBookListIds(idBookList);

      return user;
    }
  }

  @Override
  public int compareTo(User o) {
    if (!this.name.equals(o.getName())) {
      return this.name.compareTo(o.getName());
    }

    if (this.age != o.getAge()) {
      return Integer.compare(this.age, o.getAge());
    }

    if (this.gender != o.getGender()) {
      return this.gender.compareTo(o.getGender());
    }

    if (this.id != o.getId()) {
      return this.id.compareTo(o.getId());
    }

    return 0;
  }

  @Override
  public String toString() {
    return """
        ID: %s
        Name: %s
        Age: %s
        Gender: %s
        FavoriteGenres: %s
        BooksId: %s
        """.formatted(this.id, this.name, this.age, this.gender,
        this.favoriteGenres, this.idBookList);
  }
}

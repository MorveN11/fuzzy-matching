package fuzzy.project.model;

import com.google.gson.annotations.SerializedName;
import java.util.Date;

/**
 * Represents a book.
 */
public class Book {

  @SerializedName("ID")
  private String id;

  @SerializedName("Name")
  private String name;

  @SerializedName("Summary")
  private String summary;

  @SerializedName("Author")
  private String author;

  @SerializedName("Publication Date")
  private Date publicationDate;

  @SerializedName("Price")
  private String price;

  @SerializedName("Ratings")
  private String rating;

  /**
   * Constructs a new Book object with the specified properties.
   *
   * @param id              The unique identifier of the book.
   * @param name            The name of the book.
   * @param summary         A brief summary of the book.
   * @param author          The author of the book.
   * @param publicationDate The publication date of the book.
   * @param rating          The rating of the book.
   */
  public Book(String id, String name, String summary,
      String author, Date publicationDate, String rating) {
    this.id = id;
    this.name = name;
    this.summary = summary;
    this.author = author;
    this.publicationDate = publicationDate;
    this.rating = rating;
  }

  /**
   * Gets the unique identifier of the book.
   *
   * @return The unique identifier of the book.
   */
  public String getId() {
    return id;
  }

  /**
   * Sets the unique identifier of the book.
   *
   * @param id The unique identifier of the book.
   */
  public void setId(String id) {
    this.id = id;
  }

  /**
   * Gets the name of the book.
   *
   * @return The name of the book.
   */
  public String getName() {
    return name;
  }

  /**
   * Sets the name of the book.
   *
   * @param name The name of the book.
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Gets a brief summary of the book.
   *
   * @return A brief summary of the book.
   */
  public String getSummary() {
    return summary;
  }

  /**
   * Sets a brief summary of the book.
   *
   * @param summary A brief summary of the book.
   */
  public void setSummary(String summary) {
    this.summary = summary;
  }

  /**
   * Gets the author of the book.
   *
   * @return The author of the book.
   */
  public String getAuthor() {
    return author;
  }

  /**
   * Sets the author of the book.
   *
   * @param author The author of the book.
   */
  public void setAuthor(String author) {
    this.author = author;
  }

  /**
   * Gets the publication date of the book.
   *
   * @return The publication date of the book.
   */
  public Date getPublicationDate() {
    return publicationDate;
  }

  /**
   * Sets the publication date of the book.
   *
   * @param publicationDate The publication date of the book.
   */
  public void setPublicationDate(Date publicationDate) {
    this.publicationDate = publicationDate;
  }

  /**
   * Gets the price of the book.
   *
   * @return The price of the book.
   */
  public String getPrice() {
    return price;
  }

  /**
   * Sets the price of the book.
   *
   * @param price The price of the book.
   */
  public void setPrice(String price) {
    this.price = price;
  }

  /**
   * Gets the rating of the book.
   *
   * @return The rating of the book.
   */
  public String getRating() {
    return rating;
  }

  /**
   * Sets the rating of the book.
   *
   * @param rating The rating of the book.
   */
  public void setRating(String rating) {
    this.rating = rating;
  }

  @Override
  public String toString() {
    return """
        ---- Book ----
        ID: %s
        Name: %s
        Summary: %s
        Author: %s
        Publication Date: %s
        Rating: %s
        --------------""".formatted(id, name, summary, author, publicationDate, rating);
  }
}

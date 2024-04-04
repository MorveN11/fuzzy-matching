package fuzzy.matching.model;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a user.
 */
public class User {

    @SerializedName("ID")
    private String id;

    @SerializedName("Name")
    private String name;

    @SerializedName("Age")
    private int age;

    @SerializedName("Gender")
    private String gender;

    private ArrayList<Book> bookList;

    @SerializedName("BookList")
    private List<String> idBookList;

    @SerializedName("FavoriteGenres")
    private List<String> favoriteGenres;

    /**
     * Constructs a new User object with the specified id, name, age, gender, and book list.
     *
     * @param id       the user's id
     * @param name     the user's name
     * @param age      the user's age
     * @param gender   the user's gender
     * @param bookList the user's book list
     */
    public User(String id, String name, int age, String gender, ArrayList<Book> bookList, ArrayList<String> favoriteGenres) {
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
    public ArrayList<Book> getBookList() {
        return bookList;
    }

    /**
     * Sets the user's book list.
     *
     * @param bookList the user's book list
     */
    public void setBookList(ArrayList<Book> bookList) {
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
    public void setBookListIds(ArrayList<String> idBookList) {
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
    public void setFavoriteGenres(ArrayList<String> favoriteGenres) {
        this.favoriteGenres = favoriteGenres;
    }

}

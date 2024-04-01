package fuzzy.matching.model;

import java.util.List;

public class User {
    private int id;
    private String name;
    private int age;
    private String gender;
    private List<Book> bookList;

    public User(int id, String name, int age, String gender, List<Book> bookList) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.bookList = bookList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public List<Book> getBookList() {
        return bookList;
    }

    public void setBookList(List<Book> bookList) {
        this.bookList = bookList;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", gender='" + gender + '\'' +
                ", bookList=" + bookList +
                '}';
    }
}

package fuzzy.matching.model;

public class Book {
    private String name;
    private String summary;
    private String author;
    private String publicationDate;
    private double price;
    private double ratings;

    public Book(String name, String summary, String author, String publicationDate, double price, double ratings) {
        this.name = name;
        this.summary = summary;
        this.author = author;
        this.publicationDate = publicationDate;
        this.price = price;
        this.ratings = ratings;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(String publicationDate) {
        this.publicationDate = publicationDate;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getRatings() {
        return ratings;
    }

    public void setRatings(double ratings) {
        this.ratings = ratings;
    }

    @Override
    public String toString() {
        return "Book{" +
                "name='" + name + '\'' +
                ", summary='" + summary + '\'' +
                ", author='" + author + '\'' +
                ", publicationDate='" + publicationDate + '\'' +
                ", price=" + price +
                ", ratings=" + ratings +
                '}';
    }
}

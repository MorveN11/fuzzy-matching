package fuzzy.project;

import fuzzy.matching.component.MatchService;
import fuzzy.matching.domain.Document;
import fuzzy.matching.domain.Match;
import fuzzy.project.domain.UserDocument;
import fuzzy.project.model.Book;
import fuzzy.project.model.User;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Recommendations class.
 */
public class Recommendations {
  private final Set<Book> alreadyRead;
  private final Set<Book> recommendations;

  /**
   * Constructs a new Recommendations object with the specified user and list of
   * users.
   */
  public Recommendations(User user, List<User> users) {
    this.alreadyRead = new HashSet<>(user.getBookList());
    this.recommendations = getAllRecommendations(user.toUserRecommendations(),
        users.stream().map(User::toUserRecommendations).toList());
  }

  public Set<Book> getRecommendations() {
    return recommendations;
  }

  /**
   * Retrieves all recommendations for a given user by performing fuzzy matching
   * with a list of other users' documents.
   *
   * @param user  The document of the user for whom recommendations are to be
   *              retrieved.
   * @param users The list of documents of other users.
   * @return A set of books that are recommended for the given user.
   * @throws RuntimeException if no matches are found.
   */
  private Set<Book> getAllRecommendations(Document user, List<Document> users) {
    MatchService matchService = new MatchService();
    Map<Document, List<Match<Document>>> result = matchService.applyMatch(user, users);

    List<Match<Document>> matches = result.values().stream().findFirst().orElse(null);

    if (matches == null) {
      throw new RuntimeException("No matches found");
    }

    return documentsToBooks(matches);
  }

  /**
   * Converts a list of document matches to a set of books.
   *
   * @param matches the list of document matches
   * @return a set of books
   */
  private Set<Book> documentsToBooks(List<Match<Document>> matches) {
    Set<Book> bookList = new HashSet<>();

    matches.forEach(match -> {
      UserDocument userDocument = (UserDocument) match.getMatchedWith();
      User user = new User.Builder().build(userDocument);
      bookList.addAll(user.getBookList());
    });
    return removeAlreadyRead(bookList);
  }

  private Set<Book> removeAlreadyRead(Set<Book> bookList) {
    bookList.removeAll(alreadyRead);
    return bookList;
  }
}

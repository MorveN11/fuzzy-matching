package fuzzy.project;

import fuzzy.matching.component.MatchService;
import fuzzy.matching.domain.Document;
import fuzzy.matching.domain.Match;
import fuzzy.project.domain.UserDocument;
import fuzzy.project.model.SimilarUser;
import fuzzy.project.model.User;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * CheckSimilar class.
 */
public class CheckSimilar {
  private final Set<SimilarUser> users;

  public CheckSimilar(User user, List<User> users) {
    this.users = getSimilarUsers(user.toUserDocument(),
        users.stream().map(User::toUserDocument).toList());
  }

  public Set<SimilarUser> getUsers() {
    return users;
  }

  /**
   * Get similar users.
   *
   * @param user  the user
   * @param users the list of users
   * @return the set of similar users
   */
  private Set<SimilarUser> getSimilarUsers(Document user, List<Document> users) {
    MatchService matchService = new MatchService();
    Map<Document, List<Match<Document>>> result = matchService.applyMatch(user, users);

    List<Match<Document>> matches = result.values().stream().findFirst().orElse(null);

    if (matches == null) {
      throw new RuntimeException("No matches found");
    }

    return documentsToSimilarUsers(matches);
  }

  /**
   * Convert documents to similar users.
   *
   * @param matches the list of matches
   * @return the set of similar users
   */
  private Set<SimilarUser> documentsToSimilarUsers(List<Match<Document>> matches) {
    Set<SimilarUser> similarUsers = new TreeSet<>();

    matches.forEach(match -> {
      UserDocument userDocument = (UserDocument) match.getMatchedWith();
      User user = new User.Builder().build(userDocument);
      double score = match.getScore().getResult();
      SimilarUser similarUser = new SimilarUser(score, user);
      similarUsers.add(similarUser);
    });
    return getTopFive(similarUsers);
  }

  /**
   * Returns the top five similar users from the given set of similar users.
   *
   * @param similarUsers the set of similar users
   * @return the set of top five similar users
   */
  private Set<SimilarUser> getTopFive(Set<SimilarUser> similarUsers) {
    Set<SimilarUser> topFive = new TreeSet<>();

    int count = 0;
    for (SimilarUser similarUser : similarUsers) {
      topFive.add(similarUser);
      count++;
      if (count == 5) {
        break;
      }
    }

    return topFive;
  }
}

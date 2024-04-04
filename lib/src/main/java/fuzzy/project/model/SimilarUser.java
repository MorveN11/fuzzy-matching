package fuzzy.project.model;

/**
 * SimilarUser class.
 */
public class SimilarUser implements Comparable<SimilarUser> {
  private final double similarity;

  private final User user;

  public SimilarUser(double similarity, User user) {
    this.similarity = roundDouble(similarity);
    this.user = user;
  }

  private double roundDouble(double value) {
    return Math.round(value * 1000.0) / 1000.0;
  }

  public double getSimilarity() {
    return similarity;
  }

  public User getUser() {
    return user;
  }

  @Override
  public int compareTo(SimilarUser o) {
    if (this.similarity < o.getSimilarity()) {
      return 1;
    } else if (this.similarity > o.getSimilarity()) {
      return -1;
    }
    return this.user.compareTo(o.getUser());
  }

  @Override
  public String toString() {
    return """
        ---- Similar User ----
        Similarity: %s
        User:
        %s""".formatted(this.similarity, this.user);
  }
}

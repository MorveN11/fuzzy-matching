# Fuzzy Matcher Algorithms
## New Implemented Algorithm

The **Edit Distance** matching algorithm has been implemented to create the new match type called **EQUALITY_DISTANCE**.

- **Algorithm Used**: Edit Distance Algorithm (also known as Levenshtein Distance)
- **Time Complexity**: The time complexity of the Edit Distance algorithm is **O(m*n)**, where **m** and **n** represent the lengths of the two strings being compared.

## Old Implemented Algorithm

In the existing code of the fuzzy matcher, an algorithm known as the **Exact String Matching Algorithm** was found in the match type **EQUALITY**.

- **Algorithm Used**: Exact String Matching Algorithm
- **Time Complexity**: The time complexity of exact string matching is **O(n)**, where **n** is the length of the string.

## Comparison of Time Complexities and Scores

1. **EQUALITY_DISTANCE**: O(m*n) (Edit Distance)
    - The **EQUALITY_DISTANCE** algorithm usually has a higher time complexity for the number of Edit Distance calculations and it involves comparing each character of two strings.
    - Edit Distance manages the number of operations (insertions, deletions, substitutions) required to transform one string into another.
    - And for the scores, a lower Edit Distance indicates a higher similarity. For an exact match (edit distance = 0), the score will be 1.0.
    - For non-exact matches, the score will be less than 1.0 and will decrease as the edit distance increases.

2. **EQUALITY**: O(n) (Exact String Matching)
    - The **EQUALITY** algorithm has a simpler time complexity because it only requires checking for exact matches between token values.
    - And for the scores, if two tokens are exactly the same, the score is high (1.0); otherwise, it's low (0.0).

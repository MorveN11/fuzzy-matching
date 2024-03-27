# fuzzy-matching

## Members
- Jose Manuel Morales Patty
- Victor Villca Silva
- Fernando Mauricio Mamani Navarro

## Manuel Morales Work

- Initialize repository
- Add dependencies
- Refactor project
- Implement Path Tokenizer -> Changes
  1. fuzzy.matching.domain.ElementType:
    - Add new enum value: PATH
    - Call pathPreprocessing() method in the getPreprocessing() method
    - Call pathTokenizer() method in the getTokenizer() method

  2. fuzzy.matching.function.PreprocessingFunction:
    - Add new method: pathPreprocessing()

  3. fuzzy.matching.function.TokenizerFunction:
    - Add new method: pathTokenizer()

- Test Path Tokenizer -> Changes
  1. fuzzy.matching.function.TokenizerFunctionTest:
    - Add new test: itShouldGetPathTokenizer_Success()
    - Add new test: itShouldGetPathTokenizerForSingleWord_Success()
    - Add new test: itShouldGetPathTokenizerForPathWithSpaces_Success()
  2. fuzzy.matching.function.PreprocessingFunctionTest:
    - Add new test:  itShouldPreprocessPath()
  3. fuzzy.matching.component.MatchServiceTest:
    - Add new test: itShouldApplyMatchForMultiplePaths()
    - Add new test: itShouldApplyMatchForSimilarPaths()


## Victor Leon Villca Silva Work
- Review Fuzzy Matcher Documentation.
- Review decaGramTokenizer function as Phone Tokenizer
- Test Path Tokenizer ->
  1. Test case implementation: testDecaGramTokenizer_EmptyNumber()
  2. Test case implementation: testDecaGramTokenizer_SingleNumber()
  3. Test case implementation: testDecaGramTokenizer_MultipleNumberDigits()
  4. Test case implementation: testDecaGramTokenizer_MultipleLongNumber()
  5. Test case implementation: testDecaGramTokenizer_MultipleLongNumber2()
  6. Test case implementation: testDecaGramTokenizer_AlphabetElement()
  7. Test case implementation: testDecaGramTokenizer_AlphaNumericElement()

## Fernando Mauricio Mamani Navarro Work
- Review Fuzzy Matcher Documentation.

### Analysis algorithm

#### Implemented Matching Algorithms
##### Exact Word Matching:
This algorithm compares tokenized elements using an exact word match. It divides elements into individual words and compares these words to determine similarity. For example, the elements "Steven Wilson" and "Stephen Wilkson" may not match exactly, but if broken down into individual words ("Steven" and "Stephen"), the match between them can be calculated.

#####  Soundex Word Matching:
Soundex word matching uses the Soundex algorithm to phonetically encode words before comparing them. This means that words with similar pronunciations will have similar Soundex codes and thus be considered equal. For example, the words "Steven" and "Stephen" would have similar Soundex codes, allowing them to be considered matches.

#####  NGram Token Matching:
In this algorithm, elements are split into sequences of characters (n-grams), and these tokens are compared to calculate similarity. For example, in the case of emails like "parker.james@gmail.com" and "james_parker@yahoo.com", the n-grams (trigrams, in this case) can be compared to determine similarity.

#####  Nearest Neighbors Matching:
This algorithm evaluates the proximity of values within a predefined range. It is mainly used to compare elements such as numerical values or dates. For example, numerical values "100.54" and "100.00" can be considered matches if they are within a certain predefined proximity range, allowing for the detection of duplicates or similar records in a database.

#### Additional Matching Algorithms
#####  Levenshtein Distance:
This algorithm calculates the edit distance between two strings, i.e., the minimum number of operations required to transform one string into another (insertions, deletions, or character substitutions). It is useful for comparing strings with typographical errors or slight differences.

#####  Jaro-Winkler Distance:
Similar to the Levenshtein distance, the Jaro-Winkler coefficient measures the similarity between two strings taking into account character matches and transpositions. It is especially useful for comparing short strings and finding similar matches.

#####  TF-IDF (Term Frequency-Inverse Document Frequency):
This approach is commonly used in information retrieval and text classification. It calculates the importance of a word in a document relative to a collection of documents. It can be useful for comparing documents based on the frequency of relevant terms.

#####  Cosine and Jaccard Similarity Algorithms:
These algorithms are useful for comparing sets of elements. Cosine similarity measures the similarity between two vectors, while the Jaccard coefficient measures the similarity between sets by considering the intersection and union of elements.

#####  Specific Implementation
For the implementation of the address and phone number tokenizer, no new matching algorithm needed to be implemented; those already implemented by the library were used.




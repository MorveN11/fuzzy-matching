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

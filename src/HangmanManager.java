/*  Student information for assignment:
 *
 *  On my honor, Justin Foster, this programming assignment is my own work
 *  and I have not provided this code to any other student.
 *
 *  Name: Justin Foster
 *  email address: jrfoster2416@gmail.com
 *  UTEID: jdf3434
 *  Section 5 digit ID: 50220
 *  Grader name: Andrew Smith
 *  Number of slip days used on this assignment: 0
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Manages the details of EvilHangman. This class keeps tracks of the possible
 * words from a dictionary during rounds of hangman, based on guesses so far.
 *
 */
public class HangmanManager {
	private ArrayList<String> currentDictionary;
	private TreeMap<Integer, ArrayList<String>> lengthMap;
	private TreeSet<Character> guessedLetters;
	private int guessesAllowed;
	private int wordLength;
	private String currentPattern;
	private HangmanDifficulty difficulty;
	private int roundCount;
	private static final int EASYMOD = 2;
	private static final int MEDIUMMOD = 4;

	/**
	 * Create a new HangmanManager from the provided set of words and phrases. pre:
	 * words != null, words.size() > 0
	 * 
	 * @param words   A set with the words for this instance of Hangman.
	 * @param debugOn true if we should print out debugging to System.out.
	 */
	public HangmanManager(Set<String> words, boolean debugOn) {
		this(words);
		}
	
	/**
	 * Create a new HangmanManager from the provided set of words and phrases.
	 * Debugging is off. pre: words != null, words.size() > 0
	 * 
	 * @param words A set with the words for this instance of Hangman.
	 */
	public HangmanManager(Set<String> words) {
		if (words == null) {
			throw new IllegalArgumentException("Words cannot be null");
		}
		currentDictionary = new ArrayList<>();
		lengthMap = new TreeMap<>();
		guessedLetters = new TreeSet<>();
		for (String word : words) {
			if (lengthMap.get(word.length()) == null) {
				lengthMap.put(word.length(), new ArrayList<>());
			}
			lengthMap.get(word.length()).add(word);
		}
	}

	/**
	 * Get the number of words in this HangmanManager of the given length. pre: none
	 * 
	 * @param length The given length to check.
	 * @return the number of words in the original Dictionary with the given length
	 */
	public int numWords(int length) {
		if (lengthMap == null) {
			throw new IllegalArgumentException("LengthMap cannot be null");
		}
		if (lengthMap.get(length) == null) {
			return 0;
		}
		return lengthMap.get(length).size();
	}

	/**
	 * Get for a new round of Hangman. Think of a round as a complete game of
	 * Hangman.
	 * 
	 * @param wordLen    the length of the word to pick this time. numWords(wordLen)
	 *                   > 0
	 * @param numGuesses the number of wrong guesses before the player loses the
	 *                   round. numGuesses >= 1
	 * @param diff       The difficulty for this round.
	 */
	public void prepForRound(int wordLen, int numGuesses, HangmanDifficulty diff) {
		if (diff == null ) {
			throw new IllegalArgumentException("Parameters out of bounds");
		}
		StringBuilder newPattern = new StringBuilder();
		wordLength = wordLen;
		guessesAllowed = numGuesses;
		guessedLetters = new TreeSet<>();
		roundCount = 0;
		difficulty = diff;
		currentDictionary = new ArrayList<>(lengthMap.get(wordLen));
		for (int i = 0; i < wordLength; i++) {
			newPattern.append("-");
		}
		currentPattern = newPattern.toString();
	}

	/**
	 * The number of words still possible (live) based on the guesses so far.
	 * Guesses will eliminate possible words.
	 * 
	 * @return the number of words that are still possibilities based on the
	 *         original dictionary and the guesses so far.
	 */
	public int numWordsCurrent() {
		if (currentDictionary == null) {
			throw new IllegalArgumentException(" Current Dictionary cannot be null");
		}
		return currentDictionary.size();
	}

	/**
	 * Get the number of wrong guesses the user has left in this round (game) of
	 * Hangman.
	 * 
	 * @return the number of wrong guesses the user has left in this round (game) of
	 *         Hangman.
	 */
	public int getGuessesLeft() {
		if (guessesAllowed < 0) {
			throw new IllegalArgumentException("Guesses out of Range");
		}
		return guessesAllowed;
	}

	/**
	 * Return a String that contains the letters the user has guessed so far during
	 * this round. The String is in alphabetical order. The String is in the form
	 * [let1, let2, let3, ... letN]. For example [a, c, e, s, t, z]
	 * 
	 * @return a String that contains the letters the user has guessed so far during
	 *         this round.
	 */
	public String getGuessesMade() {
		if (guessedLetters == null) {
			throw new IllegalArgumentException("GuessedLetters cannot be null");
		}
		return guessedLetters.toString();
	}

	/**
	 * Check the status of a character.
	 * 
	 * @param guess The character to check.
	 * @return true if guess has been used or guessed this round of Hangman, false
	 *         otherwise.
	 */
	public boolean alreadyGuessed(char guess) {
		if (guessedLetters == null) {
			throw new IllegalArgumentException("GuessedLetters cannot be null");
		}
		return guessedLetters.contains(guess);
	}

	/**
	 * Get the current pattern. The pattern contains '-''s for unrevealed (or
	 * guessed) characters and the actual character for "correctly guessed"
	 * characters.
	 * 
	 * @return the current pattern.
	 */
	public String getPattern() {
		if (currentPattern == null) {
			throw new IllegalArgumentException("Current Pattern cannot be null");
		}
		return currentPattern;
	}

	// pre: !alreadyGuessed(ch)
	// post: return a tree map with the resulting patterns and the number of
	// words in each of the new patterns.
	// the return value is for testing and debugging purposes
	/**
	 * Update the game status (pattern, wrong guesses, word list), based on the give
	 * guess.
	 * 
	 * @param guess pre: !alreadyGuessed(ch), the current guessed character
	 * @return return a tree map with the resulting patterns and the number of words
	 *         in each of the new patterns. The return value is for testing and
	 *         debugging purposes.
	 */
	public TreeMap<String, Integer> makeGuess(char guess) {
		ArrayList<Pattern> patternList = new ArrayList<>();
		TreeMap<String, Integer> tMap = new TreeMap<>();
		Pattern result;
		if (!alreadyGuessed(guess)) {
			guessedLetters.add(guess);
			roundCount++;
			// makes Maps to be used Later
			tMap = makeTreeMap(guess);
			TreeMap<String, ArrayList<String>> patternMap = new TreeMap<>(makePatternMap(guess));
			for (String pat : patternMap.keySet()) {
				patternList.add(new Pattern(pat, patternMap.get(pat)));
			}
			// Sorts Based on the difficulty
			Collections.sort(patternList);
			// sets currentPattern and currentDictionary
			if (patternList.size() > 1	&& ((difficulty == HangmanDifficulty.EASY && roundCount % EASYMOD == 0) || (difficulty == HangmanDifficulty.MEDIUM && roundCount % MEDIUMMOD == 0))) {
				result = patternList.get(1);
			} else {
				result = patternList.get(0);
			}
			currentPattern = result.getPattern();
			currentDictionary = result.getPatternList();
			if (currentPattern.indexOf(guess) != -1) {
				guessesAllowed--;
			}
		}
		return tMap;
	}

	/**
	 * This method completes the TreeMap with patterns and number of patterns
	 * 
	 * @param Char guess, this will give the character of the guess
	 * @return tree map of patterns and number of patterns
	 */
	public TreeMap<String, Integer> makeTreeMap(char guess) {
		if (currentDictionary == null) {
			throw new IllegalArgumentException(" Current Dicionary cannot be null");
		}
		TreeMap<String, Integer> tMap = new TreeMap<>();
		for (int i = 0; i < currentDictionary.size(); i++) {
			// gets Word Pattern for word
			String pattern = getWordPattern(currentDictionary.get(i), guess);
			// sort patterns into maps with arrays and quantity of values
			if (tMap.containsKey(pattern)) {
				tMap.put(pattern, tMap.get(pattern) + 1);
			} else {
				tMap.put(pattern, 1);
			}
		}
		return tMap;
	}

	/**
	 * This method completes the PatternMap with patterns and Array of words with
	 * Patterns
	 * 
	 * @param Char guess, this will give the character of the guess
	 * @return tree map of patterns and number of patterns
	 */
	public TreeMap<String, ArrayList<String>> makePatternMap(char guess) {
		if (currentDictionary == null) {
			throw new IllegalArgumentException(" Current Dicionary cannot be null");
		}
		TreeMap<String, ArrayList<String>> patternMap = new TreeMap<>();
		for (int i = 0; i < currentDictionary.size(); i++) {
			// gets Word Pattern for word
			String pattern = getWordPattern(currentDictionary.get(i), guess);
			// sort patterns into maps with arrays and quantity of values
			if (patternMap.containsKey(pattern)) {
				patternMap.get(pattern).add(currentDictionary.get(i));
			} else {
				patternMap.put(pattern, new ArrayList<>());
				patternMap.get(pattern).add(currentDictionary.get(i));
			}
		}
		return patternMap;
	}

	/**
	 * This method returns the pattern of the string based on the guess
	 * 
	 * @param word  the word needed to find the patter
	 * @param guess the character used to determine the new pattern
	 * @return returns the String of the new Pattern
	 */
	public String getWordPattern(String word, char guess) {
		if (word == null) {
			throw new IllegalArgumentException(" Word cannot be null");
		}
		StringBuilder pattern = new StringBuilder(currentPattern);
		for (int charI = 0; charI < pattern.length(); charI++) {
			if (word.charAt(charI) == guess) {
				pattern.setCharAt(charI, guess);
			}
		}
		return pattern.toString();
	}

	/**
	 * Return the secret word this HangmanManager finally ended up picking for this
	 * round. If there are multiple possible words left one is selected at random.
	 * <br>
	 * pre: numWordsCurrent() > 0
	 * 
	 * @return return the secret word the manager picked.
	 */
	public String getSecretWord() {
		if (currentDictionary == null) {
			throw new IllegalArgumentException(" Current Dicionary cannot be null");
		}
		return currentDictionary.get((int) (Math.random() * (currentDictionary.size() - 1)));

	}

	/**
	 * adds a class called pattern allowing the program to easily sort
	 * by difficulty and retrieve lists of the words 
	 */
	private static class Pattern implements Comparable<Pattern> {
		private String pattern;
		private ArrayList<String> wordList;
		private int numDashes = 0;
		public Pattern(String pat, ArrayList<String> list) {
			if (pat == null || list == null) {
				throw new IllegalArgumentException(" Variables cannot be null");
			}
			pattern = pat;
			wordList = list;
			for (int i = 0; i < pattern.length(); i++) {
				if (pattern.charAt(i) == '-') {
					numDashes++;
				}
			}
		}

		/**
		 * this method returns the internal pattern of the Pattern object
		 * 
		 * @param none
		 * @return pattern
		 */
		public String getPattern() {
			if (pattern == null) {
				throw new IllegalArgumentException(" Pattern Cannot be null");
			}
			return pattern;
		}

		/**
		 * this method returns the internal wordList of the Pattern object
		 * 
		 * @param none
		 * @return patternList
		 */
		public ArrayList<String> getPatternList() {
			if (wordList == null) {
				throw new IllegalArgumentException("PatternList Cannot be null");
			}
			return wordList;
		}

		/**
		 * this method compares the patterns in and returns them in order of most to
		 * least difficult
		 * 
		 * @param the object being compared
		 * @return the value of more or less difficult
		 */
		@Override
		public int compareTo(Pattern o) {
			if (o == null) {
				throw new IllegalArgumentException(" No nulls in comparable");
			}
			if (this.wordList.size() == o.wordList.size()) {
				if (numDashes == o.numDashes) {
					return pattern.compareTo(o.pattern);
				} else {
					return o.numDashes - this.numDashes;
				}
			} else {
				return o.wordList.size() - this.wordList.size();
			}
		}
	}
}
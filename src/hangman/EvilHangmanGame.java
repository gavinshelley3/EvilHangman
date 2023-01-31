package hangman;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class EvilHangmanGame implements IEvilHangmanGame {
    private Set<String> words = new HashSet<>();
    private String currentKey = "";
    private SortedSet<Character> guessedLetters = new TreeSet<>();

    @Override
    public void startGame(File dictionary, int wordLength) throws IOException, EmptyDictionaryException {
        words.clear();
        currentKey = "";


        if (wordLength == 0) {
            throw new EmptyDictionaryException("Invalid word length");
        }

        if (dictionary == null || !dictionary.exists() || dictionary.length() == 0) {
            throw new EmptyDictionaryException("Empty Dictionary Exception");
        }

        if (dictionary.length() > 0) {
            Scanner scanner = new Scanner(dictionary);
            while (scanner.hasNextLine()) {
                String word = scanner.nextLine().toLowerCase();
                if (word.length() == wordLength) {
                    words.add(word);
                }
            }
        }


        if (words.isEmpty()) {
            throw new EmptyDictionaryException("There are no words in your dictionary");
        }


        for (int i = 0; i < words.iterator().next().length(); i++) {
            currentKey += "-";
        }
    }

    @Override
    public Set<String> makeGuess(char guess) throws GuessAlreadyMadeException {

        guess = getGuess(guess);
        Set<String> newWords = new HashSet<>();
        HashMap<String, Set<String>> currentMap = new HashMap<>();


        if (guessedLetters == null) {
            guessedLetters = new TreeSet<>();
        } else if (guessedLetters.contains(guess)) {
            throw new GuessAlreadyMadeException("You have already guessed this character\n");
        } else {
            guessedLetters.add(guess);
        }


        for (String word : words) {
            String key = currentKey;
            for (int i = 0; i < word.length(); i++) {
                if (word.length() == 1) {
                    key = String.valueOf(guess);
                }
                else if (i == word.length() - 1) {
                    if (word.charAt(i) == guess) {
                        key = key.substring(0, i) + guess + key.substring(i, word.length() - 1);
                    } else {
                        key = key.substring(0, i) + "-" + key.substring(i, word.length() - 1);
                    }
                }
                else {
                    if (word.charAt(i) == guess) {
                        key = key.substring(0, i) + guess + key.substring(i + 1, word.length() - 1);
                    } else {
                        key = key.substring(0, i) + "-" + key.substring(i + 1, word.length() - 1);
                    }
                }
            }


            for (int i = 0; i < word.length(); i++) {
                if (currentKey.charAt(i) != '-') {
                    if (i == currentKey.length() - 1) {
                        key = key.substring(0, i) + currentKey.charAt(i);
                    }
                    else {
                        key = key.substring(0, i) + currentKey.charAt(i) + key.substring(i + 1, word.length());
                    }
                }
            }


            if (currentMap.containsKey(key)) {
                currentMap.get(key).add(word);
            } else {
                Set<String> newSet = new HashSet<>();
                newSet.add(word);
                currentMap.put(key, newSet);
            }
        }


        for (String key : currentMap.keySet()) {
            for (int i = 0; i < key.length(); i++) {
                if (currentMap.get(key).size() > newWords.size()) {
                    newWords = currentMap.get(key);
                    currentKey = key;
                }
                else if (currentMap.get(key).size() == newWords.size()) {
                    if (key.compareTo(currentKey) < 0) {
                        newWords = currentMap.get(key);
                        currentKey = key;
                    }
                }
            }
        }


        words = newWords;
        return words;
    }

    private static char getGuess(char guess) {
        return Character.toLowerCase(guess);
    }

    @Override
    public SortedSet<Character> getGuessedLetters() {
        return guessedLetters;
    }

    public String getWord() {
        return currentKey.toString();
    }
}

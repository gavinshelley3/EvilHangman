package hangman;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class EvilHangmanGame implements IEvilHangmanGame {
    private Set<String> words = new HashSet<String>();
    private String currentKey = "";
    private SortedSet<Character> guessedLetters = new TreeSet<Character>() {
    };
    private HashMap<String, Set<String>> map = new HashMap<String, Set<String>>();
    @Override
    public void startGame(File dictionary, int wordLength) throws IOException, EmptyDictionaryException {

        try {
            FileInputStream in = new FileInputStream(dictionary);
            System.out.println("File found");
        }
        catch (IOException e) {
            System.out.println("File not found: " + dictionary);
        }
        try {
            Scanner scanner = new Scanner(dictionary);
            while (scanner.hasNextLine()) {
                String word = scanner.nextLine().toLowerCase();
                if (word.length() == wordLength) {
                    words.add(word);
                }
            }
            if (words.isEmpty()) {
                throw new EmptyDictionaryException();
            }
        }
        catch (EmptyDictionaryException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public Set<String> makeGuess(char guess) throws GuessAlreadyMadeException {
        Set<String> newWords = new HashSet<String>();
        HashMap<String, Set<String>> currentMap = new HashMap<String, Set<String>>();
        try {
            if (guessedLetters == null) {
                guessedLetters = new TreeSet<Character>();
            }
            else if (guessedLetters.contains(guess)) {
                throw new GuessAlreadyMadeException();
            } else {
                guessedLetters.add(guess);
            }
        }
        catch (GuessAlreadyMadeException e) {
            System.out.println(e.getMessage());
        }
        for (String word : words) {
            String key = currentKey;
            for (int i = 0; i < word.length(); i++) {
                if (word.charAt(i) == guess) {
                    key = key.substring(0, i) + guess + key.substring(i + 1);
                } else {
                    key = key.substring(0, i) + "-" + key.substring(i + 1);
                }
            }
            if (currentMap == null) {

            }
            else if (currentMap.containsKey(key)) {
                currentMap.get(key).add(word);
            } else {
                Set<String> newSet = new HashSet<String>();
                newSet.add(word);
                currentMap.put(key, newSet);
            }
        }
        for (String key : currentMap.keySet()) {
            if (currentMap.get(key).size() > newWords.size()) {
                newWords = currentMap.get(key);
                currentKey = key;
            }
        }
        words = newWords;
        return newWords;
    }

    @Override
    public SortedSet<Character> getGuessedLetters() {
        return guessedLetters;
    }

    public String getWord() {
        return currentKey.toString();
    }
}

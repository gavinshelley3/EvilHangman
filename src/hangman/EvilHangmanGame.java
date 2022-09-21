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

            if (wordLength == 0) {
                throw new EmptyDictionaryException("Invalid word length");
            }
            FileInputStream in = new FileInputStream(dictionary);
            System.out.println("File found");
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

    }

    @Override
    public Set<String> makeGuess(char guess) throws GuessAlreadyMadeException {

        guess = Character.toLowerCase(guess);
        Set<String> newWords = new HashSet<String>();
        HashMap<String, Set<String>> currentMap = new HashMap<String, Set<String>>();
        if (guessedLetters == null) {
            guessedLetters = new TreeSet<Character>();
        }
        else if (guessedLetters.contains(guess)) {
            throw new GuessAlreadyMadeException("You have already guessed this character\n");
        } else {
            guessedLetters.add(guess);
        }
        if (currentKey.equals("")) {
            for (int i = 0; i < words.iterator().next().length(); i++) {
                currentKey += "-";
            }
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
                Set<String> newSet = new HashSet<String>();
                newSet.add(word);
                currentMap.put(key, newSet);
            }
        }
        for (String key : currentMap.keySet()) {
            if (currentMap.get(key).size() > newWords.size()) {
                newWords = currentMap.get(key);
                currentKey = key;
            } else if (currentMap.get(key).size() == newWords.size()) {
                int keyCount = 0;
                int currentKeyCount = 0;
                int keyGuess = 0;
                int currentKeyGuess = 0;
                for (int i = 0; i < key.length(); i++) {
                    if (key.charAt(i) == '-') {
                        keyCount++;
                    }
                    if (key.charAt(i) == guess) {
                        keyGuess++;
                    }
                    if (currentKey.charAt(i) == '-') {
                        currentKeyCount++;
                    }
                    if (currentKey.charAt(i) == guess) {
                        currentKeyGuess++;
                    }
                }
                if (keyGuess == 0 && currentKeyGuess != 0) {
                    newWords = currentMap.get(key);
                    currentKey = key;
                }
                else if (currentKeyGuess == 0 && keyGuess != 0) {

                }
                else if (keyCount < currentKeyCount) {
                    newWords = currentMap.get(key);
                    currentKey = key;
                }
                else if (keyCount > currentKeyCount) {

                }
                else if (keyCount == currentKeyCount) {
                    for (int i = key.length() - 1; i >= 0; i--) {
                        if (key.charAt(i) != '-' && currentKey.charAt(i) == '-') {
                            newWords = currentMap.get(key);
                            currentKey = key;
                            break;
                        } else if (currentKey.charAt(i) != '-' && key.charAt(i) == '-') {
                            break;
                        }
                    }
                }
            }
        }









//            else {
//                int keyCount = 0;
//                int currentKeyCount = 0;
//                int keyFirstLetter = -11;
//                int currentKeyFirstLetter = -11;
//                int keySecondLetter = -13;
//                int currentKeySecondLetter = -13;
//                for (int i = 0; i < key.length(); i++) {
//                    if (key.charAt(i) != '-') {
//                        keyCount++;
//                        if (keyFirstLetter == -11) {
//                            keyFirstLetter = i;
//                        }
//                            keySecondLetter = i;
//                    }
//                    if (currentKey.charAt(i) != '-') {
//                        currentKeyCount++;
//                        if (currentKeyFirstLetter == -11) {
//                            currentKeyFirstLetter = i;
//                        }
//                            currentKeySecondLetter = i;
//                    }
//
//                }
//                if(keyCount < currentKeyCount) {
//                    newWords = currentMap.get(key);
//                    currentKey = key;
//                }
//                else if (keyCount > currentKeyCount) {
//
//                }
//                else {
//                    if (keyFirstLetter > currentKeyFirstLetter) {
//                        newWords = currentMap.get(key);
//                        currentKey = key;
//                    } else if (keyFirstLetter < currentKeyFirstLetter) {
//
//                    } else {
//                        if (keySecondLetter > currentKeySecondLetter) {
//                        newWords = currentMap.get(key);
//                        currentKey = key;
//                        }
//                        else if (keySecondLetter < currentKeySecondLetter) {
//
//                        }
//                        else {
//                            newWords = currentMap.get(key);
//                            currentKey = key;
//                        }
//                    }
//                }
//            }
//        }
        words = newWords;
        return words;

    }

    @Override
    public SortedSet<Character> getGuessedLetters() {
        return guessedLetters;
    }

    public String getWord() {
        return currentKey.toString();
    }
}

package hangman;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Locale;
import java.util.Scanner;
import java.util.Set;

public class EvilHangman {

    public static void main(String[] args) throws Exception {
        try {
            if (args[0] == null || args[1] == null || args[2] == null) {
                throw new Exception("Invalid arguments");
            }
            if (args[1].equals("0")) {
                throw new Exception("Invalid arguments");
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }
            File file = new File(args[0]);
            int wordLength = Integer.parseInt(args[1]);
            int guesses = Integer.parseInt(args[2]);
            EvilHangmanGame game = new EvilHangmanGame();
            try {
                game.startGame(file, wordLength);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            catch (EmptyDictionaryException e) {
                System.out.println(e.getMessage());
                return;
            }
            Scanner scanner = new Scanner(System.in);
            Set<Character> guessedLetters = new HashSet<Character>();
            while (guesses > 0) {
                System.out.println("You have " + guesses + " guesses left");
                System.out.println("Used letters: " + game.getGuessedLetters());
                System.out.println("Word: " + game.getWord());
                System.out.print("Enter guess: ");
                String guess = scanner.nextLine().toLowerCase();
                if (guess.length() != 1) {
                    System.out.println("Invalid guess");
                }
                else {
                    char letter = guess.charAt(0);
                    if (letter < 'a' || letter > 'z') {
                        System.out.println("Invalid guess");
                    }
                    else if (guessedLetters.contains(letter)) {
                        System.out.println("You already guessed that letter");
                    }
                    else {
                        Set<String> words = new HashSet<>();
                        try {
                            words = game.makeGuess(letter);
                        }
                        catch (GuessAlreadyMadeException e) {
                            System.out.println(e.getMessage());
                        }
                        if ((game.getWord().length() - game.getWord().replace(String.valueOf(letter), "").length()) == 0) {
                            guesses--;
                        }
                        System.out.println("Found " + (game.getWord().length() - game.getWord().replace(String.valueOf(letter), "").length()) + " " + letter + "'s");
                        if (words.size() == 1) {
                            System.out.println("You win!");
                            System.out.println("The word was: " + words.iterator().next());
                            break;
                        }
                        else if (guesses == 0) {
                            System.out.println("You lose!");
                            System.out.println("The word was: " + words.iterator().next());
                            break;
                        }
                        else {
                            guessedLetters.add(letter);
                        }
                    }
                }

        }
    }

}

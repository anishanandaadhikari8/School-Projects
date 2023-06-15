import java.util.Arrays;

public class HangmanTester {

    private static int correctTests = 0;
    private static int totalTests = 0;

    private static void clearCounts() {
        correctTests = 0;
        totalTests = 0;
    }

    private static void countTest(boolean correct) {
        if(correct) {
            correctTests++;
        }
        totalTests++;
    }

    private static void printResults(String methodName) {
        String msg = "";
        if(correctTests < totalTests) {
            // I want failed tests to really jump out at you.
            msg = " INCORRECT!";
        }
        System.out.println("testing " + methodName + ": passes " +
                           correctTests + " of " + totalTests + " tests" + msg);
    }

    private static void testIsComplete() {
        // isComplete() should only return true if the char[] passed to it
        // contains no underscores
        // it should not change the array

        String[] incompleteStrs = { "_oobar", "foo_ar", "fooba_", "_"};
        for(String str : incompleteStrs) {
            char[] arr = str.toCharArray();
            countTest(!Hangman.isComplete(arr));
            countTest(Arrays.equals(arr, str.toCharArray()));
        }
        
        String str = "foobar";
        char[] arr = str.toCharArray();
        countTest(Hangman.isComplete(arr));
        countTest(Arrays.equals(arr, str.toCharArray()));
    }

    private static void testUpdateWithGuess() {
        char[] known = "______".toCharArray();
        countTest(!Hangman.updateWithGuess("foobar", known, 'e'));
        countTest(Arrays.equals("______".toCharArray(), known));
        // When the letter is present, updateWithGuess() should return true
        countTest(Hangman.updateWithGuess("foobar", known, 'o'));
        // updateWithGuess() should also modify the char[] that was passed in
        countTest(Arrays.equals("_oo___".toCharArray(), known));
        countTest(!Hangman.updateWithGuess("foobar", known, 't'));
        countTest(Arrays.equals("_oo___".toCharArray(), known));
        countTest(Hangman.updateWithGuess("foobar", known, 'f'));
        countTest(Arrays.equals("foo___".toCharArray(), known));
        countTest(!Hangman.updateWithGuess("foobar", known, 'z'));
        countTest(Arrays.equals("foo___".toCharArray(), known));
        countTest(Hangman.updateWithGuess("foobar", known, 'r'));
        countTest(Arrays.equals("foo__r".toCharArray(), known));
    }

    private static void testSelectRandomWord() {
        // Make a tiny dictionary
        String[] dictionary = { "foo", "bar", "baz", "qux", "quux" };
        // Sort so I'll be able to use binarySearch
        Arrays.sort(dictionary);

        int[] counts = new int[dictionary.length];
        int n = 10000;
        // Many times, choose a random word from this dictionary
        for (int i = 0; i < n; i++) {
            String word = Hangman.selectRandomWord(dictionary);
            // Word selected better be in the dictionary
            countTest(word != null && Arrays.binarySearch(dictionary, word) >= 0);
            for (int j = 0; j < dictionary.length; j++) {
                // Update a counter for each word when it appears
                if (dictionary[j].equals(word)) {
                    counts[j]++;
                }
            }
        }
        // All words in the dictionary should appear with similar frequency        
        double expected = (double)n / dictionary.length;
        for (int i = 0; i < counts.length; i++) {
            countTest(counts[i] > expected * 0.9);
            countTest(counts[i] < expected * 1.1);
        }
    }

    public static void main(String[] args) {
        clearCounts();
        testIsComplete();
        printResults("isComplete");

        clearCounts();
        testUpdateWithGuess();
        printResults("updateWithGuess");

        clearCounts();
        testSelectRandomWord();
        printResults("selectRandomWord");
    }
}

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    public static AtomicInteger count1 = new AtomicInteger();
    public static AtomicInteger count2 = new AtomicInteger();
    public static AtomicInteger count3 = new AtomicInteger();

    public static void main(String[] args) throws InterruptedException {
        Random random = new Random();
        String[] words = new String[100_000];
        for (int i = 0; i < words.length; i++) {
            words[i] = generateText("abc", 3 + random.nextInt(3));
        }

        Thread palindrome = new Thread(() ->
        {
            for (String word : words) {
                if (isPalindrome(word) && !isSameLetter(word) && !isAscending(word)) {
                    incrementCounter(word.length());

                }
            }
        });
        palindrome.start();

        Thread sameLetter = new Thread(() -> {
            for (String word : words) {
                if (!isPalindrome(word) && isSameLetter(word) && !isAscending(word)) {
                    incrementCounter(word.length());
                }
            }
        });
        sameLetter.start();

        Thread ascending = new Thread(() -> {
            for (String word : words) {
                if (!isPalindrome(word) && !isSameLetter(word) && isAscending(word)) {
                    incrementCounter(word.length());
                }
            }
        });
        ascending.start();

        sameLetter.join();
        ascending.join();
        palindrome.join();

        System.out.println("Красивых слов с длиной 3: " + count1 + " шт");
        System.out.println("Красивых слов с длиной 4: " + count2 + " шт");
        System.out.println("Красивых слов с длиной 5: " + count3 + " шт");
    }

    public static boolean isPalindrome(String word) {
        return word.equals(new StringBuilder(word).reverse().toString());
    }

    public static boolean isSameLetter(String word) {
        for (int i = 1; i < word.length(); i++) {
            if (word.charAt(i) != word.charAt(i - 1))
                return false;
        }
        return true;
    }

    public static boolean isAscending(String word) {
        for (int i = 1; i < word.length(); i++) {
            if (word.charAt(i) > word.charAt(i - 1))
                return false;
        }
        return true;
    }

    public static void incrementCounter(int word) {
        switch (word) {
            case 3 -> count1.incrementAndGet();
            case 4 -> count2.incrementAndGet();
            case 5 -> count3.incrementAndGet();
        }
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder word = new StringBuilder();
        for (int i = 0; i < length; i++) {
            word.append(letters.charAt(random.nextInt(letters.length())));
        }
        return word.toString();
    }
}
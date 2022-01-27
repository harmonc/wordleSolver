import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {
        Scanner console = new Scanner(System.in);
        File wordList = new File("words-5.txt");
        File valueList = new File("words-5-values.txt");
        Scanner inValues = new Scanner(valueList);
        Scanner in = new Scanner(wordList);
        ArrayList<String> words = new ArrayList<>();
        ArrayList<Integer> values = new ArrayList<>();
        while (in.hasNextLine()) {
            words.add(in.nextLine());
        }

        while (inValues.hasNextLine()) {
            values.add(Integer.parseInt(inValues.nextLine()));
        }

        //System.out.println(words.size());
        ArrayList<Character> exclude = new ArrayList<>();
        ArrayList<Character> include = new ArrayList<>();

        runAI("00100","sanes",words,values,exclude,include);
        runAI("02020","corny",words,values,exclude,include);
        runAI("02022","point",words,values,exclude,include);
        //        runAI("00020","bales",words,values,exclude,include);


        printTop10(words, values);


       /*
        System.out.println("press enter to play or type exit to quit");
        while (!console.nextLine().equals("exit")) {
            File wordList = new File("words-5.txt");
            File valueList = new File("words-5-values.txt");
            Scanner inValues = new Scanner(valueList);
            Scanner in = new Scanner(wordList);
            ArrayList<String> words = new ArrayList<>();
            ArrayList<Integer> values = new ArrayList<>();
            while (in.hasNextLine()) {
                words.add(in.nextLine());
            }

            while (inValues.hasNextLine()) {
                values.add(Integer.parseInt(inValues.nextLine()));
            }

            //System.out.println(words.size());
            ArrayList<Character> exclude = new ArrayList<>();
            ArrayList<Character> include = new ArrayList<>();

            //System.out.println("Wordle Guesser\nThink of a 5 letter word\nThe AI will try to guess the word" +
            //        "\nThen you will score the word\n Example score: 10120\n0- Letter in position not in word\n" +
            //        "1- Letter is in word but in wrong spot\n" +
            //        "2- Letter is in correct spot");

            //printArray(words);
            System.out.print("Type a 5 letter word:");
            String answer = console.nextLine();//words.get((int)Math.floor(Math.random()*words.size()));
            System.out.println();
            String top;
            String result = "";
            int count = 0;
            System.out.println("word: " + answer);
            top = "sanes";
            while (!result.equals("22222")) {
                System.out.println("top 10 choices:");
                printTop10(words, values);
                System.out.println();
                System.out.println("Current Guess:" + top);
                result = compareWords(top, answer);
                System.out.println("Evaluation   :"+result);
                runAI(result, top, words, values, exclude, include);
                top = findTop(words, values);
                count++;
            }
            System.out.println("AI guessed in " + count + " guesses!\n" +
                    "");
            System.out.println("press enter to play or type exit to quit");
        }
        */
    }

    private static void printTop10(ArrayList<String> words, ArrayList<Integer> values) {
        ArrayList<String> topWords = new ArrayList<>();
        ArrayList<Integer> topValues = new ArrayList<>();
        topWords.add(words.get(0));
        topValues.add(values.get(0));
        for (int i = 1; i < words.size(); i++) {
            if(values.get(i)>topValues.get(0)){
                topWords.add(0,words.get(i));
                topValues.add(0,values.get(i));
            }else if(topWords.size()<10){
                topWords.add(words.get(i));
                topValues.add(values.get(i));
            }
            if(topWords.size()>10){
                topWords.remove(topWords.size()-1);
                topValues.remove(topValues.size()-1);
            }
        }
        for (int i = 0; i < topWords.size(); i++) {
            System.out.println(topWords.get(i)+":"+topValues.get(i));
        }
    }

    private static void runAI(String result, String top, ArrayList<String> words, ArrayList<Integer> values, ArrayList<Character> exclude, ArrayList<Character> include) {
        for (int i = 0; i < 5; i++) {
            char c = top.charAt(i);
            switch (result.charAt(i)) {
                case '0':
                    exclude.add(c);
                    break;
                case '1':
                    include.add(c);
                    removeWordsWithWrongSpot(words, values, c, i);
                    break;
                case '2':
                    include.add(c);
                    removeWordsWithoutRight(words, values, c, i);
                    break;
            }
        }
        removeWordsExclude(words, values, exclude);
        removeWordsInclude(words, values, include);
    }

    private static void notInList(ArrayList<String> words, ArrayList<Integer> values, String s) {
        for (int i = words.size() - 1; i >= 0; i--) {
            if (words.get(i).equals(s)) {
                words.remove(i);
                values.remove(i);
            }
        }
    }

    private static void removeWordsWithWrongSpot(ArrayList<String> words, ArrayList<Integer> values, char c, int n) {
        for (int i = words.size() - 1; i >= 0; i--) {
            String s = words.get(i);
            if (s.charAt(n) == c) {
                words.remove(i);
                values.remove(i);
            }
        }
    }

    private static String compareWords(String top, String answer) {
        String result = "";
        for (int i = 0; i < answer.length(); i++) {
            if (top.charAt(i) == answer.charAt(i)) {
                result += "2";
            } else if (answer.contains("" + top.charAt(i))) {
                result += "1";
            } else {
                result += "0";
            }
        }
        return result;
    }


    private static String findTop(ArrayList<String> words, ArrayList<Integer> values) {
        int maxValue = 0;
        int maxIndex = 0;
        for (int i = 0; i < words.size(); i++) {
            if (values.get(i) > maxValue) {
                maxIndex = i;
                maxValue = values.get(i);
            }
        }
        return words.get(maxIndex);
    }

    private static void removeWordsWithoutRight(ArrayList<String> words, ArrayList<Integer> values, char c, int n) {
        for (int i = words.size() - 1; i >= 0; i--) {
            String s = words.get(i);
            if (s.charAt(n) != c) {
                words.remove(i);
                values.remove(i);
            }
        }
    }

    private static void removeWordsInclude(ArrayList<String> words, ArrayList<Integer> values, ArrayList<Character> include) {
        for (int i = words.size() - 1; i >= 0; i--) {
            String word = words.get(i);
            boolean remove = false;
            for (char c : include) {
                if (!word.contains("" + c)) {
                    remove = true;
                }
            }
            if (remove) {
                words.remove(i);
                values.remove(i);
            }
        }

    }

    private static void printArray(ArrayList<String> words) {
        for (String s : words) {
            System.out.println(s);
        }
    }

    private static void removeWordsExclude(ArrayList<String> words, ArrayList<Integer> values, ArrayList<Character> exclude) {
        for (int i = words.size() - 1; i >= 0; i--) {
            String word = words.get(i);
            boolean remove = false;
            for (char c : exclude) {
                if (word.contains("" + c)) {
                    remove = true;
                }
            }
            if (remove) {
                words.remove(i);
                values.remove(i);
            }
        }
    }
}

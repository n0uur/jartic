package GameServer.Model;

import Shared.Logger.ServerLog;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class WordList {

    private static final String saveFileName = "words.txt";

    private static ArrayList<String> wordList;

    public static ArrayList<String> getWordList() {
        if (wordList == null) {
            loadWordList();
        }
        return wordList;
    }

    public static String[] getRandomWord(int size) {
        ArrayList<String> shuffleList = new ArrayList<>(getWordList());
        Collections.shuffle(shuffleList);

        String[] randomWord = new String[size];

        for(int i = 0 ; i < size; i++) {
            randomWord[i] = shuffleList.get(i);
        }

        return randomWord;

    }

    public static void loadWordList() {
        File wordListFile = new File(saveFileName);
        if(wordListFile.exists()) {
            wordList = new ArrayList<String>();
            try (BufferedReader reader = new BufferedReader(new FileReader(wordListFile))) {
                String line = reader.readLine();
                int wordsCount = 0;
                while (line != null) {
                    wordList.add(line);
                    wordsCount++;
                    line = reader.readLine();
                }
                ServerLog.log("loaded " + wordsCount + " words from file..");
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            ServerLog.error("failed to read words list.. please add word and save to " + saveFileName + " file.");
        }
    }
}

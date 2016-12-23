package com.google.engedu.anagrams;

import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 7;
    private static int wordLength = DEFAULT_WORD_LENGTH;
    private Random random = new Random();
    ArrayList<String> wordList=new ArrayList<>();
    HashSet<String> wordSet=new HashSet<>();
    HashMap<String,ArrayList<String>> lettersToWord=new HashMap<String, ArrayList<String>>();
    HashMap<Integer,ArrayList<String>> sizeToWord=new HashMap<>();

    public AnagramDictionary(InputStream wordListStream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(wordListStream));
        String line;

        while((line = in.readLine()) != null) {
            String word = line.trim();
            wordList.add(word);
            wordSet.add(word);
            ArrayList<String> temp_words1 = new ArrayList<String>();
            ArrayList<String> temp_words2 = new ArrayList<String>();
            int size=word.length();
            if(sizeToWord.containsKey(size))
            {
                temp_words2=sizeToWord.get(size);
                temp_words2.add(word);
                sizeToWord.put(size,temp_words2);
            }
            else {

                temp_words2.add(word);
                sizeToWord.put(size,temp_words2);
            }
            String sortWord;
            sortWord = sortLetters(word);

            if(lettersToWord.containsKey(sortWord)){

                temp_words1 = lettersToWord.get(sortWord);
                temp_words1.add(word);
            }
            else{
                temp_words1.add(word);
                lettersToWord.put(sortWord,temp_words1);
            }
        }

    }
    public String sortLetters(String word)
    {
        char[] WORD=word.toCharArray();
        Arrays.sort(WORD);
        String sortWord=new String(WORD);
        return sortWord;
    }

    public boolean isGoodWord(String word, String base) {
        boolean  flag=false;
        if(wordSet.contains(word) && !word.contains(base))
            flag=true;
        return flag;

    }

    public ArrayList<String> getAnagrams(String targetWord) {
        ArrayList<String> result = new ArrayList<String>();
        for(int i=0;i<wordList.size();i++)
        {
            if(targetWord.length()==wordList.get(i).length()) {
                if (sortLetters(wordList.get(i)).equals(sortLetters(targetWord))) {
                    result.add(wordList.get(i));
                }
            }
        }
        return result;
    }


    public ArrayList<String> getAnagramsWithOneMoreLetter(String word) {

        ArrayList<String> result = new ArrayList<String>();
        ArrayList<String> temp=new ArrayList<>();
        String NewWord;
        String sortedWord;
        for(char i='a';i<='z';i++)
        {
            NewWord=word+i;
            sortedWord=sortLetters(NewWord);

                if(lettersToWord.containsKey(sortedWord))
                {temp=lettersToWord.get(sortedWord);
                    result.addAll(lettersToWord.get(sortedWord));
                }



        }
        for(int i = result.size() -1; i>=0;i--){
            if(!isGoodWord(result.get(i),word)) {
                result.remove(i);
            }
        }

        return result;
    }

    public String pickGoodStarterWord() {

String word=new String();
Random r= new Random();
        int size=r.nextInt(MAX_WORD_LENGTH-DEFAULT_WORD_LENGTH)+DEFAULT_WORD_LENGTH;
           ArrayList<String> temp=sizeToWord.get(size);
           int index=r.nextInt(temp.size());
        for(int j=index;j<temp.size();j++)
        {
            ArrayList<String> list=getAnagramsWithOneMoreLetter(temp.get(j));
            if(list.size()>=MIN_NUM_ANAGRAMS)
            {
                word=temp.get(j);
                break;
            }

        }
        if(word==null)
        {
            for(int j=0;j<index;j++)
            {
                ArrayList<String> list=getAnagramsWithOneMoreLetter(temp.get(j));
                if(list.size()>=MIN_NUM_ANAGRAMS)
                {
                    word=temp.get(j);
                    break;
                }

            }
        }
return word;

    }
}

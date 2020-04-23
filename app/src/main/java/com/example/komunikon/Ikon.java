package com.example.komunikon;

import android.content.Context;

import java.util.List;

public class Ikon {
    private Context context;
    private long id;
    private String name;
    private List<String> meanings;
    private String filename;
    private String matchedWord;

    public Ikon(Context context, long id, String name, List<String> meanings, String filename) {
        this.context = context;
        this.id = id;
        this.name = name;
        this.meanings = meanings;
        this.filename = filename;
        this.matchedWord = "";
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getMeanings() {
        return meanings;
    }

    public void setMeanings(List<String> meanings) {
        this.meanings = meanings;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public int getImage(){
        return context.getResources().getIdentifier(this.filename, "drawable", context.getPackageName());
    }

    public String getMatchedWord() {
        return matchedWord;
    }

    public void setMatchedWord(String matchedWord) {
        this.matchedWord = matchedWord;
    }
}

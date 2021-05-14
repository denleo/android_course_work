package com.example.english_app.Entities.Database;

public class Word { // database entity class
    private int ID;
    private String ruTranslate;
    private String enTranslate;

    public Word(){}

    public Word(int ID, String ruTranslate, String enTranslate) {
        this.ruTranslate = ruTranslate;
        this.enTranslate = enTranslate;
        this.ID = ID;
    }

    public String getRuTranslate() {
        return ruTranslate;
    }

    public String getEnTranslate() {
        return enTranslate;
    }

    public void setRuTranslate(String ruTranslate) {
        this.ruTranslate = ruTranslate;
    }

    public void setEnTranslate(String enTranslate) {
        this.enTranslate = enTranslate;
    }

    public int getID() {
        return ID;
    }
}

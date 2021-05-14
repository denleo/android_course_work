package com.example.english_app.Utils.DatabaseUtils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.english_app.Entities.Database.Word;

import java.util.ArrayList;
import java.util.List;

public class DBAdapter {

    private DBHelper dbHelper;
    private SQLiteDatabase database;

    public DBAdapter(Context context){
        dbHelper = new DBHelper(context.getApplicationContext());
    }

    public DBAdapter open(){
        try {
            database = dbHelper.getWritableDatabase();
        } catch (Exception e){
            Log.e(this.getClass().getName(), "Open DB error", e);
        }

        return this;
    }

    public void close(){
        try {
            dbHelper.close();
        } catch (Exception e){
            Log.e(this.getClass().getName(), "Error with closing DB", e);
        }
    }

    private Cursor getAllEntries(){
        String[] columns = new String[] {DBHelper.COLUMN_ID, DBHelper.COLUMN_RUS, DBHelper.COLUMN_ENG};
        return database.query(DBHelper.TABLE_NAME, columns, null, null, null, null, DBHelper.COLUMN_ENG);
    }

    public List<Word> getAllWords(){
        ArrayList<Word> wordArrayList = new ArrayList<>();
        Cursor cursor = getAllEntries();
        while (cursor.moveToNext()){
            int id = cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMN_ID));
            String name = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_RUS));
            String year = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_ENG));
            wordArrayList.add(new Word(id, name, year));
        }
        cursor.close();
        return wordArrayList;
    }

    public long getCount(){
        return DatabaseUtils.queryNumEntries(database, DBHelper.TABLE_NAME);
    }

    public Word getWord(int id){
        Word word = null;
        String query = String.format("SELECT * FROM %s WHERE %s=?", DBHelper.TABLE_NAME, DBHelper.COLUMN_ID);
        Cursor cursor = database.rawQuery(query, new String[]{ String.valueOf(id)});
        if(cursor.moveToFirst()){
            String rus = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_RUS));
            String eng = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_ENG));
            word = new Word(id, rus, eng);
        }
        cursor.close();
        return word;
    }

    public boolean checkForExistence(Word word){
        List<Word> wordList = getAllWords();
        for (Word translation: wordList) {
            if (word.getEnTranslate().equals(translation.getEnTranslate())){
                return true;
            }
        }
        return false;
    }

    public void insert(Word word){

        ContentValues cv = new ContentValues();
        cv.put(DBHelper.COLUMN_RUS, word.getRuTranslate());
        cv.put(DBHelper.COLUMN_ENG, word.getEnTranslate());

        database.insert(DBHelper.TABLE_NAME, null, cv);
    }

    public void delete(int id){
        String whereClause = "ID = ?";
        String[] whereArgs = new String[]{String.valueOf(id)};
        database.delete(DBHelper.TABLE_NAME, whereClause, whereArgs);
    }

    public void deleteAll(){
        database.execSQL("DELETE FROM " + DBHelper.TABLE_NAME);
    }

    public String getPath(){
        return database.getPath();
    }

    public void update(Word word){
        String whereClause = DBHelper.COLUMN_ID + "=" + String.valueOf(word.getID());
        ContentValues cv = new ContentValues();
        cv.put(DBHelper.COLUMN_RUS, word.getRuTranslate());
        cv.put(DBHelper.COLUMN_ENG, word.getEnTranslate());
        database.update(DBHelper.TABLE_NAME, cv, whereClause, null);
    }
}
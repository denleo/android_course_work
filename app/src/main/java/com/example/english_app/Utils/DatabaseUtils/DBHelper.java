package com.example.english_app.Utils.DatabaseUtils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Vocabulary.db"; // название бд
    private static final int SCHEMA = 1; // версия базы данных
    public static final String TABLE_NAME = "Words"; // название таблицы в бд
    // названия столбцов
    public static final String COLUMN_ID = "Id";
    public static final String COLUMN_ENG = "eng_word";
    public static final String COLUMN_RUS = "rus_word";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_ENG + " TEXT, " + COLUMN_RUS + " TEXT)");

    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,  int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
        onCreate(db);
    }
}

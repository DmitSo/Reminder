package com.example.dimon.reminder;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;

class DBHelper extends SQLiteOpenHelper {
    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
    public static final int DB_VERSION = 2;
    public static final String DB_NAME = "Reminders";
    public static final String DB_FILE = "remindersDb.db";
    SQLiteDatabase db;

    public DBHelper(Context context){
        super(context, DB_FILE, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+DB_NAME+"(" +
                "id         integer primary key autoincrement," +
                "caption    text," +
                "date       text," +
                "content    text);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+DB_NAME);
        onCreate(db);
    }

    public void writeNoteToDb(Note note){
        db = this.getReadableDatabase();

        db.insert(DB_NAME, null, formContentValues(note));
        db.close();
    }

    public void editNote(int id, Note editedValue){
        db = this.getReadableDatabase();
        String whereStatement = "id =" + id;
        db.update(DB_NAME, formContentValues(editedValue), whereStatement, null);
        db.close();
    }

    private ContentValues formContentValues(Note note){
        ContentValues contentValues = new ContentValues();

        String date = "";
        String content = "";
        if (note.reminderDate != null)
            date = dateFormat.format(note.reminderDate);
        if (note.content != null)
            content = note.content;

        contentValues.put("caption", note.caption);
        contentValues.put("content", content);
        contentValues.put("date", date);

        return contentValues;
    }

    public HashMap<String, Note> readNotesFromDb(){
        db = this.getReadableDatabase();
        Cursor c = db.query(DB_NAME, null, null,
                null, null, null, null);

        HashMap<String, Note> notes = new HashMap<>();
        //ArrayList<Note> notes = new ArrayList<>();

        if (c.moveToFirst()){
            int idColIndex = c.getColumnIndex("id");
            int captionColIndex = c.getColumnIndex("caption");
            int dateColIndex = c.getColumnIndex("date");
            int contentColIndex = c.getColumnIndex("content");

            do{
                int id = c.getInt(idColIndex);
                String caption = c.getString(captionColIndex);
                String date = c.getString(dateColIndex);
                String content = c.getString(contentColIndex);
                if (date == null)
                    date = "";
                if (content == null)
                    content = "";

                try {
                    notes.put(Integer.toString(id), new Note(caption, date, content));
                }
                catch (ParseException e){
                    notes.put(Integer.toString(id), new Note(caption, content));
                }
            }while(c.moveToNext());
        }
        c.close();
        db.close();

        return notes;
    }

    public void deleteAll(){
        db = this.getReadableDatabase();

        db.delete(DB_NAME, null, null);
        db.close();
    }
}

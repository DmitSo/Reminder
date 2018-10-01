package com.example.dimon.reminder;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Dictionary;
import java.util.HashMap;

class DBHelper extends SQLiteOpenHelper {
    //public static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
    private static final int DB_VERSION = 2;
    private static final String DB_NAME = "Reminders";
    private static final String DB_FILE = "remindersDb.db";

    private SQLiteDatabase db;

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

    /**
     * Adds following value to the database
     * @param note Value you want to add in DB
     */
    public void writeNoteToDb(Note note){
        db = this.getReadableDatabase();

        db.insert(DB_NAME, null, formContentValues(note));
        db.close();
    }

    /**
     * Updates value with following ID in the database using following Note value
     * @param id Entry ID which needs to be updated with following Note value
     * @param editedValue Value which need to be inserted instead of value specified by ID in DB
     */
    public void editNote(int id, Note editedValue){
        db = this.getReadableDatabase();
        String whereStatement = "id =" + id;
        db.update(DB_NAME, formContentValues(editedValue), whereStatement, null);
        db.close();
    }

    /**
     * Removes value with following ID in the database
     * @param id Entry ID which needs to be removed
     */
    public void removeNote(int id){
        db = this.getReadableDatabase();
        String whereStatement = "id =" + id;
        db.delete(DB_NAME, whereStatement, null);
        db.close();
    }

    /**
     * Makes ContentValues variable for inserting it in the database based on Note value
     * @param note Value which should be converted to ContentValue form
     * @return ContentValues value based on information in Note value
     */
    private ContentValues formContentValues(Note note){
        ContentValues contentValues = new ContentValues();

        String date = note.getReminderDate() == null
                ? ""
                : String.valueOf((note.getReminderDate()).getTime());

        String content = note.getContent() == null ? "" : note.getContent();

        contentValues.put("caption", note.getCaption());
        contentValues.put("content", content);
        contentValues.put("date", date);

        return contentValues;
    }

    /**
     * Gets all values in REMINDERS table from DB
     * @return Notes in database
     */
    public HashMap<String, Note> readNotesFromDb(){
        db = this.getReadableDatabase();
        Cursor c = db.query(DB_NAME, null, null,
                null, null, null, null);

        HashMap<String, Note> notes = new HashMap<>();

        if (c.moveToFirst()){
            int idColIndex = c.getColumnIndex("id");
            int captionColIndex = c.getColumnIndex("caption");
            int dateColIndex = c.getColumnIndex("date");
            int contentColIndex = c.getColumnIndex("content");

            do {
                int id = c.getInt(idColIndex);
                String caption = c.getString(captionColIndex);
                String dateStr = c.getString(dateColIndex);
                String content = c.getString(contentColIndex);

                Date date = null;
                if (dateStr.length() != 0) {
                    date = new Date();
                    date.setTime(Long.parseLong(dateStr));
                }
                if (content == null)
                    content = "";

                notes.put(Integer.toString(id), new Note(caption, date, content));

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

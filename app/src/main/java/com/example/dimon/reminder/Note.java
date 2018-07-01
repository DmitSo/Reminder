package com.example.dimon.reminder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Note {
    public static final SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm");

    String caption;
    Date reminderDate;
    String content;

    public Note(String caption, String reminderDate, String content) throws ParseException{
        this(caption, format.parse(reminderDate), content);
    }

    public Note(String caption, Date reminderDate, String content){
        this(caption, content);
        this.reminderDate = reminderDate;
    }

    public Note(String caption, String content){
        this.caption = caption;
        this.content = content;
    }

    public String getCaption() {
        return caption;
    }

    public Date getReminderDate() {
        return reminderDate;
    }

    public String getContent() {
        return content;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setReminderDate(Date reminderDate) {
        this.reminderDate = reminderDate;
    }
}

package com.example.dimon.reminder;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Note implements Parcelable {
    public static final Parcelable.Creator<Note> CREATOR = new Parcelable.Creator<Note>() {

        @Override
        public Note createFromParcel(Parcel source) {
            return new Note(source);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };

    private String mCaption;
    private Date mReminderDate;
    private String mContent;

    /**
     * Constructor for Note with setting reminder date
     * @param caption Caption of the note
     * @param reminderDate Date for reminder
     * @param content content of the note
     */
    public Note(String caption, Date reminderDate, String content){
        this(caption, content);
        this.mReminderDate = reminderDate;
    }

    /**
     * Constructor for Note without setting reminder date
     * @param caption Caption of the note
     * @param content content of the note
     */
    public Note(String caption, String content){
        this.mCaption = caption;
        this.mContent = content;
    }

    public Note(Parcel in) {
        String[] data = new String[3];
        in.readStringArray(data);
        mCaption = data[0];
        if (data[1].length() != 0 ){
            mReminderDate = new Date();
            mReminderDate.setTime(Long.parseLong(data[1]));
        }
        mContent = data[2];
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[]
                {
                        mCaption,
                        mReminderDate == null ? "" : String.valueOf(mReminderDate.getTime()),
                        mContent
                });
    }


    public String getCaption() {
        return mCaption;
    }

    public Date getReminderDate() {
        return mReminderDate;
    }

    public String getContent() {
        return mContent;
    }

    public void setCaption(String caption) {
        this.mCaption = caption;
    }

    public void setContent(String content) {
        this.mContent = content;
    }

    public void setReminderDate(Date reminderDate) {
        this.mReminderDate = reminderDate;
    }

}

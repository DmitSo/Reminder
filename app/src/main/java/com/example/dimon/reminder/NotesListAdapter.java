package com.example.dimon.reminder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Adapter for list of notes in MainActivity
 */
public class NotesListAdapter extends BaseAdapter {
    // Date format used if the note's date is not today
    private static final SimpleDateFormat dateFormatFull  = new SimpleDateFormat("dd MMM yyyy HH:mm");
    // Date format used if the note's date is today
    private static final SimpleDateFormat dateFormatShort = new SimpleDateFormat("HH:mm");

    private LayoutInflater mLayoutInflater;
    private ArrayList<Note> mNotes;


    protected NotesListAdapter(Context context, ArrayList<Note> data) {
        mNotes = data;
        mLayoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return mNotes.size();
    }

    @Override
    public Note getItem(int position) {
        return mNotes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * Generates View for note
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        View v = convertView;
        Date today = getDateWithoutTime(new Date());

        // If element's view isn't created then ViewHolder creates it
        if (v == null) {
            holder = new ViewHolder();
            v = mLayoutInflater.inflate(R.layout.list_element, parent, false);

            holder.caption = v.findViewById(R.id.tvCaption);
            holder.date = v.findViewById(R.id.tvDate);
            holder.content = v.findViewById(R.id.tvContent);

            v.setTag(holder);
        }

        holder = (ViewHolder) v.getTag();
        Note note = getData(position);

        String date = "";
        if(note.getReminderDate() != null)
            date = getDateWithoutTime(note.getReminderDate()).compareTo(today) == 0 ?
                    dateFormatShort.format(note.getReminderDate()) :
                    dateFormatFull.format(note.getReminderDate());

        holder.caption.setText(note.getCaption());
        holder.date.setText(date);
        holder.content.setText(note.getContent());

        return v;
    }


    /**
     * Resets given date's time
     * @param date Date where you want to reset time
     * @return Date with time equal 00:00:00
     */
    private static Date getDateWithoutTime(Date date){
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTime();
    }

    /**
     * Method which takes object from ArrayList at specified position
     * @param position Position of the requested element
     * @return Note object from ArrayList at specified index
     */
    private Note getData(int position) {
        return (getItem(position));
    }

    /**
     * This structure are used to avoid such problems as
     * artifacts and jumping of information from one ListView to another
     * (tagging helps in it)
     */
    private static class ViewHolder {
        private TextView caption;
        private TextView date;
        private TextView content;
    }
}

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

public class NotesListAdapter extends BaseAdapter {

    public static final SimpleDateFormat dateFormatFull  = new SimpleDateFormat("dd MMM yyyy HH:mm");
    public static final SimpleDateFormat dateFormatShort = new SimpleDateFormat("HH:mm");

    private LayoutInflater LInflater;
    private ArrayList<Note> list;

    public NotesListAdapter(Context context, ArrayList<Note> data) {

        list = data;
        LInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Note getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        View v = convertView;
        Date today = getDateWithoutTime(new Date());

        /*
         * В том случае, если вид элемента не создан, производится его создание
         * с помощью ViewHolder и тегирование данного элемента конкретным holder объектом
         */
        if (v == null) {
            holder = new ViewHolder();
            v = LInflater.inflate(R.layout.reminders_list_element, parent, false);
            holder.caption = (TextView)v.findViewById(R.id.tvCaption);
            holder.date = (TextView)v.findViewById(R.id.tvDate);
            holder.content = (TextView)v.findViewById(R.id.tvContent);

            v.setTag(holder);
        }

        /*
         * После того, как все элементы определены, производится соотнесение
         * внешнего вида, данных и конкретной позиции в ListView.
         * После чего из ArrayList забираются данные для элемента ListView и
         * передаются во внешний вид элемента
         */
        holder = (ViewHolder) v.getTag();
        Note note = getData(position);

        String date = "";
        if(note.reminderDate != null)
            date = getDateWithoutTime(note.reminderDate).compareTo(today) == 0 ?
                    dateFormatShort.format(note.reminderDate) :
                    dateFormatFull.format(note.reminderDate);

        holder.caption.setText(note.caption);
        holder.date.setText(date);
        holder.content.setText(note.content);

        return v;
    }

    private static Date getDateWithoutTime(Date date){
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTime();
    }

    /*
     * Метод, который забирает объект из ArrayList для дальнейшей работы с ним
     * и передачи его данных в элемент ListView
     */
    Note getData(int position) {
        return (getItem(position));
    }

    /*
     * Данная структура данных необходима для того, чтобы при пролистывании
     * большого списка не возникало артефактов и перескакивания данных с одной позиции ListView
     * на другую, что достигается тегированием каждого элемента ListView
     */
    private static class ViewHolder {
        private TextView caption;
        private TextView date;
        private TextView content;
    }
}

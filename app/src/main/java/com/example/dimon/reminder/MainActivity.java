package com.example.dimon.reminder;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements OnClickListener {
    public static final String DEBUG_LOG = "debug_log";

    public static final int ADD_VALUE_REQUEST_CODE = 1;
    public static final int EDIT_VALUE_REQUEST_CODE = 2;


    DBHelper dbHelper;
    HashMap<String, Note> notes;
    ListView lvNotes;
    LayoutInflater ltInflater;
    int chosenElementId;

    // alarms in next version
    // try josScheduler | WorkManager | jobService ???
    //AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(this);

        lvNotes = findViewById(R.id.lvNotes);
        ltInflater = getLayoutInflater();

        try {
            // создаем объект для создания и управления версиями БД
            dbHelper = new DBHelper(this);


            if (notes == null){
                    notes = dbHelper.readNotesFromDb();
            }
        }catch (Exception exc) {
            Log.d(DEBUG_LOG, exc.getMessage());
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle(exc.getMessage())
                    .setMessage(exc.getMessage())
                    .setCancelable(false)
                    .setNegativeButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
            AlertDialog alert = builder.create();
            alert.show();
        }
        displayNotes();

        lvNotes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parentView, View childView, int position, long id) {
                // Edit value
                String k = new ArrayList<String>(notes.keySet()).get((int)id);
                int key = Integer.parseInt(k);
                Note value = new ArrayList<Note>(notes.values()).get((int)id);
                chosenElementId = key;
                Intent intent = new Intent(getApplicationContext(), AddValueActivity.class);
                intent.putExtra("activity_action", EDIT_VALUE_REQUEST_CODE);
                intent.putExtra("caption", value.caption);
                intent.putExtra("date", Note.format.format(value.reminderDate));
                intent.putExtra("content", value.content);
                startActivityForResult(intent, EDIT_VALUE_REQUEST_CODE);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab:
                // Add new value
                Intent intent = new Intent(getApplicationContext(), AddValueActivity.class);
                intent.putExtra("activity_action", ADD_VALUE_REQUEST_CODE);
                startActivityForResult(intent, ADD_VALUE_REQUEST_CODE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(data != null){
            if(resultCode == RESULT_OK){
                switch (requestCode){
                    case ADD_VALUE_REQUEST_CODE:
                    case EDIT_VALUE_REQUEST_CODE:
                        long dateMills = Long.parseLong(data.getStringExtra("note_date"));
                        Date date;
                        if (dateMills != 0)
                            date = new Date(dateMills);
                        else
                            date = null;

                        Note value = new Note(
                                data.getStringExtra("note_caption"),
                                date,
                                data.getStringExtra("note_content"));

                        if (requestCode == ADD_VALUE_REQUEST_CODE)
                            dbHelper.writeNoteToDb(value);
                        else
                            dbHelper.editNote(chosenElementId, value);

                        notes = dbHelper.readNotesFromDb();
                        displayNotes();
                        break;
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void displayNotes(){
        // создаем адаптер
        NotesListAdapter adapter = new NotesListAdapter(this, new ArrayList<Note>(notes.values()));

        // присваиваем адаптер списку
        lvNotes.setAdapter(adapter);
    }
}

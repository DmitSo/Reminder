package com.example.dimon.reminder;

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
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements OnClickListener {
    public static final String DEBUG_LOG = "reminder_debug_log";
    public static final int ADD_VALUE_REQUEST_CODE = 1;     //
    public static final int EDIT_VALUE_REQUEST_CODE = 2;

    DBHelper mDbHelper;
    HashMap<String, Note> mNotes;
    LayoutInflater mLayoutInflater;
    int mChosenElementId;

    ListView lvNotes;

    // TODO : alarms in next version
    //try josScheduler | WorkManager | jobService ???


    /**
     * Initializes variables
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fabAddValue = findViewById(R.id.fab_add_value);
        fabAddValue.setOnClickListener(this);

        lvNotes = findViewById(R.id.lv_notes);
        mLayoutInflater = getLayoutInflater();

        mDbHelper = new DBHelper(this);

        lvNotes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parentView, View childView, int position, long id) {
                // Edit value
                /*
                String k = new ArrayList<String>(mNotes.keySet()).get((int)id);
                Note value = new ArrayList<Note>(mNotes.values()).get((int)id);
                */
                String k = (String)mNotes.keySet().toArray()[(int)id];
                Note value = (Note)mNotes.values().toArray()[(int)id];
                mChosenElementId = Integer.parseInt(k);

                Intent intent = new Intent(getApplicationContext(), AddValueActivity.class);
                intent.putExtra("EXTRA_ACTIVITY_ACTION", EDIT_VALUE_REQUEST_CODE);
                // TODO: probably I should move somewhere the name parameter in line above ?
                intent.putExtra("EXTRA_VALUE", value);
                startActivityForResult(intent, EDIT_VALUE_REQUEST_CODE);
            }
        });
    }

    /**
     * Every time the activity resumes reading from DB happens
     * And these values displays in the activity
     */
    @Override
    protected void onResume() {
        super.onResume();

        try {
            // create object for creating DB and controlling its version
            if (mNotes == null) {
                mNotes = mDbHelper.readNotesFromDb();
            }
        }catch (Exception exc) {
            // Exception occurs when there's some trouble with reading data from DB
            if (BuildConfig.DEBUG) Log.d(DEBUG_LOG, exc.getMessage());

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
    }

    /**
     * Executes when user clicks on fabAddValue
     * It creates intent and starts AddValueActivity for adding new value
     * @param v Not used here, required for making possible to set this method
     *          as OnClickListener for button
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab_add_value:
                // Add new value
                Intent intent = new Intent(getApplicationContext(), AddValueActivity.class);
                intent.putExtra("activity_action", ADD_VALUE_REQUEST_CODE);
                startActivityForResult(intent, ADD_VALUE_REQUEST_CODE);
                break;
        }
    }

    /**
     * Executes when user returns to MainActivity from AddValueActivity.
     * Depending on requestCode adds new or edits existing value
     * and updates activity's content
     * @param requestCode Defines what operation has been made by user
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(data != null){
            if(resultCode == RESULT_OK){
                Note value = data.getParcelableExtra("EXTRA_VALUE");

                switch (requestCode){
                    case ADD_VALUE_REQUEST_CODE:
                        mDbHelper.writeNoteToDb(value);
                        break;
                    case EDIT_VALUE_REQUEST_CODE:
                        mDbHelper.editNote(mChosenElementId, value);
                        break;
                }
                mNotes = mDbHelper.readNotesFromDb();
                displayNotes();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * Making ListView lvNotes to show values of mNotes
     */
    private void displayNotes(){
        NotesListAdapter adapter =
                new NotesListAdapter(this, new ArrayList(mNotes.values()));
        lvNotes.setAdapter(adapter);
    }
}

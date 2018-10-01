package com.example.dimon.reminder;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

public class AddValueActivity extends AppCompatActivity {
    int mActivityAction;            // Defines type of action: add or edit value
    Calendar mDateAndTime = null;   // Date/time of the note

    Button btnDate, btnTime;
    Switch swUseDate;
    TextView tvCaption;
    TextView tvContent;

    /**
     * setting up Time Set Listener (chooses time for reminder)
     */
    final TimePickerDialog.OnTimeSetListener t = new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            mDateAndTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
            mDateAndTime.set(Calendar.MINUTE, minute);
            updateTimeButtonText();
        }
    };

    /**
     * setting up Date Set Listener (chooses date for reminder)
     */
    final DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mDateAndTime.set(Calendar.YEAR, year);
            mDateAndTime.set(Calendar.MONTH, monthOfYear);
            mDateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateDateButtonText();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_value);

        btnDate = findViewById(R.id.button_date);
        btnTime = findViewById(R.id.button_time);
        swUseDate = findViewById(R.id.switch_date);
        tvCaption = findViewById(R.id.edit_text_caption);
        tvContent = findViewById(R.id.edit_text_content);

        Intent currentIntent = getIntent();
        mActivityAction = currentIntent.getIntExtra("EXTRA_ACTIVITY_ACTION",
                MainActivity.ADD_VALUE_REQUEST_CODE);

        if (mActivityAction == MainActivity.EDIT_VALUE_REQUEST_CODE){
            Note value = currentIntent.getParcelableExtra("EXTRA_VALUE");
            tvCaption.setText(value.getCaption());
            tvContent.setText(value.getContent());

            Date date = value.getReminderDate();

            if (date != null){
                mDateAndTime = Calendar.getInstance();
                mDateAndTime.setTime(date);
                swUseDate.setChecked(true);
                updateDateButtonText();
                updateTimeButtonText();
                setDateTimeButtonsActivity();
            }
        }
    }


    /**
     * Setting up text in btnDate. The text shows date from mDateAndTime variable
      */
    private void updateDateButtonText() {
        btnDate.setText(DateUtils.formatDateTime(this,
                mDateAndTime.getTimeInMillis(),
                DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR));
    }

    /**
     * Setting up text in btnTime. The text shows time from mDateAndTime variable
     */
    private void updateTimeButtonText() {
        btnTime.setText(DateUtils.formatDateTime(this,
                mDateAndTime.getTimeInMillis(), DateUtils.FORMAT_SHOW_TIME));
    }

    /**
     * Setting up btnDate's and btnTime's content and availability
     * according to swUseDate checked state
     */
    public void setDateTimeButtonsActivity(){
        setDateTimeButtonsActivity(null);
    }

    /**
     * Setting up btnDate's and btnTime's content and availability
     * according to swUseDate checked state
     * @param v Not used here, required for making possible to set this method
     *          as OnClickListener for switch
     */
    public void setDateTimeButtonsActivity(View v){
        boolean useDate = swUseDate.isChecked();

        btnDate.setEnabled(useDate);
        btnTime.setEnabled(useDate);
        if (useDate){
            if (mDateAndTime == null) mDateAndTime = Calendar.getInstance();
            updateDateButtonText();
            updateTimeButtonText();
        }
        else{
            mDateAndTime = null;
            btnDate.setText(getString(R.string.date));
            btnTime.setText(getString(R.string.time));
        }
    }

    /**
     * Shows Date Picker to choose date for reminder
     * @param v Not used here, required for making possible to set this method
     *          as OnClickListener for button
     */
    public void showDatePicker(View v) {
        new DatePickerDialog(this,
                d,
                mDateAndTime.get(Calendar.YEAR),
                mDateAndTime.get(Calendar.MONTH),
                mDateAndTime.get(Calendar.DAY_OF_MONTH))
                .show();
    }

    /**
     * Shows Time Picker to choose time for reminder
     * @param v Not used here, required for making possible to set this method
     *          as OnClickListener for button
     */
    public void showTimePicker(View v) {
        new TimePickerDialog(this,
                t,
                mDateAndTime.get(Calendar.HOUR_OF_DAY),
                mDateAndTime.get(Calendar.MINUTE), true)
                .show();
    }

    /**
     * Preparing data of note to be sent as result to MainActivity
     * @param v Not used here, required for making possible to set this method
     *          as OnClickListener for button
     */
    public void applyNoteAndFinish(View v) {
        if(isValuesValid()) {
            Intent intent = new Intent();

            Note value = new Note(tvCaption.getText().toString(),
                    mDateAndTime == null ? null : mDateAndTime.getTime(),
                    tvContent.getText().toString());

            intent.putExtra("EXTRA_VALUE", value);

            setResult(RESULT_OK, intent);
            finish();
        }
        else{
            Toast.makeText(getApplicationContext(), getResources().getText
                    (R.string.values_not_valid), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Checks if values of the note is valid
     * @return Values are valid
     */
    public boolean isValuesValid(){
        return tvCaption.getText().length() != 0;
    }
}

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
import java.util.GregorianCalendar;

public class AddValueActivity extends AppCompatActivity {
    Calendar dateAndTime = null;
    Button btnDate, btnTime;
    Switch swUseDate;
    TextView tvCaption;
    TextView tvContent;
    int activityAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_value);

        btnDate = findViewById(R.id.btnDate);
        btnTime = findViewById(R.id.btnTime);
        swUseDate = findViewById(R.id.swUseDate);
        tvCaption = findViewById(R.id.etCaption);
        tvContent = findViewById(R.id.et2Content);

        activityAction = getIntent().getIntExtra("activity_action", MainActivity.ADD_VALUE_REQUEST_CODE);
        if(activityAction == MainActivity.EDIT_VALUE_REQUEST_CODE){
            Intent currentIntent = getIntent();
            tvCaption.setText(currentIntent.getStringExtra("caption"));
            tvContent.setText(currentIntent.getStringExtra("content"));
            try {
                Date date = Note.format.parse(currentIntent.getStringExtra("date"));
                dateAndTime = Calendar.getInstance();
                dateAndTime.setTime(date);
            }catch (ParseException pex){
                dateAndTime = null;
                Log.d(MainActivity.DEBUG_LOG, "Parse exception of datetime, msg = " + pex.getMessage());
            }

            if (dateAndTime != null){
                swUseDate.setChecked(true);
                setInitialDate();
                setInitialTime();
                setDateTimeButtonsActivity(btnDate);
            }

        }
    }

    // установка обработчика выбора времени
    TimePickerDialog.OnTimeSetListener t = new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            dateAndTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
            dateAndTime.set(Calendar.MINUTE, minute);
            setInitialTime();
        }
    };

    // установка обработчика выбора даты
    DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            dateAndTime.set(Calendar.YEAR, year);
            dateAndTime.set(Calendar.MONTH, monthOfYear);
            dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            setInitialDate();
        }
    };

    // установка начальных даты
    private void setInitialDate() {
        btnDate.setText(DateUtils.formatDateTime(this,
                dateAndTime.getTimeInMillis(),
                DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR));
    }


    // установка начального времени
    private void setInitialTime() {
        btnTime.setText(DateUtils.formatDateTime(this,
                dateAndTime.getTimeInMillis(), DateUtils.FORMAT_SHOW_TIME));
    }

    // установить активность кнопок
    public void setDateTimeButtonsActivity(View v){
        dateAndTime = swUseDate.isChecked()
                ? (dateAndTime != null ? dateAndTime : Calendar.getInstance() )
                : null;
        btnDate.setEnabled(swUseDate.isChecked());
        btnDate.setText(swUseDate.isChecked()
                ? DateUtils.formatDateTime(this, dateAndTime.getTimeInMillis(),
                DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR)
                : getResources().getText(R.string.date));

        btnTime.setEnabled(swUseDate.isChecked());
        btnTime.setText(swUseDate.isChecked()
                ? DateUtils.formatDateTime(this, dateAndTime.getTimeInMillis(),
                        DateUtils.FORMAT_SHOW_TIME)
                : getResources().getText(R.string.time));
    }

    // отображаем диалоговое окно для выбора даты
    public void setDate(View v) {
        new DatePickerDialog(this, d,
                dateAndTime.get(Calendar.YEAR),
                dateAndTime.get(Calendar.MONTH),
                dateAndTime.get(Calendar.DAY_OF_MONTH))
                .show();
    }

    // отображаем диалоговое окно для выбора времени
    public void setTime(View v) {
        new TimePickerDialog(this, t,
                dateAndTime.get(Calendar.HOUR_OF_DAY),
                dateAndTime.get(Calendar.MINUTE), true)
                .show();
    }

    // Формирование данных для создания записи в родительском окне
    public void applyNoteAndFinish(View v) {
        if(isValuesValid()) {
            Intent intent = new Intent();
            try {
                long dateMills = dateAndTime != null ? dateAndTime.getTimeInMillis() : 0;
                intent.putExtra("note_caption", tvCaption.getText().toString());
                intent.putExtra("note_content", tvContent.getText().toString());
                intent.putExtra("note_date", Long.toString(dateMills));
                // idk is better to use long digit or string format for translating dates btw activities
            }catch (Exception exc){
                Toast.makeText(getApplicationContext(), exc.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
            setResult(RESULT_OK, intent);
            finish();
        }
        else{
            Toast.makeText(getApplicationContext(), getResources().getText
                    (R.string.values_not_valid), Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isValuesValid(){
        return tvCaption.getText().length() != 0;
    }
}

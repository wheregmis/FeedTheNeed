package com.example.feedtheneed.presentation.event;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.app.Dialog;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.View;
import android.widget.TimePicker;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.feedtheneed.R;
import com.example.feedtheneed.domain.model.Event;

import java.util.Calendar;

public class AddEventActivity extends AppCompatActivity {

    private Calendar calendar;
    private TextView dateview;
    private TextView timeview;
    private TextView eventName;
    private TextView eventHost;
    private TextView eventDescription;
    private int year, month, day,hour,minute;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        dateview = (TextView) findViewById(R.id.dateview);
        eventName = (TextView) findViewById(R.id.eventName);
        eventHost = (TextView) findViewById(R.id.eventHost);
        eventDescription = (TextView) findViewById(R.id.eventDescription);
        timeview = (TextView) findViewById(R.id.timeview);

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);

        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
      minute = calendar.get(Calendar.MINUTE);
        showDate(year, month+1, day);
        showTime(hour, minute);
    }
    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(999);
        Toast.makeText(getApplicationContext(), "ca",
                        Toast.LENGTH_SHORT)
                .show();
    }
    @SuppressWarnings("deprecation")
    public void setTime(View view) {
        showDialog(998);
        Toast.makeText(getApplicationContext(), "ca",
                        Toast.LENGTH_SHORT)
                .show();
    }
    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this,
                    myDateListener, year, month, day);
        }
        if (id == 998) {
            return new TimePickerDialog(this,
                    myTimeListener, hour, minute, DateFormat.is24HourFormat(this));

        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub
                    // arg1 = year
                    // arg2 = month
                    // arg3 = day
                    showDate(arg1, arg2+1, arg3);
                }
            };
    private TimePickerDialog.OnTimeSetListener myTimeListener = new
            TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int i, int i1) {
                    showTime(i,i1);
                }
            };
//    private TimePickerDialog.OnTimeSetListener myTimeListener = new
//            TimePickerDialog.OnTimeSetListener(){
//                @Override
//                public void onTimeSet(TimePicker arg0,
//                                      int arg1, int arg2, int arg3) {
//                    // TODO Auto-generated method stub
//                    // arg1 = year
//                    // arg2 = month
//                    // arg3 = day
//                    showDate(arg1, arg2+1, arg3);
//                }
//            };
    private void showDate(int year, int month, int day) {
        dateview.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));
    }
    private void showTime(int hours, int minutes) {
        timeview.setText(new StringBuilder().append(hours).append(":")
                .append(minutes));
    }
    public void createEvent(View view){
        Event events =
        new Event("testEventID",eventName.getText().toString(),eventHost.getText().toString(), eventDescription.getText().toString(),
                dateview.getText().toString(),timeview.getText().toString());
    }
}
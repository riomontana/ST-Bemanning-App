package com.example.nikolapajovic.logindemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.CalendarView;
import android.widget.TextView;

public class SecondActivity extends AppCompatActivity {

    CalendarView calendarView;
    TextView myDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        calendarView = (CalendarView) findViewById(R.id.calenderView);
        myDate = (TextView) findViewById(R.id.myDate);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                String date = (month +  1) + "/" + dayOfMonth + "/" + year;
                myDate.setText(date);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.id_hem:

        }

        switch (item.getItemId()) {
            case R.id.id_jobb:

        }

        switch (item.getItemId()) {
            case R.id.id_info:

        }

        switch (item.getItemId()) {
            case R.id.id_länk:

        }

        switch (item.getItemId()) {
           case R.id.id_loggaUt:

               Intent intent = new Intent(SecondActivity.this, MainActivity.class);
               startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
  
}

package com.example.nikolapajovic.stbemanning.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.nikolapajovic.stbemanning.R;
import com.example.nikolapajovic.stbemanning.api.Api;
import com.example.nikolapajovic.stbemanning.api.ApiListener;
import com.example.nikolapajovic.stbemanning.api.PerformNetworkRequest;
import com.example.nikolapajovic.stbemanning.model.User;
import com.example.nikolapajovic.stbemanning.model.WorkShift;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Main activity for viewing the work calendar etc.
 *
 * @author Alex Giang, Sanna Roengaard, Simon Borjesson,
 * Lukas Persson, Nikola Pajovic, Linus Forsberg
 */

public class CalendarActivity extends AppCompatActivity implements ApiListener {

    private CompactCalendarView compactCalendar;
    private SimpleDateFormat dateFormatMonth = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private TextView tvInfo;
    private TextView tvMonthYear;
    private List<WorkShift> workShiftList = new ArrayList<>();
    private List<Event> eventList = new ArrayList<>();
    private User user;
    private DrawerLayout mDrawerLayout;
    private NavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        tvMonthYear = (TextView) findViewById(R.id.tvMonthYear);
        tvInfo = (TextView) findViewById(R.id.tvInfo);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
        actionbar.setTitle(null);
        compactCalendar = (CompactCalendarView) findViewById(R.id.compactcalendar_view);
        compactCalendar.setUseThreeLetterAbbreviation(true);
        tvMonthYear.setText(dateFormatMonth.format(compactCalendar.getFirstDayOfCurrentMonth()));
        user = (User) getIntent().getSerializableExtra("user");
        tvInfo.setText("Inloggad: " + user.getFirstName() + " " + user.getLastName());
        getWorkShifts(user.getUserId());

        mDrawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        // listener for side-menu
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {

                        menuItem.setChecked(true);
                        // close drawer when item is tapped
                        mDrawerLayout.closeDrawers();

                        switch (menuItem.getItemId()) {
                            case R.id.nav_special_workshift:

                                break;

                            case R.id.nav_info:

                                break;

                            case R.id.nav_weblink:
                                // todo lägg till funktion så att användaren är inloggad på webben
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                                        Uri.parse("https://stskolbemanning.se/profile.php"));
                                startActivity(browserIntent);
                                break;

                            case R.id.nav_logout:
                                AlertDialog.Builder adb = new AlertDialog.Builder(CalendarActivity.this);
                                adb.setTitle("Är du säker på att du vill logga ut?");
                                adb.setPositiveButton("Logga ut", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        SharedPreferences.Editor prefEditor =
                                                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
                                        prefEditor.clear();
                                        prefEditor.apply();
                                        Intent intent = new Intent(CalendarActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                    }
                                });
                                adb.setNegativeButton("Avbryt", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                                adb.show();

                                break;
                        }
                        return true;
                    }
                });

        // listener for calender
        compactCalendar.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                for (int i = 0; i < eventList.size(); i++) {
                    Event event = eventList.get(i);
                    Date dateEvent = new Date(event.getTimeInMillis());
                    Log.d("date clicked", dateClicked.toString());
                    Log.d("event time", dateEvent.toString());

                    if (dateClicked.getTime() == event.getTimeInMillis()) {
                        WorkShift workShift = workShiftList.get(i);
                        showWorkShiftDialog(workShift);
                    }
                }
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                tvMonthYear.setText(dateFormatMonth.format(firstDayOfNewMonth));
            }
        });
    }

    /**
     * Opens side menu on click on navigation button
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                navigationView.bringToFront();
                mDrawerLayout.requestLayout();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showWorkShiftDialog(WorkShift workShift) {
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setTitle("Arbetspass \n")
                .setMessage("Företag: " + workShift.getCompany() +
                        "\nFrån: " + workShift.getShiftStart() +
                        "\nTill: " + workShift.getShiftEnd());
        adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // todo lägg in länk till karta??
            }
        });
        adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        adb.show();
    }

    public void getWorkShifts(int userId) {
        HashMap<String, String> params = new HashMap<>();

        params.put("user_id", String.valueOf(userId));

        PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_GET_WORK_SHIFTS, params, this);
        request.execute();
    }

    @Override
    public void apiResponse(JSONObject response) {
        Log.d("json ", response.toString());
        try {
            JSONArray jsonArray = response.getJSONArray("work_shifts");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                int workShiftId = jsonObject.getInt("work_shift_id");
                String shiftStart = jsonObject.getString("shift_start");
                String shiftEnd = jsonObject.getString("shift_end");
                String company = jsonObject.getString("company");
                WorkShift workShift = new WorkShift(workShiftId, shiftStart, shiftEnd, company);
                workShiftList.add(workShift);
            }
            addWorkShiftsToCalender();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void addWorkShiftsToCalender() {
        for (WorkShift workShift : workShiftList) {
//            Log.d("Start", workShift.getShiftStart());
//            Log.d("End", workShift.getShiftEnd());
//            Log.d("Company", workShift.getCompany());
            String[] splitDateTime = workShift.getShiftStart().split(" ");
            Event event = new Event(R.color.colorAccent, convertDate(splitDateTime[0]), "work shift");
            eventList.add(event);
            compactCalendar.addEvent(event);
        }
    }

    private long convertDate(String shiftStart) {
        long convertedDate = 0;
        try {
            Date date = sdf.parse(shiftStart);
            convertedDate = date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return convertedDate;
    }

    /**
     * Disable back button
     */
    @Override
    public void onBackPressed() {
        //remove call to the super class
        //super.onBackPressed();
    }
}

package shugal.com.mattendance;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;

/**
 * Created by abhishek on 27/10/15.
 */
public class DatewiseList extends AppCompatActivity {

    TextView dateText;
    ListView lectureListView;
    Toolbar toolbar;

    Calendar calender = Calendar.getInstance();
    int Year = calender.get(Calendar.YEAR),
            Month = calender.get(Calendar.MONTH),
            Day = calender.get(Calendar.DAY_OF_MONTH);

    String date = makeDate(Day, (Month + 1), Year);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.datewisedata_toolbar);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Datewise Attendance");

        dateText = (TextView) findViewById(R.id.dateText);
        dateText.setText(date);
        lectureListView = (ListView) findViewById(R.id.list_of_lectures);
        printDatewiseData(date);
    }

    private void printDatewiseData(String date) {


        DatabaseHelper db = new DatabaseHelper(this);

        //Gets date from this activity

        View emptyLinearLayout = findViewById(R.id.frame);


        if (db.isDatelistEmpty(date)) {
            lectureListView.setVisibility(View.GONE);
            emptyLinearLayout.setVisibility(View.VISIBLE);
            TextView emptyText = (TextView) findViewById(R.id.emptyListText);
            Typeface tf= Typeface.createFromAsset(getAssets(), "fonts/empty_list.ttf");
            emptyText.setTypeface(tf);

        } else {
            emptyLinearLayout.setVisibility(View.GONE);
            lectureListView.setVisibility(View.VISIBLE);
            List<DatewiseData> contacts = db.showAllDatewiseData(date);


            DatewiseListAdapter adapter =
                    new DatewiseListAdapter(getApplicationContext(), contacts);
            lectureListView.setAdapter(adapter);
        }

        db.close();
    }

    private String makeDate(int day_of_month, int month, int year) {
        String date = day_of_month + " " + makeMonth(month) + " " + year;
        return date;
    }

    private String makeMonth(int month) {
        String mMonth = "";
        if (month == 1) {
            mMonth = "January";
        } else if (month == 2) {
            mMonth = "February";
        } else if (month == 3) {
            mMonth = "March";
        } else if (month == 4){
            mMonth = "April";
        } else if (month == 5) {
            mMonth = "May";
        } else if (month == 6) {
            mMonth = "June";
        } else if (month == 7) {
            mMonth = "July";
        } else if (month == 8) {
            mMonth = "August";
        } else if (month == 9) {
            mMonth = "September";
        } else if (month == 10) {
            mMonth = "October";
        } else if (month == 11) {
            mMonth = "November";
        } else if (month == 12) {
            mMonth = "December";
        }

        return mMonth;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.days_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if (id == R.id.add_lecture) {
            showDialog(1);
            return true;
        } else  if (id == R.id.today){
            date = makeDate(calender.get(Calendar.DAY_OF_MONTH),
                    (calender.get(Calendar.MONTH) + 1),
                    calender.get(Calendar.YEAR));
            printDatewiseData(date);
            Toast.makeText(getApplicationContext(), date, Toast.LENGTH_SHORT).show();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public Dialog onCreateDialog(int id) {

        if (id == 1) {
            return new DatePickerDialog(this, dPickerListener, Year, Month, Day);
        }

        return null;
    }


    private DatePickerDialog.OnDateSetListener dPickerListener = new DatePickerDialog.OnDateSetListener() {


        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            Year = year;
            Month = monthOfYear;
            Day = dayOfMonth;

            String message = makeDate(dayOfMonth, (monthOfYear+1), year);
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            dateText.setText(message);
            printDatewiseData(message);

        }
    };
}

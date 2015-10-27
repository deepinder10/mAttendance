package shugal.com.mattendance;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;

public class TodayAttendance extends AppCompatActivity {

    ListView lectureList;
    TextView mark;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today_attendance);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Mark Todays's attendance");

        mark = (TextView) findViewById(R.id.mark);
        Typeface tf= Typeface.createFromAsset(getAssets(), "fonts/empty_list.ttf");
        mark.setTypeface(tf);

        lectureList = (ListView) findViewById(R.id.list_of_lectures);
        printLectures();

        lectureList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LectureData data = (LectureData) parent.getItemAtPosition(position);
                startEditDialog(data);
            }
        });

    }

    String makeDate(int day_of_month, int month, int year) {
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

    private void startEditDialog(final LectureData data) {
        final Context context = this;
        LayoutInflater inflater = LayoutInflater.from(context);

        Calendar calender = Calendar.getInstance();
        int Year = calender.get(Calendar.YEAR),
                Month = calender.get(Calendar.MONTH),
                Day = calender.get(Calendar.DAY_OF_MONTH);

        String date = makeDate(Day, (Month + 1), Year);

        View dialogView = inflater.inflate(R.layout.attendance_dialog, null);

        final AlertDialog.Builder customEventDialog = new AlertDialog.Builder(context);

        final RadioButton presentBtn = (RadioButton) dialogView.findViewById(R.id.presentBtn);
        final RadioButton absentBtn = (RadioButton) dialogView.findViewById(R.id.absentBtn);

        final DatewiseData ddta = new DatewiseData();
        ddta.setSubject(data.get_lecture_name());
        ddta.setDate(date);
        customEventDialog.setView(dialogView);
        customEventDialog.setTitle(data.get_lecture_name());
        customEventDialog.setCancelable(true);
        customEventDialog.setPositiveButton("Mark", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                DatabaseHelper db = new DatabaseHelper(context);
                if (presentBtn.isChecked()) {
                    Toast.makeText(getApplicationContext(), "Present Marked", Toast.LENGTH_SHORT).show();
                    ddta.setStatus("Present");
                    db.addDatewiseDatta(ddta);
                    db.updatePresents(data);
                } else if (absentBtn.isChecked()) {
                    Toast.makeText(getApplicationContext(), "Absent Marked", Toast.LENGTH_SHORT).show();
                    ddta.setStatus("Absent");
                    db.addDatewiseDatta(ddta);
                    db.updateAbsents(data);
                } else {
                    Toast.makeText(getApplicationContext(), "No Class for Today", Toast.LENGTH_SHORT).show();
                }
                db.close();
                printLectures();
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });


        customEventDialog.create().show();
    }

    private void printLectures() {


        DatabaseHelper db = new DatabaseHelper(this);

        //Gets date from this activity

        View emptyLinearLayout = findViewById(R.id.frame);


        if (db.isLectureListEmpty()) {
            lectureList.setVisibility(View.GONE);
            emptyLinearLayout.setVisibility(View.VISIBLE);
            TextView emptyText = (TextView) findViewById(R.id.emptyListText);
            Typeface tf= Typeface.createFromAsset(getAssets(), "fonts/empty_list.ttf");
            emptyText.setTypeface(tf);

        } else {
            emptyLinearLayout.setVisibility(View.GONE);
            lectureList.setVisibility(View.VISIBLE);
            List<LectureData> contacts = db.showAllLectures();


            TodayAttendanceCustomListAdapter adapter =
                    new TodayAttendanceCustomListAdapter(getApplicationContext(), contacts);
            lectureList.setAdapter(adapter);
        }

        db.close();
    }


}
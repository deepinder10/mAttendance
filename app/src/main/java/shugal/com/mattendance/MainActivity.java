package shugal.com.mattendance;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;


public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener {

    ListView lectureList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("My Attendance");

        // timeTable = (Button) findViewById(R.id.timetableButton);

        SampleAlarmReceiver alarm = new SampleAlarmReceiver();
        alarm.setAlarm(this);

        // Navigation Drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        // Floating Action Button
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewLecture();
            }
        });

        lectureList = (ListView) findViewById(R.id.list_of_lectures);
        registerForContextMenu(lectureList);
        printLectures();

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            finish();
            //super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        DatabaseHelper db = new DatabaseHelper(getApplicationContext());

        if (id == R.id.showTimetable) {

            startActivity(new Intent(MainActivity.this, DaysActivity.class));

        } else if (id == R.id.todayAttendance) {

            if (db.isLectureListEmpty()) {
                Snackbar.make(getCurrentFocus(), "Add some lectures first", Snackbar.LENGTH_LONG)
                        .setAction("", null).show();
                db.close();
            } else {
                startActivity(new Intent(MainActivity.this, TodayAttendance.class));
            }

        } else if (id == R.id.dangerZone) {

            if (db.isLectureListEmpty()) {
                Snackbar.make(getCurrentFocus(), "Add some lectures first", Snackbar.LENGTH_LONG)
                        .setAction("", null).show();
                db.close();
            } else {
                startActivity(new Intent(MainActivity.this, Dangerzone.class));
            }

        }else if (id == R.id.reset) {
            resetAll();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void resetAll() {

        final Context context = this;

        final AlertDialog.Builder customEventDialog = new AlertDialog.Builder(context);

        customEventDialog.setTitle("Delete Everything ?");
        customEventDialog.setCancelable(true);

        customEventDialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DatabaseHelper db = new DatabaseHelper(getApplicationContext());
                db.removeAll();
                printLectures();
                db.close();
                Snackbar.make(getCurrentFocus(), "Delete Successful", Snackbar.LENGTH_LONG)
                        .setAction("", null).show();
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });


        customEventDialog.create().show();
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {

        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.delete_lecture, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        DatabaseHelper db = new DatabaseHelper (this);

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        LectureData data = (LectureData) lectureList.getItemAtPosition(info.position);

        switch (item.getItemId()) {
            case R.id.action_delete:
                db.deleteLecture(data);
                printLectures();
                Toast.makeText(getApplicationContext(), "Deleted", Toast.LENGTH_SHORT).show();
                db.close();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }


    private void addNewLecture() {
        final Context context = this;
        LayoutInflater inflater = LayoutInflater.from(context);

        View dialogView = inflater.inflate(R.layout.new_lecture, null);

        final AlertDialog.Builder customEventDialog = new AlertDialog.Builder(context);

        final EditText lecturename = (EditText) dialogView.findViewById(R.id.lecture_name);
        //amountText.setText("0");

        customEventDialog.setView(dialogView);
        customEventDialog.setTitle("New Lecture");
        customEventDialog.setCancelable(true);

        customEventDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (lecturename.getText().toString().equals("")) {
                    String message = "You did not Enter any Lecture";
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                } else {
                    String nameOfLecture = lecturename.getText().toString();

                    LectureData data = new LectureData(nameOfLecture, 0, 0);
                    DatabaseHelper db = new DatabaseHelper(getApplicationContext());
                    db.addLecture(data);
                    printLectures();
                    Toast.makeText(getApplicationContext(), "Lecture Added", Toast.LENGTH_SHORT).show();
                }
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });


        customEventDialog.create().show();
    }

    public void printLectures() {


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

            LectureCustomList adapter = new LectureCustomList(this, contacts);
            lectureList.setAdapter(adapter);
        }

        db.close();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.info) {
            startActivity(new Intent(MainActivity.this, InfoActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}



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
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;


public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener {

    RecyclerView lectureList;
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

        lectureList = (RecyclerView) findViewById(R.id.list_of_lectures);
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

        } else if(id == R.id.email){
            if (db.isLectureListEmpty()) {
                Snackbar.make(getCurrentFocus(), "Add some lectures first", Snackbar.LENGTH_LONG)
                        .setAction("", null).show();
                db.close();
            } else {
                startActivity(new Intent(MainActivity.this, SendEmail.class));
            }
        }  else if (id == R.id.dangerZone) {

            if (db.isLectureListEmpty()) {
                Snackbar.make(getCurrentFocus(), "Add some lectures first", Snackbar.LENGTH_LONG)
                        .setAction("", null).show();
                db.close();
            } else {
                startActivity(new Intent(MainActivity.this, Dangerzone.class));
            }

        } else if (id == R.id.reset) {
            resetAll();
        } else if (id == R.id.dateList) {
            if (db.isLectureListEmpty()) {
                Snackbar.make(getCurrentFocus(), "Add some lectures first", Snackbar.LENGTH_LONG)
                        .setAction("", null).show();
                db.close();
            } else {
                startActivity(new Intent(MainActivity.this, DatewiseList.class));
            }
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

            RVAdapter adapter = new RVAdapter(contacts);

            lectureList.setLayoutManager(new LinearLayoutManager(this));
            lectureList.setAdapter(adapter);
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

    public class RVAdapter extends RecyclerView.Adapter<RVAdapter.PersonViewHolder>{

        List<LectureData> persons;

        public RVAdapter(List<LectureData> persons){
            this.persons = persons;
        }

        @Override
        public PersonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.lecture_list2, parent, false);
            PersonViewHolder pvh = new PersonViewHolder(v);
            return pvh;
        }

        @Override
        public void onBindViewHolder(final PersonViewHolder holder, int position) {
            final LectureData data = persons.get(position);
            holder.lectureName.setText(data.get_lecture_name());
            holder.lectureAbsents.setText("Absents: "+data.get_absents());
            holder.lecturePresents.setText("Presents: " + data.get_presents());
            holder.lecturePercent.setText(data.getPercent()+"%");
            holder.cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), data.get_lecture_name(), Toast.LENGTH_SHORT).show();
                }
            });
            holder.cv.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    final PopupMenu popup = new PopupMenu(v.getContext(), v);
                    popup.getMenuInflater().inflate(R.menu.delete_lecture, popup.getMenu());
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            DatabaseHelper db = new DatabaseHelper (MainActivity.this);
                            db.deleteLecture(data);
                            printLectures();
                            Toast.makeText(getApplicationContext(), "Deleted", Toast.LENGTH_SHORT).show();
                            db.close();
                            return true;
                        }
                    });
                    popup.show();
                    return true;
                }


            });
        }

        @Override
        public int getItemCount() {
            return persons.size();
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
        }

        public class PersonViewHolder extends RecyclerView.ViewHolder {
            CardView cv;
            TextView lectureName;
            TextView lectureAbsents;
            TextView lecturePresents;
            TextView lecturePercent;

            PersonViewHolder(View itemView) {
                super(itemView);
                cv = (CardView) itemView.findViewById(R.id.cv);
                lectureName = (TextView) itemView.findViewById(R.id.lectureName);
                lectureAbsents = (TextView)itemView.findViewById(R.id.lectureAbsents);
                lecturePresents = (TextView) itemView.findViewById(R.id.lecturePresents);
                lecturePercent = (TextView) itemView.findViewById(R.id.lecturePercent);
            }
        }

    }

}



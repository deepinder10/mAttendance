package shugal.com.mattendance;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by abhishek on 24/10/15.
 */
public class Dangerzone extends AppCompatActivity {

    ListView lectureList;
    TextView mark;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dangerzone);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Shortages");

        mark = (TextView) findViewById(R.id.mark);
        Typeface tf= Typeface.createFromAsset(getAssets(), "fonts/empty_list.ttf");
        mark.setTypeface(tf);

        lectureList = (ListView) findViewById(R.id.list_of_lectures);
        printLectures();

    }

    private void printLectures() {


        DatabaseHelper db = new DatabaseHelper(this);

        //Gets date from this activity

        View emptyLinearLayout = findViewById(R.id.frame);


        if (db.isDangerListEmpty()) {
            lectureList.setVisibility(View.GONE);
            emptyLinearLayout.setVisibility(View.VISIBLE);
            TextView emptyText = (TextView) findViewById(R.id.emptyListText);
            Typeface tf= Typeface.createFromAsset(getAssets(), "fonts/empty_list.ttf");
            emptyText.setTypeface(tf);

        } else {
            emptyLinearLayout.setVisibility(View.GONE);
            lectureList.setVisibility(View.VISIBLE);
            List<LectureData> contacts = db.showShortageLectures();


            DangerListAdapter adapter =
                    new DangerListAdapter(getApplicationContext(), contacts);
            lectureList.setAdapter(adapter);
        }

        db.close();
    }
}

package shugal.com.mattendance;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button timeTable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timeTable = (Button) findViewById(R.id.timetableButton);

    }

    public void ShowTimetable(View v) {
        startActivity(new Intent(MainActivity.this, DaysActivity.class));
    }
}

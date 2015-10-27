package shugal.com.mattendance;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.support.v7.widget.Toolbar;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Aayush on 26-10-2015.
 */
public class SendEmail extends AppCompatActivity {
    EditText tot , sub , msg;

    Spinner lectureNameSpinner;

    Toolbar toolbar;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.email_toolbar);

        tot=(EditText)findViewById(R.id.emailid);
        sub=(EditText)findViewById(R.id.sub);
        msg=(EditText)findViewById(R.id.msglist);
        lectureNameSpinner = (Spinner) findViewById(R.id.selectsubject);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Share Via Email");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fillSpinner();

        fab.setOnClickListener(new View.OnClickListener(){
            public void onClick(View arg0 ){
                String to = tot.getText().toString();
                String subject = sub.getText().toString();
                String message = msg.getText().toString();
                Intent email = new Intent(Intent.ACTION_SEND);
                email.putExtra(Intent.EXTRA_EMAIL, new String[]{ to });
                email.putExtra(Intent.EXTRA_SUBJECT, subject);
                email.putExtra(Intent.EXTRA_TEXT, message);

                email.setType("message/rfc822");

                startActivity(Intent.createChooser(email, "Choose an Email client :"));

            }
        });


    }

    private void fillSpinner() {
        DatabaseHelper db = new DatabaseHelper(this);

        ArrayList<String> lectureList = db.showLectureList();

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, lectureList);

        lectureNameSpinner.setAdapter(dataAdapter);
        lectureNameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                Toast.makeText(getApplicationContext(), item, Toast.LENGTH_SHORT).show();
                DatabaseHelper db = new DatabaseHelper(getApplicationContext());
                String message = db.attendanceTillDate(item);

                StringBuffer share = new StringBuffer();
                share.append("Respected Sir,\n");
                share.append("Following is a list of my attendance till date in subject " + item + "\n\n");
                share.append(message);

                msg.setText(share.toString());
                db.close();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        db.close();
    }
}

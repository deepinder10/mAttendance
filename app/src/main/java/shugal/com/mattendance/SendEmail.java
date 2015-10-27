package shugal.com.mattendance;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
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

    TextInputLayout toLayout, subjectLayout, messageLayout;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.email_toolbar);

        tot=(EditText)findViewById(R.id.emailid);
        sub=(EditText)findViewById(R.id.sub);
        msg=(EditText)findViewById(R.id.msglist);
        lectureNameSpinner = (Spinner) findViewById(R.id.selectsubject);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        toLayout = (TextInputLayout) findViewById(R.id.toLayout);
        subjectLayout = (TextInputLayout) findViewById(R.id.subjectLayout);
        messageLayout = (TextInputLayout) findViewById(R.id.msgFormatLayout);

        tot.addTextChangedListener(new MyTextWatcher(tot));
        sub.addTextChangedListener(new MyTextWatcher(sub));
        msg.addTextChangedListener(new MyTextWatcher(msg));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Share Via Email");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fillSpinner();

        fab.setOnClickListener(new View.OnClickListener(){
            public void onClick(View arg0 ){
                checkValidation();

            }
        });


    }

    private void checkValidation() {
        /*if (!validateLectureNumber()) {
            return;
        }*/

        if (!validateEmail()) {
            return;
        }

        if (!validateSubject()) {
            return;
        }

        if (!validateMessage()) {
            return;
        }

        String to = tot.getText().toString();
        String subject = sub.getText().toString();
        String message = msg.getText().toString();
        Intent email = new Intent(Intent.ACTION_SEND);
        email.putExtra(Intent.EXTRA_EMAIL, new String[]{to });
        email.putExtra(Intent.EXTRA_SUBJECT, subject);
        email.putExtra(Intent.EXTRA_TEXT, message);

        email.setType("message/rfc822");

        startActivity(Intent.createChooser(email, "Choose an Email client :"));
        finish();

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

    private boolean validateEmail() {
        if (tot.getText().toString().trim().isEmpty()) {
            toLayout.setError("Please Enter Reciever's email id");
            requestFocus(tot);
            return false;
        } else {
            toLayout.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateSubject() {
        if (sub.getText().toString().trim().isEmpty()) {
            subjectLayout.setError("Please Enter a Subject");
            requestFocus(sub);
            return false;
        } else {
            subjectLayout.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateMessage() {
        if (msg.getText().toString().trim().isEmpty()) {
            messageLayout.setError("Please Enter a Message");
            requestFocus(msg);
            return false;
        } else {
            messageLayout.setErrorEnabled(false);
        }
        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.emailid:
                    validateEmail();
                    break;
                case R.id.sub:
                    validateSubject();
                    break;

                case R.id.msglist:
                    validateMessage();
                    break;
            }
        }
    }
}

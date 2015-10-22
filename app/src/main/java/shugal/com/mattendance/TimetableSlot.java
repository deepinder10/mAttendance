package shugal.com.mattendance;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by abhishek on 22/10/15.
 */
public class TimetableSlot extends AppCompatActivity {
    String date="";
    static final int TIME_DIALOG_ID = 0;
    private int pHour;
    private int pMinute;
    Toolbar toolbar;

    Spinner weekdaySpinner, lectureNameSpinner;

    EditText lectureNumber, lectureStartingTime, endingTime;

    TextInputLayout lectureNumberLayout, startingTimeLayout, endingTimeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_time_table_toolbar);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("New Timetable Slot");

        weekdaySpinner = (Spinner) findViewById(R.id.weekday);
        lectureNameSpinner = (Spinner) findViewById(R.id.lectureName);
        lectureNumber = (EditText) findViewById(R.id.lectureNumber);
        lectureStartingTime = (EditText) findViewById(R.id.startingTime);
        endingTime = (EditText) findViewById(R.id.endingTime);
        lectureNumberLayout = (TextInputLayout) findViewById(R.id.lectureNumberLayout);

        startingTimeLayout = (TextInputLayout) findViewById(R.id.startingTimeLayout);
        endingTimeLayout = (TextInputLayout) findViewById(R.id.endingTimeLayout);


        lectureNumber.addTextChangedListener(new MyTextWatcher(lectureNumber));
        endingTime.addTextChangedListener(new MyTextWatcher(endingTime));
        lectureStartingTime.addTextChangedListener(new MyTextWatcher(lectureStartingTime));


        lectureStartingTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(TIME_DIALOG_ID);
            }
        });

        endingTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(TIME_DIALOG_ID);
            }
        });



        fillSpinners();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkValidation();
            }

        });

    }

    private void checkValidation() {
        if (!validateLectureNumber()) {
            return;
        }

        if (!validateStartingTime()) {
            return;
        }

        if (!validateEndingTime()) {
            return;
        }

        Toast.makeText(getApplicationContext(), "Added", Toast.LENGTH_SHORT).show();
    }

    private void fillSpinners() {



        DatabaseHelper db = new DatabaseHelper(this);


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.weekday_array, android.R.layout.simple_spinner_item);

        weekdaySpinner.setAdapter(adapter);
        weekdaySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                Toast.makeText(getApplicationContext(), item, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayList<String> lectureList = db.showLectureList();

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, lectureList);

        lectureNameSpinner.setAdapter(dataAdapter);
        lectureNameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                Toast.makeText(getApplicationContext(), item, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        db.close();
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
                case R.id.lectureNumber:
                    validateLectureNumber();
                    break;
                case R.id.startingTime:
                    validateStartingTime();
                    break;
                case R.id.endingTime:
                    validateEndingTime();
                    break;
            }
        }
    }

    private boolean validateEndingTime() {
        if (endingTime.getText().toString().trim().isEmpty()) {
            endingTimeLayout.setError("Please Enter lecture ending time");
            requestFocus(endingTime);
            return false;
        } else {
            endingTimeLayout.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateStartingTime() {
        if (lectureStartingTime.getText().toString().trim().isEmpty()) {
            startingTimeLayout.setError("Please Enter lecture starting time");
            requestFocus(lectureStartingTime);
            return false;
        } else {
            startingTimeLayout.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateLectureNumber() {
        if (lectureNumber.getText().toString().trim().isEmpty()) {
            lectureNumberLayout.setError("Please Enter lecture number");
            requestFocus(lectureNumber);
            return false;
        } else {
            lectureNumberLayout.setErrorEnabled(false);
        }

        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private TimePickerDialog.OnTimeSetListener mTimeSetListener =
            new TimePickerDialog.OnTimeSetListener() {
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    pHour = hourOfDay;
                    pMinute = minute;
                    date = pad(pHour) + ":" + pad(pMinute);
                    displayToast();
                }
            };

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case TIME_DIALOG_ID:
                return new TimePickerDialog(this,
                        mTimeSetListener, pHour, pMinute, false);
        }
        return null;
    }

    private void displayToast() {
        Toast.makeText(this, date,   Toast.LENGTH_SHORT).show();

    }

    private static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }
}

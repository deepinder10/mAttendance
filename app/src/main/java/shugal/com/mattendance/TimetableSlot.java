package shugal.com.mattendance;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
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

import java.util.ArrayList;

/**
 * Created by abhishek on 22/10/15.
 */
public class TimetableSlot extends AppCompatActivity {
    static final int TIME_DIALOG_ID = 0;
    Toolbar toolbar;
    Spinner weekdaySpinner, lectureNameSpinner;
    EditText lectureStartingTime, endingTime;
    TextInputLayout startingTimeLayout, endingTimeLayout;
    private String date = "";
    private TimetableData data = new TimetableData();
    private boolean isStartingTimeSelected = false;
    private int pHour;
    private int pMinute;
    private TimePickerDialog.OnTimeSetListener mTimeSetListener =
            new TimePickerDialog.OnTimeSetListener() {
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    pHour = hourOfDay;
                    pMinute = minute;
                    date = pad(pHour) + ":" + pad(pMinute);
                    displayToast(isStartingTimeSelected);
                }
            };

    private static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }

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
        lectureStartingTime = (EditText) findViewById(R.id.startingTime);
        endingTime = (EditText) findViewById(R.id.endingTime);
        startingTimeLayout = (TextInputLayout) findViewById(R.id.startingTimeLayout);
        endingTimeLayout = (TextInputLayout) findViewById(R.id.endingTimeLayout);



        endingTime.addTextChangedListener(new MyTextWatcher(endingTime));
        lectureStartingTime.addTextChangedListener(new MyTextWatcher(lectureStartingTime));


        lectureStartingTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isStartingTimeSelected = true;
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
        /*if (!validateLectureNumber()) {
            return;
        }*/

        if (!validateStartingTime()) {
            return;
        }

        if (!validateEndingTime()) {
            return;
        }
        DatabaseHelper db = new DatabaseHelper(this);
        //data.setLecture_number(Integer.parseInt(lectureNumber.getText().toString()));
        db.addTimetableSlot(data);
        db.close();
        startActivity(new Intent(this, DaysActivity.class));
        finish();

    }

    private void fillSpinners() {



        DatabaseHelper db = new DatabaseHelper(this);


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.weekday_array, android.R.layout.simple_spinner_dropdown_item);

        weekdaySpinner.setAdapter(adapter);
        weekdaySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                data.setDay(item);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayList<String> lectureList = db.showLectureList();

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, lectureList);

        lectureNameSpinner.setAdapter(dataAdapter);
        lectureNameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                data.setLecture_name(item);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        db.close();
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

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case TIME_DIALOG_ID:
                return new TimePickerDialog(this,
                        mTimeSetListener, pHour, pMinute, false);
        }
        return null;
    }

    private void displayToast(boolean select) {

        if(select) {
            isStartingTimeSelected = false;
            lectureStartingTime.setText(date);
            data.setStarting_time(date);
        } else {
            endingTime.setText(date);
            data.setEnding_time(date);
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
                case R.id.startingTime:
                    validateStartingTime();
                    break;
                case R.id.endingTime:
                    validateEndingTime();
                    break;
            }
        }
    }

}

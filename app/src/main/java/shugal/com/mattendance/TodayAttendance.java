package shugal.com.mattendance;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class TodayAttendance extends AppCompatActivity {

    ListView listView;
    TodayCustomList adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today_attendance);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        DatabaseHelper db = new DatabaseHelper(getApplicationContext());

        listView = (ListView) findViewById(R.id.todayAttendance);

        String weekDay;
        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.US);

        Calendar calendar = Calendar.getInstance();
        weekDay = dayFormat.format(calendar.getTime());

        ArrayList<TimetableData> contacts = db.showTimetable(weekDay);
        //db.showAllTimetable(DAY);

        adapter = new TodayCustomList(getApplicationContext(), contacts);
        listView.setAdapter(adapter);

        checkButtonClick();
    }

    private void checkButtonClick() {
        Button myButton = (Button) findViewById(R.id.submitId);
        myButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                StringBuffer responseText = new StringBuffer();
                responseText.append("The following were selected...\n");

                ArrayList<TimetableData> subjectsList = adapter.subjectsList;
                for (int i = 0; i < subjectsList.size(); i++) {
                    TimetableData subj = subjectsList.get(i);
                    if (subj.equals(subj.getLecture_name())) {
                        responseText.append("\n" + subj.getLecture_name());
                    }
                }

                Toast.makeText(getApplicationContext(),
                        responseText, Toast.LENGTH_LONG).show();

            }
        });
    }

    public class TodayCustomList extends ArrayAdapter<TimetableData> {

        private ArrayList<TimetableData> subjectsList;

        public TodayCustomList(Context context,
                               ArrayList<TimetableData> countryList) {
            super(context, 0, countryList);
            this.subjectsList = new ArrayList<TimetableData>();
            this.subjectsList.addAll(subjectsList);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            TimetableData data = getItem(position);

            ViewHolder holder = null;
            Log.v("ConvertView", String.valueOf(position));


            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.custom_list_today_attendance, null);

                holder = new ViewHolder();
                holder.name = (CheckBox) convertView.findViewById(R.id.checkboxTick);
                convertView.setTag(holder);

                holder.name.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        CheckBox cb = (CheckBox) v;
                        TimetableData subdata = (TimetableData) cb.getTag();
                        Toast.makeText(getContext(),
                                "Clicked on Checkbox: " + cb.getText() +
                                        " is " + cb.isChecked(),
                                Toast.LENGTH_LONG).show();

                    }
                });

            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            TextView lectureName = (TextView) convertView.findViewById(R.id.lectureName);

            lectureName.setText(data.getLecture_name());
            TimetableData country = subjectsList.get(position);
            holder.name.setTag(country);

            return convertView;

        }

        private class ViewHolder {
            CheckBox name;
        }


    }

}
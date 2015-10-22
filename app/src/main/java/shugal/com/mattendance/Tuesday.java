package shugal.com.mattendance;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class Tuesday extends Fragment {

    public final String DAY = "Tuesday";
    View emptyList;
    ListView timetableList;
    public Tuesday() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (container == null ) {
            return null;
        } else {
            View view = inflater.inflate(R.layout.activity_tuesday, container, false);
            timetableList = (ListView) view.findViewById(R.id.timetable_list);
            emptyList = view.findViewById(R.id.frame);

            printTimetable();
            return view;
        }

    }

    private void printTimetable() {
        DatabaseHelper db = new DatabaseHelper(getContext());

        //Gets date from this activity


        if (db.isTimetableEmpty(DAY)) {
            timetableList.setVisibility(View.GONE);
            emptyList.setVisibility(View.VISIBLE);
            TextView emptyText = (TextView) emptyList.findViewById(R.id.emptyListText);
            Typeface tf= Typeface.createFromAsset(getContext().getAssets(), "fonts/empty_list.ttf");
            emptyText.setTypeface(tf);

        } else {
            emptyList.setVisibility(View.GONE);
            timetableList.setVisibility(View.VISIBLE);
            List<LectureData> contacts = db.showAllLectures();

            LectureCustomList adapter = new LectureCustomList(getContext(), contacts);
            timetableList.setAdapter(adapter);
        }

        db.close();
    }
}

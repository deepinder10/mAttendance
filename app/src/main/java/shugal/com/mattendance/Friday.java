package shugal.com.mattendance;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Friday extends Fragment {

    public final String DAY = "Friday";
    View emptyList;
    ListView timetableList;

    public Friday() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (container == null ) {
            return null;
        } else {
            View view = inflater.inflate(R.layout.activity_friday, container, false);
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
            ArrayList<TimetableData> contacts = db.showTimetable(DAY);
            //db.showAllTimetable(DAY);

            TimetableCustomList adapter = new TimetableCustomList(getContext(), contacts);
            timetableList.setAdapter(adapter);
        }

        db.close();
    }


}

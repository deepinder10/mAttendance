package shugal.com.mattendance;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
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

/**
 * Created by Deepinder on 10/17/2015.
 */
public class Monday extends Fragment {
    public final String DAY = "Monday";
    View emptyList;
    ListView timetableList;
    public Monday() {
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
            View view = inflater.inflate(R.layout.activity_monday, container, false);
            timetableList = (ListView) view.findViewById(R.id.timetable_list);
            emptyList = view.findViewById(R.id.frame);

            registerForContextMenu(timetableList);
            printTimetable();
            return view;
        }

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {

        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.delete_lecture, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        DatabaseHelper db = new DatabaseHelper (getContext());
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        TimetableData data = (TimetableData) timetableList.getItemAtPosition(info.position);

        switch (item.getItemId()) {
            case R.id.action_delete:
                db.deleteTimetable(data);
                printTimetable();
                Toast.makeText(getContext(), "Deleted", Toast.LENGTH_SHORT).show();
                db.close();
                return true;
            default:
                return super.onContextItemSelected(item);
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

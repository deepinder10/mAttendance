package shugal.com.mattendance;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
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
import java.util.List;

public class Tuesday extends Fragment {

    public final String DAY = "Tuesday";
    View emptyList;
    RecyclerView timetableList;
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
            View view = inflater.inflate(R.layout.activity_monday, container, false);
            timetableList = (RecyclerView) view.findViewById(R.id.timetable_list);
            emptyList = view.findViewById(R.id.frame);
            printTimetable();
            return view;
        }

    }
    /*
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.delete_lecture, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        DatabaseHelper db = new DatabaseHelper(getContext());
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        //TimetableData data = (TimetableData) timetableList.getItemAtPosition(info.position);

        switch (item.getItemId()) {
            case R.id.action_delete:
                db.deleteTimetable(data);
                db.close();
                printTimetable();
                Toast.makeText(getContext(), "Deleted", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }*/

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

            RVAdapter adapter = new RVAdapter(contacts);
            timetableList.setLayoutManager(new LinearLayoutManager(getContext()));
            timetableList.setAdapter(adapter);
            timetableList.setAdapter(adapter);
        }

        db.close();
    }


    public class RVAdapter extends RecyclerView.Adapter<RVAdapter.PersonViewHolder>{

        List<TimetableData> persons;

        public RVAdapter(List<TimetableData> persons){
            this.persons = persons;
        }

        @Override
        public PersonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.lecture_list2, parent, false);
            PersonViewHolder pvh = new PersonViewHolder(v);
            return pvh;
        }

        @Override
        public void onBindViewHolder(final PersonViewHolder holder, int position) {
            final TimetableData data = persons.get(position);
            holder.lectureName.setText(data.getLecture_name());
            holder.lectureAbsents.setText("Ends at: "+data.getEnding_time());
            holder.lecturePresents.setText("Starts at: " + data.getStarting_time());
            holder.lecturePercent.setText("Lecture Room: " + data.getRoom_no());
            holder.cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), data.getLecture_name(), Toast.LENGTH_SHORT).show();
                }
            });
            holder.cv.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    final PopupMenu popup = new PopupMenu(v.getContext(), v);
                    popup.getMenuInflater().inflate(R.menu.delete_lecture, popup.getMenu());
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            DatabaseHelper db = new DatabaseHelper (getContext());
                            db.deleteTimetable(data);
                            printTimetable();
                            Toast.makeText(getContext(), "Deleted", Toast.LENGTH_SHORT).show();
                            db.close();
                            return true;
                        }
                    });
                    popup.show();
                    return true;
                }


            });
        }

        @Override
        public int getItemCount() {
            return persons.size();
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
        }

        public class PersonViewHolder extends RecyclerView.ViewHolder {
            CardView cv;
            TextView lectureName;
            TextView lectureAbsents;
            TextView lecturePresents;
            TextView lecturePercent;

            PersonViewHolder(View itemView) {
                super(itemView);
                cv = (CardView) itemView.findViewById(R.id.cv);
                lectureName = (TextView) itemView.findViewById(R.id.lectureName);
                lectureAbsents = (TextView)itemView.findViewById(R.id.lectureAbsents);
                lecturePresents = (TextView) itemView.findViewById(R.id.lecturePresents);
                lecturePercent = (TextView) itemView.findViewById(R.id.lecturePercent);
            }
        }

    }


}

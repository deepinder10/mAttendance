package shugal.com.mattendance;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by abhishek on 23/10/15.
 */
public class TimetableCustomList extends ArrayAdapter<TimetableData> {

    public TimetableCustomList(Context context, ArrayList<TimetableData> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        TimetableData data = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.timetable_list, parent, false);
        }

        TextView lectureName = (TextView) convertView.findViewById(R.id.lectureName);
        TextView lectureAbsents = (TextView) convertView.findViewById(R.id.lectureAbsents);
        TextView lecturePresents = (TextView) convertView.findViewById(R.id.lecturePresents);


        lectureName.setText(data.getLecture_name());
        lectureAbsents.setText("Starts at: "+data.getStarting_time());
        lecturePresents.setText("Ends at: " + data.getEnding_time());

        return convertView;

    }
}

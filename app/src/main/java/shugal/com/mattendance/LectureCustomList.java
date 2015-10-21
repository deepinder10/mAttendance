package shugal.com.mattendance;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by abhishek on 21/10/15.
 */
public class LectureCustomList extends ArrayAdapter<LectureData> {

    public LectureCustomList(Context context, List<LectureData> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LectureData data = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.lectures_list, parent, false);
        }

        TextView lectureName = (TextView) convertView.findViewById(R.id.lectureName);
        TextView lectureAbsents = (TextView) convertView.findViewById(R.id.lectureAbsents);
        TextView lecturePresents = (TextView) convertView.findViewById(R.id.lecturePresents);
        TextView lecturePercent = (TextView) convertView.findViewById(R.id.lecturePercent);

        float total = data.get_absents() + data.get_presents();
        float percent;

        if (total == 0) {
            percent = 0;
        } else {
            percent = (data.get_presents()/total) * 100;
        }

        lectureName.setText(data.get_lecture_name());
        lectureAbsents.setText("Absents: "+data.get_absents());
        lecturePresents.setText("Presents: " + data.get_presents());
        lecturePercent.setText(percent+" %");

        return convertView;

    }
}

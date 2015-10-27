package shugal.com.mattendance;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by abhishek on 27/10/15.
 */
public class DangerListAdapter extends ArrayAdapter<LectureData> {

    public DangerListAdapter(Context context, List<LectureData> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LectureData data = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.custom_list_dangerzone, parent, false);
        }

        TextView lectureName = (TextView) convertView.findViewById(R.id.lectureName);
        TextView lecturePercent = (TextView) convertView.findViewById(R.id.lecturePercent);
        TextView lectureNeeded = (TextView) convertView.findViewById(R.id.lectureNeeded);

        float total = data.get_absents() + data.get_presents();
        float percent;

        if (total == 0) {
            percent = 0;
        } else {
            percent = (data.get_presents()/total) * 100;
        }

        float presents = data.get_presents();


        lectureName.setText(data.get_lecture_name());
        lecturePercent.setText(percent + " %");
        while(percent < 75) {
            if (total == 0) {
                percent = 0;
            } else {
                percent = (presents/total) * 100;
            }
            ++presents; ++total;
        }

        int needed = (int) ((int) presents - data.get_presents()) - 1;
        lectureNeeded.setText("Classes needed: " + needed);

        return convertView;

    }
}

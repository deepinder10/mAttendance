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
public class DatewiseListAdapter extends ArrayAdapter<DatewiseData> {

    public DatewiseListAdapter(Context context, List<DatewiseData> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        DatewiseData data = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.datewise_custom_list, parent, false);
        }

        TextView lectureName = (TextView) convertView.findViewById(R.id.lectureName);
        TextView lecturePercent = (TextView) convertView.findViewById(R.id.lecturePercent);



        lectureName.setText(data.getSubject());
        lecturePercent.setText(data.getStatus());

        return convertView;

    }
}

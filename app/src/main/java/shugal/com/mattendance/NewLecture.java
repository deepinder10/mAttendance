package shugal.com.mattendance;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.widget.EditText;

/**
 * Created by abhishek on 20/10/15.
 */
public class NewLecture extends Activity {

    private TextInputLayout layoutLectureNo, layoutLectureName;
    private EditText lectureNo, lectureName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_lecture);

    }


}

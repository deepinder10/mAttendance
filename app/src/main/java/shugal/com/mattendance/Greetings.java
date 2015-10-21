package shugal.com.mattendance;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.github.paolorotolo.appintro.AppIntro2;
import com.github.paolorotolo.appintro.AppIntroFragment;

/**
 * Created by abhishek on 20/10/15.
 */
public class Greetings extends AppIntro2 {
    @Override
    public void init(Bundle savedInstanceState) {
        addSlide(AppIntroFragment.newInstance("Attendance Tracker",
                "This App will help you track your attendance", R.drawable.playstore_icon, Color.LTGRAY));

        addSlide(AppIntroFragment.newInstance("Attendance Tracker",
                "This App will help you track your attendance", R.drawable.playstore_icon, Color.LTGRAY));

        addSlide(AppIntroFragment.newInstance("Attendance Tracker",
                "This App will help you track your attendance", R.drawable.playstore_icon, Color.LTGRAY));


        showDoneButton(true);

    }

    @Override
    public void onDonePressed() {
        Intent proceedToMain = new Intent(Greetings.this, MainActivity.class);
        startActivity(proceedToMain);
    }
}

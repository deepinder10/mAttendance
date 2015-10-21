package shugal.com.mattendance;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;

/**
 * Created by abhishek on 20/10/15.
 */
public class Greetings extends AppIntro {
    @Override
    public void init(Bundle savedInstanceState) {
        addSlide(AppIntroFragment.newInstance("Attendance Tracker",
                "This App will help you track your attendance", R.drawable.playstore_icon, Color.LTGRAY));

        addSlide(AppIntroFragment.newInstance("Attendance Tracker",
                "This App will help you track your attendance", R.drawable.playstore_icon, Color.LTGRAY));

        addSlide(AppIntroFragment.newInstance("Attendance Tracker",
                "This App will help you track your attendance", R.drawable.playstore_icon, Color.LTGRAY));


        showSkipButton(true);
        showDoneButton(true);

    }

    @Override
    public void onSkipPressed() {
        Intent proceedToMain = new Intent(Greetings.this, MainActivity.class);
        startActivity(proceedToMain);
    }

    @Override
    public void onDonePressed() {
        Intent proceedToMain = new Intent(Greetings.this, MainActivity.class);
        startActivity(proceedToMain);
    }
}

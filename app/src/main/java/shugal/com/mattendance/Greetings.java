package shugal.com.mattendance;

import android.content.Intent;
import android.os.Bundle;

import com.github.paolorotolo.appintro.AppIntro;

/**
 * Created by abhishek on 20/10/15.
 */
public class Greetings extends AppIntro {
    @Override
    public void init(Bundle savedInstanceState) {

        addSlide(SampleSlide.newInstance(R.layout.intro1));

        addSlide(SampleSlide.newInstance(R.layout.intro2));

        addSlide(SampleSlide.newInstance(R.layout.intro3));

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

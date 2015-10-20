package shugal.com.mattendance;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

/**
 * Created by abhishek on 20/10/15.
 */
public class Splashscreen extends Activity {

    int SCREEN_TIMEOUT = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);
        //createTablesAndDatabases();
        startSplashscreen();
    }

    private boolean isFirstTime() {
        boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getBoolean("isfirstrun", true);
        if (isFirstRun) {
            getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit().
                    putBoolean("isfirstrun", false).commit();
            return true;

        } else {
            return false;
        }
    }

    private void startSplashscreen() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (isFirstTime()) {
                    Intent firstInputs = new Intent(Splashscreen.this, Greetings.class);
                    startActivity(firstInputs);
                } else {
                    Intent proceedToMain = new Intent(Splashscreen.this, MainActivity.class);
                    startActivity(proceedToMain);
                }

                finish();
            }
        }, SCREEN_TIMEOUT);
    }

}

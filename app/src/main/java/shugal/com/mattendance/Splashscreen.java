package shugal.com.mattendance;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

/**
 * Created by abhishek on 20/10/15.
 */
public class Splashscreen extends Activity {

    int SCREEN_TIMEOUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);

        if (isFirstTime()) {
            //createTablesAndDatabases();
            startSplashscreen();
        } else {
            startSplashscreen();
        }

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
                Intent proceedToMain = new Intent(Splashscreen.this, MainActivity.class);
                startActivity(proceedToMain);
                finish();
            }
        }, SCREEN_TIMEOUT);
    }

}

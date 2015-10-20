package shugal.com.mattendance;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * Created by abhishek on 20/10/15.
 */
public class Greetings extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.greetings);

    }

    public void proceedClick(View view) {
        Intent proceedToMain = new Intent(Greetings.this, MainActivity.class);
        startActivity(proceedToMain);
    }
}

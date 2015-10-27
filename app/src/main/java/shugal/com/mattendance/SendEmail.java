package shugal.com.mattendance;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.support.v7.widget.Toolbar;

/**
 * Created by Aayush on 26-10-2015.
 */
public class SendEmail extends AppCompatActivity {
    EditText tot , sub , msg;
    Button selsubt , send;

    Toolbar toolbar;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.email_toolbar);

        tot=(EditText)findViewById(R.id.emailid);
        sub=(EditText)findViewById(R.id.sub);
        msg=(EditText)findViewById(R.id.msglist);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        send=(Button)findViewById(R.id.send);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Share Via Email");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        send.setOnClickListener(new View.OnClickListener(){
            public void onClick(View arg0 ){
                String to = tot.getText().toString();
                String subject = sub.getText().toString();
                String message = msg.getText().toString();

                Intent email = new Intent(Intent.ACTION_SEND);
                email.putExtra(Intent.EXTRA_EMAIL, new String[]{ to});
                email.putExtra(Intent.EXTRA_SUBJECT, subject);
                email.putExtra(Intent.EXTRA_TEXT, message);

                email.setType("message/rfc822");

                startActivity(Intent.createChooser(email, "Choose an Email client :"));

            }
        });


    }}

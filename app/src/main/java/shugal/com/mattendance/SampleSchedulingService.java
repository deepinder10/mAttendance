package shugal.com.mattendance;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;

import java.util.Calendar;

/**
 * Created by Deepinder on 10/24/2015.
 */
public class SampleSchedulingService extends IntentService {
    public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;

    public SampleSchedulingService() {
        super("SchedulingService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        sendNotification("Mark Attendance");
    }

    private void sendNotification(String msg) {
        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, TodayAttendance.class), 0);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.playstore_icon)
                        .setContentTitle("mAttendance")
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(msg))
                        .setContentText(msg);

        Calendar c = Calendar.getInstance();
        int d = c.get(Calendar.HOUR_OF_DAY);


        mBuilder.setContentIntent(contentIntent);
        mBuilder.setAutoCancel(true);
        if (d == 21) {
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
        Vibrator v = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        v.vibrate(1000);
        }

    }
}

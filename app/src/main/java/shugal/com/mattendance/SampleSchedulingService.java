package shugal.com.mattendance;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Deepinder on 10/24/2015.
 */
public class SampleSchedulingService extends IntentService {
    public static final int NOTIFICATION_ID = 1;
    NotificationCompat.Builder m5min, mDanger;
    private NotificationManager mNotificationManager;
    private int l, k;
    public SampleSchedulingService() {
        super("SchedulingService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String weekDay;
        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.US);

        Calendar calendar = Calendar.getInstance();
        weekDay = dayFormat.format(calendar.getTime());

        if (!weekDay.equals("Saturday") && !weekDay.equals("Sunday"))
            sendNotification("Mark Attendance");
    }

    private void sendNotification(String msg) {
        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        DatabaseHelper db = new DatabaseHelper(getApplicationContext());

        String weekDay;
        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.US);

        Calendar calendar = Calendar.getInstance();
        weekDay = dayFormat.format(calendar.getTime());


        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, TodayAttendance.class), 0);

        PendingIntent contentIntent2 = PendingIntent.getActivity(this, 0,
                new Intent(this, Dangerzone.class), 0);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.playstore_icon)
                        .setContentTitle("mAttendance")
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(msg))
                        .setContentText(msg);

     /*
            for(int i =0; i<contacts.size();i++){
                TimetableData data = contacts.get(i);
                String time = data.getStarting_time();
                String name = data.getLecture_name();
                String stHour = time.substring(0, 2);
                String stMin = time.substring(3, 5);
                int sHour = Integer.parseInt(stHour);
                int sMin = Integer.parseInt(stMin);
                if (sMin >= 0 && sMin < 6 ){
                    l =sHour - 1;
                    k= sMin - 5;
                }
                else {
                    l =sHour;
                    k = sMin - 5;
                }

                Log.d("5min","5mim + " + l + "  " + k);
                m5min =
                        new NotificationCompat.Builder(this)
                                .setSmallIcon(R.drawable.playstore_icon)
                                .setContentTitle("mAttendance")
                                .setStyle(new NotificationCompat.BigTextStyle()
                                        .bigText(msg))
                                .setContentText(name + " Class in 5 minutes");
                m5min.setAutoCancel(true);
                m5min.setContentIntent(contentIntent);
            }
*/

        Calendar c = Calendar.getInstance();
        int d = c.get(Calendar.HOUR_OF_DAY);
        int e = c.get(Calendar.MINUTE);
        int f = c.get(Calendar.SECOND);

        mBuilder.setContentIntent(contentIntent);
        mBuilder.setAutoCancel(true);


        if (!db.isLectureListEmpty() && d == 21 && e == 1 && f < 3) {
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
        Vibrator v = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        v.vibrate(1000);
        }

        if (!db.isTimetableEmpty(weekDay)) {
            ArrayList<TimetableData> contacts = db.showTimetable(weekDay);

            for (int i = 0; i < contacts.size(); i++) {
                TimetableData data = contacts.get(i);
                String time = data.getStarting_time();
                String name = data.getLecture_name();
                String stHour = time.substring(0, 2);
                String stMin = time.substring(3, 5);
                String lecH = data.getRoom_no();
                int sHour = Integer.parseInt(stHour);
                int sMin = Integer.parseInt(stMin);

                if (sMin >= 0 && sMin < 6) {
                    l = sHour - 1;
                    k = sMin - 5;
                } else {
                    l = sHour;
                    k = sMin - 5;
                }

                if (d == l && e == k && f < 4) {
                    m5min = new NotificationCompat.Builder(this)
                            .setSmallIcon(R.drawable.playstore_icon)
                            .setContentTitle("mAttendance")
                            .setStyle(new NotificationCompat.BigTextStyle()
                                    .bigText(name + " Class in 5 minutes in Room No. " + lecH))
                            .setContentText(name + " Class in 5 minutes in Room No. " + lecH);

                    m5min.setAutoCancel(true);
                    m5min.setContentIntent(contentIntent);

                    mNotificationManager.notify(2, m5min.build());
                    Vibrator v = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
                    // Vibrate for 500 milliseconds
                    v.vibrate(1000);
                }
            }
        }

        if (!db.isDangerListEmpty() && d == 8 && e == 55 && f < 3) {
            mDanger = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.playstore_icon)
                    .setContentTitle("mAttendance")
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText("You have Short of Attendance , Click here to check the Subjects"))
                    .setContentText("You have Short of Attendance , Click here to check the Subjects");

            mDanger.setAutoCancel(true);
            mDanger.setContentIntent(contentIntent2);

            mNotificationManager.notify(3, mDanger.build());
            Vibrator v = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
            // Vibrate for 500 milliseconds
            v.vibrate(1000);

        }


        Log.d("exaxt time", "exact time " + d + " " + e);
    }
}

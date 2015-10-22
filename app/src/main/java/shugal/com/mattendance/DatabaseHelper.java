package shugal.com.mattendance;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by abhishek on 21/10/15.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "Attendance";

    // Table Names
    private static final String KEY_ID = "id";
    private static final String LECTURE_TABLE_NAME = "lectures";
    private static final String TIMETABLE = "timetable";


    //Lectures Table Columns
    private static final String LECTURE_NAME = "lecture_name";
    private static final String KEY_PRESENTS = "presents";
    private static final String KEY_ABSENTS = "absents";


    // timetable table columns
    private static final String LECTURE_NUMBER = "lecture_number";
    private static final String LECTURE_DAY = "day";
    private static final String STARTING_TIME = "starting_time";
    private static final String ENDING_TIME = "ending_time";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "create table if not exists " + LECTURE_TABLE_NAME + "(" +
                KEY_ID + " integer primary key autoincrement, " +
                LECTURE_NAME + " string, " +
                KEY_PRESENTS + " real, " +
                KEY_ABSENTS + " real);";

        db.execSQL(query);

        query = "create table if not exists " + TIMETABLE + "(" +
                KEY_ID + " integer primary key autoincrement, " +
                LECTURE_DAY + " string, " +
                LECTURE_NAME + " string, " +
                LECTURE_NUMBER + " integer, " +
                STARTING_TIME + " string, " +
                ENDING_TIME + " string);";
        db.execSQL(query);

        Log.d("Error", "Creating Table");
        Log.d("Error", query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + LECTURE_TABLE_NAME);
        db.execSQL("drop table if exists " + TIMETABLE);
        onCreate(db);
    }





    // Timetable table methods
    public void addTimetableSlot(TimetableData data) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(LECTURE_DAY, data.getDay());
        values.put(LECTURE_NAME, data.getLecture_name());
        values.put(LECTURE_NUMBER, data.getLecture_number());
        values.put(STARTING_TIME, data.getStarting_time());
        values.put(ENDING_TIME, data.getEnding_time());

        db.insert(TIMETABLE, null, values);
        Log.d("Error", data.getId() + " " + data.getLecture_name() + " " +data.getLecture_number());
        db.close();
    }

    public void deleteFirstTimeTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TIMETABLE, KEY_ID + " = ?",
                new String[]{String.valueOf(1)});
        Log.d("Error", "Deleted Lecture");
        db.close();
    }

    public void updateTimetableSlot(TimetableData data) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(LECTURE_DAY, data.getDay());
        values.put(LECTURE_NAME, data.getLecture_name());
        values.put(LECTURE_NUMBER, data.getLecture_number());
        values.put(STARTING_TIME, data.getStarting_time());
        values.put(ENDING_TIME, data.getEnding_time());

        db.update(TIMETABLE, values, KEY_ID + " = ?",
                new String[]{String.valueOf(data.getId())});
    }

    public void deleteTimetableSlot(TimetableData data) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TIMETABLE, KEY_ID + " = ?",
                new String[]{String.valueOf(data.getId())});
        Log.d("Error", "Deleted Lecture");
        db.close();
    }

    public ArrayList<TimetableData> showTimetable(String day) {

        ArrayList<TimetableData> expenseList = new ArrayList();

        TimetableData data = new TimetableData();

        String selectQuery = "SELECT * FROM " + TIMETABLE + " where " + LECTURE_DAY  +
                " = " + "\"" + day + "\" order by " + KEY_ID + " asc";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                // Adding contact to list
                data.setId(Integer.parseInt(cursor.getString(0)));
                data.setDay(cursor.getString(1));
                data.setLecture_name(cursor.getString(2));
                data.setLecture_number(Integer.parseInt(cursor.getString(3)));
                data.setStarting_time(cursor.getString(4));
                data.setEnding_time(cursor.getString(5));

                expenseList.add(data);
            } while (cursor.moveToNext());
        }

        /*for (TimetableData d : expenseList) {
            Log.d("Error", d.getId() + ", " + d.getLecture_name() + " " + d.getLecture_number());
        }*/
        db.close();
        return expenseList;
    }

    public ArrayList<TimetableData> showAllTimetable(String day) {

        ArrayList<TimetableData> expenseList = new ArrayList();

        TimetableData data = new TimetableData();

        String selectQuery = "SELECT * FROM " + TIMETABLE + " where " + LECTURE_DAY  +
                " = " + "\"" + day + "\"";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                // Adding contact to list
                data.setId(Integer.parseInt(cursor.getString(0)));
                data.setDay(cursor.getString(1));
                data.setLecture_name(cursor.getString(2));
                data.setLecture_number(Integer.parseInt(cursor.getString(3)));
                data.setStarting_time(cursor.getString(4));
                data.setEnding_time(cursor.getString(5));

                expenseList.add(data);
            } while (cursor.moveToNext());
        }

        for (TimetableData d : expenseList) {
            Log.d("Error", d.getLecture_number() + ", " + d.getLecture_name());
        }
        db.close();
        return expenseList;
    }

    public boolean isTimetableEmpty(String day) {
        SQLiteDatabase db = this.getReadableDatabase();
        String count = "SELECT count(*) FROM " + TIMETABLE + " where " + LECTURE_DAY  +
                " = " + "\"" + day + "\"";
        Cursor mcursor = db.rawQuery(count, null);
        mcursor.moveToFirst();
        int icount = mcursor.getInt(0);

        if (icount > 0) {
            db.close();
            return false;
        } else {
            db.close();
            return true;
        }

    }








    // Lecture table methods
    public void deleteFirstLecture() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(LECTURE_TABLE_NAME, KEY_ID + " = ?",
                new String[]{String.valueOf(1)});
        Log.d("Error", "Deleted Lecture");
        db.close();
    }

    public void addLecture(LectureData data) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(LECTURE_NAME, data.get_lecture_name());
        values.put(KEY_PRESENTS, data.get_presents());
        values.put(KEY_ABSENTS, data.get_absents());


        db.insert(LECTURE_TABLE_NAME, null, values);
        Log.d("Error", "Added new lecture");
        db.close();
    }

    public List<LectureData> showAllLectures() {
        List<LectureData> expenseList = new ArrayList<LectureData>();

        String selectQuery = "SELECT  * FROM " + LECTURE_TABLE_NAME + " order by id desc";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                LectureData lectureData = new LectureData();
                lectureData.set_id(Integer.parseInt(cursor.getString(0)));
                lectureData.set_lecture_name(cursor.getString(1));
                lectureData.set_present(Float.parseFloat(cursor.getString(2)));
                lectureData.set_absent(Float.parseFloat(cursor.getString(3)));
                // Adding contact to list
                expenseList.add(lectureData);
            } while (cursor.moveToNext());
        }
        db.close();
        return expenseList;
    }

    public ArrayList<String> showLectureList() {

        ArrayList<String> expenseList = new ArrayList();

        String selectQuery = "SELECT  "+LECTURE_NAME+" FROM " + LECTURE_TABLE_NAME + " order by id desc";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                // Adding contact to list
                expenseList.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        db.close();
        return expenseList;
    }

    public boolean isLectureListEmpty() {
        SQLiteDatabase db = this.getReadableDatabase();
        String count = "SELECT count(*) FROM " + LECTURE_TABLE_NAME;
        Cursor mcursor = db.rawQuery(count, null);
        mcursor.moveToFirst();
        int icount = mcursor.getInt(0);

        if (icount > 0) {
            db.close();
            return false;
        } else {
            db.close();
            return true;
        }

    }
}

package shugal.com.mattendance;

/**
 * Created by abhishek on 22/10/15.
 */
public class TimetableData {
    private int id;
    private String lecture_name;
    private String day;
    private String starting_time;
    private String ending_time;

    public TimetableData() {

    }

    public TimetableData(String lecture_name, String day,
                         String starting_time, String ending_time) {
        this.lecture_name = lecture_name;
        this.day = day;
        this.starting_time = starting_time;
        this.ending_time = ending_time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLecture_name() {
        return lecture_name;
    }

    public void setLecture_name(String lecture_name) {
        this.lecture_name = lecture_name;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getStarting_time() {
        return starting_time;
    }

    public void setStarting_time(String starting_time) {
        this.starting_time = starting_time;
    }

    public String getEnding_time() {
        return ending_time;
    }

    public void setEnding_time(String ending_time) {
        this.ending_time = ending_time;
    }


}

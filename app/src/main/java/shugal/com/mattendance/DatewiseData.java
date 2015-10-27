package shugal.com.mattendance;

/**
 * Created by abhishek on 27/10/15.
 */
public class DatewiseData {
    private int id;
    private String date;
    private String subject;
    private String status;

    public DatewiseData() {

    }

    public DatewiseData(String date, String subject, String status) {
        this.date = date;
        this.subject = subject;
        this.status = status;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSubject() {
        return this.subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}

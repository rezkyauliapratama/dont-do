package rezkyaulia.android.dont_do.Model.Firebase;

/**
 * Created by Mutya Nayavashti on 13/03/2017.
 */

public class DateModel {

    public int day;
    public int month;
    public int year;
    public long timestamp;

    public DateModel() {
    }

    public DateModel(int day, int month, int year, long timestamp) {

        this.day = day;
        this.month = month;
        this.year = year;
        this.timestamp = timestamp;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}

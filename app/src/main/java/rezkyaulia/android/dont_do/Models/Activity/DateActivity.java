package rezkyaulia.android.dont_do.Models.Activity;

/**
 * Created by Mutya Nayavashti on 13/03/2017.
 */

public class DateActivity {

    public int day;
    public int month;
    public int year;
    public long timestamp;

    public DateActivity() {
    }

    public DateActivity(int day, int month, int year, long timestamp) {

        this.day = day;
        this.month = month;
        this.year = year;
        this.timestamp = timestamp;
    }


}

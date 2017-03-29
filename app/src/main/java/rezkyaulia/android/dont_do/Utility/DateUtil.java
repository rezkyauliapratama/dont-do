package rezkyaulia.android.dont_do.Utility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import rezkyaulia.android.dont_do.Models.Activity.DateActivity;

/**
 * Created by Mutya Nayavashti on 12/03/2017.
 */

public class DateUtil {

    public DateUtil(){

    }
    public DateActivity getDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        return formatFirebaseDate(calendar);
    }

    private DateActivity formatFirebaseDate(Calendar cal) {
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.UK);

        DateActivity dte = new DateActivity(cal.get(Calendar.DAY_OF_MONTH),cal.get(Calendar.MONTH),cal.get(Calendar.YEAR),cal.getTimeInMillis());
        return dte;
    }

    public String getUserFriendlyDate(Date date){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy MMM,dd HH:mm", Locale.UK);
        return simpleDateFormat.format(date);
    }

    public String getDateFromFirebase(DateActivity date){
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(date.timestamp);
        return getUserFriendlyDate(cal.getTime());
    }

    private Date convertStringToDate (String str){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = format.parse(str);
            return date;
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }

}

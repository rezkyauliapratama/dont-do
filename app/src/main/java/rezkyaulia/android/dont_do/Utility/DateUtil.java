package rezkyaulia.android.dont_do.Utility;

import android.support.annotation.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import rezkyaulia.android.dont_do.Models.Firebase.DateModel;
import timber.log.Timber;

/**
 * Created by Mutya Nayavashti on 12/03/2017.
 */

public class DateUtil {

    public DateUtil(){
    }

    public DateModel getDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        return formatFirebaseDate(calendar);
    }

    public DateModel getDate(Calendar cal){
        return formatFirebaseDate(cal);
    }
    private DateModel formatFirebaseDate(Calendar cal) {
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.UK);

        DateModel dte = new DateModel(cal.get(Calendar.DAY_OF_MONTH),cal.get(Calendar.MONTH),cal.get(Calendar.YEAR),cal.getTimeInMillis());
        return dte;
    }

    public String getUserFriendlyDate(Date date){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy MMM,dd HH:mm", Locale.UK);
        return simpleDateFormat.format(date);
    }

    public String getDateFromFirebase(DateModel date){
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(date.timestamp);
        return getUserFriendlyDate(cal.getTime());
    }

    public String getHowLongItHasBeen(DateModel dateModel){
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(dateModel.getTimestamp());
        return calculateHowLongItHasBeen(cal);
    }

    public long getTriggerEveryHours(){
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE,1);

        Timber.e("trigger : " +getUserFriendlyDate(cal.getTime()));

        return cal.getTimeInMillis();

    }
    private String calculateHowLongItHasBeen(Calendar cal){
        Calendar calNow = Calendar.getInstance();

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long different = calNow.getTimeInMillis() - cal.getTimeInMillis();

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;


        String ret = "";

        if (elapsedDays>0){
            if (elapsedDays==1){
                ret = elapsedDays+" day";
            }else{
                ret = elapsedDays+" days";
            }
        }else{
            if (elapsedHours>0){
                ret = "Today";

            }else{
                ret = "Newly Added";
            }
        }


        return ret;
    }

    @Nullable
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
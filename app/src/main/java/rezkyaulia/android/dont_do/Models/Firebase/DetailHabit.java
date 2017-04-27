package rezkyaulia.android.dont_do.Models.Firebase;

import rezkyaulia.android.dont_do.Utility.Util;

/**
 * Created by User on 30/3/2017.
 */

public class DetailHabit {
    public DateModel date;

    public DetailHabit() {
        this.date = Util.getInstance().dateUtil().getDate();

    }

    public DetailHabit(DateModel dateModel) {
        this.date = dateModel;
    }


    public DateModel getDate() {
        return date;
    }

    public void setDate(DateModel date) {
        this.date = date;
    }
}

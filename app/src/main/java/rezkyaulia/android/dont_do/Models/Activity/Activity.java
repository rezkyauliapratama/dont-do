package rezkyaulia.android.dont_do.Models.Activity;

import rezkyaulia.android.dont_do.Utility.Util;

/**
 * Created by Mutya Nayavashti on 21/02/2017.
 */

public class Activity {
    public String name;
    public DateActivity date;

    public Activity(){

    }

    public Activity(String name) {
        this.name = name;
        this.date= Util.getInstance().dateUtil().getDate();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DateActivity getDate() {
        return date;
    }

    public void setDate(DateActivity dateFirebase) {
        this.date = dateFirebase;
    }
}


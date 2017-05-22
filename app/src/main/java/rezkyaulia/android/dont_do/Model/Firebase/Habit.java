package rezkyaulia.android.dont_do.Model.Firebase;

import com.google.firebase.database.Exclude;

import java.io.UTFDataFormatException;

import rezkyaulia.android.dont_do.Utility.DateUtil;
import rezkyaulia.android.dont_do.Utility.Util;

/**
 * Created by Mutya Nayavashti on 21/02/2017.
 */

public class Habit {
    @Exclude
    public String activityKey;
    @Exclude
    private DateModel dateModel;

    public String name;
    public boolean active;
    public long createDate;



    public Habit(){
    }

    public Habit(String name) {
        this.dateModel = Util.getInstance().dateUtil().getDate();
        this.name = name;
        this.active = true;
        this.createDate = dateModel.getTimestamp();
    }

    public Habit(String activityKey, String name) {
        this.dateModel = Util.getInstance().dateUtil().getDate();
        this.activityKey = activityKey;
        this.name = name;
        this.active = true;
        this.createDate = dateModel.getTimestamp();
    }

    public Habit(String name, boolean active) {
        this.name = name;
        this.active = active;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }
}


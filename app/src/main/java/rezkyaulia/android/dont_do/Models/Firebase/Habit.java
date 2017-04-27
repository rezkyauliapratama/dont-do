package rezkyaulia.android.dont_do.Models.Firebase;

import com.google.firebase.database.Exclude;

import rezkyaulia.android.dont_do.Utility.Util;

/**
 * Created by Mutya Nayavashti on 21/02/2017.
 */

public class Habit {
    @Exclude
    public String activityKey;

    public String name;
    public boolean active;
    public Habit(){

    }

    public Habit(String name) {
        this.name = name;
        this.active = true;
    }

    public Habit(String activityKey, String name) {
        this.activityKey = activityKey;
        this.name = name;
        this.active = true;
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


}


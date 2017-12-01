package rezkyaulia.android.dont_do.Model.Firebase;

import com.google.firebase.database.Exclude;

import java.io.UTFDataFormatException;

import rezkyaulia.android.dont_do.Utility.DateUtil;
import rezkyaulia.android.dont_do.Utility.Util;

/**
 * Created by Mutya Nayavashti on 21/02/2017.
 */

public class Habit {
    private String name;
    private DateModel dateModel;


    public Habit(){
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DateModel getDateModel() {
        return dateModel;
    }

    public void setDateModel(DateModel dateModel) {
        this.dateModel = dateModel;
    }
}


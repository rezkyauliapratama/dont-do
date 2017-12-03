package rezkyaulia.android.dont_do.Model.Firebase;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;

import java.io.UTFDataFormatException;

import rezkyaulia.android.dont_do.Utility.DateUtil;
import rezkyaulia.android.dont_do.Utility.Util;

/**
 * Created by Mutya Nayavashti on 21/02/2017.
 */

public class Habit implements Parcelable {
    private String activityId;
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

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.activityId);
        dest.writeString(this.name);
        dest.writeParcelable(this.dateModel, flags);
    }

    protected Habit(Parcel in) {
        this.activityId = in.readString();
        this.name = in.readString();
        this.dateModel = in.readParcelable(DateModel.class.getClassLoader());
    }

    public static final Creator<Habit> CREATOR = new Creator<Habit>() {
        @Override
        public Habit createFromParcel(Parcel source) {
            return new Habit(source);
        }

        @Override
        public Habit[] newArray(int size) {
            return new Habit[size];
        }
    };
}


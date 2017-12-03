package rezkyaulia.android.dont_do.Model.Firebase;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;

/**
 * Created by Mutya Nayavashti on 13/03/2017.
 */

public class DateModel implements Parcelable {

    public int day;
    public int month;
    public int year;
    public long timestamp;

    @Exclude
    public long runningDay;

    @Exclude
    public boolean showLine;



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

    public long getRunningDay() {
        return runningDay;
    }

    public void setRunningDay(long runningDay) {
        this.runningDay = runningDay;
    }

    public boolean isShowLine() {
        return showLine;
    }

    public void setShowLine(boolean showLine) {
        this.showLine = showLine;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.day);
        dest.writeInt(this.month);
        dest.writeInt(this.year);
        dest.writeLong(this.timestamp);
        dest.writeLong(this.runningDay);
        dest.writeByte(this.showLine ? (byte) 1 : (byte) 0);
    }

    protected DateModel(Parcel in) {
        this.day = in.readInt();
        this.month = in.readInt();
        this.year = in.readInt();
        this.timestamp = in.readLong();
        this.runningDay = in.readLong();
        this.showLine = in.readByte() != 0;
    }

    public static final Creator<DateModel> CREATOR = new Creator<DateModel>() {
        @Override
        public DateModel createFromParcel(Parcel source) {
            return new DateModel(source);
        }

        @Override
        public DateModel[] newArray(int size) {
            return new DateModel[size];
        }
    };
}

package rezkyaulia.android.dont_do.database.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Transient;

/**
 * Created by Rezky Aulia Pratama on 12/1/2017.
 */
@Entity(nameInDb = "DetailActivityTbl",indexes = {@Index(value = "DetailActivityId", unique = true)})
public class DetailActivityTbl implements Parcelable {
    @Id
    @Property(nameInDb = "DetailActivityId")
    @Exclude
    private String DetailActivityId;

    @Property(nameInDb = "ActivityId")
    @Exclude
    private String ActivityId;

    @Property(nameInDb = "Day")
    private int Day;

    @Property(nameInDb = "Month")
    private int Month;

    @Property(nameInDb = "Timestamp")
    private Long Timestamp;

    @Property(nameInDb = "Year")
    private int Year;



    @Generated(hash = 1671446290)
    public DetailActivityTbl(String DetailActivityId, String ActivityId, int Day, int Month,
            Long Timestamp, int Year) {
        this.DetailActivityId = DetailActivityId;
        this.ActivityId = ActivityId;
        this.Day = Day;
        this.Month = Month;
        this.Timestamp = Timestamp;
        this.Year = Year;
    }

    @Generated(hash = 1447081241)
    public DetailActivityTbl() {
    }

    public String getDetailActivityId() {
        return this.DetailActivityId;
    }

    public void setDetailActivityId(String DetailActivityId) {
        this.DetailActivityId = DetailActivityId;
    }

    public String getActivityId() {
        return this.ActivityId;
    }

    public void setActivityId(String ActivityId) {
        this.ActivityId = ActivityId;
    }

    public int getDay() {
        return this.Day;
    }

    public void setDay(int Day) {
        this.Day = Day;
    }

    public int getMonth() {
        return this.Month;
    }

    public void setMonth(int Month) {
        this.Month = Month;
    }

    public Long getTimestamp() {
        return this.Timestamp;
    }

    public void setTimestamp(Long Timestamp) {
        this.Timestamp = Timestamp;
    }

    public int getYear() {
        return this.Year;
    }

    public void setYear(int Year) {
        this.Year = Year;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.DetailActivityId);
        dest.writeString(this.ActivityId);
        dest.writeInt(this.Day);
        dest.writeInt(this.Month);
        dest.writeValue(this.Timestamp);
        dest.writeInt(this.Year);
    }

    protected DetailActivityTbl(Parcel in) {
        this.DetailActivityId = in.readString();
        this.ActivityId = in.readString();
        this.Day = in.readInt();
        this.Month = in.readInt();
        this.Timestamp = (Long) in.readValue(Long.class.getClassLoader());
        this.Year = in.readInt();
    }

    public static final Creator<DetailActivityTbl> CREATOR = new Creator<DetailActivityTbl>() {
        @Override
        public DetailActivityTbl createFromParcel(Parcel source) {
            return new DetailActivityTbl(source);
        }

        @Override
        public DetailActivityTbl[] newArray(int size) {
            return new DetailActivityTbl[size];
        }
    };
}

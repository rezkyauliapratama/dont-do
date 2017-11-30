package rezkyaulia.android.dont_do.database.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

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
    private long Day;

    @Property(nameInDb = "Month")
    private long Month;

    @Property(nameInDb = "Timestamp")
    private long Timestamp;

    @Property(nameInDb = "Year")
    private long Year;

    @Generated(hash = 1628794098)
    public DetailActivityTbl(String DetailActivityId, String ActivityId, long Day, long Month,
            long Timestamp, long Year) {
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

    public long getDay() {
        return this.Day;
    }

    public void setDay(long Day) {
        this.Day = Day;
    }

    public long getMonth() {
        return this.Month;
    }

    public void setMonth(long Month) {
        this.Month = Month;
    }

    public long getTimestamp() {
        return this.Timestamp;
    }

    public void setTimestamp(long Timestamp) {
        this.Timestamp = Timestamp;
    }

    public long getYear() {
        return this.Year;
    }

    public void setYear(long Year) {
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
        dest.writeLong(this.Day);
        dest.writeLong(this.Month);
        dest.writeLong(this.Timestamp);
        dest.writeLong(this.Year);
    }

    protected DetailActivityTbl(Parcel in) {
        this.DetailActivityId = in.readString();
        this.ActivityId = in.readString();
        this.Day = in.readLong();
        this.Month = in.readLong();
        this.Timestamp = in.readLong();
        this.Year = in.readLong();
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

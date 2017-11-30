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
@Entity(nameInDb = "ActivityTbl",indexes = {@Index(value = "ActivityId", unique = true)})
public class ActivityTbl implements Parcelable{

    @Id
    @Property(nameInDb = "ActivityId")
    @Exclude
    private String ActivityId;


    @Property(nameInDb = "UserId")
    @Exclude
    private String UserId;

    @Property(nameInDb = "Active")
    private String Active;

    @Property(nameInDb = "Name")
    private String Name;

    @Property(nameInDb = "CreatedDate")
    private String CreatedDate;

    @Generated(hash = 35667501)
    public ActivityTbl(String ActivityId, String UserId, String Active, String Name,
            String CreatedDate) {
        this.ActivityId = ActivityId;
        this.UserId = UserId;
        this.Active = Active;
        this.Name = Name;
        this.CreatedDate = CreatedDate;
    }

    @Generated(hash = 990479463)
    public ActivityTbl() {
    }

    public String getActivityId() {
        return this.ActivityId;
    }

    public void setActivityId(String ActivityId) {
        this.ActivityId = ActivityId;
    }

    public String getUserId() {
        return this.UserId;
    }

    public void setUserId(String UserId) {
        this.UserId = UserId;
    }

    public String getActive() {
        return this.Active;
    }

    public void setActive(String Active) {
        this.Active = Active;
    }

    public String getName() {
        return this.Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getCreatedDate() {
        return this.CreatedDate;
    }

    public void setCreatedDate(String CreatedDate) {
        this.CreatedDate = CreatedDate;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.ActivityId);
        dest.writeString(this.UserId);
        dest.writeString(this.Active);
        dest.writeString(this.Name);
        dest.writeString(this.CreatedDate);
    }

    protected ActivityTbl(Parcel in) {
        this.ActivityId = in.readString();
        this.UserId = in.readString();
        this.Active = in.readString();
        this.Name = in.readString();
        this.CreatedDate = in.readString();
    }

    public static final Creator<ActivityTbl> CREATOR = new Creator<ActivityTbl>() {
        @Override
        public ActivityTbl createFromParcel(Parcel source) {
            return new ActivityTbl(source);
        }

        @Override
        public ActivityTbl[] newArray(int size) {
            return new ActivityTbl[size];
        }
    };
}

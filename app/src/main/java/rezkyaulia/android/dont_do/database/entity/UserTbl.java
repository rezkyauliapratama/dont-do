package rezkyaulia.android.dont_do.database.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;

import rezkyaulia.android.dont_do.Model.Firebase.User;

/**
 * Created by Rezky Aulia Pratama on 12/1/2017.
 */
@Entity(nameInDb = "UserTbl",indexes = {@Index(value = "UserId", unique = true)})
public class UserTbl implements Parcelable{

    @Id
    @Property(nameInDb = "UserId")
    @Exclude
    private String UserId;

    @Property(nameInDb = "Email")
    private String Email;

    @Property(nameInDb = "Password")
    private String Password;

    @Property(nameInDb = "Token")
    private String Token;

    @Property(nameInDb = "Username")
    private String Username;


    @Generated(hash = 375584856)
    public UserTbl(String UserId, String Email, String Password, String Token,
            String Username) {
        this.UserId = UserId;
        this.Email = Email;
        this.Password = Password;
        this.Token = Token;
        this.Username = Username;
    }

    @Generated(hash = 585658511)
    public UserTbl() {
    }

    public String getUserId() {
        return this.UserId;
    }

    public void setUserId(String UserId) {
        this.UserId = UserId;
    }

    public String getEmail() {
        return this.Email;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }

    public String getPassword() {
        return this.Password;
    }

    public void setPassword(String Password) {
        this.Password = Password;
    }

    public String getToken() {
        return this.Token;
    }

    public void setToken(String Token) {
        this.Token = Token;
    }

    public String getUsername() {
        return this.Username;
    }

    public void setUsername(String Username) {
        this.Username = Username;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.UserId);
        dest.writeString(this.Email);
        dest.writeString(this.Password);
        dest.writeString(this.Token);
        dest.writeString(this.Username);
    }

    protected UserTbl(Parcel in) {
        this.UserId = in.readString();
        this.Email = in.readString();
        this.Password = in.readString();
        this.Token = in.readString();
        this.Username = in.readString();
    }

    public static final Creator<UserTbl> CREATOR = new Creator<UserTbl>() {
        @Override
        public UserTbl createFromParcel(Parcel source) {
            return new UserTbl(source);
        }

        @Override
        public UserTbl[] newArray(int size) {
            return new UserTbl[size];
        }
    };
}

package rezkyaulia.android.dont_do.Utility;

import android.content.Context;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.iid.FirebaseInstanceId;

import rezkyaulia.android.dont_do.Model.Firebase.User;

/**
 * Created by Mutya Nayavashti on 06/11/2016.
 */

public class Util {

    private static Util mInstance;
    private final Context mContext;

    public static Util init(Context context){
        mInstance = new Util(context);
        return mInstance;
    }

    public Util(Context context) {
        this.mContext = context;
    }

    public static Util getInstance() {
        return mInstance;
    }

    public DateUtil dateUtil() {
        DateUtil dateUtil = new DateUtil();
        return dateUtil;
    }


    public static String token() {
        return FirebaseInstanceId.getInstance().getToken();
    }

    public static String saveUserFirebase(DatabaseReference mDatabase, String token){
        User usr = new User();
        //Adding values
        usr.setUsername("");
        usr.setToken(token);
        usr.setEmail("");
        usr.setPassword("");
//                DatabaseReference newRef = mDatabase.child("users").push();
//                mDatabase.child("users").child(token).setValue(usr);
        String key = mDatabase.child("users").push().getKey();
        mDatabase.child("users").push().setValue(usr);
        return key;
    }




}

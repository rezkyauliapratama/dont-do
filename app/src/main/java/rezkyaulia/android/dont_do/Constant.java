package rezkyaulia.android.dont_do;

import android.content.Context;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import timber.log.Timber;

/**
 * Created by Mutya Nayavashti on 07/11/2016.
 */

public class Constant {
    private static Constant mInstance;
    // Step 1: private static variable of INSTANCE variable
    private static volatile Constant INSTANCE;

    // Step 2: private constructor
    private Constant() {

    }

    // Step 3: Provide public static getInstance() method returning INSTANCE after checking
    public static Constant getInstance() {

        // double-checking lock
        if(null == INSTANCE){

            // synchronized block
            synchronized (Constant.class) {
                if(null == INSTANCE){
                    INSTANCE = new Constant();
                }
            }
        }
        return INSTANCE;
    }

    public final String DB_NAME = "db_dontdo";
    public final String NAME_PREF = "app";
    public final String TOKEN_PREF = "token";
    public final String USER_KEY_PREF = "user_key";
    public final String FONT_SIZE_PREF = "FONT_SIZE_KEY";

    public final String EMAIL = "email";
    public final String UID = "uid";


//    firebase child name
    public final String USERS = "users";
    public final String ACTIVITIES = "activities";
    public final String DETAILS = "detail_activities";

//    Firebase reference
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private PreferencesManager preferencesManager = PreferencesManager.getInstance();
    public final DatabaseReference PrimaryRef = databaseReference.child(ACTIVITIES);
    public final DatabaseReference SecondaryPref = databaseReference.child(DETAILS);

//    Variable for services
    public final String  EVERYHOUR_RUNNING_SERVICE="EVERYHOUR_RUNNING_SERVICE";


}

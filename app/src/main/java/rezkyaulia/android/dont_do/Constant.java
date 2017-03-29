package rezkyaulia.android.dont_do;

/**
 * Created by Mutya Nayavashti on 07/11/2016.
 */

public class Constant {

    private static Constant mInstance;
    public static Constant instanceOf() {
        if (mInstance == null) {
            mInstance = new Constant();
        }
        return mInstance;
    }

    public final String NAME_PREF = "app";
    public final String TOKEN_PREF = "token";
    public final String USER_KEY_PREF = "user_key";
    public final String EMAIL = "email";
    public final String UID = "uid";


//    firebase child name

    public final String USERS = "users";
    public final String ACTIVITIES = "activities";

}

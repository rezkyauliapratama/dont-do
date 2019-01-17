package rezkyaulia.android.dont_do.Model;

/**
 * Created by Rezky Aulia Pratama on 12/21/2017.
 */

public class FirebaseToken {
    private String token;
    private int type = 1;

    public FirebaseToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}

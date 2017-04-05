package rezkyaulia.android.dont_do.Models.Firebase;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {

    public String token;
    public String username;
    public String email;
    public String password;



    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

  /*  public User(String token, String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.token = token;
        this.password = password;
    }
*/
    public User(String token){
        this.username = "";
        this.email = "";
        this.token = token;
        this.password = "";
    }


    public void setUsername (String value){
        this.username = value;
    }
    public String getUsername(){
        return this.username;
    }


    public void setToken(String value){
        this.token = value;
    }
    public String getToken(){
        return this.token;
    }

    public void setEmail(String value){
        this.email = value;
    }
    public String getEmail(){
        return this.email;
    }


    public void setPassword (String value){
        this.password = value;
    }
    public String getPassword(){
        return this.password;
    }

}
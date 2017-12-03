package rezkyaulia.android.dont_do.Utility;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import rezkyaulia.android.dont_do.Model.Firebase.User;
import rezkyaulia.android.dont_do.R;
import timber.log.Timber;

import static java.lang.Math.round;

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

    public float convertDpToPixel(float dp) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return round(px);
    }

    public void setStatusBarColor(Activity context){
        Window window = context.getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(context, R.color.colorPrimaryTransparent));
        }
    }



    public File getFolder(String questionFileFolder) {
        File folder = Environment.getExternalStorageDirectory();
        String[] folderNames = questionFileFolder.split("/");
        for (String folderName : folderNames) {
            folder = new File(folder, folderName);
            if (!folder.exists())
                folder.mkdirs();
        }

        return folder;
    }


    public List<String> convertStringIntoList(String text){
        List<String> words = new ArrayList<>();
        try {

            String[] temp = text.trim().split("\\s+");
            for (String str : temp){
                words.add(str);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return words;
    }

    public String readTextFile(Context context,Uri uri) {
        InputStream inputStream = null;
        String words = "";
        try {
            inputStream = context.getContentResolver().openInputStream(uri);
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    inputStream));

            String line;
            Timber.e("open text file - content"+"\n");
            /*while ((line = reader.readLine()) != null) {
                String[] temp = line.split("\\s+");
                for (String str : temp){
                    words.add(str);
                }
            }*/

            while ((line = reader.readLine()) != null) {
                words = words+"\n"+line;
            }
            reader.close();
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return words;

    }

    public long getMilisWPM(int wpm){
        long minuteMilis = TimeUnit.MINUTES.toMillis(1);
        long wpmMilis = round(minuteMilis/wpm);

        return wpmMilis;
    }

    public String getWords(int mIndex,int pointWord,int mNol, int mGs, List<String> mWords){
        String tempWord = "";

        if (pointWord == mIndex && mWords != null){
            Timber.e("pointWord == mIndex");
            for (int i = 0; i<mNol ; i++){
                Timber.e("For mNol");
                for (int j = 0; j<mGs; j++){
                    Timber.e("for mGS");
                    if (pointWord>=mWords.size()){
                        pointWord = 0;
                        Timber.e("Point Word : "+pointWord+" | break GS");
                        break;
                    }

                    tempWord = tempWord+" ".concat(mWords.get(pointWord));
                    Timber.e("tempWord GS:"+tempWord);

                    pointWord++;


                }
                tempWord=tempWord+"\n";
                Timber.e("tempWord NOL:"+tempWord);

                Timber.e("pointwords : "+pointWord);
                if (pointWord>=mWords.size()){
                    pointWord = 0;
                    Timber.e("Point Word : "+pointWord+" | break NOL");
                    break;
                }


            }
        }
        return tempWord;

    }

    public String getUniqueID(Context context) {
        return getUniqueID(context, "");
//        return uuid.fromString(String.valueOf(deviceId.hashCode())).toString();
    }


    public String getUniqueID(Context context, String i) {
        final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        final String tmDevice, tmSerial, androidId;
        tmDevice = "" + tm.getDeviceId();
        tmSerial = "" + tm.getSimSerialNumber();
        androidId = "" + android.provider.Settings.Secure.getString(context.getContentResolver(),
                android.provider.Settings.Secure.ANDROID_ID);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        UUID deviceUuid = new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
        String deviceId = deviceUuid.toString() + System.currentTimeMillis() + i;// + '-' + getDate().replace(' ', '-');
//        try {
//            return new String(encrypt(generateKey(), deviceId.getBytes()),"UTF-8");
//        } catch (Exception e) {
//
//            return deviceId;
//        }


        return UUID.nameUUIDFromBytes(deviceId.getBytes()).toString();
//        return uuid.fromString(String.valueOf(deviceId.hashCode())).toString();
    }




}

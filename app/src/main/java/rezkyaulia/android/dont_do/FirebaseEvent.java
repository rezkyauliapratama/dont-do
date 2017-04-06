package rezkyaulia.android.dont_do;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import timber.log.Timber;

/**
 * Created by User on 31/3/2017.
 */

public class FirebaseEvent {
    /*private static FirebaseEvent mInstance;
    public static FirebaseEvent instanceOf() {
        if (mInstance == null) {
            mInstance = new FirebaseEvent();
        }
        return mInstance;
    }

    */

    Query query;

    private onChildEventListener mChildEventListener;
    private onSingleValueListener mSingleValueListener;


    public FirebaseEvent(Query query){
       this.query = query;
    }

    public void addChildEventListener(onChildEventListener listener){
        Timber.e("Query == null");
        if (query != null){
            Timber.e("Query != null");
            mChildEventListener = listener;

            query.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    mChildEventListener.onChildAdded(dataSnapshot,s);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    mChildEventListener.onChildChanged(dataSnapshot,s);
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    public void addSingleValueListener(onSingleValueListener listener){
        if (query != null){
            mSingleValueListener = listener;

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    mSingleValueListener.onDataChange(dataSnapshot);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    mSingleValueListener.onCancelled(databaseError);
                }
            });

        }
    }

    public interface onChildEventListener{
        void onChildAdded(DataSnapshot dataSnapshot, String s);
        void onChildChanged(DataSnapshot dataSnapshot, String s);
    }


    public interface onSingleValueListener{
        void onDataChange(DataSnapshot dataSnapshot);
        void onCancelled (DatabaseError databaseError);
    }


}

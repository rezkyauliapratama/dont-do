package rezkyaulia.android.dont_do;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.LinearLayout;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import rezkyaulia.android.dont_do.Models.Firebase.Activity;
import rezkyaulia.android.dont_do.Models.Firebase.DateModel;
import rezkyaulia.android.dont_do.Models.Firebase.DetailActivity;
import rezkyaulia.android.dont_do.Utility.Util;
import rezkyaulia.android.dont_do.databinding.DialogAddActivityBinding;
import rezkyaulia.android.dont_do.databinding.DialogUpdateActivityBinding;
import rezkyaulia.android.dont_do.databinding.ListItemTaskBinding;
import timber.log.Timber;

/**
 * Created by Mutya Nayavashti on 24/02/2017.
 */

public class ActivityRvAdapter  extends RecyclerView.ViewHolder {

    private ListItemTaskBinding binding;
    private Context mContext;
    DateModel date;

    public ActivityRvAdapter(View itemView) {
        super(itemView);
        binding = DataBindingUtil.bind(itemView);
        mContext = itemView.getContext();
    }

    public void bind(final String key, Activity item){
        binding.text01.setText(item.getName());

        Query query = FirebaseDatabase.getInstance().getReference().child(Constant.instanceOf().DETAILS).child(key).orderByPriority().limitToFirst(1);

        FirebaseEvent event = new FirebaseEvent(query);
        event.addChildEventListener(new FirebaseEvent.onChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                for (DataSnapshot item1 : dataSnapshot.getChildren()){

                    date = item1.getValue(DateModel.class);
//                    Timber.e(date.getTimestamp()+"");
                    binding.text02.setText(Util.getInstance().dateUtil().getHowLongItHasBeen(date));
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }
        });

        binding.cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            customDialog(key);
            }
        });
    }

    private void customDialog(final String key){
        final DialogUpdateActivityBinding dialogBinding;
        final Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);

        final Calendar c = Calendar.getInstance();

        dialogBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.dialog_update_activity, null, false);
        dialog.setContentView(dialogBinding.getRoot());

        dialogBinding.edit01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(mContext,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                c.set(year,monthOfYear,dayOfMonth);
                                DateModel date = Util.getInstance().dateUtil().getDate(c);
                                dialogBinding.edit01.setText(Util.getInstance().dateUtil().getDateFromFirebase(date));

                            }
                        }, year, month, day);
                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(date.getTimestamp());
//                cal.add(Calendar.DAY_OF_MONTH,-1);
                datePickerDialog.getDatePicker().setMinDate(cal.getTimeInMillis());
                datePickerDialog.show();

            }
        });

        dialogBinding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DetailActivity detailActivity = new DetailActivity(Util.getInstance().dateUtil().getDate(c));
                DatabaseReference save  = Constant.instanceOf().SecondaryPref.child(key).push();
                save.setValue(detailActivity);
                String saveKey = save.getKey();

                Constant.instanceOf().SecondaryPref.child(key).child(saveKey).setPriority(-(detailActivity.date.getTimestamp()));
                Constant.instanceOf().PrimaryRef.child(PreferencesManager.getInstance().getUserKey()).child(key).setPriority(-(detailActivity.date.getTimestamp()));
                if (!saveKey.isEmpty()){
                    dialog.hide();
                }

            }
        });

        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);

    }



}

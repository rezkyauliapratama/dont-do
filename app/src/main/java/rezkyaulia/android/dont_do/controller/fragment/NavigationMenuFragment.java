package rezkyaulia.android.dont_do.controller.fragment;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.gson.Gson;

import rezkyaulia.android.dont_do.BuildConfig;
import rezkyaulia.android.dont_do.R;
import rezkyaulia.android.dont_do.databinding.FragmentNavMenuBinding;
import timber.log.Timber;

/**
 * Created by Rezky Aulia Pratama on 12/3/2017.
 */

public class NavigationMenuFragment extends BaseFragment {

    private final int RC_SIGN_IN = 9001;

    FragmentNavMenuBinding binding;
    private OnFragmentInteractionListener mListener;

    private FirebaseAuth mAuth;

    GoogleSignInClient mGoogleSignInClient;
    public static NavigationMenuFragment newInstance() {
        NavigationMenuFragment fragment = new NavigationMenuFragment();
        /*Bundle args = new Bundle();
        args.putParcelable(ARGS1, habit);
        fragment.setArguments(args);*/
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        if (getArguments() != null) {
//            Timber.e("SAVEDINSTACESTATE");
//            mHabit = getArguments().getParcelable(ARGS1);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding =  DataBindingUtil.inflate(inflater, R.layout.fragment_nav_menu,container,false);

        if(savedInstanceState != null){
            Timber.e("SAVEDINSTACESTATE");
        }

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initSignIn();

        signIn();

        signOut();

//        revokeAccess();
    }

    private void initSignIn(){
        String webClientId = BuildConfig.WEB_CLIENT_ID;
        Timber.e("webClientId : "+webClientId);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("835334935417-6bs7cpon14vg7g3eg95nqh5u33n67hsm.apps.googleusercontent.com")
                .requestEmail()
                .build();


        mGoogleSignInClient = GoogleSignIn.getClient(getContext(), gso);
        mAuth = FirebaseAuth.getInstance();

/*
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getContext());
        updateUi(account);*/
    }

    private void signIn(){
        binding.signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Timber.e("Google sign in failed : "+ new Gson().toJson(e));
                updateUi(null);
            }

        }

    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUi(currentUser);

    }

    private void updateSignInData(FirebaseUser acct){
        if (acct != null) {
            String personName = acct.getDisplayName();
            String personEmail = acct.getEmail();
            String personId = acct.getUid();
            String phone = acct.getPhoneNumber();
            Uri personPhoto = acct.getPhotoUrl();
            String providerId = acct.getProviderId();
            Timber.e(personName + " | " + phone + " | " +providerId + " | " +personEmail + " | " +personId + " | " +personPhoto);
        }

    }
    private void signOut() {
        binding.buttonSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();

                mGoogleSignInClient.signOut()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                updateUi(null);                    }
                        });
            }
        });

    }


    private void revokeAccess() {
        mGoogleSignInClient.revokeAccess()
                .addOnCompleteListener( new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...

                        Timber.e("ONREVOKE ACCESS : "+new Gson().toJson(task));
                    }
                });
    }

    private void updateUi(FirebaseUser account){

        if (account != null){
            binding.signInButton.setVisibility(View.GONE);
            binding.buttonSignOut.setVisibility(View.VISIBLE);
            updateSignInData(account);
        }else{
            binding.signInButton.setVisibility(View.VISIBLE);
            binding.buttonSignOut.setVisibility(View.GONE);
        }

    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Timber.e("firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Timber.e( "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUi(user);
                            mListener.onSignInInteraction(user);

                        } else {
                            // If sign in fails, display a message to the user.
                            Timber.e("signInWithCredential:failure : "+ task.getException());
                            Toast.makeText(getContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUi(null);
                        }

                        // ...
                    }
                });
    }

    /*private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            Timber.e("account sign in : "+new Gson().toJson(account));
            updateUi(account);
            // Signed in successfully, show authenticated UI.
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Timber.w("signInResult:failed code=" + e.getStatusCode());
        }
    }
*/
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onSignInInteraction(FirebaseUser user);

     }
}

package com.example.go4lunch.views.activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.go4lunch.R;
import com.example.go4lunch.api.WorkerHelper;
import com.example.go4lunch.base.BaseActivity;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;

public class AuthenticationActivity extends BaseActivity {
    //FOR DATA
    // 1 - Identifier for Sign-In Activity
    private static final int RC_SIGN_IN = 123 ;
    private static final String TAG = "AUTHENTICATION ACTIVITY" ;

    @BindView(R.id.googleLoginButton)
    Button googleLoginButton;
    @BindView(R.id.facebookLoginButton)
    Button facebookLoginButton;
    @BindView(R.id.emailLoginButton)
    Button emailLoginButton;
    //FOR DESIGN
    // 1 - Get Coordinator Layout
    @BindView(R.id.constraintLayout_auth)
    ConstraintLayout coordinatorLayout;

    @Override
    public int getActivityLayout() {
        return R.layout.activity_authentication;
    }

    @Nullable
    @Override
    protected Toolbar getToolbar() { return null; }

    @Override
    protected void onResume() {
        super.onResume();
        // Checks if user is signed in (non-null)
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            this.startMainActivity();
        }
    }

    //------------------
    //OnClick
    //------------------

    @OnClick({R.id.googleLoginButton,
            R.id.facebookLoginButton,
            R.id.emailLoginButton,})
    public void onClickLoginButton(View view) {
        switch (view.getId()) {
            case R.id.googleLoginButton:
                this.startSignInWithGoogle();
                break;
            case R.id.facebookLoginButton:
                this.startSignInWithFacebook();
                break;
            case R.id.emailLoginButton:
                this.startSignInWithEmail();
                break;
        }
    }
    // --------------------
    // NAVIGATION
    // --------------------

    /**
     * Sign in With Google
     */
    private void startSignInWithGoogle()
    {
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(Collections.singletonList(
                                new AuthUI.IdpConfig.GoogleBuilder().build()))
                        .setIsSmartLockEnabled(false, true)
                        .build(),RC_SIGN_IN);
    }


    /**
     * Sign in With Facebook
     */
   private void startSignInWithFacebook()
    {
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(Collections.singletonList(
                                new AuthUI.IdpConfig.FacebookBuilder().build()))
                        .setIsSmartLockEnabled(false, true)
                        .build(),RC_SIGN_IN);
    }


    /**
     * Sign in With Facebook
     */
    private void startSignInWithEmail()
    {
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(Collections.singletonList(
                                new AuthUI.IdpConfig.EmailBuilder().build()))
                        .setIsSmartLockEnabled(false, true)
                        .build(),RC_SIGN_IN);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 4 - Handle SignIn Activity response on activity result
        this.handleResponseAfterSignIn(requestCode, resultCode, data);
    }

    // 2 - Show Snack Bar with a message
    private void showSnackBar(ConstraintLayout constraintLayout, String message){
        Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_SHORT).show();
    }

    // 3 - Method that handles response after SignIn Activity close
    private void handleResponseAfterSignIn(int requestCode, int resultCode, Intent data){
        IdpResponse response = IdpResponse.fromResultIntent(data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) { // SUCCESS
                showSnackBar(this.coordinatorLayout, getString(R.string.connection_succeed));
                if(this.getCurrentUser()!=null)
                {
                   this.startMainActivity();
                    }
                else {
                    this.createWorkerInFireStore();
                }

            } else { // ERRORS
                if (response == null) {
                    showSnackBar(this.coordinatorLayout, getString(R.string.error_authentication_canceled));
                }   else if (Objects.requireNonNull(response.getError()).getErrorCode() == ErrorCodes.NO_NETWORK) {
                   showSnackBar(this.coordinatorLayout, getString(R.string.error_no_internet));
            } else if (response.getError().getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                   showSnackBar(this.coordinatorLayout, getString(R.string.error_unknown_error));
            }

            }
        }
    }

    /**
     * Http request that create user in fireStore
     */
      private void createWorkerInFireStore() {
        if (this.getCurrentUser() != null) {
            String username = this.getCurrentUser().getDisplayName();
            String urlPicture = (this.getCurrentUser().getPhotoUrl() != null) ? this.getCurrentUser().getPhotoUrl().toString() : null;
            String placeId = "";
            String restaurant ="";
            WorkerHelper.createWorker(username, urlPicture, restaurant, placeId ).addOnFailureListener(this.onFailureListener());
        }
    }


    /**
     * Start MainActivity after login
     */
    private void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public static String getKeyHash(Context context) {
        String keyHash = null;
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(),
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                keyHash = Base64.encodeToString(md.digest(), Base64.DEFAULT);
                Log.d(TAG, "KeyHash:" + keyHash);
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return keyHash;
    }
}


package com.example.go4lunch.base;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Locale;

import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity {

    // fields
    protected FirebaseAuth mFirebaseAuth;
    private Locale mCurrentLocale;

    // Methods
    public abstract int getActivityLayout();

    @Nullable
    protected abstract Toolbar getToolbar();

    // --------------------
    // Activity
    // --------------------

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(this.getActivityLayout());
        ButterKnife.bind(this); //Configure Butterknife
        this.configureFirebaseAuth(); // configure firebase
    }

    @Override
    protected void onStart() {
        super.onStart();

        mCurrentLocale = getResources().getConfiguration().locale;
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        Locale locale = getLocale(this);

        if (!locale.equals(mCurrentLocale)) {

            mCurrentLocale = locale;
            recreate();
        }
    }

    public static Locale getLocale(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        String lang = sharedPreferences.getString("language", "en");
        switch (lang) {
            case "English":
                lang = "en";
                break;
            case "French":
                lang = "fr";
                break;
        }
        return new Locale(lang);
    }

    // --------------------
    // UI
    // --------------------

    public void configureToolBar(String text) {
        // If ToolBar exists
        if (this.getToolbar() != null) {
            getToolbar().setTitle(text);
            setSupportActionBar(this.getToolbar());
        }
    }

    /**
     * Configures the {@link FirebaseAuth}
     */
    private void configureFirebaseAuth() {
        this.mFirebaseAuth = FirebaseAuth.getInstance();
    }

    // --------------------
    // UTILS
    // --------------------

    @Nullable
    protected FirebaseUser getCurrentUser() {
        return mFirebaseAuth.getCurrentUser();
    }

// --------------------
    // ERROR HANDLER
    // --------------------

    protected OnFailureListener onFailureListener() {
        return e -> Toast.makeText(getApplicationContext(), "error_unknown_error", Toast.LENGTH_LONG).show();
    }
}

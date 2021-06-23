package com.example.go4lunch.base;

import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity {

    // fields
    protected FirebaseAuth mFirebaseAuth;

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
        ButterKnife.bind(this); //Configure ButterKnife
        this.configureFirebaseAuth(); // configure firebase
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

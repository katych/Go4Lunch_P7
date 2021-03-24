package com.example.go4lunch.settings;


import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.example.go4lunch.R;
import com.example.go4lunch.base.BaseActivity;

import java.util.Objects;

import butterknife.BindView;

public class SettingsActivity extends BaseActivity {


    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @Override
    public int getActivityLayout() {
        return R.layout.settings_activity;
    }

    @Nullable
    @Override
    protected Toolbar getToolbar() {
        return mToolbar ;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.configureToolBar(getResources().getString(R.string.settings));
        if (mToolbar != null) {
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }
    }
}

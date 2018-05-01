package com.artf.poloa.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity {

    protected Bundle savedInstanceState;
    BaseActivity(){}

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.savedInstanceState = savedInstanceState;
        setContentView(getContentLayout());
        ButterKnife.bind(this);
        initNavigation();
        initComponents();
    }

    public abstract int getContentLayout();

    public abstract void initNavigation();

    public abstract void initComponents();
}

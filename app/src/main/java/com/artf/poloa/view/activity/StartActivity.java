package com.artf.poloa.view.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;

import com.artf.poloa.R;

import javax.inject.Inject;

public class StartActivity extends BaseActivity{

    @Inject
    SharedPreferences sharedPreferences;

    @Override
    public int getContentLayout() {
        return R.layout.activity_start;
    }

    @Override
    public void initNavigation() {

    }

    @Override
    public void initComponents() {
        new Handler().postDelayed(() -> runMainUse(), 2000);

    }

    private void runMainUse() {
        Intent goToMainUse = new Intent(StartActivity.this, MainActivity.class);
        startActivityForResult(goToMainUse, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 0) {
            StartActivity.this.finish();
        }
    }

}

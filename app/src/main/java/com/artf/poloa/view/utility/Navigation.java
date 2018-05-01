package com.artf.poloa.view.utility;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.artf.poloa.view.activity.DetailsActivity;

public class Navigation {

    public static void setFragmentFrame(AppCompatActivity context, int frameId, Fragment fragment) {
        context.getSupportFragmentManager().beginTransaction()
                .replace(frameId, fragment)
                .commit();
    }

    public static void startCommentsActivity(Activity activity, String postObject){
        Intent intent = new Intent(activity, DetailsActivity.class);
        intent.putExtra("link", postObject);
        initActivity(activity, intent);
    }

    private static void initActivity(Activity activity, Intent intent) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(activity).toBundle();
            activity.startActivity(intent, bundle);
        } else {
            activity.startActivity(intent);
        }
    }
}

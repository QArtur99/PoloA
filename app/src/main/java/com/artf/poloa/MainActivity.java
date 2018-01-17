package com.artf.poloa;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.artf.poloa.data.database.Utility;
import com.artf.poloa.data.entity.TradeObject;
import com.artf.poloa.presenter.manager.ManagerMVP;
import com.artf.poloa.presenter.manager.ManagerThread;
import com.artf.poloa.utility.Settings;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Set;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements ManagerMVP.View {


    ManagerMVP.ThreadReceiver managerService;
    boolean mBound = false;
    /**
     * Defines callbacks for service binding, passed to bindService()
     */
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            ManagerThread.LocalBinder binder = (ManagerThread.LocalBinder) service;
            managerService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @OnClick(R.id.startButton)
    public void startButton() {
        HashMap<String, TradeObject> ccMap = Settings.Trade.CC_LIST;
        HashMap<String, TradeObject> ccMap2 = Utility.loadHashMap(getApplicationContext());

        Set<String> keys = ccMap.keySet();
        for (String key : keys) {
            if (!ccMap2.containsKey(key)) {
                ccMap2.put(key, ccMap.get(key));
            }
        }

        Set<String> keys2 = ccMap2.keySet();
        for (String key : keys2) {
            if (!ccMap.containsKey(key)) {
                ccMap2.remove(key);
            }
        }

        Type type = new TypeToken<HashMap<String, TradeObject>>() {}.getType();
        String jsonStringHashMap = new Gson().toJson(ccMap2, type);
        Utility.updateDatabase(getApplicationContext(), jsonStringHashMap);

        ManagerThread.startService(this);

    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        if (manager != null) {
            for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
                if (serviceClass.getName().equals(service.service.getClassName())) {
                    Log.i("isMyServiceRunning?", true + "");
                    return true;
                }
            }
        }
        Log.i("isMyServiceRunning?", false + "");
        return false;
    }

    @OnClick(R.id.isAlive)
    public void isAlive() {
        if (isMyServiceRunning(ManagerThread.class) && mBound) {
            int num = managerService.getRandomNumber();
            Toast.makeText(this, "isAlive: true" + num, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "isAlive: false", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {
//        MainActivity.this.finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, ManagerThread.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unbindService(mConnection);
        mBound = false;
    }

}

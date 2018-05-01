package com.artf.poloa.view.activity;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.artf.poloa.R;
import com.artf.poloa.data.database.Utility;
import com.artf.poloa.data.entity.TradeObject;
import com.artf.poloa.data.entity.WrapDetailsData;
import com.artf.poloa.presenter.manager.ManagerMVP;
import com.artf.poloa.presenter.manager.ManagerThread;
import com.artf.poloa.presenter.utility.Settings;
import com.artf.poloa.view.fragment.CcListFragment;
import com.artf.poloa.view.utility.Navigation;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import butterknife.BindView;
import butterknife.OnClick;

import static com.artf.poloa.view.utility.Navigation.startCommentsActivity;

public class MainActivity extends BaseActivity implements ManagerMVP.View, CcListFragment.CcListFragmentInt {

    ManagerMVP.ThreadReceiver managerService;
    boolean mBound = false;
    @BindView(R.id.mainActivityFrame) RelativeLayout mainActivityFrame;
    private CcListFragment ccListFragment;
    /**
     * Defines callbacks for service binding, passed to bindService()
     */
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
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
    public int getContentLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void initNavigation() {

    }

    @Override
    public void initComponents() {
        loadStartFragment();
        //this.deleteDatabase("PoloA.db");
        super.setResult(0);
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

        Set<String> keys2 = new HashSet<>();
        keys2.addAll(ccMap2.keySet());
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

    @OnClick(R.id.loadData)
    public void loadDataFromService() {
        if (isMyServiceRunning(ManagerThread.class) && mBound) {
            HashMap<String, TradeObject> ccMap = managerService.getCcMap();
            if (ccMap != null) {
                ccListFragment.setCcData(ccMap);
            } else {
                // ccListFragment.setRefreshing(false);
                loginFailed();
            }
        } else {
            Toast.makeText(this, "Data load failed", Toast.LENGTH_LONG).show();
        }
    }

    public void loadStartFragment() {
        ccListFragment = new CcListFragment();
        Navigation.setFragmentFrame(this, R.id.ccMapViewFrame, ccListFragment);
    }

    private void loginFailed() {
        Snackbar snackbar = Snackbar
                .make(mainActivityFrame, "Trading system isn't turned on!", Snackbar.LENGTH_LONG)
                .setAction("Turn on!", view -> {
                    startButton();
                });
        snackbar.show();
    }

    @Override
    public void onBackPressed() {}

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

    @Override
    public void loadData() {
        loadDataFromService();
    }

    @Override
    public void setDetails(String coinName, TradeObject tradeObject) {
        WrapDetailsData wrapDetailsData = new WrapDetailsData(coinName, tradeObject);
        String wrapDetailsDataString = new Gson().toJson(wrapDetailsData);
        startCommentsActivity(this, wrapDetailsDataString);
    }

}

package com.artf.poloa;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.artf.poloa.presenter.manager.ManagerMVP;
import com.artf.poloa.presenter.manager.ManagerThread;
import com.artf.poloa.presenter.rmi.RmiMVP;
import com.artf.poloa.presenter.root.App;
import com.artf.poloa.presenter.volume.VolumeMVP;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements ManagerMVP.View {

    @Inject
    ManagerMVP.ThreadReceiver managerThreadReceiver;

    @Inject
    RmiMVP.ThreadUI rmiThread;

    @Inject
    VolumeMVP.ThreadUI volumeThread;
    ManagerMVP.ThreadReceiver mService;
    boolean mBound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        ((App) getApplication()).getComponent().inject(this);
        Intent intent = new Intent(this, ManagerThread.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }


    @OnClick(R.id.startButton)
    public void startButton() {
//        ManagerThread thread = new ManagerThread(this, this);
//        thread.startThread();
//
//        if (thread.isAlive()) {
//            Toast.makeText(this, "isAlive: true", Toast.LENGTH_LONG).show();
//        } else {
//            Toast.makeText(this, "isAlive: false", Toast.LENGTH_LONG).show();
//        }
      //  managerThreadReceiver.setView(this);
//        managerThreadReceiver.startThread();
        ManagerThread.startService(this);

//        ManagerThread.startService(this);
//        volumeThread.setDataReciver(managerThreadReceiver);
//        volumeThread.startThread();
//
//        rmiThread.setDataReciver(managerThreadReceiver);
//        rmiThread.startThread();

     //   isAlive();
    }

    @Override
    protected void onPause() {
        this.onResume();
        super.onPause();
    }

    @OnClick(R.id.isAlive)
    public void isAlive() {
        if (mBound) {
            // Call a method from the LocalService.
            // However, if this call were something that might hang, then this request should
            // occur in a separate thread to avoid slowing down the activity performance.
            int num = mService.getRandomNumber();
            Toast.makeText(this, "number: " + num, Toast.LENGTH_SHORT).show();
        }
//        if (managerThreadReceiver.isItAlive() && volumeThread.isItAlive() && rmiThread.isItAlive()) {
//            Toast.makeText(this, "isAlive: true", Toast.LENGTH_LONG).show();
//        } else {
//            Toast.makeText(this, "isAlive: false", Toast.LENGTH_LONG).show();
//        }
    }

    @Override
    public void onBackPressed() {
//        MainActivity.this.finish();
    }

//    @SuppressLint("MissingSuperCall")
//    @Override
//    protected void onPause() {
//        // super.onPause();
//    }
//
//    @SuppressLint("MissingSuperCall")
//    @Override
//    protected void onStop() {
//        //  super.onStop();
//    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        managerThreadReceiver.onStop();
        volumeThread.onStop();
        rmiThread.onStop();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    /** Defines callbacks for service binding, passed to bindService() */
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            ManagerThread.LocalBinder binder = (ManagerThread.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };

}

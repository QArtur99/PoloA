package com.artf.poloa;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.artf.poloa.presenter.manager.ManagerMVP;
import com.artf.poloa.presenter.rmi.RmiMVP;
import com.artf.poloa.presenter.root.App;
import com.artf.poloa.presenter.volume.VolumeMVP;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity implements ManagerMVP.View {

    @Inject
    ManagerMVP.ThreadReceiver managerThreadReceiver;

    @Inject
    RmiMVP.ThreadUI rmiThread;

    @Inject
    VolumeMVP.ThreadUI volumeThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((App) getApplication()).getComponent().inject(this);

        managerThreadReceiver.setView(this);
        managerThreadReceiver.startThread();

        volumeThread.setDataReciver(managerThreadReceiver);
        volumeThread.startThread();

        rmiThread.setDataReciver(managerThreadReceiver);
        rmiThread.startThread();

    }


    @Override
    public void onBackPressed() {
//        MainActivity.this.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        managerThreadReceiver.onStop();
        rmiThread.onStop();
    }

    @Override
    public void onStart() {
        super.onStart();
    }
}

package com.artf.poloa;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.artf.poloa.presenter.manager.ManagerMVP;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        ((App) getApplication()).getComponent().inject(this);
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
        managerThreadReceiver.setView(this);
        managerThreadReceiver.startThread();

        volumeThread.setDataReciver(managerThreadReceiver);
        volumeThread.startThread();

        rmiThread.setDataReciver(managerThreadReceiver);
        rmiThread.startThread();

        isAlive();
    }

    @Override
    protected void onPause() {
        this.onResume();
        super.onPause();
    }

    @OnClick(R.id.isAlive)
    public void isAlive() {

        if (managerThreadReceiver.isItAlive() && volumeThread.isItAlive() && rmiThread.isItAlive()) {
            Toast.makeText(this, "isAlive: true", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "isAlive: false", Toast.LENGTH_LONG).show();
        }
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
}

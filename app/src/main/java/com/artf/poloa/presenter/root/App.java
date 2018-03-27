package com.artf.poloa.presenter.root;

import android.app.Application;

import com.artf.poloa.data.network.RetrofitModule;
import com.artf.poloa.presenter.ema.EmaModule;
import com.artf.poloa.presenter.manager.ManagerModule;
import com.artf.poloa.presenter.rmi.RmiModule;

public class App extends Application {

    private ApplicationComponent component;

    @Override
    public void onCreate() {
        super.onCreate();

        component = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .managerModule(new ManagerModule())
                .emaModule(new EmaModule())
                .rmiModule(new RmiModule())
                .retrofitModule(new RetrofitModule())
                .build();
    }

    public ApplicationComponent getComponent() {
        return component;
    }
}

package com.artf.poloa.presenter.root;

import com.artf.poloa.view.activity.MainActivity;
import com.artf.poloa.data.network.RetrofitModule;
import com.artf.poloa.presenter.ema.EmaModule;
import com.artf.poloa.presenter.manager.ManagerModule;
import com.artf.poloa.presenter.manager.ManagerThread;
import com.artf.poloa.presenter.rmi.RmiModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class, EmaModule.class, ManagerModule.class, RmiModule.class, RetrofitModule.class})
public interface ApplicationComponent {

    void inject(MainActivity mainActivity);
    void inject(ManagerThread managerThread);

}

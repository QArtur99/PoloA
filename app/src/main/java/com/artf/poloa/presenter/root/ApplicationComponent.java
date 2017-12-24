package com.artf.poloa.presenter.root;

import com.artf.poloa.MainActivity;
import com.artf.poloa.data.network.RetrofitModule;
import com.artf.poloa.presenter.manager.ManagerModule;
import com.artf.poloa.presenter.rmi.RmiModule;
import com.artf.poloa.presenter.volume.VolumeModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class, ManagerModule.class, VolumeModule.class, RmiModule.class, RetrofitModule.class})
public interface ApplicationComponent {

    void inject(MainActivity mainActivity);

}

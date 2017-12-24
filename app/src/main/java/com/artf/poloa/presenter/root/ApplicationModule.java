package com.artf.poloa.presenter.root;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.artf.poloa.data.network.PoloniexPublicAPI;
import com.artf.poloa.data.network.PoloniexTradingAPI;
import com.artf.poloa.data.repository.DataRepository;
import com.artf.poloa.data.repository.PoloniexPublicApi;
import com.artf.poloa.data.repository.PoloniexTradingApi;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {

    private Application application;

    public ApplicationModule(Application application) {
        this.application = application;
    }

    @Provides
    @Singleton
    public Context provideContext() {
        return application;
    }

    @Provides
    @Singleton
    public SharedPreferences provideSharedPrefs() {
        return PreferenceManager.getDefaultSharedPreferences(application);
    }

    @Provides
    @Singleton
    public DataRepository.PublicAPI provideRetrofitRepository(Context context, SharedPreferences sharedPreferences, PoloniexPublicAPI poloniexPublicAPI){
        return new PoloniexPublicApi(context, sharedPreferences, poloniexPublicAPI);
    }

    @Provides
    @Singleton
    public DataRepository.TradingAPI provideTradingRepository(Context context, SharedPreferences sharedPreferences, PoloniexTradingAPI poloniexTradingAPI){
        return new PoloniexTradingApi(context, sharedPreferences, poloniexTradingAPI);
    }




}

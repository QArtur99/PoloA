package com.artf.poloa.presenter.manager;

import com.artf.poloa.data.repository.DataRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;


@Module
public class ManagerModule {


//    @Provides
//    @Singleton
//    public ManagerMVP.ThreadReceiver provideThread(ManagerMVP.Presenter presenter){
//        return new ManagerThread(presenter);
//    }


    @Provides
    @Singleton
    public ManagerMVP.Presenter provideLoginActivityPresenter(ManagerMVP.Model model){
        return new ManagerPresenter(model);
    }

    @Provides
    @Singleton
    public ManagerMVP.Model provideLoginActivityModel(DataRepository.TradingAPI repositoryRetrofit){
        return new ManagerModel(repositoryRetrofit);
    }

}

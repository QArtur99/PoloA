package com.artf.poloa.presenter.rmi;

import com.artf.poloa.data.repository.DataRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;


@Module
public class RmiModule {


    @Provides
    @Singleton
    public RmiMVP.ThreadUI provideThread(RmiMVP.Presenter presenter){
        return new RmiThread(presenter);
    }


    @Provides
    @Singleton
    public RmiMVP.Presenter provideLoginActivityPresenter(RmiMVP.Model model){
        return new RmiPresenter(model);
    }

    @Provides
    @Singleton
    public RmiMVP.Model provideLoginActivityModel(DataRepository.PublicAPI repositoryRetrofit){
        return new RmiModel(repositoryRetrofit);
    }

}

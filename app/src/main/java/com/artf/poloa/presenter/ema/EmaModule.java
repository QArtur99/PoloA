package com.artf.poloa.presenter.ema;

import com.artf.poloa.data.repository.DataRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;


@Module
public class EmaModule {


    @Provides
    @Singleton
    public EmaMVP.ThreadUI provideThread(EmaMVP.Presenter presenter){
        return new EmaThread(presenter);
    }


    @Provides
    @Singleton
    public EmaMVP.Presenter provideLoginActivityPresenter(EmaMVP.Model model){
        return new EmaPresenter(model);
    }

    @Provides
    @Singleton
    public EmaMVP.Model provideLoginActivityModel(DataRepository.PublicAPI repositoryRetrofit){
        return new EmaModel(repositoryRetrofit);
    }

}

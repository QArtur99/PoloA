package com.artf.poloa.presenter.volume;

import com.artf.poloa.data.repository.DataRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;


@Module
public class VolumeModule {


    @Provides
    @Singleton
    public VolumeMVP.ThreadUI provideThread(VolumeMVP.Presenter presenter){
        return new VolumeThread(presenter);
    }


    @Provides
    @Singleton
    public VolumeMVP.Presenter provideLoginActivityPresenter(VolumeMVP.Model model){
        return new VolumePresenter(model);
    }

    @Provides
    @Singleton
    public VolumeMVP.Model provideLoginActivityModel(DataRepository.PublicAPI repositoryRetrofit){
        return new VolumeModel(repositoryRetrofit);
    }

}

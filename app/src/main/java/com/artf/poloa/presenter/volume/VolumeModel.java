package com.artf.poloa.presenter.volume;

import com.artf.poloa.data.entity.WrapJSONArray;
import com.artf.poloa.data.repository.DataRepository;

import io.reactivex.Observable;


public class VolumeModel implements VolumeMVP.Model {

    private DataRepository.PublicAPI repositoryRetrofit;

    public VolumeModel(DataRepository.PublicAPI repositoryRetrofit) {
        this.repositoryRetrofit = repositoryRetrofit;
    }


    @Override
    public Observable<WrapJSONArray> returnChartData(int timePeriod) {
        return repositoryRetrofit.returnChartData(timePeriod);
    }

    @Override
    public Observable<WrapJSONArray> returnTradeHistory(int timePeriod) {
        return repositoryRetrofit.returnTradeHistory(timePeriod);
    }

}

package com.artf.poloa.presenter.ema;

import com.artf.poloa.data.entity.WrapJSONArray;
import com.artf.poloa.data.repository.DataRepository;

import io.reactivex.Observable;


public class EmaModel implements EmaMVP.Model {

    private DataRepository.PublicAPI repositoryRetrofit;

    public EmaModel(DataRepository.PublicAPI repositoryRetrofit) {
        this.repositoryRetrofit = repositoryRetrofit;
    }


    @Override
    public Observable<WrapJSONArray> returnChartData(String ccName, int timePeriod) {
        return repositoryRetrofit.returnChartData(ccName, timePeriod);
    }

}

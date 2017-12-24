package com.artf.poloa.presenter.rmi;

import com.artf.poloa.data.entity.WrapJSONArray;
import com.artf.poloa.data.repository.DataRepository;

import io.reactivex.Observable;


public class RmiModel implements RmiMVP.Model {

    private DataRepository.PublicAPI repositoryRetrofit;

    public RmiModel(DataRepository.PublicAPI repositoryRetrofit) {
        this.repositoryRetrofit = repositoryRetrofit;
    }


    @Override
    public Observable<WrapJSONArray> returnChartData(int timePeriod) {
        return repositoryRetrofit.returnChartData(timePeriod);
    }

}

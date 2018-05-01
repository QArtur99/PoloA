package com.artf.poloa.presenter.manager;

import com.artf.poloa.data.entity.Buy;
import com.artf.poloa.data.entity.WrapJSONObject;
import com.artf.poloa.data.repository.DataRepository;

import io.reactivex.Observable;


public class ManagerModel implements ManagerMVP.Model {

    private DataRepository.TradingAPI repositoryRetrofit;

    public ManagerModel(DataRepository.TradingAPI repositoryRetrofit) {
        this.repositoryRetrofit = repositoryRetrofit;
    }

    @Override
    public Observable<WrapJSONObject> returnBalances() {
        return repositoryRetrofit.returnBalances();
    }

    @Override
    public Observable<Buy> buy(String ccName, double rate, double amount) {
        return repositoryRetrofit.buy(ccName, rate, amount);
    }

    @Override
    public Observable<WrapJSONObject> sell(String type, String ccName, double rate, double amount) {
        return repositoryRetrofit.sell(type, ccName, rate, amount);
    }

}

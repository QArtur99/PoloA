package com.artf.poloa.data.repository;

import com.artf.poloa.data.entity.Buy;
import com.artf.poloa.data.entity.WrapJSONArray;
import com.artf.poloa.data.entity.WrapJSONObject;

import io.reactivex.Observable;

public interface DataRepository {
    interface PublicAPI {

        Observable<WrapJSONArray> returnTradeHistory(int timePeriod);
        Observable<WrapJSONArray> returnChartData(int timePeriod);

    }

    interface TradingAPI {

        Observable<WrapJSONObject> returnBalances();
        Observable<Buy> buy(double rate, double amount);
        Observable<WrapJSONObject> sell(double rate, double amount);

    }
}

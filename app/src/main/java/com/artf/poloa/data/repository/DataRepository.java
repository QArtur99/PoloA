package com.artf.poloa.data.repository;

import com.artf.poloa.data.entity.Buy;
import com.artf.poloa.data.entity.WrapJSONArray;
import com.artf.poloa.data.entity.WrapJSONObject;

import io.reactivex.Observable;

public interface DataRepository {
    interface PublicAPI {

        Observable<WrapJSONArray> returnTradeHistory(String ccName, int timePeriod);
        Observable<WrapJSONArray> returnChartData(String ccName, int timePeriod);

    }

    interface TradingAPI {

        Observable<WrapJSONObject> returnBalances();
        Observable<Buy> buy(String ccName, double rate, double amount);
        Observable<WrapJSONObject> sell(String type, String ccName, double rate, double amount);

    }
}

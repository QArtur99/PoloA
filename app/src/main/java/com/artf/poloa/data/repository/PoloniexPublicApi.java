package com.artf.poloa.data.repository;

import android.content.Context;
import android.content.SharedPreferences;

import com.artf.poloa.data.entity.WrapJSONArray;
import com.artf.poloa.data.network.PoloniexPublicAPI;

import java.util.ArrayDeque;
import java.util.HashMap;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;


public class PoloniexPublicApi implements DataRepository.PublicAPI {

    private static final long TIME_LIMIT_IN_MILLIS = 1000;
    private static final int CALLS_PER_SEC = 6;

    private Subject<String> mObservable = PublishSubject.create();
    private Context context;
    private SharedPreferences sharedPreferences;
    private PoloniexPublicAPI poloniexPublicAPI;
    private ArrayDeque<Long> callCounterLine = new ArrayDeque<>();


    public PoloniexPublicApi(Context context, SharedPreferences sharedPreferences, PoloniexPublicAPI poloniexPublicAPI) {
        this.context = context;
        this.sharedPreferences = sharedPreferences;
        this.poloniexPublicAPI = poloniexPublicAPI;
        this.callCounterLine.addFirst(System.currentTimeMillis());
    }



    private boolean setCallCounter() {
        synchronized (this) {
            if (callCounterLine.size() > CALLS_PER_SEC) {
                callCounterLine.removeLast();
            }

            long now = System.currentTimeMillis();
            long timeGap = now - callCounterLine.getLast();
            if (callCounterLine.size() >= CALLS_PER_SEC && TIME_LIMIT_IN_MILLIS > timeGap) {
                String waitString = String.valueOf(TIME_LIMIT_IN_MILLIS - timeGap);
                String x = "You have to wait:" + waitString + "millis";
                mObservable.onNext(x);
                return true;
            }
            callCounterLine.addFirst(System.currentTimeMillis());
            return false;
        }
    }

    @Override
    public Observable<WrapJSONArray> returnTradeHistory(final String ccName, final int timePeriod) {
        if (setCallCounter()) {
            return Observable.empty();
        }

        long unixTime = System.currentTimeMillis() / 1000L;
        HashMap<String, String> args = new HashMap<>();
        args.put("command", "returnTradeHistory");
        args.put("currencyPair", "BTC_" + ccName);
        args.put("start", String.valueOf(unixTime - (timePeriod)));
        args.put("end", String.valueOf(unixTime));
        return poloniexPublicAPI.returnTradeHistory(args).flatMap(new Function<WrapJSONArray, ObservableSource<WrapJSONArray>>() {
            @Override
            public ObservableSource<WrapJSONArray> apply(WrapJSONArray wrapJSONArray) throws Exception {
                wrapJSONArray.ccName = ccName;
                wrapJSONArray.periodTime = timePeriod;
                return Observable.just(wrapJSONArray);
            }
        });
    }

    @Override
    public Observable<WrapJSONArray> returnChartData(final String ccName, int timePeriod) {
        if (setCallCounter()) {
            return Observable.empty();
        }

        long unixTime = System.currentTimeMillis() / 1000L;
        HashMap<String, String> args = new HashMap<>();
        args.put("command", "returnChartData");
//        args.put("currencyPair", Settings.Trade.CC_NAME_PAIR);
        args.put("currencyPair", "BTC_" + ccName);
        args.put("period", String.valueOf(timePeriod));
        args.put("start", String.valueOf(unixTime - (timePeriod * 240)));
        args.put("end", String.valueOf(unixTime));

        return poloniexPublicAPI.returnChartData(args).flatMap(new Function<WrapJSONArray, ObservableSource<WrapJSONArray>>() {
            @Override
            public ObservableSource<WrapJSONArray> apply(WrapJSONArray wrapJSONArray) throws Exception {
                wrapJSONArray.ccName = ccName;
                return Observable.just(wrapJSONArray);
            }
        });
    }

}

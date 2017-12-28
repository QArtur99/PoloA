package com.artf.poloa.data.repository;

import android.content.Context;
import android.content.SharedPreferences;

import com.artf.poloa.BuildConfig;
import com.artf.poloa.data.entity.Buy;
import com.artf.poloa.data.entity.WrapJSONObject;
import com.artf.poloa.data.network.PoloniexTradingAPI;
import com.artf.poloa.data.utility.Utility;

import java.util.ArrayDeque;
import java.util.HashMap;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

public class PoloniexTradingApi implements DataRepository.TradingAPI {

    private static final long TIME_LIMIT_IN_MILLIS = 1000;
    private static final int CALLS_PER_SEC = 6;
    private static final String KEY = BuildConfig.KEY;
    private static final String SECRET_KEY = BuildConfig.SECRET;

    private Subject<String> mObservable = PublishSubject.create();
    private Context context;
    private SharedPreferences sharedPreferences;
    private PoloniexTradingAPI poloniexTradingAPI;
    private ArrayDeque<Long> callCounterLine = new ArrayDeque<>();


    public PoloniexTradingApi(Context context, SharedPreferences sharedPreferences, PoloniexTradingAPI poloniexTradingAPI) {
        this.context = context;
        this.sharedPreferences = sharedPreferences;
        this.poloniexTradingAPI = poloniexTradingAPI;
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
    public Observable<WrapJSONObject> returnBalances() {
        if (setCallCounter()) {
            return Observable.empty();
        }

        HashMap<String, String> args = new HashMap<>();
        args.put("command", "returnBalances");
        args.put("nonce", String.valueOf(System.currentTimeMillis()));
        String postData = Utility.getUri(args);
        String signature = Utility.getSignature(SECRET_KEY, postData);

        return poloniexTradingAPI.returnBalances(KEY, signature, args);
    }

    @Override
    public Observable<Buy> buy(final String ccName, double rate, double amount) {
        if (setCallCounter()) {
            return Observable.empty();
        }

        HashMap<String, String> args = new HashMap<String, String>();
        args.put("command", "buy");
        args.put("currencyPair", "BTC_" + ccName);
        args.put("rate", String.valueOf(rate));
        args.put("amount", String.valueOf(amount));
        args.put("immediateOrCancel", "1");

        args.put("nonce", String.valueOf(System.currentTimeMillis()));
        String postData = Utility.getUri(args);
        String signature = Utility.getSignature(SECRET_KEY, postData);

        return poloniexTradingAPI.buy(KEY, signature, args).flatMap(new Function<Buy, ObservableSource<Buy>>() {
            @Override
            public ObservableSource<Buy> apply(Buy wrapJSONArray) throws Exception {
                wrapJSONArray.ccName = ccName;
                return Observable.just(wrapJSONArray);
            }
        });
    }

    @Override
    public Observable<WrapJSONObject> sell(String type, final String ccName, double rate, double amount) {
        if (setCallCounter()) {
            return Observable.empty();
        }

        HashMap<String, String> args = new HashMap<String, String>();
        args.put("command", "sell");
        args.put("currencyPair", "BTC_" + ccName);
        args.put("rate", String.valueOf(rate));
        args.put("amount", String.valueOf(amount));
        args.put(type, "1");

        args.put("nonce", String.valueOf(System.currentTimeMillis()));
        String postData = Utility.getUri(args);
        String signature = Utility.getSignature(SECRET_KEY, postData);

        return poloniexTradingAPI.sell(KEY, signature, args).flatMap(new Function<WrapJSONObject, ObservableSource<WrapJSONObject>>() {
            @Override
            public ObservableSource<WrapJSONObject> apply(WrapJSONObject wrapJSONArray) throws Exception {
                wrapJSONArray.ccName = ccName;
                return Observable.just(wrapJSONArray);
            }
        });
    }




}

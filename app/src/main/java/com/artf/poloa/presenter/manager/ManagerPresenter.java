package com.artf.poloa.presenter.manager;

import android.support.annotation.Nullable;
import android.util.Log;

import com.artf.poloa.data.entity.Buy;
import com.artf.poloa.data.entity.WrapJSONObject;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.reactivex.Scheduler;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;


public class ManagerPresenter implements ManagerMVP.Presenter {

    @Nullable
    private ManagerMVP.Thread thread;
    private ManagerMVP.Model model;
    private CompositeDisposable disposable = new CompositeDisposable();

    public ManagerPresenter(ManagerMVP.Model model) {
        this.model = model;
    }

    @Override
    public void returnBalances() {
        int threadCount = Runtime.getRuntime().availableProcessors();
        ExecutorService threadPoolExecutor = Executors.newFixedThreadPool(threadCount);
        Scheduler scheduler = Schedulers.from(threadPoolExecutor);

        DisposableObserver<WrapJSONObject> disposableObserver = model.returnBalances().observeOn(scheduler).
                subscribeOn(scheduler).subscribeWith(new DisposableObserver<WrapJSONObject>() {

            @Override
            public void onNext(@NonNull WrapJSONObject postParent) {
                if (thread != null) {
                    thread.returnBalances(postParent.jsonObject);
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                if (thread != null) {
                    thread.erorrReturnBalances();
                }
            }

            @Override
            public void onComplete() { }

        });
        disposable.add(disposableObserver);
    }

    @Override
    public void buy(String ccName, double rate, double amount) {
        Log.e(ManagerPresenter.class.getSimpleName(), "BUY" + ccName);
        int threadCount = Runtime.getRuntime().availableProcessors();
        ExecutorService threadPoolExecutor = Executors.newFixedThreadPool(threadCount);
        Scheduler scheduler = Schedulers.from(threadPoolExecutor);

        DisposableObserver<Buy> disposableObserver = model.buy(ccName, rate, amount).observeOn(scheduler).
                subscribeOn(scheduler).subscribeWith(new DisposableObserver<Buy>() {

            @Override
            public void onNext(@NonNull Buy postParent) {
                if (thread != null) {
                    thread.buy(postParent);
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                if (thread != null) {
                    Log.e(ManagerPresenter.class.getSimpleName(), "BUY" + e.toString());
                    thread.erorrBuy();
                }
            }

            @Override
            public void onComplete() { }

        });
        disposable.add(disposableObserver);
    }

    @Override
    public void sell(String type, String ccName, double rate, double amount) {
        Log.e(ManagerPresenter.class.getSimpleName(), "SELL" + ccName);
        int threadCount = Runtime.getRuntime().availableProcessors();
        ExecutorService threadPoolExecutor = Executors.newFixedThreadPool(threadCount);
        Scheduler scheduler = Schedulers.from(threadPoolExecutor);

        DisposableObserver<WrapJSONObject> disposableObserver = model.sell(type, ccName, rate, amount).observeOn(scheduler).
                subscribeOn(scheduler).subscribeWith(new DisposableObserver<WrapJSONObject>() {

            @Override
            public void onNext(@NonNull WrapJSONObject postParent) {
                if (thread != null) {
                    thread.sell(postParent.jsonObject);
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                if (thread != null) {
                    Log.e(ManagerPresenter.class.getSimpleName(), "SELL" + e.toString());
                    thread.erorrSell();
                }
            }

            @Override
            public void onComplete() { }

        });
        disposable.add(disposableObserver);
    }

    @Override
    public void onStop() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }


    @Override
    public void setThread(ManagerMVP.Thread thread) {
        this.thread = thread;
    }
}

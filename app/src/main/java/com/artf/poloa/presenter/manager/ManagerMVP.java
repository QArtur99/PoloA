package com.artf.poloa.presenter.manager;

import com.artf.poloa.data.entity.Buy;
import com.artf.poloa.data.entity.WrapJSONObject;
import com.google.gson.JsonObject;

import io.reactivex.Observable;

public interface ManagerMVP {


    interface View {

    }

    interface Thread {
        void returnBalances(JsonObject jsonArray);
        void erorrReturnBalances();

        void buy(Buy jsonArray);
        void erorrBuy();

        void sell(JsonObject jsonArray);
        void erorrSell();
    }

    interface ThreadReceiver {
        void setRmiData(double rmiValue, double rmiSingal);
        void setTradeHistory24H(double trend24H);
        void setTradeHistory15m(double trend15m);
        void setStochasticData(double rmiValue, double rmiSingal);
        void setView(ManagerMVP.View view);
        void startThread();
        void onStop();
        void setLastValue(double close);
    }


    interface Presenter {
        void returnBalances();
        void buy(double rate, double amount);
        void sell(double rate, double amount);
        void setThread(ManagerMVP.Thread thread);
        void onStop();
    }

    interface Model {
        Observable<WrapJSONObject> returnBalances();
        Observable<Buy> buy(double rate, double amount);
        Observable<WrapJSONObject> sell(double rate, double amount);
    }
}

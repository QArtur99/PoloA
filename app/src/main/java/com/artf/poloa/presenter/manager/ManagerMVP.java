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
        void setRmiData(String ccName, double rmiValue, double rmiSingal);
        void setTradeHistory24H(String ccName, double trend24H);
        void setTradeHistory15m(String ccName, double trend15m);
        void setStochasticData(String ccName, double rmiValue, double rmiSingal);
        void setView(ManagerMVP.View view);
        void startThread();
        Boolean isItAlive();
        int getRandomNumber();
        void onStop();
        void setLastValue(String ccName, double close);
    }


    interface Presenter {
        void returnBalances();
        void buy(String ccName, double rate, double amount);
        void sell(String type, String ccName, double rate, double amount);
        void setThread(ManagerMVP.Thread thread);
        void onStop();
    }

    interface Model {
        Observable<WrapJSONObject> returnBalances();
        Observable<Buy> buy(String ccName, double rate, double amount);
        Observable<WrapJSONObject> sell(String type, String ccName, double rate, double amount);
    }
}

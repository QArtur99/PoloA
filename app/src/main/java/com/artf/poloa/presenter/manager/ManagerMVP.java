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
        void setEmaData(String ccName, double emaValue);
        void setRmiData(String ccName, double rmiValue, double rmiSingal);
        void setView(ManagerMVP.View view);
        int getRandomNumber();
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

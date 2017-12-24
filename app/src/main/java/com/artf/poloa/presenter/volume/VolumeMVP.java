package com.artf.poloa.presenter.volume;

import com.artf.poloa.data.entity.WrapJSONArray;
import com.artf.poloa.presenter.manager.ManagerMVP;

import io.reactivex.Observable;

public interface VolumeMVP {


    interface View {

    }

    interface Thread {
        void returnTradeHistory(WrapJSONArray wrapJSONArray);
    }

    interface ThreadUI {
        void setDataReciver(ManagerMVP.ThreadReceiver threadReceiver);
        void startThread();
        void onStop();
    }


    interface Presenter {
        void returnTradeHistory(int timePeriod);
        void setThread(VolumeMVP.Thread thread);
        void onStop();
    }

    interface Model {
        Observable<WrapJSONArray> returnChartData(int timePeriod);
        Observable<WrapJSONArray> returnTradeHistory(int timePeriod);
    }
}

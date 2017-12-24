package com.artf.poloa.presenter.rmi;

import com.artf.poloa.data.entity.WrapJSONArray;
import com.artf.poloa.presenter.manager.ManagerMVP;

import io.reactivex.Observable;

public interface RmiMVP {


    interface View {

    }

    interface Thread {
        void returnChartData(WrapJSONArray jsonArray);
    }

    interface ThreadUI {
        void setDataReciver(ManagerMVP.ThreadReceiver threadReceiver);
        void startThread();
        void onStop();
    }


    interface Presenter {
        void returnChartData(String ccName, int timePeriod);
        void setThread(RmiMVP.Thread thread);
        void onStop();
    }

    interface Model {
        Observable<WrapJSONArray> returnChartData(String ccName, int timePeriod);;
    }
}

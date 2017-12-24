package com.artf.poloa.presenter.volume;


import com.artf.poloa.data.entity.PublicTradeHistory;
import com.artf.poloa.data.entity.WrapJSONArray;
import com.artf.poloa.presenter.manager.ManagerMVP;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.artf.poloa.utility.Constant.PERIOD_1D;
import static com.artf.poloa.utility.Constant.PERIOD_1M;
import static com.artf.poloa.utility.Constant.PERIOD_30M;
import static com.artf.poloa.utility.Constant.PERIOD_5M;

public class VolumeThread extends Thread implements VolumeMVP.Thread, VolumeMVP.ThreadUI {

    private List<PublicTradeHistory> publicTradeHistoryList = new ArrayList<>();
    private VolumeMVP.Presenter presenter;
    private ManagerMVP.ThreadReceiver threadReceiver;
    private LoopTradeHistory24H loopTradeHistory24H = new LoopTradeHistory24H();
    private LoopTradeHistory15m loopTradeHistory15m = new LoopTradeHistory15m();


    public VolumeThread(VolumeMVP.Presenter presenter) {
        this.presenter = presenter;
        presenter.setThread(this);
    }


    @Override
    public void run() {
        long wait24H = 1000L * PERIOD_5M + 1L;
        long wait30m = 1000L * PERIOD_1M + 1L;
        Timer timer = new Timer();
//        timer.scheduleAtFixedRate(loopTradeHistory24H, 0, wait24H);
        timer.scheduleAtFixedRate(loopTradeHistory15m, 0, wait30m);
    }


    @Override
    public void returnTradeHistory(WrapJSONArray wrapJSONArray) {
        publicTradeHistoryList.clear();
        setData(wrapJSONArray.jsonArray);
        switch (wrapJSONArray.periodTime) {
            case PERIOD_30M:
                threadReceiver.setTradeHistory15m(getVolumeRate(publicTradeHistoryList));
                break;
            case PERIOD_1D:
                threadReceiver.setTradeHistory24H(getVolumeRate(publicTradeHistoryList));
                break;
        }
    }

    private double getVolumeRate(List<PublicTradeHistory> publicTradeHistoryList){
        double buyOperations = 0.0;
        double sellOperations = 0.0;
        for (PublicTradeHistory historyObject : publicTradeHistoryList) {
            if (historyObject.type.equals("buy")) {
                buyOperations += Double.valueOf(historyObject.total);
            } else {
                sellOperations += Double.valueOf(historyObject.total);
            }
        }
        double trend = buyOperations / sellOperations;
        return trend;
    }

    private void setData(JsonArray jsonArray) {
        int jsonArrayLength = jsonArray.size();
        for (int i = jsonArrayLength - 1; i >= 0; i--) {
            String string = jsonArray.get(i).toString();
            PublicTradeHistory publicTradeHistory = new Gson().fromJson(string, PublicTradeHistory.class);

            if (publicTradeHistory != null) {
                publicTradeHistoryList.add(publicTradeHistory);
            }
        }
    }


    @Override
    public void onStop() {
        VolumeThread.this.interrupt();
    }

    @Override
    public void setDataReciver(ManagerMVP.ThreadReceiver threadReceiver) {
        this.threadReceiver = threadReceiver;
    }

    @Override
    public void startThread() {
        VolumeThread.this.start();
    }

    private class LoopTradeHistory24H extends TimerTask {
        public void run() {
            presenter.returnTradeHistory(PERIOD_1D);
        }
    }

    private class LoopTradeHistory15m extends TimerTask {
        public void run() {
            presenter.returnTradeHistory(PERIOD_30M);
        }
    }

}

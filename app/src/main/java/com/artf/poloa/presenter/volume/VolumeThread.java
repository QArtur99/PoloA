package com.artf.poloa.presenter.volume;


import android.util.Log;

import com.artf.poloa.data.entity.PublicTradeHistory;
import com.artf.poloa.data.entity.TradeObject;
import com.artf.poloa.data.entity.WrapJSONArray;
import com.artf.poloa.presenter.manager.ManagerMVP;
import com.artf.poloa.utility.Settings;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import static com.artf.poloa.utility.Constant.PERIOD_1D;
import static com.artf.poloa.utility.Constant.PERIOD_2M;
import static com.artf.poloa.utility.Constant.PERIOD_30M;
import static com.artf.poloa.utility.Constant.PERIOD_5M;

public class VolumeThread extends Thread implements VolumeMVP.Thread, VolumeMVP.ThreadUI {

    private List<PublicTradeHistory> publicTradeHistoryList = new ArrayList<>();
    private VolumeMVP.Presenter presenter;
    private ManagerMVP.ThreadReceiver threadReceiver;
    private LoopTradeHistory24H loopTradeHistory24H = new LoopTradeHistory24H();
    private LoopTradeHistory15m loopTradeHistory15m = new LoopTradeHistory15m();
    private HashMap<String, TradeObject> ccMap;


    public VolumeThread(VolumeMVP.Presenter presenter) {
        this.presenter = presenter;
        presenter.setThread(this);
        ccMap = Settings.Trade.CC_LIST;
    }

    @Override
    public Boolean isItAlive(){
        return VolumeThread.this.isAlive();
    }


    @Override
    public void run() {
        long wait24H = 1000L * PERIOD_5M + 1L;
        long wait30m = 1000L * PERIOD_2M + 1L;
        Timer timer = new Timer();
//        timer.scheduleAtFixedRate(loopTradeHistory24H, 0, wait24H);
        timer.scheduleAtFixedRate(loopTradeHistory15m, 2001L, wait30m);
    }


    @Override
    public synchronized void returnTradeHistory(WrapJSONArray wrapJSONArray) {
            String ccName = wrapJSONArray.ccName;
            publicTradeHistoryList.clear();
            setData(wrapJSONArray.jsonArray);
            switch (wrapJSONArray.periodTime) {
                case PERIOD_30M:
                    threadReceiver.setTradeHistory15m(ccName, getVolumeRate(publicTradeHistoryList));
                    break;
                case PERIOD_1D:
                    threadReceiver.setTradeHistory24H(ccName, getVolumeRate(publicTradeHistoryList));
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
        if(!VolumeThread.this.isAlive()) {
            VolumeThread.this.setPriority(Thread.MAX_PRIORITY);
            VolumeThread.this.start();
        }
    }

    private class LoopTradeHistory24H extends TimerTask {
        public void run() {
            Set<String> keys = ccMap.keySet();
            for (String key : keys) {
                presenter.returnTradeHistory(key, PERIOD_1D);
                Log.i(VolumeThread.class.getSimpleName() , key);
                try {
                    Thread.sleep(3001L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class LoopTradeHistory15m extends TimerTask {
        public void run() {
            Set<String> keys = ccMap.keySet();
            for (String key : keys) {
                presenter.returnTradeHistory(key, PERIOD_30M);
                Log.i(VolumeThread.class.getSimpleName() , key);
                try {
                    Thread.sleep(3501L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}

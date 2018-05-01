package com.artf.poloa.presenter.ema;


import android.util.Log;

import com.artf.poloa.data.entity.PublicChartData;
import com.artf.poloa.data.entity.TradeObject;
import com.artf.poloa.data.entity.WrapJSONArray;
import com.artf.poloa.presenter.manager.ManagerMVP;
import com.artf.poloa.presenter.utility.Constant;
import com.artf.poloa.presenter.utility.Settings;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

public class EmaThread extends Thread implements EmaMVP.Thread, EmaMVP.ThreadUI {

    private List<Double> closeList = new ArrayList<>();
    private List<PublicChartData> publicChartDataList = new ArrayList<>();
    private HashMap<String, TradeObject> ccMap;
    private double emaValue;

    private EmaMVP.Presenter presenter;
    private ManagerMVP.ThreadReceiver threadReceiver;
    private LoopTask loopTask = new LoopTask();


    public EmaThread(EmaMVP.Presenter presenter) {
        this.presenter = presenter;
        presenter.setThread(this);
        ccMap = Settings.Trade.CC_LIST;
    }

    @Override
    public void run() {
        long wait = 1000L * Constant.PERIOD_3M;
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(loopTask, 0, wait);
    }


    @Override
    public synchronized void returnChartData(WrapJSONArray wrapJSONArray) {
            String ccName = wrapJSONArray.ccName;

            clearLists();
            setCloseData(wrapJSONArray.jsonArray);
            countEma(publicChartDataList);

            threadReceiver.setEmaData(ccName, emaValue);
    }

    private void clearLists() {
        publicChartDataList.clear();
        closeList.clear();
    }


    private void countEma(List<PublicChartData> data) {
        for (int i = 0; i < data.size(); i++) {
            closeList.add(data.get(i).close);
        }

        emaValue = ema2(closeList, Settings.EMA.LENHTH);
    }

    private void setCloseData(JsonArray jsonArray) {
        int jsonArrayLength = jsonArray.size();
        for (int i = jsonArrayLength - 1; i >= 0; i--) {
            String string = jsonArray.get(i).toString();
            PublicChartData publicChartData = new Gson().fromJson(string, PublicChartData.class);


            if (publicChartData != null) {
                publicChartDataList.add(publicChartData);
            }
        }
    }

    private double ema2(List<Double> list, int length) {
        double ema = 0;
        for (int i = list.size() - 1; i >= 0; i--) {
            double value = list.get(i);
            double alpha = 2.0 / (length + 1.0);
            ema = alpha * value + (1.0 - alpha) * ema;
        }
        return ema;
    }


    @Override
    public void onStop() {
        EmaThread.this.interrupt();
    }

    @Override
    public void setDataReciver(ManagerMVP.ThreadReceiver threadReceiver) {
        this.threadReceiver = threadReceiver;
    }

    @Override
    public void startThread() {
        if (EmaThread.this.getState() == State.NEW){
            EmaThread.this.setPriority(Thread.MAX_PRIORITY);
            EmaThread.this.start();
        }
    }

    private class LoopTask extends TimerTask {

        public void run() {

                Set<String> keys = ccMap.keySet();
                for (String key : keys) {
                    presenter.returnChartData(key, Settings.EMA.TIME_PERIOD);
                    Log.i(EmaThread.class.getSimpleName() , key);
                    try {
                        Thread.sleep(4001L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
        }
    }

}

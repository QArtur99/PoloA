package com.artf.poloa.presenter.rmi;


import android.util.Log;

import com.artf.poloa.data.entity.PublicChartData;
import com.artf.poloa.data.entity.TradeObject;
import com.artf.poloa.data.entity.WrapJSONArray;
import com.artf.poloa.presenter.manager.ManagerMVP;
import com.artf.poloa.utility.Constant;
import com.artf.poloa.utility.Settings;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

public class RmiThread extends Thread implements RmiMVP.Thread, RmiMVP.ThreadUI {

    private ArrayDeque<Double> rmi = new ArrayDeque<>();
    private ArrayDeque<Double> upMax = new ArrayDeque<>();
    private ArrayDeque<Double> dnMax = new ArrayDeque<>();
    private ArrayDeque<Double> upEma = new ArrayDeque<>();
    private ArrayDeque<Double> dnEma = new ArrayDeque<>();
    private List<Double> upMaxList = new ArrayList<>();
    private List<Double> dnMaxList = new ArrayList<>();
    private List<Double> upEmaList = new ArrayList<>();
    private List<Double> dnEmaList = new ArrayList<>();
    private List<PublicChartData> publicChartDataList = new ArrayList<>();
    private HashMap<String, TradeObject> ccMap;
    private double signalRmiValue;

    private RmiMVP.Presenter presenter;
    private ManagerMVP.ThreadReceiver threadReceiver;
    private LoopTask loopTask = new LoopTask();


    public RmiThread(RmiMVP.Presenter presenter) {
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
            countRmi(publicChartDataList);

            threadReceiver.setRmiData(ccName, rmi.getFirst(), signalRmiValue);
            threadReceiver.setLastValue(ccName, publicChartDataList.get(0).close);
    }

    private void clearLists() {
        publicChartDataList.clear();
        rmi.clear();
        upMax.clear();
        dnMax.clear();
        upEma.clear();
        dnEma.clear();
        upEmaList.clear();
        dnEmaList.clear();
    }


    private void countRmi(List<PublicChartData> data) {
        for (int i = 0; i < data.size() - Settings.RMI.MOMENTUM; i++) {
            upMax.addLast(max(data.get(i).close - data.get(i + Settings.RMI.MOMENTUM).close, 0.0));
            dnMax.addLast(max(data.get(i + Settings.RMI.MOMENTUM).close - data.get(i).close, 0.0));
        }

        upMax.removeFirst();
        dnMax.removeFirst();

        for (int i = 0; i < Settings.RMI.SIGNAL; i++) {
            upMaxList.clear();
            dnMaxList.clear();
            upMaxList.addAll(upMax);
            dnMaxList.addAll(dnMax);
            upEma.addLast(ema2(upMaxList, Settings.RMI.LENHTH));
            dnEma.addLast(ema2(dnMaxList, Settings.RMI.LENHTH));
            upMax.removeFirst();
            dnMax.removeFirst();
        }

        upEmaList.addAll(upEma);
        dnEmaList.addAll(dnEma);

        for (int i = 0; i < Settings.RMI.SIGNAL; i++) {
            rmi.addLast(rmi2(upEmaList.get(i), dnEmaList.get(i)));
        }

        signalRmiValue = sma9(rmi);
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


    private double rmi2(double up, double dn) {
        double rmi = 100.0 * up / (up + dn);
        return up == 0 ? 100 : rmi;
    }

    private double sma9(ArrayDeque<Double> list) {
        double sum = 0;
        for (double value : list) {
            sum += value;
        }
        return sum / list.size();
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


    private double max(double first, double second) {
        return first > second ? first : second;
    }


    @Override
    public void onStop() {
        RmiThread.this.interrupt();
    }

    @Override
    public void setDataReciver(ManagerMVP.ThreadReceiver threadReceiver) {
        this.threadReceiver = threadReceiver;
    }

    @Override
    public void startThread() {
        if (RmiThread.this.getState() == Thread.State.NEW){
            RmiThread.this.setPriority(Thread.MAX_PRIORITY);
            RmiThread.this.start();
        }
    }

    private class LoopTask extends TimerTask {

        public void run() {

                Set<String> keys = ccMap.keySet();
                for (String key : keys) {
                    presenter.returnChartData(key, Settings.RMI.TIME_PERIOD);
                    Log.i(RmiThread.class.getSimpleName() , key);
                    try {
                        Thread.sleep(4001L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
        }
    }

}

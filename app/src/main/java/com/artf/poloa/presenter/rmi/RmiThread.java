package com.artf.poloa.presenter.rmi;


import com.artf.poloa.data.entity.PublicChartData;
import com.artf.poloa.presenter.manager.ManagerMVP;
import com.artf.poloa.utility.Constant;
import com.artf.poloa.utility.Settings;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class RmiThread extends Thread implements RmiMVP.Thread, RmiMVP.ThreadUI {

    private ArrayDeque<Double> rmi = new ArrayDeque<>();
    private ArrayDeque<Double> upMax = new ArrayDeque<>();
    private ArrayDeque<Double> dnMax = new ArrayDeque<>();
    private ArrayDeque<Double> upEma = new ArrayDeque<>();
    private ArrayDeque<Double> dnEma = new ArrayDeque<>();
    private ArrayDeque<Double> stochK = new ArrayDeque<>();
    private ArrayDeque<Double> stochD = new ArrayDeque<>();
    private List<Double> upMaxList = new ArrayList<>();
    private List<Double> dnMaxList = new ArrayList<>();
    private List<Double> upEmaList = new ArrayList<>();
    private List<Double> dnEmaList = new ArrayList<>();
    private List<PublicChartData> publicChartDataList = new ArrayList<>();

    private double signalRmiValue, signalStochValue;

    private RmiMVP.Presenter presenter;
    private ManagerMVP.ThreadReceiver threadReceiver;
    private LoopTask loopTask = new LoopTask();


    public RmiThread(RmiMVP.Presenter presenter) {
        this.presenter = presenter;
        presenter.setThread(this);
    }


    @Override
    public void run() {
        long wait = 1000L * Constant.PERIOD_3M;
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(loopTask, 0, wait);
    }


    @Override
    public void returnChartData(JsonArray jsonArray) {

        clearLists();
        setCloseData(jsonArray);
        countRmi(publicChartDataList);
        stoch(publicChartDataList);

        threadReceiver.setRmiData(rmi.getFirst(), signalRmiValue);
        threadReceiver.setStochasticData(stochD.getFirst(), signalStochValue);
        threadReceiver.setLastValue(publicChartDataList.get(0).close);

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
        stochK.clear();
        stochD.clear();
    }


    private void countRmi(List<PublicChartData> data) {
        for (int i = 0; i < data.size() - Settings.RMI.MOMENTUM; i++) {
            upMax.addLast(max(data.get(i).close - data.get(i + Settings.RMI.MOMENTUM).close, 0.0));
            dnMax.addLast(max(data.get(i + Settings.RMI.MOMENTUM).close - data.get(i).close, 0.0));
        }

//        upMax.removeFirst();
//        dnMax.removeFirst();

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

    private void stoch(List<PublicChartData> data) {
//        ArrayDeque<PublicChartData> stochKK = new ArrayDeque<>();
//        stochKK.addAll(data);
//        stochKK.removeFirst();
//        data.clear();
//        data.addAll(stochKK);
        for (int j = 0; j < 2 * Settings.Stochastic.SIGNAL; j++) {
            double lowest14 = 0.0, highest14 = 0.0;

            for (int k = 0; k < Settings.Stochastic.LENHTH; k++) {
                double low = data.get(k + j).low;
                if (lowest14 == 0.0 || lowest14 > low) {
                    lowest14 = low;
                }

                double high = data.get(k + j).high;
                if (high > highest14) {
                    highest14 = high;
                }
            }

            double k = 100 * (data.get(j).close - lowest14) / (highest14 - lowest14);
            stochK.addLast(k);
        }

        List<Double> stochKTemp = new ArrayList<>();
        stochKTemp.addAll(stochK);
        for (int i = 0; i < Settings.Stochastic.SIGNAL; i++) {
            ArrayDeque<Double> sum = new ArrayDeque<>();
            for (int j = 0; j < Settings.Stochastic.SIGNAL; j++) {
                double value = stochKTemp.get(i + j);
                sum.addLast(value);
            }
            stochD.addLast(sma9(sum));
        }

        signalStochValue = sma9(stochD);

    }

    private double rmi(double up, double dn) {
        double rm = up / dn;
        double rmi = 100.0 - (100.0 / (1.0 + rm));
        return dn == 0 ? 0 : rmi;
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

    private double ema(List<Double> list, int length) {
        double x = 0.0;
        double upOfEma = 0.0;
        double downOfEma = 0.0;
        for (int i = 0; i < length; i++) {

            double value = list.get(i);
            double alpha = 2.0 / (x + 1.0);
            upOfEma += value * Math.pow((1.0 - alpha), x);
            downOfEma += Math.pow((1.0 - alpha), x);
            x++;
        }
        return upOfEma / downOfEma;
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
//        if (disposable != null && !disposable.isDisposed()) {
//            disposable.dispose();
//        }
    }

    @Override
    public void setDataReciver(ManagerMVP.ThreadReceiver threadReceiver) {
        this.threadReceiver = threadReceiver;
    }

    @Override
    public void startThread() {
        RmiThread.this.start();
    }

    private class LoopTask extends TimerTask {
        public void run() {
            presenter.returnChartData(Constant.PERIOD_5M);
        }
    }

}

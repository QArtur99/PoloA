package com.artf.poloa.presenter.manager;


import android.util.Log;

import com.artf.poloa.data.entity.Buy;
import com.artf.poloa.data.entity.TradeObject;
import com.artf.poloa.utility.Constant;
import com.artf.poloa.utility.Mode;
import com.artf.poloa.utility.Settings;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;


public class ManagerThread extends Thread implements ManagerMVP.Thread, ManagerMVP.ThreadReceiver {

//    @Inject
    ManagerMVP.Presenter presenter;
//    @Inject
//    RmiMVP.ThreadUI rmiThread;
//    @Inject
//    VolumeMVP.ThreadUI volumeThread;
    private double balanceBTC;
    private HashMap<String, TradeObject> ccMap;
    private ManagerMVP.View view;
    private LoopTask loopTask = new LoopTask();

    public ManagerThread(ManagerMVP.Presenter presenter) {
        this.presenter = presenter;
        presenter.setThread(this);
        ccMap = Settings.Trade.CC_LIST;
    }

//    public ManagerThread(Context context, ManagerMVP.View view) {
//        ((App) context.getApplicationContext()).getComponent().inject(this);
//        this.view = view;
//        presenter.setThread(this);
//        ccMap = Settings.Trade.CC_LIST;
//
//
//        volumeThread.setDataReciver(this);
//        volumeThread.startThread();
//
//        rmiThread.setDataReciver(this);
//        rmiThread.startThread();
//    }


    @Override
    public Boolean isItAlive() {
        return ManagerThread.this.isAlive();
    }


    @Override
    public void run() {
        long delay = 1000L * Constant.PERIOD_3M;
        long wait = 1000L * Constant.PERIOD_1M;
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(loopTask, delay, wait);
    }

    @Override
    public void setRmiData(String ccName, double rmiValue, double rmiSingal) {
        ccMap.get(ccName).rmiValue = rmiValue;
        ccMap.get(ccName).rmiSingal = rmiSingal;
    }

    @Override
    public void setTradeHistory24H(String ccName, double trend24H) {
        ccMap.get(ccName).trend24H = trend24H;
    }

    @Override
    public void setTradeHistory15m(String ccName, double trend15m) {
        ccMap.get(ccName).trend15m = trend15m;

    }

    @Override
    public void setStochasticData(String ccName, double stochValue, double stochSignal) {
        ccMap.get(ccName).stochValue = stochValue;
        ccMap.get(ccName).stochSignal = stochSignal;
    }


    @Override
    public void onStop() {
        ManagerThread.this.interrupt();
//        if (disposable != null && !disposable.isDisposed()) {
//            disposable.dispose();
//        }
    }


    @Override
    public void setLastValue(String ccName, double lastValueCC) {
        ccMap.get(ccName).lastValueCC = lastValueCC;
        Log.i(ManagerThread.class.getSimpleName(), ccName + " : " + String.valueOf(lastValueCC));
    }

    @Override
    public void setView(ManagerMVP.View view) {
        this.view = view;
    }

    @Override
    public void startThread() {
        ManagerThread.this.setPriority(Thread.MAX_PRIORITY);
        ManagerThread.this.start();
    }

    @Override
    public synchronized void returnBalances(JsonObject jsonObject) {
        balanceBTC = jsonObject.get(Constant.BTC_NAME).getAsDouble();
        Set<String> keys = ccMap.keySet();
        for (String key : keys) {
            ccMap.get(key).balanceSelectedCC = jsonObject.get(key).getAsDouble();
            if ((ccMap.get(key).balanceSelectedCC - ccMap.get(key).dontCareBalance) > Constant.VALID_AMOUNT_OF_BTC / ccMap.get(key).lastValueCC) {
                ccMap.get(key).tradeMode = Mode.SELL;
            } else {
                ccMap.get(key).tradeMode = Mode.BUY;
            }
            startBot(key, ccMap.get(key));

            try {
                Thread.sleep(501L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void erorrReturnBalances() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                presenter.returnBalances();
            }
        }, 1000);
    }

    @Override
    public synchronized void buy(Buy buyObject) {
        if (buyObject.resultingTrades != null && buyObject.resultingTrades.size() > 0) {
            double temp = Double.valueOf(buyObject.resultingTrades.get(0).rate);
            if (temp != Double.NaN && temp > 0.0) {
                ccMap.get(buyObject.ccName).rateOfLastBuy = temp;
                Log.e(ManagerThread.class.getSimpleName(), String.valueOf(temp));
            }
        }
    }

    @Override
    public void erorrBuy() {
//        new Timer().schedule(new TimerTask() {
//            @Override
//            public void run() {
//                presenter.returnBalances();
//            }
//        }, 1000);
    }

    @Override
    public void sell(JsonObject jsonArray) {

    }

    @Override
    public void erorrSell() {
//        new Timer().schedule(new TimerTask() {
//            @Override
//            public void run() {
//                presenter.returnBalances();
//            }
//        }, 1000);

    }

    private void startBot(String ccName, TradeObject to) {
        double sellLock = to.rateOfLastBuy + (to.rateOfLastBuy * 0.0169);
        double sellLock2 = to.rateOfLastBuy - (to.rateOfLastBuy * Settings.Trade.SELL_IF_DROPPED_PERCENTAGE);
        double sellLock3 = to.rateOfLastBuy + (to.rateOfLastBuy * 0.0269);
        double sellLock4 = to.rateOfLastBuy - (to.rateOfLastBuy * 0.10);
        if (to.tradeMode.isBuy() && to.trend15m > Settings.Trend.RULE_LONG_TREND) {

            if (to.rmiValue > to.rmiSingal && 30 > to.rmiSingal && to.stochValue - to.stochSignal > 3 && 20 > to.stochSignal
                    || to.rmiValue - to.rmiSingal > 3 && 30 > to.rmiSingal) {

                double availableBTC = balanceBTC * Settings.Trade.AVAILABLE_BTC_FOR_TRADE_PERCENTAGE;
                double rateForBuy = to.lastValueCC + (to.lastValueCC * 0.01);
                double amount = availableBTC / rateForBuy;
                ccMap.get(ccName).rateOfLastBuy = rateForBuy;
                presenter.buy(ccName, rateForBuy, amount);
            }
        } else if (to.tradeMode.isSell() && to.rmiSingal > to.rmiValue && to.rmiSingal - to.rmiValue > 3 && to.lastValueCC > sellLock
                || to.tradeMode.isSell() && to.rmiSingal > to.rmiValue && to.rmiSingal - to.rmiValue > 3 && sellLock2 > to.lastValueCC
                || to.tradeMode.isSell() && to.rmiSingal > to.rmiValue && to.rmiSingal - to.rmiValue > 3 && sellLock4 > to.lastValueCC
                || to.tradeMode.isSell() && to.lastValueCC > sellLock3 && to.rmiSingal - to.rmiValue > 1) {

            if (sellLock4 > to.lastValueCC) {
                double rateForSell = to.rateOfLastBuy + (to.rateOfLastBuy * 0.05);
                presenter.sell(Constant.POST_ONLY, ccName, rateForSell, to.balanceSelectedCC);
            } else {
                double rateForSell = to.lastValueCC - (to.lastValueCC * 0.01);
                presenter.sell(Constant.FILL_OR_KILL, ccName, rateForSell, to.balanceSelectedCC);
            }
        }
    }

    private class LoopTask extends TimerTask {
        public void run() {
            presenter.returnBalances();
        }
    }

}

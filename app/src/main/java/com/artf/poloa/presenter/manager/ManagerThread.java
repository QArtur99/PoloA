package com.artf.poloa.presenter.manager;


import android.util.Log;

import com.artf.poloa.data.entity.Buy;
import com.artf.poloa.utility.Constant;
import com.artf.poloa.utility.Mode;
import com.artf.poloa.utility.Settings;
import com.google.gson.JsonObject;

import java.util.Timer;
import java.util.TimerTask;


public class ManagerThread extends Thread implements ManagerMVP.Thread, ManagerMVP.ThreadReceiver {

    private double rmiValue, rmiSingal;
    private double stochValue, stochSignal;
    private double balanceSelectedCC;
    private double availableBTC, balanceBTC;
    private double lastValueCC;
    private double rateOfLastBuy;
    private double trend24H, trend15m;

    private Mode tradeMode = Mode.SELL;

    private ManagerMVP.Presenter presenter;
    private ManagerMVP.View view;
    private LoopTask loopTask = new LoopTask();


    public ManagerThread(ManagerMVP.Presenter presenter) {
        this.presenter = presenter;
        presenter.setThread(this);
        rateOfLastBuy = Settings.Trade.RATE_OF_LAST_BUY;
    }


    @Override
    public void run() {
//        long wait = 1000L * Constant.PERIOD_5M;
//        Timer timer = new Timer();
//        timer.scheduleAtFixedRate(loopTask, 0, wait);
    }

    @Override
    public void setRmiData(double rmiValue, double rmiSingal) {
        this.rmiValue = rmiValue;
        this.rmiSingal = rmiSingal;
        double xx = rmiSingal + rmiValue;
    }

    @Override
    public void setTradeHistory24H(double trend24H) {
        this.trend24H = trend24H;
    }

    @Override
    public void setTradeHistory15m(double trend15m) {
        this.trend15m = trend15m;

    }

    @Override
    public void setStochasticData(double stochValue, double stochSignal) {
        this.stochValue = stochValue;
        this.stochSignal = stochSignal;
        double xx = rmiSingal + rmiValue;
    }


    @Override
    public void onStop() {
        ManagerThread.this.interrupt();
//        if (disposable != null && !disposable.isDisposed()) {
//            disposable.dispose();
//        }
    }


    @Override
    public void setLastValue(double close) {
        this.lastValueCC = close;
        presenter.returnBalances();

    }

    @Override
    public void setView(ManagerMVP.View view) {
        this.view = view;
    }

    @Override
    public void startThread() {
        ManagerThread.this.start();

    }

    @Override
    public void returnBalances(JsonObject jsonObject) {
        balanceBTC = jsonObject.get(Constant.BTC_NAME).getAsDouble();
        balanceSelectedCC = jsonObject.get(Settings.Trade.CC_NAME).getAsDouble();
        if ((balanceSelectedCC - Settings.Trade.DONT_CARE_BALANCE) > Settings.Trade.VALID_AMOUNT) {
            tradeMode = Mode.SELL;
        } else {
            tradeMode = Mode.BUY;
        }
        startBot();
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
    public void buy(Buy jsonArray) {
        double temp = Double.valueOf(jsonArray.resultingTrades.get(0).rate);
        if(temp != Double.NaN && temp > 0.0) {
            this.rateOfLastBuy = temp;
            Log.e(ManagerThread.class.getName(), String.valueOf(temp));
        }
    }

    @Override
    public void erorrBuy() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                presenter.returnBalances();
            }
        }, 1000);
    }

    @Override
    public void sell(JsonObject jsonArray) {

    }

    @Override
    public void erorrSell() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                presenter.returnBalances();
            }
        }, 1000);

    }

    public void startBot() {
        double sellLock = rateOfLastBuy + (rateOfLastBuy * 0.0169);
        double sellLock2 = rateOfLastBuy - (rateOfLastBuy * 0.05);
        if (tradeMode.isBuy()  && trend15m > Settings.Trend.RULE_LONG_TREND && rmiValue > rmiSingal && rmiValue - rmiSingal > 5 && 30 > stochSignal
                || tradeMode.isBuy() && trend15m > Settings.Trend.RULE_LONG_TREND && rmiValue > rmiSingal && rmiValue - rmiSingal > 5 && 40 > rmiSingal) {
            availableBTC = balanceBTC * Settings.Trade.AVAILABLE_BTC_FOR_TRADE_PERCENTAGE;
            double rateForBuy = lastValueCC + (lastValueCC * 0.01);
            double amount = availableBTC / rateForBuy;
            this.rateOfLastBuy = rateForBuy;
            presenter.buy(rateForBuy, amount);
        } else if (tradeMode.isSell() && rmiSingal > rmiValue && rmiSingal - rmiValue > 3 && lastValueCC > sellLock
                || tradeMode.isSell() && rmiSingal > rmiValue && rmiSingal - rmiValue > 3 && sellLock2 > lastValueCC) {
            double rateForSell = lastValueCC - (lastValueCC * 0.01);
            presenter.sell(rateForSell, balanceSelectedCC);
        }
    }

    private class LoopTask extends TimerTask {
        public void run() {
//            presenter.returnChartData(Constant.PERIOD_15M);
        }
    }

}

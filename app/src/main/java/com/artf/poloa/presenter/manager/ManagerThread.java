package com.artf.poloa.presenter.manager;


import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.artf.poloa.R;
import com.artf.poloa.data.database.Utility;
import com.artf.poloa.data.entity.Buy;
import com.artf.poloa.data.entity.TradeObject;
import com.artf.poloa.presenter.ema.EmaMVP;
import com.artf.poloa.presenter.rmi.RmiMVP;
import com.artf.poloa.presenter.root.App;
import com.artf.poloa.presenter.utility.Constant;
import com.artf.poloa.presenter.utility.Mode;
import com.artf.poloa.presenter.utility.Settings;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;


public class ManagerThread extends Service implements ManagerMVP.Thread, ManagerMVP.ThreadReceiver {

    public static final String ACTION_START_SERVICE = "com.artf.poloa.presenter.manager.start.service";
    private final IBinder mBinder = new LocalBinder();
    @Inject
    ManagerMVP.Presenter presenter;
    @Inject
    RmiMVP.ThreadUI rmiThread;
    @Inject
    EmaMVP.ThreadUI emaThread;
    private double balanceBTC;
    private HashMap<String, TradeObject> ccMap;
    private ManagerMVP.View view;
    private LoopTask loopTask;
    private Timer timer;

    public ManagerThread() {
        super();
    }

    public static void startService(Context context) {
        Intent intent = new Intent(context, ManagerThread.class);
        intent.setAction(ACTION_START_SERVICE);
        context.startService(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Notification notification =
                new Notification.Builder(getApplicationContext())
                        .setContentTitle("PoloA")
                        .setContentText("PoloA is running!")
                        .setSmallIcon(R.mipmap.ic_launcher_round)
                        .build();

        startForeground(1, notification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        ((App) getApplicationContext()).getComponent().inject(this);

        presenter.setThread(this);
        ccMap = Utility.loadHashMap(getApplicationContext());

        emaThread.setDataReciver(this);
        emaThread.startThread();

        rmiThread.setDataReciver(this);
        rmiThread.startThread();

        if (loopTask != null) {
            loopTask.cancel();
        }

        if (timer != null) {
            timer.cancel();
        }

        long delay = 1000L * Constant.PERIOD_3M;
        long wait = 1000L * Constant.PERIOD_2M;
        loopTask = new LoopTask();
        timer = new Timer();
        timer.scheduleAtFixedRate(loopTask, delay, wait);

        return START_STICKY;
    }

    @Override
    public void setEmaData(String ccName, double emaValue) {
        ccMap.get(ccName).emaValue = emaValue;
        double x = emaValue;
    }

    @Override
    public void setRmiData(String ccName, double rmiValue, double rmiSingal) {
        ccMap.get(ccName).rmiValue = rmiValue;
        ccMap.get(ccName).rmiSingal = rmiSingal;
    }

    @Override
    public void setLastValue(String ccName, double lastValueCC) {
        ccMap.get(ccName).lastValueCC = lastValueCC;
        Log.i(ManagerThread.class.getSimpleName(), ccName + " : " + String.valueOf(lastValueCC));
    }

    @Override
    public void setView(ManagerMVP.View view) {
        this.view = view;
        ccMap = Settings.Trade.CC_LIST;
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
                Thread.sleep(1001L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        Type type = new TypeToken<HashMap<String, TradeObject>>() {}.getType();
        String jsonStringHashMap = new Gson().toJson(ccMap, type);
        int rowsUpdated = Utility.updateDatabase(getApplicationContext(), jsonStringHashMap);
        Log.e(ManagerThread.class.getSimpleName(), "ccMap SAVE updated rows:" + rowsUpdated);
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
    public void erorrBuy() {}

    @Override
    public void sell(JsonObject jsonArray) {}

    @Override
    public void erorrSell() {}

    private void startBot(String ccName, TradeObject to) {
        double sellLock = to.rateOfLastBuy + (to.rateOfLastBuy * Settings.Trade.SELL_IF_RAISED_PERCENTAGE / 100);
        double sellLock2 = to.rateOfLastBuy - (to.rateOfLastBuy * Settings.Trade.SELL_IF_DROPPED_PERCENTAGE / 100);

        if (to.tradeMode.isBuy() && Settings.RMI.OVER_SOLD > to.rmiSingal) {


            if (to.rmiValue - to.rmiSingal > Settings.RMI.RMI_OVER_SIGNAL
                    && to.emaValue * Settings.EMA.PERCENTAGE_VALUE / 100 > to.lastValueCC) {

                double availableBTC = balanceBTC * Settings.Trade.AVAILABLE_BTC_FOR_TRADE_PERCENTAGE / 100;
                if (availableBTC > Constant.VALID_AMOUNT_OF_BTC) {
                    double rateForBuy = to.lastValueCC + (to.lastValueCC * 0.01);
                    double amount = availableBTC / rateForBuy;
                    ccMap.get(ccName).rateOfLastBuy = rateForBuy;
                    presenter.buy(ccName, rateForBuy, amount);
                }
            }
        } else if (to.tradeMode.isSell()) {
            boolean exitS1, exitS2;

            if (Settings.Trade.CAN_I_LOSE) {
                exitS1 = to.rmiValue > Settings.RMI.OVER_BOUGHT;
                exitS2 = sellLock2 > to.lastValueCC && to.rmiValue > Settings.Trade.OVER_BOUGHT_SELL_IF_DROPPED_PERCENTAGE;
            } else {
                exitS1 = to.lastValueCC < sellLock;
                exitS2 = false;
            }

            if (exitS1 || exitS2) {

                if (to.rmiSingal - to.rmiValue > Settings.RMI.SIGNAL_OVER_RMI || exitS2) {
                    double rateForSell = to.lastValueCC - (to.lastValueCC * 0.01);
                    presenter.sell(Constant.FILL_OR_KILL, ccName, rateForSell, to.balanceSelectedCC);
                }
            }
        }

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public HashMap<String, TradeObject> getCcMap() {
        return ccMap;
    }

    public class LocalBinder extends Binder {
        public ManagerMVP.ThreadReceiver getService() {
            return ManagerThread.this;
        }
    }

    private class LoopTask extends TimerTask {
        public void run() {
            presenter.returnBalances();
        }
    }

}

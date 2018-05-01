package com.artf.poloa.data.entity;

import com.artf.poloa.presenter.utility.Mode;


public class TradeObject {

    public double dontCareBalance;
    public double balanceSelectedCC;
    public Mode tradeMode = Mode.SELL;
    public double lastValueCC;
    public double rateOfLastBuy;
    public double emaValue;
    public double rmiValue, rmiSingal;

    public TradeObject(double dontCareBalance, double rateOfLastBuy) {
        this.dontCareBalance = dontCareBalance;
        this.rateOfLastBuy = rateOfLastBuy;
    }
}

package com.artf.poloa.data.entity;

/**
 * Created by ART_F on 2017-12-24.
 */

public class TradeObject {

    public double dontCareBalance;
    public double rateOfLastBuy;
    public double rmiValue, rmiSingal;
    public double stochValue, stochSignal;
    public double balanceSelectedCC;
    public double lastValueCC;
    public double trend24H, trend15m;

    public TradeObject(double dontCareBalance, double rateOfLastBuy) {
        this.dontCareBalance = dontCareBalance;
        this.rateOfLastBuy = rateOfLastBuy;
    }
}

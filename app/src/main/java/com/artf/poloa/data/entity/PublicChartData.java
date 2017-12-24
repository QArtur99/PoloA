package com.artf.poloa.data.entity;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class PublicChartData{

	@SerializedName("date")
	public int date;

	@SerializedName("volume")
	public double volume;

	@SerializedName("high")
	public double high;

	@SerializedName("low")
	public double low;

	@SerializedName("weightedAverage")
	public double weightedAverage;

	@SerializedName("quoteVolume")
	public double quoteVolume;

	@SerializedName("close")
	public double close;

	@SerializedName("open")
	public double open;
}
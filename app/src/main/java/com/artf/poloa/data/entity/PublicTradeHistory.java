package com.artf.poloa.data.entity;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class PublicTradeHistory{

	@SerializedName("date")
	public String date;

	@SerializedName("amount")
	public String amount;

	@SerializedName("total")
	public String total;

	@SerializedName("rate")
	public String rate;

	@SerializedName("type")
	public String type;
}
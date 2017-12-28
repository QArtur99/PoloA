package com.artf.poloa.data.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class Buy{


	public String ccName;

	@SerializedName("resultingTrades")
	public List<ResultingTradesItem> resultingTrades;

	@SerializedName("orderNumber")
	public long orderNumber;
}
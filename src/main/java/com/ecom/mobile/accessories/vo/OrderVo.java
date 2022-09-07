package com.ecom.mobile.accessories.vo;

import java.util.ArrayList;
import java.util.List;

import com.ecom.mobile.accessories.entites.UserAddress;

public class OrderVo {
	public int items;
	public int total;
	public List<DataVo> data = new ArrayList<>();
	public UserAddress address;
	public String txnid;
}
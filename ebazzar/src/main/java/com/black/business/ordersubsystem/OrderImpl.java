package com.black.business.ordersubsystem;

import java.time.LocalDate;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.stream.Collectors;

import com.black.business.externalinterfaces.Address;
import com.black.business.externalinterfaces.CreditCard;
import com.black.business.externalinterfaces.Order;
import com.black.business.externalinterfaces.OrderItem;

public class OrderImpl implements Order {
	private List<OrderItem> orderItems;
	private int orderId;
	private LocalDate date;

	private double totalPriceAmount;
	private Address shipAddr;
	private Address billAddr;
	private CreditCard creditCard;

	public double getTotalPriceAmount() {
		return totalPriceAmount;
	}

	public void setTotalPriceAmount(double totalPriceAmount) {
		this.totalPriceAmount = totalPriceAmount;
	}

	public OrderImpl() {
		date = LocalDate.now();
	}

	public List<OrderItem> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(List<OrderItem> orderItems) {
		this.orderItems = orderItems;
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public double getTotalPrice() {
		if(orderItems == null) {
			return 0.0;
		} else {
			DoubleSummaryStatistics summary
					= orderItems.stream().collect(
					Collectors.summarizingDouble(
							(OrderItem item) -> item.getUnitPrice() * item.getQuantity()));
			return summary.getSum();
		}
	}

	public LocalDate getOrderDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}


	@Override
	public Address getShipAddress() {
		return shipAddr;
	}

	@Override
	public Address getBillAddress() {
		return billAddr;
	}

	@Override
	public CreditCard getPaymentInfo() {
		return creditCard;
	}

	public void setShipAddress(Address add) {
		shipAddr = add;
	}

	public void setBillAddress(Address add) {
		billAddr = add;
	}

	public void setPaymentInfo(CreditCard cc) {
		creditCard = cc;
	}

}

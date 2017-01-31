package com.netreadystaging.godine.models;

/**
 * Created by sony on 10-12-2016.
 */

public class BillingHistory {
    String OrderDate;
    String SalesOrderNumber;
    String SalesPaymentStatus;
    String ShipingStatus;
    String TotalAmount;

    public String getTotalAmount() {
        return TotalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        TotalAmount = totalAmount;
    }

    public String getShipingStatus() {
        return ShipingStatus;
    }

    public void setShipingStatus(String shipingStatus) {
        ShipingStatus = shipingStatus;
    }

    public String getSalesPaymentStatus() {
        return SalesPaymentStatus;
    }

    public void setSalesPaymentStatus(String salesPaymentStatus) {
        SalesPaymentStatus = salesPaymentStatus;
    }

    public String getSalesOrderNumber() {
        return SalesOrderNumber;
    }

    public void setSalesOrderNumber(String salesOrderNumber) {
        SalesOrderNumber = salesOrderNumber;
    }

    public String getOrderDate() {
        return OrderDate;
    }

    public void setOrderDate(String orderDate) {
        OrderDate = orderDate;
    }

}

package com.example.do_an.ui_admin;

import com.example.do_an.ui_user.CartItem;
import java.util.List;

public class Order {
    private String orderID;
    private String userID;
    private List<CartItem> items;
    private String totalPrice;
    private String paymentMethod;
    private String address;
    private String orderDate;  // Thêm ngày đặt hàng

    public Order() {}

    public Order(String orderID, String userID, List<CartItem> items, String totalPrice, String paymentMethod, String address, String orderDate) {
        this.orderID = orderID;
        this.userID = userID;
        this.items = items;
        this.totalPrice = totalPrice;
        this.paymentMethod = paymentMethod;
        this.address = address;
        this.orderDate = orderDate;
    }

    public String getOrderID() { return orderID; }
    public String getUserID() { return userID; }
    public List<CartItem> getItems() { return items; }
    public String getTotalPrice() { return totalPrice; }
    public String getPaymentMethod() { return paymentMethod; }
    public String getAddress() { return address; }
    public String getOrderDate() { return orderDate; }  // Getter cho ngày đặt hàng
}

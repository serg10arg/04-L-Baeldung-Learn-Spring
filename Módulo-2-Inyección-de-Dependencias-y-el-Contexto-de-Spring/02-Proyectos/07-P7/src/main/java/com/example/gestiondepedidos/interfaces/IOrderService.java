package com.example.gestiondepedidos.interfaces;

import com.example.gestiondepedidos.model.Order;

public interface IOrderService {

    Order createOrder(String customerName, double totalAmount);
    Order getOrderById(Long id);
}

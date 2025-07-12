package com.example.gestiondepedidos.model;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class Order {

    private Long id;
    private String customerName;
    private double totalAmount;
    private String status;

    @Override
    public String toString() {
        return new StringBuilder("Order[")
                .append("id=").append(id)
                .append(", customerName='").append(customerName).append('\'')
                .append(", totalAmount=").append(totalAmount)
                .append(", status='").append(status).append('\'')
                .append(']')
                .toString();
    }
}
